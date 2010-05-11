

//
//  RegionViewController.m
//  Location
//
//  Created by Heather Buletti on 6/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "RegionViewController.h"
#import "KeychainWrapper.h"

@implementation RegionViewController
@synthesize latitude, longitude, mylocationCoordinate, mylat;
@synthesize mapController, regionName, selectedRegion, tableView, sampleCategory, criteria, Uname;

-(void)viewDidLoad
{
	
	//initialize all the arrays used in this controller
	letters=[[NSMutableArray alloc] init];
	tableObjects=[[NSMutableDictionary alloc] init];
	regions = [[NSMutableArray alloc] init];
	sampleLocations= [[NSMutableArray alloc] init];
	//if a user is signed in, set the Uname variable and pass it to the server to get the user's private samples
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	[self getRegions];
	
	
	
	
	//the dictionary will hold values and the key will be the letter that the region begins with
	//make an array of all the regions of each letter as well as an array of the letters 
	char c;
	int x;
	for(c ='A'; c<='Z'; c++)
	{
		NSMutableArray *regionsForLetter=[[NSMutableArray alloc] init];
		for(x=0; x< [regions count]; x++)
		{
			NSString *tempRegion=[regions objectAtIndex:x];
			if([tempRegion characterAtIndex:0]==c) //add the region name to the array for that letter
			{
				[regionsForLetter addObject:tempRegion];
			}
		}
		//add the array of regions that start with that letter to the dictionary with the letter as the key
		if([regionsForLetter count]!=0)
		{
			[letters addObject:[NSString stringWithFormat:@"%c",c]];
			[tableObjects setObject:regionsForLetter forKey:[NSString stringWithFormat:@"%c",c]];
		}
	}
	
	
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return [letters count];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [[tableObjects objectForKey:[letters objectAtIndex:section]] count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	static NSString *MyIdentifier = @"MyIdentifier";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:MyIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:MyIdentifier] autorelease];
	}
	
	// Set up the cell
	cell.text = [[tableObjects objectForKey:[letters objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
	return cell;
}
- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
	//NSMutableArray *characters= [[NSMutableArray alloc]init];
	//for(char c = 'A'; c<='Z';c++)
	//	[characters addObject:[NSString stringWithFormat:@"%c",c]];
	//	return characters;
	return letters;
}
- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index {
	NSInteger count = 0;
	for(NSString *character in letters)
	{
		if([character isEqualToString:title])
			return count;
		count ++;
	}
	return 0;// in case of some eror dont crash app
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	if([letters count]==0)
		return @"";
	return [letters objectAtIndex:section];
}
//if a row of the table is selected, load the map with the samples from that region
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	UITableViewCell *selectedCell=[tableView cellForRowAtIndexPath:indexPath];
	selectedRegion= selectedCell.text;
	[tableView  deselectRowAtIndexPath:indexPath  animated:NO]; 
	
	[self loadMap];
}

//This function is called when the search button is pressed and it loads the MapView
-(void)loadMap
{
	//make a post request to get the criteria for the samples that are returned from the region search
	PostRequest *post= [[PostRequest init] alloc];
	[post setData:nil :nil :nil :nil :currentSearchData.currentPublicStatus :selectedRegion:nil:0:@"true"];
	myReturn=[post buildPostString]; 
	
	
	xmlParser *x= [[xmlParser alloc] init];
	criteria= [x parseSearchCriteria:myReturn];
	[x release];
	//if no samples are returned from the search an alert should be displayed instead of the map
	if(criteria.totalCount==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Samples." message:@"Please increase your search radius or choose different coordinates." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
	else
	{
		
		
		//make a thread so the samples are loaded in the background
		//[self getSamples];
		//myThread = [[NSThread alloc] initWithTarget:self selector:@selector(getSamples) object:nil];
		
		//[myThread start];
		[self getSamples];
		//set the current search data so it can be passed to the map view
		[currentSearchData setRegion:selectedRegion];
		
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		[viewController setData:sampleLocations:criteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.mapController = viewController;
		[viewController release];
		UIView *ControllersView = [mapController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:mapController animated:NO];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
	
}
-(void)getSamples{
	
	// NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];  
	
	//create a post request by passing the necessary data to a PostRequest object
	//the NSData that is returned will be the samples that are contained in whatever region the user has selected
	PostRequest *post= [[PostRequest init] alloc];
	//since the user has not yet been able to narrow the search, nil can be passed instead of the arrays
	[post setData:nil:nil:nil :nil:currentSearchData.currentPublicStatus :selectedRegion:nil:0:@"false"];
	myReturn=[post buildPostString];
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test3.txt"];
	[fh writeData:myReturn];
	
	xmlParser *x= [[xmlParser init] alloc];
	sampleLocations= [x parseSamples:myReturn];
	/*for(int z=0; z<[sampleLocations count]; z++)
	 {
	 [mapController makeAnnotations:[sampleLocations objectAtIndex:z]];
	 }
	 [mapController setSpan];
	 [mapController makeSearchBox];
	 [mapController makeNavBar];
	 [xmlParser release];
	 [x release];
	 [mapController release];
	 
	 [(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	 [pool release];*/
	
}
-(void)getRegions{
	//NSString *urlString= [[NSString alloc] initWithFormat:@"http://localhost:8888/edu.rpi.metpetdb.MetPetDBApplication/searchIPhonePost.svc?"];
	//NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhonePost.svc?"];					 
	NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhonePost.svc?"];
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest requestWithURL:myURL];	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString = [[NSString alloc] init];
	
	if(Uname!=NULL)
	{
		postString=[postString stringByAppendingFormat:@"username= %@\n", Uname];
	}
	postString= [postString stringByAppendingString:@"regions= true\n"];
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	
	NSString *returnValue=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	xmlParser *y=[[xmlParser alloc] init];
	regions=[y parseRegions:myReturn];
	[y release];
	
}
-(void)setData:(CurrentSearchData*)data
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
	[mapController release];
	[tableView release];
	[letters release];
	[tableObjects release];
	[regions release];
	[sampleLocations release];
	[selectedRegion release];
	[regionName release];
	[latitude release];
	[longitude release];
	[mylocationCoordinate release];
	[regionsResponse release];
	[currentStringValue release];
	[myReturn release];
	[locations release];
	[myThread release];
	[criteria release];
	
    [super dealloc];
}


@end



