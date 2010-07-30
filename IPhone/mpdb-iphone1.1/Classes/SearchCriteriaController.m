//
//  SearchCriteriaController.m
//  Location
//
//  Created by Heather Buletti on 6/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SearchCriteriaController.h"
#import "MetPetDBAppDelegate.h"
#import "KeychainWrapper.h"


#define TITLELABEL_TAG 1
#define SUBTITLELABEL_TAG 2
#define PHOTO_TAG 3

@implementation SearchCriteriaController
@synthesize tableController, toolbar, addButton, removeButton, clearButton, searchButton, mapController, editButton, group, newgroup;
@synthesize  deleteButton, minimumLat, maximumLat, minimumLong, maximumLong, newTag, doneButton, searchCriteria;
@synthesize  Uname, publicPrivateController;

-(void)viewDidLoad
{
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	pool = [[NSAutoreleasePool alloc] init]; 
	self.navigationItem.title=@"Search Criteria";
	int z;
	removing=FALSE; //the user has not yet selected the button to remove search criteria
	rowSelected=FALSE;
	canShowMap=TRUE;
	edit=FALSE; //the user has not yet pressed the edit button
	//create an array that will hold all the main titles of the rows and another that will hold the subtitles
	//the subtitles are the search criteria that have been selected thus far
	rowTitles = [[NSMutableArray alloc]init];
	rowSubtitles=[[NSMutableArray alloc] init];
	//the only criteria that is automatically displayed is the geographic location, the rest are only displayed if they have been added
	[rowTitles addObject:@"Geographic Location"];
	[rowTitles addObject:@"Public Status"];
	if(currentSearchData.region!=nil)
	{
		NSString *regionName=[[NSString alloc] initWithFormat:@"Region: %@", currentSearchData.region];
		[rowSubtitles addObject:regionName];
	}
	else
	{
		CLLocationCoordinate2D centerCoord = [CurrentSearchData getCenterCoordinate];
		NSString *coordinate=[[NSString alloc] initWithFormat:@"Center Coordinate:\n%.5g, %.5g", centerCoord.latitude, centerCoord.longitude];
		[rowSubtitles addObject:coordinate];
	}
	//make a subtitle depending on whether the user is logged in and whether they are viewing private, public, or both types of samples
	if([currentSearchData.currentPublicStatus isEqualToString:@"public"])
	{
		[rowSubtitles addObject:@"Public Samples Only"];
	}
	else if([currentSearchData.currentPublicStatus isEqualToString:@"private"])
	{
		[rowSubtitles addObject:@"Private Samples Only"];
	}
	else if([currentSearchData.currentPublicStatus isEqualToString:@"both"])
	{
		[rowSubtitles addObject:@"Public and Private Samples"];
	}
	if([[currentSearchData rockTypes] count]!=0)
	{
		[rowTitles addObject:@"Rock Type"];
		NSString *myRockTypes=[[NSString alloc] initWithFormat:@"%@",[[currentSearchData rockTypes] objectAtIndex:0]];
		
		for(z=1; z<[[currentSearchData rockTypes] count]; z++)
		{
			myRockTypes=[myRockTypes stringByAppendingFormat:@", %@",[[currentSearchData rockTypes] objectAtIndex:z]];
		}
		[rowSubtitles addObject:myRockTypes];
	}
	if([[currentSearchData owners] count]!=0)
	{
		[rowTitles addObject:@"Sample Owner"];
		NSString *myOwners=[[NSString alloc] initWithFormat:@"%@", [[currentSearchData owners] objectAtIndex:0]];
		
		for(z=1; z<[[currentSearchData owners] count]; z++)
		{
			myOwners=[[NSString alloc] initWithFormat:@"%@", [[currentSearchData owners] objectAtIndex:z]];
		}
		[rowSubtitles addObject:myOwners];
	}
	if([[currentSearchData minerals] count]!=0)
	{
		[rowTitles addObject:@"Minerals"];
		NSString *myMinerals=[[NSString alloc] initWithFormat:@"%@",[[currentSearchData minerals] objectAtIndex:0]];
		for(z=1; z<[[currentSearchData minerals] count]; z++)
		{
			myMinerals=[myMinerals stringByAppendingFormat:@", %@",[[currentSearchData minerals] objectAtIndex:z]];
		}
		[rowSubtitles addObject:myMinerals];
	}
	if([[currentSearchData metamorphicGrades] count]!=0)
	{
		[rowTitles addObject:@"Metamorphic Grade"];
		NSString *myMetGrades=[[NSString alloc] initWithFormat:@"%@",[[currentSearchData metamorphicGrades] objectAtIndex:0]];
		for(z=1; z<[[currentSearchData metamorphicGrades] count]; z++)
		{
			myMetGrades=[myMetGrades stringByAppendingFormat:@", %@",[[currentSearchData metamorphicGrades] objectAtIndex:z]];
		}
		[rowSubtitles addObject:myMetGrades];
		
	}
	//for each of the rows in the table, add a boolean to indicate whether it has been selected for deletion and set them all initially to false
	delete= [[NSMutableArray alloc] init];
	
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
    tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	[self makeToolbar];
	//calculate the search criteria in case it has changed becasue of additions or deletions
	//[self calculateCriteria];
	
	//if the user is logged in get the username to send to the server so private samples can be aquired
	
}
-(void)calculateCriteria
{
	//recaluclate the search criteria
	PostRequest *post= [[PostRequest alloc] init];
	[post setData:[currentSearchData minerals] :[currentSearchData rockTypes] :[currentSearchData owners] :[currentSearchData metamorphicGrades] :currentSearchData.currentPublicStatus :currentSearchData.region:[currentSearchData originalCoordinates]:0:@"true"];
	postReturn=[post buildPostString]; 
	
	xmlParser *x= [[xmlParser alloc] init];
	searchCriteria= [x parseSearchCriteria:postReturn];
	[x release];
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	
	
	
	
}
-(void)makeToolbar
{
	//make a toolbar at the bottom of the view and add a button to add search criteria
	buttons=[[NSMutableArray alloc] init];
	//editButton=[[UIBarButtonItem alloc] initWithTitle:@"Edit" style: UIBarButtonItemStyleBordered target:self action:@selector(edit)];
	//self.navigationItem.rightBarButtonItem=editButton;
	searchButton=[[UIBarButtonItem alloc] initWithTitle:@"Search" style:UIBarButtonItemStyleBordered target:self action:@selector(backToMap)];
	addButton= [[UIBarButtonItem alloc] initWithTitle:@"Add Criteria" style:UIBarButtonItemStyleBordered target:self action:@selector(addCriteria)];
	//if at least one search criteria has been provided, then display the edit button, otherwise hide it
	[buttons addObject:searchButton];
	[buttons addObject:addButton]; 
	//if some search criteria has been specified OR the user is logged in, display the edit button
	if([currentSearchData.rockTypes count]>0 || [currentSearchData.owners count]>0 || [currentSearchData.metamorphicGrades count]>0 || [currentSearchData.minerals count]>0 || Uname!=nil)
	{
		
		editButton=[[UIBarButtonItem alloc] initWithTitle:@"Edit Criteria" style: UIBarButtonItemStyleBordered target:self action:@selector(edit)];
		[buttons addObject:editButton];
	}
	
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
}
-(void)edit
{
	edit=TRUE;
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];	
	
	
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	[self makeToolbar];
	
	removing=TRUE; //the user wants to remove search criteria so we want to register their row selections
	doneButton=[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleBordered target:self action:@selector(done)];
	self.navigationItem.rightBarButtonItem=doneButton;	
	
	
	
}
-(void)done
{
	int index= [rowTitles count]-1;
	edit=FALSE;
	removing=FALSE;
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];	
	self.navigationItem.rightBarButtonItem=nil;
	
	//self.navigationItem.rightBarButtonItem=editButton;
	
}

