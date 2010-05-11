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
	[segControl addTarget:self action:@selector(changeType) forControlEvents:UIControlEventValueChanged];
	
	//highlight the segment representing the current map type
	if([currentSearchData.mapType isEqualToString:@"map"]){
		[segControl setSelectedSegmentIndex:0];
	}
	else if([currentSearchData.mapType isEqualToString:@"hybrid"]){
		[segControl setSelectedSegmentIndex:1];
	}
	else if([currentSearchData.mapType isEqualToString:@"map"]){
		[segControl setSelectedSegmentIndex:2];
	}
}
-(void)changeType
{
	if([segControl selectedSegmentIndex]==0)
	{
		[segControl setSelectedSegmentIndex:0];
		currentSearchData.mapType=[[NSString alloc] initWithString:@"map"];
	}
	else if([segControl selectedSegmentIndex]==1)
	{
		[segControl setSelectedSegmentIndex:1];
		currentSearchData.mapType=[[NSString alloc] initWithString:@"hybrid"];
	}
	else if([segControl selectedSegmentIndex]==2)
	{
		[segControl setSelectedSegmentIndex:2];
		currentSearchData.mapType=[[NSString alloc] initWithString:@"satellite"];
	}
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
