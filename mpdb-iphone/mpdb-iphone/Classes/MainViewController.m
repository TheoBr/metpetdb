

#import "MainViewController.h"
#import "RadiusController.h"
#import "MyCLController.h"
#include <string.h>


@implementation MainViewController
@synthesize myLocationButton, regionButton, coordinateButton, locationViewController, radiusController, myLat, myLong, myCoordinate;
@synthesize coordController, regionController, tableView;
-(void)viewDidLoad
{
	[MyCLController sharedInstance].delegate = self;
	rows=[[NSMutableArray alloc] init]; //this array is the data source for the table

	[rows addObject:@"Use My Location"];
	[rows addObject:@"Input Coordinate"];
	[rows addObject:@"Select Region"];
	CGRect frame= CGRectMake(0, 150, 320, 200);
	tableView=[[UITableView alloc] initWithFrame:frame style:UITableViewStyleGrouped];
	
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	[self. view addSubview:tableView];
	
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
	if(indexPath.row==0)
	{
		[self useMyLocation];
	}
	//if the button corresponding with minerals is pressed, load the corresponding view
	else if(indexPath.row==1)
	{
		[self enterCoordinate];
	}
	//if the button corresponding to mineral grade is pressed, load the corresponding view
	else if(indexPath.row==2)
	{
		[self chooseRegion];
	}	
}
-(void)useMyLocation
{
	[[MyCLController sharedInstance].locationManager startUpdatingLocation];
	//when the current latitude and longitude have been determined, load the next view
}
-(void)enterCoordinate
{
	CoordInputController *viewController=[[CoordInputController alloc] initWithNibName: @"CoordInputView" bundle:nil];
	self.coordController= viewController;
	[viewController release];
	UIView *newview=[coordController view];
	[self.view addSubview:newview];
	[self.navigationController pushViewController:coordController animated:NO];
}

-(void)chooseRegion
{	
	RegionViewController *viewController= [[RegionViewController alloc] initWithNibName:@"RegionView" bundle:nil];
	self.regionController = viewController;
	[viewController release];
	UIView *newView= [regionController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:regionController animated:NO];
}
	

-(void)addTextToLog:(NSString *)text {
	//output.text=text;
	}
-(void)newLocationUpdate:(NSString *)text:(CLLocation*) coordinate{
	myCoordinate=coordinate;
	myLat= [[NSString alloc] initWithFormat:@"%g", myCoordinate.coordinate.latitude];
	myLong=[[NSString alloc] initWithFormat:@"%g", myCoordinate.coordinate.longitude];
	NSString *temp=[[NSString alloc] initWithFormat:@"Lat: %@, Long: %@", myLat, myLong];
	//output.text=temp;
    [self addTextToLog:text];
	
	//now the the latitude and longitude have been obtained, load the next view 
	RadiusController *viewController=[[RadiusController alloc] initWithNibName:@"SearchView" bundle:nil];
	[viewController setData:myLat:myLong];
	self.radiusController= viewController;
	[viewController release];
	UIView *newview= [radiusController view];
	[self.view addSubview:newview];
	[self.navigationController pushViewController:radiusController animated:NO];
	
}



- (void)dealloc {
	[myLocationButton release];
	[coordinateButton release];
	[regionButton release];
    
}

@end
