
#import "MineralsController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@implementation MineralsController
@synthesize output, sampleSelector, currentStringValue, mineralName, toolbar, refineButton, criteriaController;
@synthesize min, newgroup, searchCriteria;



-(void)viewDidLoad{
	modifiedLocations=[[NSMutableArray alloc] init];
	
	
	//make the toolbar and bar button to go back to the map and diplay only the samples with the specified metamorphic grade
	NSMutableArray *buttonArray=[[NSMutableArray alloc] init];
	refineButton=[[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStyleBordered target:self action:@selector(backToCriteria)];
	[buttonArray addObject:refineButton];
//	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
//	toolbar = [ [ UIToolbar alloc ] init ];
//	toolbar.frame = toolBarFrame;
//	toolbar.items= buttonArray;
//	[self.view addSubview:toolbar];
	
	
	
	[self.navigationController setToolbarHidden:NO animated:YES];
	[self.navigationController.toolbar setBarStyle:UIBarStyleBlack];

	[self setToolbarItems:buttonArray animated:YES];
	
	
	if([myMinerals count] ==0) //the search has been narrowed and there are now no samples that have minerals listed
	{
		[myMinerals addObject:@"No minerals listed."];
	}
	[myMinerals sortUsingSelector:@selector(compare:)];
	
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	mineralName=[myMinerals objectAtIndex:0];
	
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
	//before adding the new mineral to the array, loop through to make sure it has not already been added to the currentMinerals array
	int p;
	bool alreadyAdded=FALSE;
	for(p=0; p<[[CurrentSearchData getMinerals] count]; p++)
	{
		if([[[CurrentSearchData getMinerals] objectAtIndex:p] isEqualToString:mineralName])
		{
			alreadyAdded=TRUE; //this mineral is already in the array and should not be added again
		}
	}
	if(alreadyAdded==FALSE)
	{
		[[CurrentSearchData getMinerals] addObject:mineralName];
	}		
	
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:modifiedLocations2:searchCriteria];
	//[viewController setCurrentSearchData:currentSearchData];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
	
}	

-(void)setData:(NSMutableArray*)locations: (NSMutableArray*)mins:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	myMinerals=mins;
	myLocations=locations;
}
/*
-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
}*/

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[criteriaController release];
	[sampleSelector release];
	[output release];
	[myMinerals release];
	[currentStringValue release];
	[newAnnotation release];
	[myLocations release];
	[modifiedLocations release];
	[modifiedLocations2 release];
	[mineralName release];
	//[toolbar release];
	[refineButton release];
	[points release];
	[min release];
	[newgroup release];
	[searchCriteria release];
	
	
}
@end
