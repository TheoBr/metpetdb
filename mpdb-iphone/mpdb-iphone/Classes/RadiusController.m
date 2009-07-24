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



/*#if TARGET_IPHONE_SIMULATOR
#else
#import <CFNetwork/CFNetwork.h>
#endif*/



//#import "CFHTTPMessage/CFHTTPMessage.h"
@implementation RadiusController
@synthesize searchButton, radius, outputlabel, toolbar, mylocationCoordinate;
@synthesize currentStringValue, mapController, sampleID, sampleName, publicStatus, currentRockType, currentOwner, indicator;
-(void)viewDidLoad{
	toolbar.barStyle=UIBarStyleBlack;
	locations=[[NSMutableArray alloc] init];
	minerals=[[NSMutableArray alloc] init];
	metamorphicGrades=[[NSMutableArray alloc] init];
	rockTypes=[[NSMutableArray alloc] init];
	mineralFlag=FALSE;
	metamorphicFlag=FALSE;
	nameFlag=TRUE;
	rockFlag=FALSE;
	uniqueFlag=TRUE;
	indicator=[[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(280, 15, 20, 20)];
	[indicator setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhite];
	[toolbar addSubview:indicator];
	searchButton=[[UIBarButtonItem alloc] initWithTitle:@"Search" style:UIBarButtonItemStyleBordered target:self action:@selector(showMap)];
	NSMutableArray *items=[[NSMutableArray alloc] init];
	[items addObject:searchButton];
	toolbar.items= items;
	
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
	
	radiusPicker.showsSelectionIndicator=YES;
	radiusPicker.dataSource = self;
	radiusPicker.delegate=self;
	[radiusPicker selectRow:7 inComponent:0 animated:NO];
	
	NSString *output=[[NSString alloc] initWithFormat:@"Latitude: %@\nLongitude: %@\nSelect a radius (in Kilometers) to\nuse for your search:", latitude, longitude];
	outputlabel.text=output;
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
	radius=[[NSString alloc] initWithString:[radii objectAtIndex:row]];
}

-(void)showMap
{
	//start the activity indicator spinning when the map button is pressed
	[indicator startAnimating];
		//since this is the first time we are loading sample location information, we pass this flag in as true and copy the
	//data into an array in the mapController
	//when the sample location annotations are loaded from there narrow search table, this bool is passed in as false and no data is copied
	bool dataFlag=TRUE;
	[self getSamples];
		
	MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
	[viewController setData:locations:locations];
	[viewController setType:@"map"];
	//since there is no region the geographic search criteria displayed will be a lat and long coordinate
	[viewController setRegion:nil];
	//set the point we want to be the center of the map view when it first appears and the zoom information
	//first convert lat and long strings to doubles and then make a cllocation to pass to the map
	double tempLat=[latitude doubleValue];
	double tempLong=[longitude doubleValue];
	CLLocation *center=[[CLLocation alloc] initWithLatitude:tempLat longitude:tempLong];
	double latspan=(latitudeDegrees)*2;
	double longspan=(longitudeDegrees)*2;
	[viewController setCoordinate: center.coordinate:latspan:longspan:n:s:e:w];
	self.mapController = viewController;
	[viewController release];
	UIView *ControllersView = [mapController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:mapController animated:NO];
	[indicator stopAnimating];
}
-(void)getSamples{
	

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

	NSString *north=[[NSString alloc] initWithFormat:@"%f",n];
	NSString *south=[[NSString alloc] initWithFormat:@"%f",s];
	NSString *east=[[NSString alloc] initWithFormat:@"%f",e];
	NSString *west=[[NSString alloc] initWithFormat:@"%f",w];
	
	//make a url that has the four corners appended on the end
	
	NSString *urlstring=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?north=%@&south=%@&east=%@&west=%@", north, south, east, west];

	NSURL *myURL= [NSURL URLWithString:urlstring];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	
	NSURLResponse *response;
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:&response error:&error];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
	
}


- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"set"])
	{
		//make sure this array is reset here so that when the back button is pressed the samples do not get added to the original array
		locations=[[NSMutableArray alloc]init];
	}
	
	if([elementName isEqualToString:@"sample"])
	{
		nameFlag=TRUE; //since we are now starting a new sample, we want to record a different name
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"x"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"y"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"long"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"rockType"])
	{
		rockFlag=FALSE;
		currentStringValue= nil;
		return;
	}
	if([elementName isEqualToString:@"owner"])
	{
		ownerFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"minerals"])
	{
		//if the beginning of the minerals list is detected, set the flag to true and put the values inside
		//these string tags in the minerals array
		currentMinerals=[[NSMutableArray alloc] init];
		currentStringValue=nil;
		mineralFlag=TRUE;
	}
	if([elementName isEqualToString:@"metamorphicGrades"])
	{
		//if the beginning of the metamorphicGrade list is detected, set the flag to true and put the values inside
		//these string tags in the metamorphicGrades array
		currentMetamorphicGrades=[[NSMutableArray alloc] init];
		currentStringValue=nil;
		metamorphicFlag=TRUE;
	}
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (!currentStringValue) {
        // currentStringValue is an NSMutableString instance variable
        currentStringValue = [[NSMutableString alloc] initWithCapacity:50];
    }
    [currentStringValue appendString:string];
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
	if([elementName isEqualToString:@"string"])
	{
		if(nameFlag==TRUE)
		{
			sampleName= [[NSString alloc] initWithString:currentStringValue];
			currentStringValue=nil;
			nameFlag=FALSE;
			return;
		}
		else if(ownerFlag==TRUE)
		{
			currentOwner=currentStringValue;
			ownerFlag=FALSE;
			currentStringValue=nil;
			return;
		}
		else if(mineralFlag==TRUE)
		{
			//each value from one of these tags is a string representing a type of mineral found in a sample
			//for each new sample, loop through the existing mineral array to make sure it is not already in the array
			int x;
			bool flag=FALSE;
			for(x=0; x<[minerals count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[minerals objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[currentMinerals addObject:currentStringValue];
				currentStringValue=nil;
				return;
			}
		}
		else if(metamorphicFlag==TRUE)
		{
			int x;
			bool flag=FALSE;
			for(x=0; x<[metamorphicGrades count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[metamorphicGrades objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[currentMetamorphicGrades addObject:currentStringValue];
				currentStringValue=nil;
				return;
			}
		}
		
	}
	
	if ( [elementName isEqualToString:@"x"] ) {
		longdouble= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble=[[NSString alloc] initWithFormat: @"%.4f", longdouble];
		longdouble= [tempDouble doubleValue];
		currentStringValue = nil;
    }
	if([elementName isEqualToString:@"y"])
	{
		double latValue= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble2=[[NSString alloc] initWithFormat: @"%.4f", latValue];
		NSString *foo=[[NSString alloc] initWithString:tempDouble2];
		latdouble= [tempDouble2 doubleValue];
		currentStringValue = nil;
		
	}
	if([elementName isEqualToString:@"long"])
	{
		sampleID= [[NSString alloc] initWithString:currentStringValue];
		currentStringValue = nil;
		return;
	}
	
	if([elementName isEqualToString:@"sample"])
	{
		CLLocationDegrees latdegrees= (CLLocationDegrees)latdouble;
		CLLocationDegrees longdegrees= (CLLocationDegrees) longdouble;
		CLLocation *tempCoordinate= [[CLLocation alloc] initWithLatitude:latdegrees longitude:longdegrees];
		
		newAnnotation= [AnnotationObjects new];
		[newAnnotation setCoordinate:tempCoordinate.coordinate];
		[newAnnotation setTitle:[[NSString alloc] initWithString:sampleName]];
		[newAnnotation setId:[[NSString alloc] initWithString:sampleID]];
		[newAnnotation setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
		[newAnnotation setPublicData:publicStatus];
		[newAnnotation setRockType:currentRockType];
		[newAnnotation setMinerals:currentMinerals];
		[newAnnotation setName:sampleName];
		[newAnnotation setOwner:currentOwner];
		[newAnnotation setMetamorphicGrades:currentMetamorphicGrades];
		if(uniqueFlag==TRUE)
		{
			//automatically add the first sample to the set since there will not be any duplicates yet
			uniqueSamples *newSet=[uniqueSamples new];
			newSet.samples=[[NSMutableArray alloc] init];
			newSet.count=1;
			[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
			[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
			[newSet.samples addObject:newAnnotation];
			newSet.coordinate=tempCoordinate.coordinate;
			[newSet setId:sampleID];
			uniqueFlag=FALSE;
			[locations addObject:newSet];
		}
		else
		{
			int p;
			int q;
			bool added=FALSE;
			for(p=0; p<[locations count]; p++)
			{
				
				uniqueSamples *tempSample= [locations objectAtIndex:p];
				if((tempSample.coordinate.latitude ==tempCoordinate.coordinate.latitude) && (tempSample.coordinate.longitude == tempCoordinate.coordinate.longitude)) //this sample exists at a location where there is already a location
				{
					tempSample.count++;
					[tempSample setTitle:[[NSString alloc] initWithFormat:@"%d samples", tempSample.count]];
					[tempSample setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
					[tempSample.samples addObject:newAnnotation];
					[locations replaceObjectAtIndex:p withObject:tempSample];
					[tempSample setId:@"multiple samples."];
					added=TRUE;
				}
			}
			if(added==FALSE) //there is no sample at this location yet, so add it to the array
			{
				uniqueSamples *newSet=[uniqueSamples new];
				newSet.samples=[[NSMutableArray alloc] init];
				newSet.count=1;
				[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
				[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
				[newSet.samples addObject:newAnnotation];
				newSet.coordinate=tempCoordinate.coordinate;
				[newSet setId:sampleID];
				[locations addObject:newSet];
			}
		}
	}				if([elementName isEqualToString:@"rockType"])
	{
		if(rockFlag==FALSE)
		{
			currentRockType=currentStringValue;
			int x;
			bool flag=FALSE;
			for(x=0; x<[rockTypes count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[rockTypes objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					rockFlag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[rockTypes addObject:currentStringValue];
				currentStringValue=nil;
				rockFlag=TRUE;
				return;
			}
		}
	}
	if([elementName isEqualToString:@"minerals"])
	{
		//the end of the mineral list for this sample has been reached
		mineralFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"metamorphicGrades"])
	{
		metamorphicFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"boolean"])//indicates the sample's status as public or private
	{
		publicStatus=[[NSString alloc] initWithString:currentStringValue];
	}
	
}
	
-(void)setData:(NSString*)mylat:(NSString*)mylong{
	latitude= mylat;
	longitude= mylong;
}
	
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
}


- (void)dealloc{
	[currentStringValue release];
	[searchButton release];
	[radius release];
	[outputlabel release];
	[toolbar release];
    [super dealloc];
	[minerals release];
}


@end
