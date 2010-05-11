
#import "RockTypeController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation RockTypeController
@synthesize output, rockSelector, currentStringValue, rockName, criteriaController, refineButton, toolbar, newAnnotation, tempRock;
@synthesize group, newgroup, searchCriteria;

-(void)viewDidLoad
{
	rockSelector.showsSelectionIndicator=YES;
	rockSelector.dataSource = self;
	rockSelector.delegate=self;
	
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(refineSearch)];
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
	[myRockTypes sortUsingSelector:@selector(compare:)];
	rockName=[myRockTypes objectAtIndex:0];
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)rockSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)rockSelector numberOfRowsInComponent:(NSInteger)component {
	return [myRockTypes count];
}
- (NSString *)pickerView:(UIPickerView *)rockSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [myRockTypes objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)rockSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	rockName=nil;
	rockName=[[NSString alloc] initWithString:[myRockTypes objectAtIndex:row]];
}


-(void)refineSearch
{
	//before adding this rock type to the array, loop through to make sure it has not already been added to the array
	int p;
	bool alreadyAdded=FALSE;
	for(p=0; p<[[currentSearchData rockTypes] count]; p++)
	{
		if([[[currentSearchData rockTypes] objectAtIndex:p] isEqualToString:rockName])
		{
			alreadyAdded=TRUE; //this rock name is already in the array and should not be added again
		}
	}
	if(alreadyAdded==FALSE)
	{
		[[currentSearchData rockTypes] addObject:rockName];
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

-(void)setData:(NSMutableArray*)locations: (NSMutableArray*)rocks:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	myRockTypes=rocks;
	myLocations=locations;
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
	[rockSelector release];
	[output release];
	[myLocations release];
	[currentStringValue release]; 
	[rockName release];
	[newAnnotation release];
	[myRockTypes release];
	[criteriaController release];
	[toolbar release];
	[refineButton release];
	[modifiedLocations release];
	
	[tempRock release];
	[group release];
	[newgroup release];
	[points release];
	[searchCriteria release];
	
}
@end
