//
//  untitled.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/9/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapTypeController.h"


@implementation MapTypeController
@synthesize mapController, segControl, sampleCategory, searchCriteria;

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
	
	NSString *mapType = [CurrentSearchData getMapType];
	
	//highlight the segment representing the current map type
	if([mapType isEqualToString:@"map"]){
		[segControl setSelectedSegmentIndex:0];
	}
	else if([mapType isEqualToString:@"hybrid"]){
		[segControl setSelectedSegmentIndex:1];
	}
	else if([mapType isEqualToString:@"map"]){
		[segControl setSelectedSegmentIndex:2];
	}

	[segControl addTarget:self action:@selector(changeType) forControlEvents:UIControlEventValueChanged];

}

-(void)changeType
{
	
	if([segControl selectedSegmentIndex]==0)
	{
		[segControl setSelectedSegmentIndex:0];
		CurrentSearchData.mapType=[[NSString alloc] initWithString:@"map"];
	}
	else if([segControl selectedSegmentIndex]==1)
	{
		[segControl setSelectedSegmentIndex:1];
		CurrentSearchData.mapType=[[NSString alloc] initWithString:@"hybrid"];
	}
	else if([segControl selectedSegmentIndex]==2)
	{
		[segControl setSelectedSegmentIndex:2];
		CurrentSearchData.mapType=[[NSString alloc] initWithString:@"satellite"];
	}
		
	NSString *mapType = [CurrentSearchData getMapType];
	
		int segControlIndex = segControl.selectedSegmentIndex;
	

	if((mapType != nil) && [mapType isEqualToString:@"map"])
	{
		mapController.mapView.mapType=MKMapTypeStandard;
	}
	else if((mapType != nil) && [mapType isEqualToString:@"hybrid"])
	{
		mapController.mapView.mapType=MKMapTypeHybrid;
	}
	else if((mapType != nil) && [mapType isEqualToString:@"satellite"])
	{
		mapController.mapView.mapType=MKMapTypeSatellite;
	}

		
	[self loadMap];
}
-(IBAction)loadMap
{
	MapController *viewController=[[MapController alloc] initWithNibName:@"MapView" bundle:nil];
	[viewController setData:samples:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	self.mapController= viewController;
	[viewController release];
	UIView *newView=[mapController view];
	[self.view addSubview:newView];
	[mapController makeNavBar];
	[self.navigationController popToViewController:self animated:NO];
	[self.navigationController pushViewController:mapController animated:NO];

}
//the following 3 functions preserve the information needed by the map so no data is lost when the view is reloaded
-(void)setSamples:(NSMutableArray*)mySamples:criteria
{
	searchCriteria=criteria;
	samples=mySamples;
}

-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData= data;
}

- (void)dealloc {
	[segControl release];
	[mapController release];
	[samples release];
	[sampleCategory release];
    [super dealloc];
}


@end
