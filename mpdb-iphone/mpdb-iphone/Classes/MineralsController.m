
#import "MineralsController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation MineralsController
@synthesize output, sampleSelector, currentStringValue, toolbar, mineralName, toolbar, refineButton, region, criteriaController;
@synthesize mapType, min, newgroup;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
	//	[self.navigationController pushViewController:self animated:NO];
	}
}


-(void)viewDidLoad{
	modifiedLocations=[[NSMutableArray alloc] init];
	
	//if the user has not yet specified any minerals, allocate the array
	if([currentMinerals count]==0)
	{
		currentMinerals =[[NSMutableArray alloc] init];
	}
	//make the toolbar and bar button to go back to the map and diplay only the samples with the specified metamorphic grade
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(refineSearch)];
	[buttonArray addObject:refineButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items= buttonArray;
	[self.view addSubview:toolbar];
	
	if([myMinerals count] ==0) //the search has been narrowed and there are now no samples that have minerals listed
	{
		[myMinerals addObject:@"No minerals listed."];
	}
	
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
	return [myMinerals count];
}
- (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [myMinerals objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	mineralName=nil;
	mineralName= [myMinerals objectAtIndex:row];
}

-(void)backToCriteria
{
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:original:modifiedLocations2:mapType:points];
	[viewController setCurrentSearchData:currentOwners:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
}	
-(void)refineSearch
{
	if(mineralName==nil)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Select a Mineral" message:@"Move the scroll wheel to select a mineral." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		//before adding this mineral to the array, loop through to make sure it has not already been added to the array
		int p;
		bool alreadyAdded=FALSE;
		for(p=0; p<[currentMinerals count]; p++)
		{
			if([[currentMinerals objectAtIndex:p] isEqualToString:mineralName])
			{
				alreadyAdded=TRUE; //this rock name is already in the array and should not be added again
			}
		}
		if(alreadyAdded==FALSE)
		{
			[currentMinerals addObject:mineralName];
		}

		
		modifiedLocations=[[NSMutableArray alloc] init];
		modifiedLocations2=[[NSMutableArray alloc] init];
			
		int x, y, z, w;
		bool added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		bool modified=FALSE;
		
		//first loop through and narrow down based on other search criteria, not including minerals
		for(x=0; x<[original count]; x++)
		//for(x=0; x<[myLocations count]; x++)
		{
			modified=FALSE;
			uniqueSamples *group=[original objectAtIndex:x];
			//uniqueSamples *group= [myLocations objectAtIndex:x];
			newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			//in case any of them have the specified rock type
			newgroup.count=0;
			newgroup.samples= [[NSMutableArray alloc] init];
			NSMutableArray *groupAnnotations= group.samples;
			for(y=0; y<[groupAnnotations count]; y++)
			{
				AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				NSMutableArray *tempMinerals= [[NSMutableArray alloc] init];
				tempMinerals=tempSample.minerals;
				NSMutableArray *tempMetGrades=[[NSMutableArray alloc] init];
				tempMetGrades=tempSample.metamorphicGrades;
				NSString *tempRockType=[[NSString alloc] initWithFormat:@"%@", tempSample.rockType];
				NSString *tempOwner=[[NSString alloc] initWithFormat:@"%@", tempSample.owner];
				bool addSampleToMap1=FALSE;
				bool addSampleToMap2= FALSE;
				bool addSampleToMap3= FALSE;
				//for all of the following, a sample is only added if ALL of the criteria that have been specified are true
				if([currentRockTypes count]==0)
				{
					addSampleToMap1=TRUE;
				}
				else
				{
					for(z=0; z<[currentRockTypes count]; z++)
					{
						if([[currentRockTypes objectAtIndex:z] isEqualToString:tempRockType])
						{
							addSampleToMap1=TRUE;
						}
					}
				}
				if([currentOwners count]==0)
				{
					addSampleToMap2= TRUE;
				}
				else
				{
					for(z=0; z<[currentOwners count]; z++)
					{
						if([[currentOwners objectAtIndex:z] isEqualToString:tempOwner])
						{
							addSampleToMap2=TRUE;
						}
					}
				}
				if([currentMetamorphicGrades count]==0)
				{
					addSampleToMap3=TRUE;
				}
				else
				{
					for(z=0; z<[currentMetamorphicGrades count]; z++)
					{
						for(w=0; w<[tempMetGrades count]; w++)
						{
							if([[currentMetamorphicGrades objectAtIndex:z] isEqualToString:[tempMetGrades objectAtIndex:w]])
							{
								
								addSampleToMap3=TRUE;
							}
						}
					}
				}
				if(addSampleToMap1==TRUE && addSampleToMap2==TRUE && addSampleToMap3==TRUE)
				{ 
					if([group.id isEqualToString:@"multiple samples"])
					{
						newgroup.count++;
						[newgroup.samples addObject:tempSample];
						newgroup.count=[newgroup.samples count];
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
							index=[modifiedLocations count];
							index--;
							//[remainingLocations replaceObjectAtIndex:index withObject:newgroup];
							[modifiedLocations removeObjectAtIndex:index];
							[modifiedLocations addObject:newgroup];
						}
						
					}
					else //there are multiple samples at this location, so we have to subtract them from the original number if not the specified rock type
					{
						[modifiedLocations addObject:group];
						added=TRUE;
					}
				}
			}
		}
		//loop through the array containing the map annotations for the samples and only display the samples with specified mineral(s)
		added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		 modified=FALSE;
		 bool minAdded=FALSE;
		if([modifiedLocations count]==0)
		{
			modifiedLocations=original;
		}
		 
		 for(x=0; x<[modifiedLocations count]; x++) //first loop through the unique locations and remove any without the right rock type
		 //for(x=0; x<[original count]; x++)
		 {
			 modified=FALSE;
			 uniqueSamples *group=[modifiedLocations objectAtIndex:x];
			 //uniqueSamples *group= [original objectAtIndex:x];
			 newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			 //in case any of them have the specified rock type
			 newgroup.count=0;
			 newgroup.samples= [[NSMutableArray alloc] init];
			 NSMutableArray *groupAnnotations= group.samples;
			 for(y=0; y<[groupAnnotations count]; y++)
			 {
				 minAdded=FALSE;
				 AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				 NSMutableArray *tempMinerals= [[NSMutableArray alloc] init];
				 tempMinerals=tempSample.minerals;
				 for(z=0; z<[tempMinerals count]; z++)
				 {
					 min=[tempMinerals objectAtIndex:z];
					 for(int r=0; r<[currentMinerals count]; r++)
					 {
						 NSString *minName=[currentMinerals objectAtIndex:r];
						 if([min isEqualToString:minName] && minAdded==FALSE)
						 { 
							 minAdded=TRUE;
							 if([group.id isEqualToString:@"multiple samples"])
							 {
								 
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
									 [modifiedLocations2 addObject:newgroup];
									 added=TRUE;
									 modified=TRUE;
								 }
								 else if(modified==TRUE)
								 {
									 //replace the group of samples that was at this coordinate with the new group of samples
									 index=[modifiedLocations2 count]-1; //last spot in the array
									 //[modifiedLocations replaceObjectAtIndex:index withObject:newgroup];
									 [modifiedLocations2 removeObjectAtIndex:index];
									 [modifiedLocations2 addObject:newgroup];
								 }
							 }
							 else //there are not multiple samples at this location						{
							 {
								 [modifiedLocations2 addObject:group];
								 added=TRUE;
							 }
							 break;
							 break;
						 }
					 }
				 }
			 }
		 } 
		 
	}
	[self backToCriteria];
}

