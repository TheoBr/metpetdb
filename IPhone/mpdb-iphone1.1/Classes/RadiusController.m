//
//  SearchViewController.m
//  LocateMe
//
//  Created by Heather Buletti on 5/3/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "RadiusController.h"
#import "MapController.h"
#include <math.h>
#include <stdio.h>
#import "MetPetDBAppDelegate.h"



@implementation RadiusController
@synthesize searchButton, radius, outputlabel, toolbar, mylocationCoordinate, timer;
@synthesize mapController, sampleID, sampleName, indicator, searchCriteria;
@synthesize north, south, east, west, currentSearchData;
-(void)viewDidLoad{
	toolbar.barStyle=UIBarStyleBlack;
	locations=[[NSMutableArray alloc] init];
	
	//build the array that the picker view will use
	radii=[[NSMutableArray alloc] init];
	[radii addObject:@".1"];
	[radii addObject:@".5"];
	[radii addObject:@"1"];
	[radii addObject:@"2"];
	[radii addObject:@"3"];
	[radii addObject:@"4"];
	[radii addObject:@"5"];
	[radii addObject:@"10"];
	[radii addObject:@"20"];
	[radii addObject:@"50"];
	[radii addObject:@"100"];
	[radii addObject:@"200"];
	[radii addObject:@"300"];
	[radii addObject:@"400"];
	[radii addObject:@"500"];
	
	
	CLLocationCoordinate2D centerCoordinate = [CurrentSearchData getCenterCoordinate];
	
	double lat = centerCoordinate.latitude;
	
	double longish = centerCoordinate.longitude;
	
	NSString *output=[[NSString alloc] initWithFormat:@"Latitude: %.5f\nLongitude: %.5f\nSelect a radius (in Kilometers) to\nuse for your search:", centerCoordinate.latitude, centerCoordinate.longitude];
	outputlabel.text=output;
	
	radiusPicker.showsSelectionIndicator=YES;
	radiusPicker.dataSource = self;
	radiusPicker.delegate=self;
	[radiusPicker selectRow:7 inComponent:0 animated:NO];
	radius=[[NSString alloc] initWithString:[radii objectAtIndex:7]];
	
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
	return [radii count];
}
- (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [radii objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	[radiusPicker selectRow:row inComponent:0 animated:NO];
	radius=[[NSString alloc] initWithString:[radii objectAtIndex:row]];
}

-(IBAction)showMap:(id)sender
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	//before querying the database, we must convert the entered radius into degrees to create a box that all returned samples must be within
	//since the scroll wheel contains values in kilometers, all values must be converted to meters
	radiusMeters=([radius doubleValue])*1000; 
	//next convert the meters into degress
	radiusDegrees=(radiusMeters)*(.000009);
	//find the corners of the box
	latdouble=[latitude doubleValue];
	latitudeDegrees= (radiusMeters)/(111120);
	
	double temp= cos((3.1415926535897932384626433832795/180)*latdouble);
	longitudeDegrees= (radiusMeters)/((111120)* temp);
	//longitudeDegrees=111120*cos(latitudeDegrees);
	
	double centerLatitude=[latitude doubleValue];
	double centerLongitude=[longitude doubleValue];
	n= centerLatitude+(latitudeDegrees);
	s= centerLatitude-(latitudeDegrees);
	e=centerLongitude+(longitudeDegrees);
	w=centerLongitude-(longitudeDegrees);
	
	north=[[NSString alloc] initWithFormat:@"%f",n];
	south=[[NSString alloc] initWithFormat:@"%f",s];
	east=[[NSString alloc] initWithFormat:@"%f",e];
	west=[[NSString alloc] initWithFormat:@"%f",w];
	
	
	NSMutableArray *tempCoordinates=[[NSMutableArray alloc] init];
	[tempCoordinates addObject:north];
	[tempCoordinates addObject:south];
	[tempCoordinates addObject:east];
	[tempCoordinates addObject:west];
	
	PostRequest *post = [[PostRequest alloc] init];
	[post setData:nil:nil :nil :nil :currentSearchData.currentPublicStatus :nil:tempCoordinates:0:@"true"];
	myReturn=[post buildPostString];
	
//	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/scball/Documents/test1.txt"];
//	[fh writeData:myReturn];
	
	xmlParser *x= [[xmlParser alloc] init];
	searchCriteria= [x parseSearchCriteria:myReturn];
	[x release];
	
	//if no samples were returned from the search then an alert is displayed and the map view is not loaded
	if(searchCriteria.totalCount==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Samples." message:@"Please increase your search radius or choose different coordinates." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
	else
	{
		
		
		//make a thread so the samples are loaded in the background
		searchButton.enabled=NO;
		//myThread = [[NSThread alloc] initWithTarget:self selector:@selector(getSamples) object:nil];
		
		//[myThread start];
		NSNumber *num=[NSNumber numberWithDouble:searchCriteria.maxLat];
		[[currentSearchData originalCoordinates] addObject:num];
		num= [NSNumber numberWithDouble:searchCriteria.minLat];
		[[currentSearchData originalCoordinates] addObject:num];
		num=[NSNumber numberWithDouble:searchCriteria.maxLong];
		[[currentSearchData originalCoordinates] addObject:num];
		num=[NSNumber numberWithDouble:searchCriteria.minLong];
		[[currentSearchData originalCoordinates]  addObject:num];
		int size= [[currentSearchData originalCoordinates] count];
		
		[self getSamples: tempCoordinates];
		
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		[viewController setData:locations:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		//since there is no region the geographic search criteria displayed will be a lat and long coordinate
		//set the point we want to be the center of the map view when it first appears and the zoom information
		//first convert lat and long strings to doubles and then make a cllocation to pass to the map
		/*double tempLat=[latitude doubleValue];
		 double tempLong=[longitude doubleValue];
		 double latspan=(latitudeDegrees)*2;
		 double longspan=(longitudeDegrees)*2;*/
		self.mapController = viewController;
		[viewController release];
		UIView *ControllersView = [mapController view];
		[self.mapController makeNavBar];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:mapController animated:NO];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
}

-(void)getSamples:(NSMutableArray*)coordinates{
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	
	//NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];  	
	
	//NSMutableArray *coordArray = [currentSearchData coordinates];
	
	
	
	
	//create a post request by sending the necessary data to a PostRequest object
	//since the user has not yet specified any 
	PostRequest *post= [[PostRequest alloc] init];
	[post setData:nil:nil :nil :nil :currentSearchData.currentPublicStatus :nil:coordinates:0:@"false"];
//	[post setData:nil:nil :nil :nil :nil :nil:coordinates:0:nil];

	myReturn=[post buildPostString];
//	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/scball/Documents/test2.txt"];
//	[fh writeData:myReturn];
	
	xmlParser *y= [[xmlParser alloc] init];
	locations= [y parseSamples:myReturn];
	
	/*for(int z=0; z<[locations count]; z++)
	 {
	 [mapController makeAnnotations:[locations objectAtIndex:z]];
	 }
	 [mapController setSpan];
	 [mapController makeSearchBox];
	 [mapController makeNavBar];
	 [xmlParser release];
	 [y release];
	 [mapController release];
	 [(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	 */
	//[myThread exit];
	//[pool release];
}


-(void)setData:(CurrentSearchData*)data
{
	currentSearchData=data;
	
	CLLocationCoordinate2D centerCoord = [CurrentSearchData getCenterCoordinate];
	
	latitude= [[NSString alloc] initWithFormat:@"%.5f", centerCoord.latitude];
	longitude=[[NSString alloc] initWithFormat:@"%.5f", centerCoord.longitude];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
}


- (void)dealloc{
	[radiusPicker release];
	[searchButton release];
	[outputlabel release];
	[toolbar release];
	[radius release];
	[latitude release];
	[longitude release];
	[north release];
	[south release];
	[east release];
	[west release];
	[mapController release];
	[locations release];
	[newAnnotation release];
	[sampleID release];
	[sampleName release];
	[minerals release];
	[metamorphicGrades release];
	[rockTypes release];
	[currentRockType release];
	[radii release];
	[indicator release];
	[timer release];
	[description release];
	[myThread release];
	[newSet release];
	[myParser release];
	[searchCriteria release];
	[super dealloc];
}


@end
