
#import "MetamorphicGradeController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation MetamorphicGradeController
@synthesize output, sampleSelector, toolbar, refineButton, gradeName, criteriaController, grade;
@synthesize newgroup, group, searchCriteria;


-(void)viewDidLoad{
	//make the toolbar and bar button to go back to the map and diplay only the samples with the specified metamorphic grade
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(backToCriteria)];
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
	[myMetamorphicGrades sortUsingSelector:@selector(compare:)];
	
	gradeName=[myMetamorphicGrades objectAtIndex:0];
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	
	
}
-(void)backToCriteria
{
	//before adding the new metamorphic grade to the array, loop through to make sure it has not already been added to the currentMetamorphicGrades array
	int p;
	bool alreadyAdded=FALSE;
	for(p=0; p<[[currentSearchData metamorphicGrades] count]; p++)
	{
		if([[[currentSearchData metamorphicGrades] objectAtIndex:p] isEqualToString:gradeName])
		{
			alreadyAdded=TRUE; //this metamorphic grade is already in the array and should not be added again
		}
	}
	if(alreadyAdded==FALSE)
	{
		[[currentSearchData metamorphicGrades] addObject:gradeName];
	}		
	
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:modifiedLocations2:searchCriteria];
	[viewController setCurrentSearchData: currentSearchData];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
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
-(void)setData:(NSMutableArray*)locations: (NSMutableArray*)metgrades:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	mylocations=locations;
	myMetamorphicGrades=metgrades;
}
-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[sampleSelector release];
	[output release];
	[myMetamorphicGrades release];
	[currentStringValue release];
	[newAnnotation release];
	[toolbar release];
	[mylocations release];
	[modifiedLocations release];
	[refineButton release];
	[criteriaController release];
	[gradeName release];
	[points release];
	[modifiedLocations2 release];
	[grade release];
	[newgroup release];
	[group release];
	[searchCriteria release];
	
	
}
@end
