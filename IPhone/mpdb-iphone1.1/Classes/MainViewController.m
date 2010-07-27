

#import "MainViewController.h"
#include <string.h>
#import "MetPetDBAppDelegate.h"
#import <Security/Security.h>


@implementation MainViewController
@synthesize radiusController, myLat, myLong, myCoordinate, loginController, existingPassword;
@synthesize coordController, regionController, tableView, username, segControl, security;
@synthesize currentSearchData, mainViewController, logoutAlertView, infoController;


#define LocStr(key) [[NSBundle mainBundle] localizedStringForKey:(key) value:@"" table:nil]
-(void)viewDidLoad
{
	self.navigationItem.hidesBackButton=YES;
	currentSearchData=[[currentSearchData alloc] init];
 	[MyCLController sharedInstance].delegate = self;
	rows=[[NSMutableArray alloc] init]; //this array is the data source for the table
	
	[rows addObject:@"Use My Location"];
	[rows addObject:@"Input Coordinate"];
	[rows addObject:@"Select Region"];
	
	CGRect frame= CGRectMake(0, 120, 320, 200);
	tableView=[[UITableView alloc] initWithFrame:frame style:UITableViewStyleGrouped];
	
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	CGRect loginFrame=CGRectMake(0, 315, 320, 64);
	UITableView *loginTable= [[UITableView alloc] initWithFrame:loginFrame style:UITableViewStyleGrouped];
	//loginTable.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	loginTable.delegate=self;
	loginTable.dataSource=self;
	[self.view addSubview:loginTable];
	
	dontAllow=0;	
	
	//check to see if a username and password have been stored in the keychain
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	if(Uname!= nil)
	{
		signedIn= TRUE;
	}
	if(signedIn==TRUE)
	{
		//if the user is signed in they will initially view public and private samples and can later refine
		[currentSearchData setCurrentPublicStatus:@"both"];
		
		CGRect frameButton=CGRectMake(228, 348, 72, 23);
		UIButton *signOutButton=[UIButton buttonWithType:UIButtonTypeRoundedRect];
		
	}
	else
	{
		[currentSearchData setCurrentPublicStatus:@"public"]; //the user is not signed in, so they cannot see private samples
	}
	UIButton* infoButton = [UIButton buttonWithType:UIButtonTypeInfoLight];
	[infoButton addTarget:self action:@selector(infoButtonPressed) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *infoBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:infoButton];
	self.navigationItem.rightBarButtonItem = infoBarButtonItem;
	
	
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	if(tableView==self.tableView){	
		return [rows count];
	}
	else{
		return 1;
	}
}


