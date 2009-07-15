//
//  PublicStatusController.m
//  Location
//
//  Created by Heather Buletti on 5/29/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "PublicStatusController.h"
#import "uniqueSamples.h"


@implementation PublicStatusController
@synthesize label, toolbar, criteriaController, mapType;

-(void)viewDidLoad{
	currentPublicStatus=[[NSMutableArray alloc] init];
	items=[[NSMutableArray alloc] init];
	[items addObject:@"All"];
	//[items addObject:@"Public"];
	[items addObject:@"My Private"];
	segcontrol=[[UISegmentedControl alloc] initWithItems:items];
	CGRect frame = CGRectMake(0.0, 120.0, 300.0, 44.0);
	[segcontrol setFrame:frame];
	//[segcontrol setSelectedSegmentIndex:0];
	[segcontrol addTarget:self action:@selector(showPublic:) forControlEvents:UIControlEventValueChanged];
	[self.view addSubview:segcontrol];
	
}
-(void)backToCriteria
{
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:original:modifiedLocations:mapType];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
}
-(IBAction)showPublic:(id)sender{
	if([segcontrol selectedSegmentIndex]==0)
	{
		[segcontrol setSelectedSegmentIndex:0];
		[self showAll];
	}
	else if([segcontrol selectedSegmentIndex]==1)
	{
		[segcontrol setSelectedSegmentIndex:1];
		[self showPrivate];
	}
}
-(void)showAll
{
	//before adding the status to the array, make sure it has not already been added
	int q;
	bool alreadyAdded=FALSE;
	for(q=0; q< [currentPublicStatus count]; q++)
	{
		if([[currentPublicStatus objectAtIndex:q] isEqualToString:@"All samples"])
		{
			alreadyAdded=TRUE;
		}
	}
	if(alreadyAdded==FALSE)
	{
		[currentPublicStatus addObject:@"All samples"];
	}
	[self backToMap];
}
-(void)showPrivate{
		modifiedLocations=[[NSMutableArray alloc] init];
		//loop through the array containing the map annotations for the samples and only display the samples
		//that have a rockType corresponding to the rock type that was selected
		int x, y, z;
		bool added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		bool modified=FALSE;
		
		for(x=0; x<[myLocations count]; x++) //first loop through the unique locations and remove any without the right rock type
		{
			modified=FALSE;
			uniqueSamples *group=[myLocations objectAtIndex:x];
			uniqueSamples *newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			//in case any of them have the specified rock type
			newgroup.count=0;
			newgroup.samples= [[NSMutableArray alloc] init];
			NSMutableArray *groupAnnotations= group.samples;
			for(y=0; y<[groupAnnotations count]; y++)
			{
				AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				NSString *status= [[NSString alloc] initWithString:tempSample.publicData] ;
				if(![status isEqualToString:@"true"]) //if the status is not true, then this sample is private and should be displayed				{ 
				{
					if([group.title isEqualToString:@"multiple samples"])
					{
						modified=TRUE;
						newgroup.count++;
						[newgroup.samples addObject:tempSample];
						if(newgroup.count==1)
						{
							[newgroup setTitle:[[NSString alloc] initWithFormat:@"%d sample", newgroup.count]];
							newgroup.id= tempSample.id;
						}
						else
						{
							[newgroup setTitle:[[NSString alloc] initWithFormat:@"%d samples", newgroup.count]];
							newgroup.id=@"multiple samples";
						}
						newgroup.subtitle= @"Click for more info.";
						newgroup.coordinate=tempSample.coordinate;
						if(added==FALSE || modified==FALSE)
						{
							[modifiedLocations addObject:newgroup];
							added=TRUE;
							modified=TRUE;
						}
						else if(modified==TRUE)
						{
							//replace the group of samples that was at this coordinate with the new group of samples
							int index; //represents the current position in the modified locations array where the object should be added, for multiple samples it replaces the last object added
							index=[modifiedLocations count]-1; //last spot in the array
							[modifiedLocations replaceObjectAtIndex:index withObject:newgroup];
						}
					}
					else //there are not multiple samples at this location
					{
						[modifiedLocations addObject:group];
						added=TRUE;
					}
				}
			}
	} 
	//before adding the status to the array, make sure it has not already been added
	int q;
	bool alreadyAdded=FALSE;
	for(q=0; q< [currentPublicStatus count]; q++)
	{
		if([[currentPublicStatus objectAtIndex:q] isEqualToString:@"Private"])
		{
			alreadyAdded=TRUE;
		}
	}
	if(alreadyAdded==FALSE)
	{
		[currentPublicStatus addObject:@"Private"];
	}
	[self backToMap];
}

//the only array that needs to be passed is the one containing all the pin annotations since the 
//public status string is a member of the annotation class
-(void)setData:(NSMutableArray*)originalData:(NSMutableArray*)locations:(NSString*)type
{
	mapType=type;
	original=originalData; //we must pass the original search results so the map can be reset to its original values
	myLocations=locations;
}
-(void)setCurrentSearchData:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metgrades:(NSMutableArray*)public:(NSString*)aregion:(CLLocationCoordinate2D)coord
{
	currentRockTypes=rocks;
	currentMinerals=mins;
	currentMetamorphicGrades=metgrades;
	currentPublicStatus=public;
	region=aregion;
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
    [super dealloc];
}


@end
