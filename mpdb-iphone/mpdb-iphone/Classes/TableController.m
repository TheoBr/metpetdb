//
//  TableController.m
//  Location
//
//  Created by Heather Buletti on 5/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "TableController.h"
#import "MapController.h"
#import "RockTypeController.h"
#import "uniqueSamples.h"

@implementation TableController
@synthesize tableView, rockTypeController, metamorphicGradeController, publicStatusController, mineralsController, mapController;
@synthesize mapType, region;


-(void)viewDidLoad{
	rows= [[NSMutableArray alloc]init];
	[rows addObject:@"Rock Type"];
	[rows addObject:@"Minerals"];
	[rows addObject:@"Metamorphic Grade"];
	//[rows addObject:@"Public/Private status"];
	tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
                                             style:UITableViewStylePlain];
    tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;

	tableView.dataSource=self;
	tableView.delegate=self;


}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [rows count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
   UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"My Identifier"];
	//UITableViewCell *cell= [[UITableViewCell alloc] init];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:@"My Identifier"] autorelease];
    }
    // Get the section index, and so the region dictionary for that section
	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	cell.text= [rows objectAtIndex:indexPath.row];
	   	return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	//if the button corresponding with rocktype is pressed, load the corresponding view
	if(indexPath.row==0)
	{
		[self loadRockTypeView];
	}
	//if the button corresponding with minerals is pressed, load the corresponding view
	else if(indexPath.row==1)
	{
		[self loadMineralsView];
	}
	//if the button corresponding to mineral grade is pressed, load the corresponding view
	else if(indexPath.row==2)
	{
		[self loadMetamorphicGradeView];
	}
	//if the button corresponding to public status is pressed, load the corresponding view
	else if(indexPath.row==3)
	{
		[self loadPublicController];
	}

		
}


//function to load the Minerals view
-(void)loadMineralsView
{
	MineralsController *viewController= [[MineralsController alloc] initWithNibName:@"MineralsView" bundle:nil];
	[viewController setData:original:locations:mapType];	
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	self.mineralsController=viewController;
	[viewController release];
	UIView *ControllersView =[mineralsController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:mineralsController animated:NO];
}
//function to load the MineralsGrade view
-(void)loadMetamorphicGradeView{

	MetamorphicGradeController *viewController=[[MetamorphicGradeController alloc] initWithNibName:@"MetamorphicGradeView" bundle:nil];
	[viewController setData:original:locations:mapType];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	self.metamorphicGradeController= viewController;
	[viewController release];
	UIView *ControllersView=[metamorphicGradeController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:metamorphicGradeController animated:NO];
}
//function to load the RockType view
-(void)loadRockTypeView{
	
		RockTypeController *viewController = [[RockTypeController alloc] initWithNibName:@"RockTypeView" bundle:nil];
	//all of the following values must be passed so they can be passed back to the map straight from the rock view
	[viewController setData:original:locations:mapType];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
		self.rockTypeController = viewController;
		[viewController release];
		UIView *ControllersView = [rockTypeController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:rockTypeController animated:NO];
}
-(void)loadPublicController
{
	PublicStatusController *viewController = [[PublicStatusController alloc] initWithNibName:@"PublicStatusView" bundle:nil];
	[viewController setData:original:locations:mapType];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	self.publicStatusController=viewController;
	[viewController release];
	UIView *ControllersView = [publicStatusController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:publicStatusController animated:NO];
}

/*-(void)clearSearch{
	//before the map is loaded, find the center coordinate and span for the zoom
	//to find the center, average all the latitudes and then all the longitudes and that will be the coordinate
	int p,q;
	bool flag= FALSE;
	//place all the latitudes and longitudes in an array and then use all the numbers to take obtain the average lat and long which will be the center coordinate
	NSMutableArray *lats=[[NSMutableArray alloc] init];
	NSMutableArray *longs=[[NSMutableArray alloc] init];
	NSMutableArray *coords=[[NSMutableArray alloc] init];
	double averageLat=0;
	double averageLong=0;
	double maxLat, maxLong, minLat, minLong;
	for(p=0; p<[original count]; p++)
	{
		uniqueSamples *sampleGroup=[uniqueSamples new];
		sampleGroup=[original objectAtIndex:p];
		NSMutableArray *tempSamples=sampleGroup.samples;
		for(q=0; q<[tempSamples count]; q++)
		{
			AnnotationObjects *annotation=[AnnotationObjects new];
			annotation=[tempSamples objectAtIndex:q];
			//we do not want to consider the current location in the calculations of min, max or average location
			[coords addObject:annotation];
			//initially set the max and min and compare all later ones to these numbers
			if(flag==FALSE)
			{
				maxLat=annotation.coordinate.latitude;
				minLat= annotation.coordinate.latitude;
				maxLong=annotation.coordinate.longitude;
				minLong= annotation.coordinate.longitude;
				flag=TRUE;
			}
			else
			{
				//set the max and min lats and longs so that the span can later be determined
				if(annotation.coordinate.latitude > maxLat)
				{
					maxLat=annotation.coordinate.latitude;
				}
				if(annotation.coordinate.latitude < minLat)
				{
					minLat= annotation.coordinate.latitude;
				}
				if(annotation.coordinate.longitude > maxLong)
				{
					maxLong= annotation.coordinate.longitude;
				}
				if(annotation.coordinate.longitude < minLong)
				{
					minLong= annotation.coordinate.longitude;
				}
			}
		}
	}
	//obtain the average lat and long and make then the center coordinate
	int x,y;
	for(x=0; x<[coords count]; x++)
	{
		AnnotationObjects *annot=[coords objectAtIndex:x];
		CLLocationCoordinate2D coordinate=annot.coordinate;
		double tempLat=coordinate.latitude;
		double tempLong=coordinate.longitude;
		averageLong= averageLong+ tempLong;
		averageLat= averageLat+ tempLat;
	}
	averageLat=averageLat/[coords count];
	averageLong=averageLong/[coords count];
	center.latitude= averageLat;
	center.longitude= averageLong;
	latSpan= maxLat - minLat;
	longSpan= maxLong- minLong;

	
	//this flag is false becasue we do not want to overwrite the original location data we obtained so that it can be restored if needed
	MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
	[viewController setData:original:original]; //pass the new data back to the map
	[viewController setCoordinate:center:latSpan:longSpan];
	self.mapController = viewController;
	[viewController release];
	UIView *ControllersView = [mapController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:mapController animated:NO];
}*/

//set the various arrays in this controller with the information about the samples
-(void)setData:(NSMutableArray*)originalData:(NSMutableArray*)sampleLocations:(NSString*)type
{
	original=originalData;
	locations= sampleLocations;
	mapType=type;
}
-(void)setCurrentSearchData:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metgrades:(NSMutableArray*)public:(NSString*)aregion:(CLLocationCoordinate2D)coord
{
	currentRockTypes=rocks;
	currentMinerals=mins;
	currentMetamorphicGrades=metgrades;
	currentPublicStatus=public;
	region= aregion;
	myCoordinate= coord;
}
- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
	[locations release];
	[rows release];
	[rockTypeController release];
	[metamorphicGradeController release];
	[mineralsController release];
	[publicStatusController release];
	[mapController release];
	[super dealloc];
}


@end
