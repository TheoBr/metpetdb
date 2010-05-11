//
//  TableController.m
//  Location
//
//  Created by Heather Buletti on 5/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "TableController.h"
#import "MapController.h"
#import "RockTypeController.h"
#import "uniqueSamples.h"
#import "KeychainWrapper.h"

@implementation TableController
@synthesize tableView, rockTypeController, metamorphicGradeController, mineralsController, mapController, ownerController;
@synthesize searchCriteria, Uname, publicPrivateController;


-(void)viewDidLoad{
	rows= [[NSMutableArray alloc]init];
	[rows addObject:@"Rock Type"];
	[rows addObject:@"Minerals"];
	[rows addObject:@"Metamorphic Grade"];
	[rows addObject:@"Sample Owner"];
	//only display a view for public/private refinement if the user is logged in
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
											style:UITableViewStylePlain];
    tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [rows count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"My Identifier"];
	//UITableViewCell *cell= [[UITableViewCell alloc] init];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:@"My Identifier"] autorelease];
    }
    // Get the section index, and so the region dictionary for that section
	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	cell.text= [rows objectAtIndex:indexPath.row];
	return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	//if the button corresponding with rocktype is pressed, load the corresponding view
	if(indexPath.row==0)
	{
		[self loadRockTypeView];
	}
	//if the button corresponding with minerals is pressed, load the corresponding view
	else if(indexPath.row==1)
	{
		[self loadMineralsView];
	}
	//if the button corresponding to mineral grade is pressed, load the corresponding view
	else if(indexPath.row==2)
	{
		[self loadMetamorphicGradeView];
	}
	//if the button corresponding to sample owner is pressed, load the corresponding row
	if(indexPath.row ==3)
	{
		[self loadOwnerView];
	}
	else if(indexPath.row == 4)
	{
		[self loadPublicPrivateView];
	}
	[tableView  deselectRowAtIndexPath:indexPath  animated:NO]; 
	
	
}
//function to load the view to specify public/private status
/*-(void)loadPublicPrivateView
 {
 //if the user was able to select this option they are logged in, so no alert needs to be displayed
 PublicPrivateViewController *viewController= [[PublicPrivateViewController alloc] initWithNibName:@"PublicPrivateView" bundle:nil];
 [viewController setData:locations:mapType:visibleLocation:searchCriteria:originalCoordinates];
 [viewController setCurrentSearchData:currentOwners:currentRockTypes:currentMinerals:currentMetamorphicGrades:currentPublicStatus:region:myCoordinate];
 self.publicPrivateController = viewController;
 [viewController release];
 UIViewController *ControllersView= [publicPrivateController view];
 [self.view addSubview:ControllersView];
 [self.navigationController pushViewController:publicPrivateController animated:NO];
 }*/

//function to load the Minerals view
-(void)loadMineralsView
{
	int x;
	NSMutableArray *myMinerals=searchCriteria.minerals;
	if([myMinerals count]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Minerals." message:@"You may not narrow your search on minerals." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		
		MineralsController *viewController= [[MineralsController alloc] initWithNibName:@"MineralsView" bundle:nil];
		[viewController setData:locations:myMinerals:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.mineralsController=viewController;
		[viewController release];
		UIView *ControllersView =[mineralsController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:mineralsController animated:NO];
	}
}
//function to load the MineralsGrade view
-(void)loadMetamorphicGradeView{
	bool added;
	int x;
	NSMutableArray *myMetamorphicGrades= searchCriteria.metamorphicGrades;
	if([myMetamorphicGrades count]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Metamorphic Grades." message:@"You may not narrow your search on metamorphic grade." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		
		MetamorphicGradeController *viewController=[[MetamorphicGradeController alloc] initWithNibName:@"MetamorphicGradeView" bundle:nil];
		[viewController setData:locations:myMetamorphicGrades:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.metamorphicGradeController= viewController;
		[viewController release];
		UIView *ControllersView=[metamorphicGradeController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:metamorphicGradeController animated:NO];
	}
}
//function to load the RockType view
-(void)loadRockTypeView
{
	int x;
	bool added;
	NSMutableArray *myRockTypes=searchCriteria.rockTypes;
	if([myRockTypes count]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Rock Types." message:@"You may not narrow your search on rock type." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		
		RockTypeController *viewController = [[RockTypeController alloc] initWithNibName:@"RockTypeView" bundle:nil];
		//all of the following values must be passed so they can be passed back to the map straight from the rock view
		[viewController setData:locations:myRockTypes:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.rockTypeController = viewController;
		[viewController release];
		UIView *ControllersView = [rockTypeController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:rockTypeController animated:NO];
	}
}
-(void)loadOwnerView
{
	NSMutableArray *owners=[[NSMutableArray alloc] init];
	int x;
	owners=searchCriteria.owners;
	
	if([owners count]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Owners." message:@"You may not narrow your search on sample owner." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		OwnerViewController *viewController = [[OwnerViewController alloc] initWithNibName:@"OwnerView" bundle:nil];
		[viewController setData:locations:owners:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.ownerController= viewController;
		[viewController release];
		UIView *ControllersView= [ownerController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:ownerController animated:NO];
	}
}


//set the various arrays in this controller with the information about the samples
-(void)setData:(NSMutableArray*)sampleLocations:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	locations= sampleLocations;
}
-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
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
	[tableView release];
	[locations release];
	[rows release];
	[rockTypeController release];
	[metamorphicGradeController release];
	[mineralsController release];
	[mapController release];
	[ownerController release];
	[searchCriteria release];
	[super dealloc];
}


@end
