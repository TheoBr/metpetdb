
#import "MetamorphicGradeController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation MetamorphicGradeController
@synthesize output, sampleSelector, currentStringValue, toolbar, refineButton, gradeName, criteriaController;
@synthesize mapType;


-(void)viewDidLoad{
	//if the user has not yet specified any metamorphic grades, allocate the array
	if([currentMetamorphicGrades count]==0)
	{
		currentMetamorphicGrades=[[NSMutableArray alloc] init];
	}
	//make the toolbar and bar button to go back to the map and diplay only the samples with the specified metamorphic grade
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(refineSearch:)];
	[buttonArray addObject:refineButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.backgroundColor= [UIColor blackColor];
	toolbar.items= buttonArray;
	[self.view addSubview:toolbar];
	
	if([myMetamorphicGrades count]==0)//none of the samples in the specified search have a metamorphic grade listed, so display a message in the picker view
	{
		[myMetamorphicGrades addObject:@"No Metamorphic Grades"];
	}
	
	
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	
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
	if(gradeName==nil)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Select a Metamorphic Grade" message:@"Move the scroll wheel to select a metamorphic grade." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		//before adding this mineral to the array, loop through to make sure it has not already been added to the array
		int p;
		bool alreadyAdded=FALSE;
		for(p=0; p<[currentMetamorphicGrades count]; p++)
		{
			if([[currentMetamorphicGrades objectAtIndex:p] isEqualToString:gradeName])
			{
				alreadyAdded=TRUE; //this rock name is already in the array and should not be added again
			}
		}
		if(alreadyAdded==FALSE)
		{
			[currentMetamorphicGrades addObject:gradeName];
		}
		
		
		modifiedLocations=[[NSMutableArray alloc] init];
		//loop through the array containing the map annotations for the samples and only display the samples
		//that have a rockType corresponding to the rock type that was selected
		int x, y, z;
		bool added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		bool modified=FALSE;
		bool metGradeMatch=FALSE;
		for(x=0; x<[mylocations count]; x++) //first loop through the unique locations and remove any without the right rock type
		{
			modified=FALSE;
			uniqueSamples *group=[mylocations objectAtIndex:x];
			uniqueSamples *newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			//in case any of them have the specified rock type
			newgroup.count=0;
			newgroup.samples= [[NSMutableArray alloc] init];
			NSMutableArray *groupAnnotations= group.samples;
			for(y=0; y<[groupAnnotations count]; y++)
			{
				AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				NSMutableArray *tempGrades= [[NSMutableArray alloc] init];
				tempGrades=tempSample.metamorphicGrades;
				for(z=0; z<[tempGrades count]; z++)
				{
					metGradeMatch=FALSE;
					NSString *metGrade= [[NSString alloc] init];
					metGrade=[tempGrades objectAtIndex:z];
					if([metGrade isEqualToString:gradeName])
					{ 
						metGradeMatch=TRUE;
						if([group.id isEqualToString:@"multiple samples"])
						{
							
							modified=TRUE;
							newgroup.count++;
							[newgroup.samples addObject:tempSample];
							if(newgroup.count==1)
							{
								[newgroup setTitle:[[NSString alloc] initWithFormat:@"%d sample", newgroup.count]];
								newgroup.id=tempSample.id;
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
		} 
		[self backToCriteria];
	}		
}



- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
	return [myMetamorphicGrades count];
}
- (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [myMetamorphicGrades objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	gradeName=nil;
	gradeName= [myMetamorphicGrades objectAtIndex:row];
}
-(void)setData:(NSMutableArray*)originalData: (NSMutableArray*)locations:(NSString*)type
{
	mapType=type;
	original=originalData;
	mylocations=locations;
	bool added;
	int x, y, z, w;
	myMetamorphicGrades=[[NSMutableArray alloc] init];	
	//initially add a blank line so the user is forced to move the scroll wheel
	[myMetamorphicGrades addObject:@""];
	for(x=0; x<[locations count]; x++) //loop through each coordinate and consider each sample located there
	{
		uniqueSamples *samplesAtCoordinate= [locations objectAtIndex:x];
		NSMutableArray *annotationSamples=samplesAtCoordinate.samples;
		annotationSamples= samplesAtCoordinate.samples;
		
		for(y=0; y<[annotationSamples count]; y++) //loop through each of the samples at each coordinate and add the list of met. grades to the current list to be displayed
		{
			AnnotationObjects *tempAnnotation=[AnnotationObjects new];
			tempAnnotation=[annotationSamples objectAtIndex:y];
			NSMutableArray *metGradeArray=[[NSMutableArray alloc] init];
			metGradeArray= tempAnnotation.metamorphicGrades;
			for(z=0; z<[metGradeArray count]; z++)
			{
				if([myMetamorphicGrades count]==0)//automatically add the first metamorhpic grade since there is no chance it could be a duplicate
				{
					[myMetamorphicGrades addObject:[metGradeArray objectAtIndex:z]];
				}
				else
				{
					added=FALSE;
					for(w=0; w<[myMetamorphicGrades count]; w++) //we must loop throught the myMetamorphicGrades array to ensure we do not add any minerals twice
					{
						if([[myMetamorphicGrades objectAtIndex:w] isEqualToString:[metGradeArray objectAtIndex:z]])
						{
							added=TRUE;
						}
					}
					if(added==FALSE)//the metamorphic grade has not yet been added to the array, add it now
					{
						//this array will contain all the metamorphic grades in the current search  results.
						[myMetamorphicGrades addObject:[metGradeArray objectAtIndex:z]]; 
					}
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
	region= aregion;
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