-(void)backToMap
{
	
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	[self calculateCriteria];
	if(searchCriteria.totalCount==0)
	{	
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Matching Samples." message:@"This combination of criteria did not match any samples. Try removing some criteria and searching again." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
	else 
	{
		//start a new thread to get the samples
		//myThread = [[NSThread alloc] initWithTarget:self selector:@selector(getSamples) object:nil];
		//[myThread start];
		remainingSamples=[[NSMutableArray alloc] init];
		[self getSamples];
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		[viewController setData:remainingSamples:searchCriteria];
		//[viewController setType:@"map"];
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
	//make a post request to get the samples that match the search criteria
	PostRequest *post= [[PostRequest alloc] init];
	
	[post setData:[currentSearchData minerals] :[currentSearchData rockTypes] :[currentSearchData owners] :[currentSearchData metamorphicGrades] :currentSearchData.currentPublicStatus :currentSearchData.region:[currentSearchData originalCoordinates]:0:@"false"];
	postReturn=[post buildPostString];
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test2.txt"];
	[fh writeData:postReturn];
	
	xmlParser *x= [[xmlParser alloc] init];
	remainingSamples= [x parseSamples:postReturn];
	/*for(int z=0; z<[remainingSamples count]; z++)
	 {
	 [mapController makeAnnotations:[remainingSamples objectAtIndex:z]];
	 }
	 [mapController setSpan];
	 [mapController makeSearchBox];
	 [mapController makeNavBar];
	 [xmlParser release];
	 [x release];
	 [mapController release];
	 
	 [(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];*/
	
}

-(void)deleteRow:(id)sender
{
	
	canShowMap=TRUE;
	//removing=FALSE;
	//num=[[sender tag] intValue];
	num=[sender tag];
	[delete addObject:[rowTitles objectAtIndex:num]];
	
	self.navigationItem.rightBarButtonItem=nil;
	remainingSamples=[[NSMutableArray alloc] init];
	
	//loop through the bools to determine which subtitles should be set back to nil and which samples need to be added back to
	//the map now that the search criteria has been cleared
	int p, q;
	
	for(p=0; p<[delete count]; p++)
	{
		NSString *rowToDelete= [delete objectAtIndex:p];
		for(q=0; q<[rowTitles count]; q++)
		{
			if([[rowTitles objectAtIndex:q] isEqualToString:rowToDelete])
			{
				[rowTitles removeObjectAtIndex:q];
				[rowSubtitles removeObjectAtIndex:q];
			}
		}
		if([rowToDelete isEqualToString:@"Rock Type"])
		{
			[[currentSearchData rockTypes] removeAllObjects];
		}
		if([rowToDelete isEqualToString:@"Minerals"])
		{
			[[currentSearchData minerals] removeAllObjects];
		} 
		if([rowToDelete isEqualToString:@"Metamorphic Grade"])
		{
			[[currentSearchData metamorphicGrades] removeAllObjects];
		}
		if([rowToDelete isEqualToString:@"Sample Owner"])
		{
			[[currentSearchData owners] removeAllObjects];
		}
		
	}
	
	doneButton=[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleBordered target:self action:@selector(done)];
	
	self.navigationItem.rightBarButtonItem=doneButton;	
	rowSelected=FALSE;
	removing=TRUE;
	
	
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	
	if([[currentSearchData rockTypes] count]>0 || [[currentSearchData owners] count]>0 || [[currentSearchData metamorphicGrades] count]>0 || [[currentSearchData minerals] count]>0)
	{
	}
	else
	{
		[buttons removeLastObject];
	}
	[self.view addSubview:tableView];
	[self makeToolbar];
	//	[self calculateCriteria];
	
}
-(void)addCriteria
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	//load the table containing the choices for search criteria so the user can add more
	//clear the rows of the table so the app doesnt crash if the user goes back and then forward again
	[self calculateCriteria];
	NSMutableArray *tempRows=[[NSMutableArray alloc] init];
	tableController=tempRows;
	TableController *viewController = [[TableController alloc] initWithNibName:@"TableView" bundle:nil];
	[viewController setData:remainingSamples:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	self.tableController = viewController;
	[viewController release];
	UIView *ControllersView = [tableController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:tableController animated:NO];	
	removing=FALSE;
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [rowTitles count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UILabel *titleLabel, *subtitleLabel;
	
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"My Identifier"];
	UIImageView *checkmarkView;
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"My Identifier"];
		// cell.accessoryType = UITableViewCellAccessoryCheckmark;
		//int height= [tableView heightForRowAtIndexPath:indexPath];
        titleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(0.0, 0.0, 100.0, 45)] autorelease];
		// titleLabel.frame=CGRectMake(0.0, 0.0, 100.0, 45);
		titleLabel.tag = TITLELABEL_TAG;
        titleLabel.font = [UIFont systemFontOfSize:15.0];
        titleLabel.textAlignment = UITextAlignmentCenter;
		titleLabel.numberOfLines=2;
        titleLabel.textColor = [UIColor blackColor];
        titleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
		//set the background of the labels to be alternating colors
		if(indexPath.row==0)
		{
			titleLabel.backgroundColor=[UIColor purpleColor];
		}
		else if(indexPath.row==1)
		{
			titleLabel.backgroundColor=[UIColor blueColor];
		}
		else if(indexPath.row==2)
		{
			titleLabel.backgroundColor=[UIColor greenColor];
		}
		else if(indexPath.row==3)
		{
			titleLabel.backgroundColor=[UIColor yellowColor];
		}
		else if(indexPath.row==4)
		{
			titleLabel.backgroundColor=[UIColor cyanColor];
		}
		else if(indexPath.row==5)
		{
			titleLabel.backgroundColor=[UIColor lightGrayColor];
		}
		else if(indexPath.row==6)
		{
			titleLabel.backgroundColor=[UIColor magentaColor];
		}
		//if(indexPath.row==1 && edit==TRUE){
        [cell.contentView addSubview:titleLabel];
		
        subtitleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(110.0, 0.0, 180.0, 50.0)] autorelease];
        //subtitleLabel.frame=CGRectMake(110.0, 0.0, 180.0, 50.0);
		subtitleLabel.tag = SUBTITLELABEL_TAG;
		subtitleLabel.numberOfLines=4;
        subtitleLabel.font = [UIFont systemFontOfSize:12.0];
        subtitleLabel.textAlignment = UITextAlignmentLeft;
        subtitleLabel.textColor = [UIColor darkGrayColor];
        subtitleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
        [cell.contentView addSubview:subtitleLabel];
		
		checkmarkView = [[UIImageView alloc] initWithFrame:CGRectMake(250.0, 8.0, 30.0, 30.0)];
		checkmarkView.tag = PHOTO_TAG;
		checkmarkView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
		[cell.contentView addSubview:checkmarkView];
		//}
		if(removing==TRUE && indexPath.row!=0 && indexPath.row!=1)
		{
			CGRect frame= CGRectMake(240.0, 8.0, 60.0, 30.0);
			UIButton *button=[[UIButton alloc] initWithFrame:frame];
			deleteButton= [UIButton buttonWithType:UIButtonTypeRoundedRect];
			deleteButton.frame=frame;
			[deleteButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
			[deleteButton setTitle:@"Delete" forState:UIControlStateNormal];
			//newTag=[[NSString alloc] initWithFormat:@"%d", indexPath.row];
			[deleteButton setTag:indexPath.row];
			//num=indexPath.row;
			[deleteButton addTarget:self action:@selector(deleteRow:) forControlEvents:UIControlEventTouchUpInside];
			[cell.contentView addSubview:deleteButton];
		}
		//if the user is logged in and in "edit mode" then display an arrow to take the user to the view to edit public/private status
		//the title and subtitle labels must also be repositioned so that the row does not appear to be shifted to the left
		if(edit==TRUE && Uname!=nil &&indexPath.row==1)
		{
			cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
			titleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(20.0, 0.0, 100.0, 45)] autorelease];
			titleLabel.tag = TITLELABEL_TAG;
			titleLabel.font = [UIFont systemFontOfSize:15.0];
			titleLabel.textAlignment = UITextAlignmentCenter;
			titleLabel.numberOfLines=2;
			titleLabel.textColor = [UIColor blackColor];
			titleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
			titleLabel.backgroundColor=[UIColor blueColor];
			//the title and subtitle labels need to be adjusted when there is an accessorydisclosure
			[cell.contentView addSubview:titleLabel];
			
			subtitleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(130.0, 0.0, 180.0, 50.0)] autorelease];
			subtitleLabel.tag = SUBTITLELABEL_TAG;
			subtitleLabel.numberOfLines=4;
			subtitleLabel.font = [UIFont systemFontOfSize:12.0];
			subtitleLabel.textAlignment = UITextAlignmentLeft;
			subtitleLabel.textColor = [UIColor darkGrayColor];
			subtitleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
			[cell.contentView addSubview:subtitleLabel];
			
			checkmarkView = [[UIImageView alloc] initWithFrame:CGRectMake(250.0, 8.0, 30.0, 30.0)];
			checkmarkView.tag = PHOTO_TAG;
			checkmarkView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
			[cell.contentView addSubview:checkmarkView];
		}
	}
	
	else {
        titleLabel = (UILabel *)[cell.contentView viewWithTag:TITLELABEL_TAG];
        subtitleLabel = (UILabel *)[cell.contentView viewWithTag:SUBTITLELABEL_TAG];
		checkmarkView = (UIImageView *)[cell.contentView viewWithTag:PHOTO_TAG];
    }
	//NSDictionary *aDict = [self.list objectAtIndex:indexPath.row];
	int rowNum = indexPath.row;
	int rowTitlesNum = [rowTitles count];
	int subTitlesNum = [rowSubtitles count];
    titleLabel.text = [rowTitles objectAtIndex:indexPath.row];
	
	if (indexPath.row < subTitlesNum)
		subtitleLabel.text = [rowSubtitles objectAtIndex:indexPath.row];
	
	index= indexPath.row;
	
	cell.selectionStyle=UITableViewCellSelectionStyleNone;
	return cell;
	
}
//this function is needed only when the user is in "edit mode" and only for the row containing the public/private status
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	if(indexPath.row==1 && edit==TRUE)
	{
		PublicPrivateViewController *viewController= [[PublicPrivateViewController alloc] initWithNibName:@"PublicPrivateView" bundle:nil];
		[viewController setData:locations:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		self.publicPrivateController = viewController;
		[viewController release];
		UIViewController *ControllersView= [publicPrivateController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:publicPrivateController animated:NO];
	}
}


//this is the data of the annotations from the original search result and the current annotations that are left
//this information needs to be passed to the table controller where more search criteria can be added
-(void)setData:(NSMutableArray*)sampleLocations:(CriteriaSummary*)criteria
{
	searchCriteria=criteria;
	//buttonVisible=button;
	remainingSamples= sampleLocations;
}

//this function will set the data that has been specified in the current search
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
	[rowTitles release];
	[rowSubtitles release];
	[tableController release];
	[buttons release];
	[toolbar release];
	[addButton release];
	[removeButton release];
	[clearButton release];
	[searchButton release];
	[editButton release];
	[doneButton release];
	[removing release];
	[locations release];
	[delete release];
	[remainingSamples release];
	[group release];
	[newgroup release];
	[mapType release];
	[deleteButton release];
	[minimumLat release];
	[maximumLat release];
	[minimumLong release];
	[maximumLong release];
	[newTag release];
	[searchCriteria release];
	[super dealloc];
}


@end
