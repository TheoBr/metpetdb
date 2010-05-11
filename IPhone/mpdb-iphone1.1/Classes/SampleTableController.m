//
//  SampleTableController.m
//  Location
//
//  Created by Heather Buletti on 6/5/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SampleTableController.h"
#import "AnnotationObjects.h"
#import "SampleInfoController.h"
#import "uniqueSamples.h"
#import "KeychainWrapper.h"

@implementation SampleTableController
@synthesize tableView, sampleID, sampleInfo, tempSample, tempName, tempOwner, toolbar;
@synthesize searchCriteria;
-(void)viewDidLoad{
	
	//the text of each of the rows will be the sample id of each of the samples that were located at the chosen point
	rows=[[NSMutableArray alloc] init];
	names= [[NSMutableArray alloc] init];
	owners=[[NSMutableArray alloc] init];
	if(allSamples==FALSE)
	{
		currentSampleCount=[mySamples count];
		self.navigationItem.title=[[NSString alloc] initWithFormat:@"%d Samples", [mySamples count]];
		int x;
		for(x=0; x<[mySamples count]; x++)
		{
			AnnotationObjects *sample=[mySamples objectAtIndex:x];
			NSString *id=[[NSString alloc] initWithFormat:@"%@, %@",sample.name, sample.rockType];
			[rows addObject:id];
			[names addObject:sample.name];
			[owners addObject:sample.owner];
		}
	}
	else
	{
		//display the number of samples in the table in the navigation bar too
		currentSampleCount=0;
		int p;
		for(p=0; p<[mySamples count]; p++)
		{
			uniqueSamples *group= [mySamples objectAtIndex:p];
			NSMutableArray *samples= group.samples;
			currentSampleCount+=[samples count];
		}
		self.navigationItem.title=[[NSString alloc] initWithFormat:@"%d / %d Samples", currentSampleCount, searchCriteria.totalCount];
		
		int y, z;
		for(y=0; y<[mySamples count]; y++)
		{
			uniqueSamples *sampleGroup=[mySamples objectAtIndex:y];
			NSMutableArray *samplesInGroup=sampleGroup.samples;
			for(z=0; z< [samplesInGroup count]; z++)
			{
				AnnotationObjects *sample= [samplesInGroup objectAtIndex:z];
				NSString *id=[[NSString alloc] initWithFormat:@"%@, %@",sample.name, sample.rockType];//, sample.rockType];
				[rows addObject:id];
				[names addObject:sample.name];
				[owners addObject:sample.owner];
			}
		}
	}
	
	tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
											style:UITableViewStylePlain];
    tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	
	//make a bar button on the navigation bar with the option of emailing samples to yourself
	
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	UIBarButtonItem *emailButton= [[UIBarButtonItem alloc] initWithTitle:@"Email Sample Info" style:UIBarButtonItemStyleBordered target:self action:@selector(sendEmail)];
	//self.navigationItem.rightBarButtonItem=emailButton;
	[buttons addObject:emailButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
	
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
	cell.font=[UIFont boldSystemFontOfSize:14];
	return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	//indexPath.row will correspond with the index in mySamples array and we need to pass the id of this object to the sampleInfo controller
	
	if(allSamples==FALSE)
	{
		//int x;
		//for(x=0; x<[mySamples count]; x++)
		//{
		selectedSample= [mySamples objectAtIndex:indexPath.row];
		//}
	}
	else
	{
		int y, z;
		for(y=0; y<[mySamples count]; y++)
		{
			uniqueSamples *sampleGroup=[mySamples objectAtIndex:y];
			NSMutableArray *samplesInGroup=sampleGroup.samples;
			for(z=0; z< [samplesInGroup count]; z++)
			{
			    tempName=[names objectAtIndex:indexPath.row];
				tempOwner= [owners objectAtIndex:indexPath.row];
				tempSample= [samplesInGroup objectAtIndex:z];		
				if(([tempSample.name isEqualToString:tempName]) && ([tempSample.owner isEqualToString:tempOwner]))
				{
					selectedSample= tempSample;
				}
			}
		}
	}
	[tableView  deselectRowAtIndexPath:indexPath  animated:NO]; 
	
	//load the sampleInfo view and pass the id of the chosen sample to the sampleInfoController
	SampleInfoController *viewController = [[SampleInfoController alloc] initWithNibName:@"SampleInfo" bundle:nil];
	//[viewController setData:selectedSample:mySamples];
	[viewController setSamples:locations:selectedSample:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	self.sampleInfo = viewController;
	[viewController release];
	UIView *ControllersView=[sampleInfo view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleInfo animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)sendEmail
{
	//first make a string out of the urls that for all the samples that will be the body of the email
	//NSString *body=[[NSString alloc] initWithFormat:@"%d samples were returned in your search:\n", currentSampleCount];
	NSString *body=[[NSString alloc] initWithString:@"Your search returned the following samples:\n"];
	for(int p=0; p<[mySamples count]; p++)
	{
		uniqueSamples *sampleGroup=[mySamples objectAtIndex:p];
		NSMutableArray *samplesInGroup=sampleGroup.samples;
		for(int z=0; z< [samplesInGroup count]; z++)
		{
			AnnotationObjects *sample= [samplesInGroup objectAtIndex:z];
			NSString *sampleURL=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/#sample/%@", sample.id];
			body=[body stringByAppendingString:sampleURL];
			body=[body stringByAppendingString:@"\n"];
		}
	}
	NSString *subject=[[NSString alloc] initWithString:@"MetPetDB Samples"];
	//if the user is not logged in, there will be no email address, leave this field blank
	NSString *Uname= [[NSString alloc] init];
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	if(Uname==nil)
	{
		NSString *temp=[[NSString alloc] initWithFormat:@"mailto:?subject=%@&body=%@",[subject stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding], [body stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
		[[UIApplication sharedApplication] openURL: [NSURL URLWithString: temp]];
	}
	
	else //the user is logged in, so make them the default recipient
	{
		NSString *temp=[[NSString alloc] initWithFormat:@"mailto:%@?subject=%@&body=%@", Uname, [subject stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding], [body stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding]];
		[[UIApplication sharedApplication] openURL: [NSURL URLWithString: temp]];
	}
}
//this function will rotate the phone if it is tilted

-(void)setData:(NSMutableArray*)samples:(bool)sampleFlag
{
	//samples represents an array of map annotations that are all located at the same coordinate
	mySamples=samples;
	allSamples=sampleFlag;
}
-(void)setSamples:(NSMutableArray*)samples:(CriteriaSummary*)criteria
{
	locations=samples;
	//visibleLocation=visible;
	//currentPublicStatus=publicStatus;
	searchCriteria=criteria;
	//originalCoordinates= original;
}

-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData= data;
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
	[sampleID release];
	[selectedSample release];
    [super dealloc];
}


@end