-(void)infoButtonPressed
{
	InfoViewController *viewController = [[InfoViewController alloc] initWithNibName:@"InfoView" bundle:nil];
	self.infoController= viewController;
	[viewController release];
	UIView *newView=[infoController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:infoController animated:NO];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	if(tableView ==self.tableView){
		
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
	else{
		usernameCell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle 
											  reuseIdentifier:usernameCell.textLabel.text];
		
		usernameCell.accessoryType= UITableViewCellAccessoryDisclosureIndicator;
		usernameCell.font=[UIFont boldSystemFontOfSize:12];
		if(signedIn==TRUE){
			usernameCell.textLabel.text =[[NSString alloc] initWithFormat:@"Hello, %@", Uname];
			usernameCell.detailTextLabel.text=[[NSString alloc] initWithString:@"Click to sign out."];
		}
		else{
			usernameCell.textLabel.text=[[NSString alloc] initWithString:@"Already have a MetPetDB Account?"];
			usernameCell.detailTextLabel.text=[[NSString alloc] initWithString:@"Sign in to view your private samples."];
		}
		return usernameCell;
	}
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	if(indexPath.row==0)
	{
		if(tableView==self.tableView){
			currentSearchData.locationVisible=TRUE;
			[self useMyLocation];
			currentSearchData.locationVisible=FALSE;
		}
		else{
			if(signedIn==TRUE){
				[self logoutAlert];
			}
			else
			{
				[self login];
			}
		}
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
	[tableView  deselectRowAtIndexPath:indexPath  animated:NO]; 
	
}
-(void)login
{
	LoginViewController *loginController = [[LoginViewController alloc] initWithNibName:@"LoginView" bundle:nil];
	
	UIView *newView=[loginController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:loginController animated:NO];
	
}
-(void)logoutAlert
{
	//display an alert to make sure the user really wants to log out
	logoutAlertView = [[UIAlertView alloc] initWithTitle:@"Are you sure you want to logout?" message:nil
												delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
	[logoutAlertView show];
}
//if the user clicks that they wish to logout in the logout alert, this function will log the user out
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex { 
	if(alertView== logoutAlertView){
		if(buttonIndex==1){
			[self logout];
		}
		
	}
	
}
-(void)logout{
	signedIn=FALSE;
	//remove the username from the keychain
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	[keychain deleteKeychainValue:@"Username"];
	
	username=nil;
	currentSearchData.currentPublicStatus=@"public"; //the user is not signed in, so they cannot see private samples
	MainViewController *viewController = [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainViewController = viewController;
	UIView *controllersview= [mainViewController view];
	[self.view addSubview:controllersview];
	[viewController release];
	[self.navigationController pushViewController:mainViewController animated:NO];
	
	
}


//this function is called after the user has logged in to set the boolean so that the user is logged in
-(void)setSignIn:(bool)value:(NSString*)user
{
	username=user;
	signedIn=value;
}
-(void)useMyLocation
{
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	
	[[MyCLController sharedInstance].locationManager locationServicesEnabled];
	[[MyCLController sharedInstance].locationManager startUpdatingLocation];	
	
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	//[[MyCLController sharedInstance].locationManager stopUpdatingLocation];
	
}
-(void)enterCoordinate
{
	currentSearchData.locationVisible =FALSE;
	currentSearchData.zoomed=FALSE;
	CoordInputController *viewController=[[CoordInputController alloc] initWithNibName: @"CoordinateInputView" bundle:nil];
	[viewController setData:currentSearchData];
	self.coordController= viewController;
	[viewController release];
	UIView *newview=[coordController view];
	[self.view addSubview:newview];
	[self.navigationController pushViewController:coordController animated:NO];
	//[self dealloc];
}

-(void)chooseRegion
{	
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	currentSearchData.locationVisible=FALSE;
	currentSearchData.zoomed=FALSE;
	RegionViewController *viewController= [[RegionViewController alloc] initWithNibName:@"RegionView" bundle:nil];
	[viewController setData:currentSearchData];
	self.regionController = viewController;
	[viewController release];
	UIView *newView= [regionController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:regionController animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}


-(void)addTextToLog:(NSString *)text {
	//output.text=text;
}

-(void)locationError
{
	[[MyCLController sharedInstance].locationManager stopUpdatingLocation];
	//after the user chooses dont allow twice (dontAllow=2) they will no longer be able to use their location without resetting the phone
	dontAllow++;
	if(dontAllow==1)// || dontAllow==2)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"WARNING:" message:@"In order to search using your current location you must choose 'Allow'" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else if(dontAllow>=2)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Current Location Finder Disabled" message:@"To reset, from your home menu choose: Settings -> General -> Reset -> Reset Location Warnings" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	
}


-(void)newLocationUpdate:(NSString *)text:(CLLocation*) coordinate{
	[[MyCLController sharedInstance].locationManager stopUpdatingLocation];
	myCoordinate=coordinate;
	myLat= [[NSString alloc] initWithFormat:@"%g", myCoordinate.coordinate.latitude];
	myLong=[[NSString alloc] initWithFormat:@"%g", myCoordinate.coordinate.longitude];
	NSString *temp=[[NSString alloc] initWithFormat:@"Lat: %@, Long: %@", myLat, myLong];
	//output.text=temp;
	[self addTextToLog:text];
	//now the the latitude and longitude have been obtained, load the next view 
	//set the bool in the following view according to whether the user wants to view private or public samples
	currentSearchData.locationVisible=TRUE;
	currentSearchData.zoomed=FALSE;
	RadiusController *viewController=[[RadiusController alloc] initWithNibName:@"SearchView" bundle:nil];
	
	CLLocationCoordinate2D myTrueCoord = myCoordinate.coordinate;
	
	[currentSearchData setCenterCoordinate:myTrueCoord];
	[viewController setData:currentSearchData];
	self.radiusController= viewController;
	[viewController release];
	UIView *newview= [radiusController view];
	[self.view addSubview:newview];
	[self.navigationController pushViewController:radiusController animated:NO];
	//	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	
}


- (void)dealloc {
	[security release];
	[tableView release];
	[loginController release];
	[radiusController release];
	[coordController release];
	[regionController release];
	[myLat release];
	[myLong release];
	[myCoordinate release];
	[rows release];
	[infoController release];
	[username release];
	[segControl release];
	[items release];
	[existingPassword release];
	[usernameLabel release];
	[myLocation release];
	[copyrightLabel release];
	[alertController release];
	[coord release];
	
	[super dealloc];
    
}

@end