-(void)setData:(NSMutableArray*)originalData: (NSMutableArray*)locations:(NSString*)type:(NSMutableArray*)LatLongPoints
{
	mapType=type;
	original=originalData; //pass the array with the original search results so the map can be reset
	myLocations=locations;
	if([LatLongPoints count]!=0)
	{
		points= LatLongPoints;
	}

	//create an array containing the minerals that are in the remaining samples after some search criteria have been provided
	int x, y, z, w;
	bool added;
	myMinerals=[[NSMutableArray alloc] init];
	//initally add a blank line so the user is forced to move the scroll wheel
	[myMinerals addObject:@""];
	for(x=0; x<[locations count]; x++) //loop through each coordinate and consider each sample located there
	{
		uniqueSamples *samplesAtCoordinate= [locations objectAtIndex:x];
		NSMutableArray *annotationSamples=samplesAtCoordinate.samples;
		annotationSamples= samplesAtCoordinate.samples;
		
		for(y=0; y<[annotationSamples count]; y++) //loop through each of the samples at each coordinate and add the list of minerals to the current list to be displayed
		{
			AnnotationObjects *tempAnnotation=[AnnotationObjects new];
			tempAnnotation=[annotationSamples objectAtIndex:y];
			NSMutableArray *minArray=[[NSMutableArray alloc] init];
			minArray= tempAnnotation.minerals;
			for(z=0; z<[minArray count]; z++)
			{
				if([myMinerals count]==0)//automatically add the first mineral since there is no chance it could be a duplicate
				{
					[myMinerals addObject:[minArray objectAtIndex:z]];
				}
				else
				{
					added=FALSE;
					for(w=0; w<[myMinerals count]; w++) //we must loop throught the myMinerals array to ensure we do not add any minerals twice
					{
						if([[myMinerals objectAtIndex:w] isEqualToString:[minArray objectAtIndex:z]])
						{
							added=TRUE;
						}
					}
					if(added==FALSE)//the mineral has not yet been added to the array, add it now
					{
						//this array will contain all the minerals in the current search  results.
						[myMinerals addObject:[minArray objectAtIndex:z]]; 
					}
				}
					
			}
		
		}
	}
}
-(void)setCurrentSearchData:(NSMutableArray*)owners:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metgrades:(NSMutableArray*)public:(NSString*)aregion:(CLLocationCoordinate2D)coord
{
	currentOwners=owners;
	currentRockTypes=rocks;
	currentMinerals=mins;
	currentMetamorphicGrades=metgrades;
	currentPublicStatus=public;
	region=aregion;
	myCoordinate=coord;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[sampleSelector release];
	
}
@end
