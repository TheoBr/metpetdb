
#import "RockTypeController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation RockTypeController
@synthesize output, sampleSelector, currentStringValue, rockName, criteriaController, refineButton, toolbar, region, newAnnotation, tempRock;
@synthesize group, newgroup, mapType;

-(void)viewDidLoad{
sampleSelector.showsSelectionIndicator=YES;
 sampleSelector.dataSource = self;
 sampleSelector.delegate=self;
	
	//if the user has not yet specified any rock type, allocate the array
	if([currentRockTypes count]==0)
	{
		currentRockTypes=[[NSMutableArray alloc] init];
	}
	
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(refineSearch:)];
	[buttonArray addObject:refineButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items= buttonArray;
	[self.view addSubview:toolbar];
	
	if([myRockTypes count]==0) //if the search has been narrowed and none of the remaining samples have a rock type listed, we still display something in the picker
	{
		[myRockTypes addObject:@"No Rock Types Listed"];
	}
 }
	
 - (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
 
 return 1;
 }
 - (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
 return [myRockTypes count];
 }
 - (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
 
 return [myRockTypes objectAtIndex:row];
 }
 - (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	rockName=nil;
	rockName=[[NSString alloc] initWithString:[myRockTypes objectAtIndex:row]];
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
	
-(IBAction)refineSearch:(id)sender
{
	if(rockName==nil)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Select a Rock Type" message:@"Move the scroll wheel to select a rock type." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		//before adding this rock type to the array, loop through to make sure it has not already been added to the array
		int p;
		bool alreadyAdded=FALSE;
		for(p=0; p<[currentRockTypes count]; p++)
		{
			if([[currentRockTypes objectAtIndex:p] isEqualToString:rockName])
			{
				alreadyAdded=TRUE; //this rock name is already in the array and should not be added again
			}
		}
		if(alreadyAdded==FALSE)
		{
			[currentRockTypes addObject:rockName];
		}		
		
		
		modifiedLocations=[[NSMutableArray alloc] init];
		//loop through the array containing the map annotations for the samples and only display the samples
		//that have a rockType corresponding to the rock type that was selected
		int x, y, z;
		bool added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		bool modified=FALSE;
		for(x=0; x<[myLocations count]; x++) //first loop through the unique locations and remove any without the right rock type
		{
			modified=FALSE;
			group=[myLocations objectAtIndex:x];
			newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			//in case any of them have the specified rock type
			newgroup.count=0;
			newgroup.samples= [[NSMutableArray alloc] init];
			NSMutableArray *groupAnnotations= group.samples;
			for(y=0; y<[groupAnnotations count]; y++)
			{
				AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				tempRock= [[NSString alloc] initWithString:tempSample.rockType];
				if([tempRock isEqualToString:rockName])
				{ 
					if([group.id isEqualToString:@"multiple samples"])
					{
						
						//modified=TRUE;
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
					else //there are multiple samples at this location, so we have to subtract them from the original number if not the specified rock type
					{
						[modifiedLocations addObject:group];
						added=TRUE;					
					}
				}
			}
			
			
		} 
		[self backToCriteria];
	}
}



//if the sample has the correct rock type, add it to the new array
//[modifiedLocations addObject:[myLocations objectAtIndex:x]];
-(void)setData:(NSMutableArray*) originalData:(NSMutableArray*)locations:(NSString*)type
{
	mapType=type;
	original=originalData; //we must pass the original search results so that the map can be reset
	myLocations=locations;
	myRockTypes=[[NSMutableArray alloc] init];
	
	//initially add a blank line so the user is forced to move the scroll wheel
	[myRockTypes addObject:@""];

	
	//create an array containing the minerals that are in the remaining samples after some search criteria have been provided
	int x, y, z, w;
	bool added;
	for(x=0; x<[myLocations count]; x++) //loop through each coordinate and consider each sample located there
	{
		uniqueSamples *samplesAtCoordinate= [locations objectAtIndex:x];
		NSMutableArray *annotationSamples=samplesAtCoordinate.samples;
		annotationSamples= samplesAtCoordinate.samples;
				
		for(y=0; y<[annotationSamples count]; y++) //loop through each of the samples at each coordinate and add the list of minerals to the current list to be displayed
		{
			AnnotationObjects *tempAnnotation=[AnnotationObjects new];
			tempAnnotation=[annotationSamples objectAtIndex:y];
			NSString *rock=[[NSMutableArray alloc] init];
			rock= tempAnnotation.rockType;
				if([myRockTypes count]==0)//automatically add the first mineral since there is no chance it could be a duplicate
				{
					[myRockTypes addObject:rock];
				}
				else
				{
					added=FALSE;
					for(w=0; w<[myRockTypes count]; w++) //we must loop throught the myMinerals array to ensure we do not add any minerals twice
					{
						if([[myRockTypes objectAtIndex:w] isEqualToString:rock])
						{
							added=TRUE;
						}
					}
					if(added==FALSE)//the mineral has not yet been added to the array, add it now
					{
						//this array will contain all the minerals in the current search  results.
						[myRockTypes addObject:rock]; 
					}
				}
		}
			
	}
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
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[sampleSelector release];

}
@end
