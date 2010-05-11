//
//  OwnerViewController.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "OwnerViewController.h"


@implementation OwnerViewController
@synthesize output, sampleSelector, currentStringValue, ownerName, criteriaController, refineButton, toolbar,newAnnotation, tempOwner;
@synthesize group, newgroup, searchCriteria;


-(void)viewDidLoad{
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	//	[sampleSelector selectRow:1 inComponent:0 animated:NO];
	[owners sortUsingSelector:@selector(compare:)];
	ownerName=[owners objectAtIndex:0];
	
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(backToCriteria)];
	[buttonArray addObject:refineButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items= buttonArray;
	[self.view addSubview:toolbar];
	
	if([owners count]==0) //if the search has been narrowed and none of the remaining samples have a rock type listed, we still display something in the picker
	{
		[owners addObject:@"No Owners Listed"];
	}
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
	return [owners count];
}
- (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [owners objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	ownerName=nil;
	ownerName=[[NSString alloc] initWithString:[owners objectAtIndex:row]];
}


-(void)backToCriteria
{
	
	//before adding the new owner to the array, loop through to make sure it has not already been added to the currentOwners array
	int p;
	bool alreadyAdded=FALSE;
	for(p=0; p<[[currentSearchData owners] count]; p++)
	{
		if([[[currentSearchData owners] objectAtIndex:p] isEqualToString:ownerName])
		{
			alreadyAdded=TRUE; //this owner name is already in the array and should not be added again
		}
	}
	if(alreadyAdded==FALSE)
	{
		[[currentSearchData owners] addObject:ownerName];
	}	
	
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:modifiedLocations:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
}
//if the sample has the correct rock type, add it to the new array

-(void)setData:(NSMutableArray*)locations: (NSMutableArray*)people:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	myLocations=locations;
	owners=people;
}

-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData= data;
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
	[sampleSelector release];
	[output release];
	[myLocations release];
	[currentStringValue release]; 
	[ownerName release];
	[newAnnotation release];
	[owners release];
	[criteriaController release];
	[toolbar release];
	[refineButton release];
	[modifiedLocations release];
	[tempOwner release];
	[group release];
	[newgroup release];
	[mapType release];
	[points release];
	[searchCriteria release];
	
    [super dealloc];
}


@end
