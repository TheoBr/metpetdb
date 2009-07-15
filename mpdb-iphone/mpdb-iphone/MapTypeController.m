//
//  untitled.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/9/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapTypeController.h"


@implementation MapTypeController
@synthesize mapController, segControl, mapType;

-(void)viewDidLoad
{
	NSMutableArray *items=[[NSMutableArray alloc] init];
	[items addObject:@"Map"];
	[items addObject:@"Hybrid"];
	[items addObject:@"Satellite"];
	segControl=[[UISegmentedControl alloc] initWithItems:items];
	CGRect frame = CGRectMake(10.0, 120.0, 300.0, 50.0);
	[segControl setFrame:frame];
	[self.view addSubview:segControl];
	[segControl addTarget:self action:@selector(changeType) forControlEvents:UIControlEventValueChanged];
}
-(void)changeType
{
	if([segControl selectedSegmentIndex]==0)
	{
		[segControl setSelectedSegmentIndex:0];
		mapType=[[NSString alloc] initWithString:@"map"];
	}
	else if([segControl selectedSegmentIndex]==1)
	{
		[segControl setSelectedSegmentIndex:1];
		mapType=[[NSString alloc] initWithString:@"hybrid"];
	}
	else if([segControl selectedSegmentIndex]==2)
	{
		[segControl setSelectedSegmentIndex:2];
		mapType=[[NSString alloc] initWithString:@"satellite"];
	}
	[self loadMap];
}
-(void)loadMap
{
	MapController *viewController=[[MapController alloc] initWithNibName:@"MapView" bundle:nil];
	[viewController setType:mapType];
	[viewController setData:originalData :samples];
	[viewController setCoordinate:myLocation :latitudeSpan :longitudeSpan:maxLat:minLat:maxLong:minLong];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus :region :myLocation];
	self.mapController= viewController;
	[viewController release];
	UIView *newView=[mapController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:mapController animated:NO];
}
//the following 3 functions preserve the information needed by the map so no data is lost when the view is reloaded
-(void)setSamples:(NSMutableArray*)mySamples:(NSMutableArray*)original
{
	samples=mySamples;
	originalData=original;
}
-(void)setCoordinate:(CLLocationCoordinate2D)center:(double)latSpan:(double) longSpan:(double)latmax:(double)latmin:(double)longmax:(double)longmin
{
	//this function gets called the first time the map loads and it provides the center coordinate and zoom for the initial map view
	myLocation=center;
	//the span is a value in degrees that is used to set the zoom on the map view when it first appears
	latitudeSpan= latSpan; 
	longitudeSpan= longSpan;    
	maxLat=latmax;
	minLat= latmin;
	maxLong= longmax;
	minLong= longmin;
}


-(void)setCurrentSearchData:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metGrades:(NSMutableArray*)public:(NSString*)aRegion:(CLLocationCoordinate2D)coord
{
	currentRockTypes=rocks; 
	currentMinerals=mins; 
	currentMetamorphicGrades=metGrades; 
	currentPublicStatus=public; 
	myLocation=coord;
	region=aRegion;
}

- (void)dealloc {
    [super dealloc];
}


@end
