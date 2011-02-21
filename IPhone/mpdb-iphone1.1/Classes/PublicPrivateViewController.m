//
//  PublicPrivate.m
//  MetPetDB
//
//  Created by MetPetDB on 3/2/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "PublicPrivateViewController.h"

@implementation PublicPrivateViewController
@synthesize segControl, criteriaController, searchCriteria, group, newgroup, newAnnotation;
@synthesize refineButton, Uname, okButton;
-(void)viewDidLoad{
	NSMutableArray *segControlItems=[[NSMutableArray alloc] init];
	[segControlItems addObject:@"Public"];
	[segControlItems addObject:@"Private"];
	[segControlItems addObject:@"Both"];
	//set the highlighted section depending on currentPublicStatus
	
	segControl=[[UISegmentedControl alloc] initWithItems:segControlItems];
	CGRect frame = CGRectMake(0.0, 120.0, 320.0, 44.0);
	[segControl setFrame:frame];
	segControl.segmentedControlStyle= UISegmentedControlStylePlain;
	if([[CurrentSearchData getCurrentPublicStatus] isEqualToString:@"public"])
	{
		[segControl setSelectedSegmentIndex:0];
	}
	else if([[CurrentSearchData getCurrentPublicStatus] isEqualToString:@"private"])
	{
		[segControl setSelectedSegmentIndex:1];
	}
	else if([[CurrentSearchData getCurrentPublicStatus]
			 isEqualToString:@"both"])
	{
		[segControl setSelectedSegmentIndex:2];
	}	
	
	[segControl addTarget:self action:@selector(changeStatus:) forControlEvents:UIControlEventValueChanged];
	[self.view addSubview:segControl];
	
}
-(IBAction)changeStatus:(id)sender{
	if([segControl selectedSegmentIndex]==0)
	{
		[CurrentSearchData setCurrentPublicStatus:@"public"];
		[segControl setSelectedSegmentIndex:0];
	}
	else if([segControl selectedSegmentIndex]==1)
	{
		[CurrentSearchData setCurrentPublicStatus:@"private"];
		[segControl setSelectedSegmentIndex:1];
	}
	else if([segControl selectedSegmentIndex] == 2)
	{
		[CurrentSearchData setCurrentPublicStatus:@"both"];
		[segControl setSelectedSegmentIndex:2];
	}
	//after the user has selected whether they want to view private, public, or both types of samples, they should be taken back to the search criteria view
	//[self backToCriteria];
	
}
-(IBAction)backToCriteria{
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:myLocations:searchCriteria];
	//[viewController setCurrentSearchData:currentSearchData];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
	
	
	
	
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


-(void)setData:(NSMutableArray*)locations:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	myLocations=locations;
}
/*
-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
}*/



- (void)dealloc {
    [super dealloc];
}


@end
