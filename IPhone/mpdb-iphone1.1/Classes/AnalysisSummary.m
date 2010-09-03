//
//  AnalysisSummary.m
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AnalysisSummary.h"
#import "KeychainWrapper.h"
#import "constants.h"

@implementation AnalysisSummary
@synthesize sampleID, textView, imageCount, analysisCount, subsampleCount, currentStringValue, bulkrock, sampleName, Uname;
-(void)viewDidLoad{
	minerals= [[NSMutableArray alloc] init];
	imageBool=FALSE;
	analysisBool=FALSE;
	subsampleCount=nil;
	[self getSubsampleInfo];
	
	CGSize textViewSize= CGSizeMake(320, 500);
	CGRect textViewFrame=	CGRectMake(0, 100, 320, 250);
	textView.frame= textViewFrame;
	textView.scrollEnabled=YES;
	textView.editable=NO;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];
	textView.textColor=[UIColor whiteColor];
	NSString *titleText=[[NSString alloc] initWithFormat:@"Subsample Info for Sample\n %@",sampleName];
	CGRect labelFrame= CGRectMake(0,0,320, 100);
	UILabel *title=[[UILabel alloc] initWithFrame:labelFrame];
	title.textColor=[UIColor whiteColor];
	title.textAlignment= UITextAlignmentCenter;
	title.backgroundColor=[UIColor blackColor];
	title.text=titleText;
	[self.view addSubview:title];
	if([subsampleCount isEqualToString:@"0"])
	{
		NSString *output= [[NSString alloc] initWithString:@"No subsamples exist for this sample."];
		textView.text= output;
	}
	else
	{
		NSString *output=[[NSString alloc] initWithFormat: @"Number of Subsamples: %@ \nNumber of Subsample Images: %@ \nNumber of Analyses Available: %@ \n", subsampleCount, imageCount, analysisCount];
		output=[output stringByAppendingString:@"Minerals Analyzed:"];
		int x;
		for(x=0; x<[minerals count]; x++)
		{
			output=[output stringByAppendingString:@"\n     "];
			output=[output stringByAppendingString:[minerals objectAtIndex:x]];
		}
		output= [output stringByAppendingFormat:@"\nBulk Rock Analysis: %@", bulkrock];
		textView.text=output;
	}
	//[self.view addSubview:textView];
	//if the user has logged in get their usename so it can be passed to the server 
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	
}
-(void)getSubsampleInfo
{
	//send the username and password to the server to be authenticated
	NSString *urlString= [[NSString alloc] initWithFormat:@"%@searchIPhonePost.svc?", RootURL];
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString=[[NSString alloc] initWithFormat: @"subsampleInfo= %@\n", sampleID];
	if(Uname!=nil)
	{
		postString= [[NSString alloc] stringByAppendingFormat:@"user= %@\n", Uname];
	}
	//postString = [postString stringByAppendingFormat:@"user= buleth@rpi.edu\n"];
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	NSString *returnValue=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if((myReturn == nil) && (error!=nil))
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
////	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/mpdb-iphone/mpdb-iphone/searchResults.kml"];
//	[fh writeData:myReturn];
	
	NSURLResponse *response;
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
	
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"subsamples"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"imageCount"])
	{
		imageBool=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"analysisCount"])
	{
		analysisBool=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"boolean"])
	{
		currentStringValue=nil;
		return;
	}
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (!currentStringValue) {
        // currentStringValue is an NSMutableString instance variable
        currentStringValue = [[NSMutableString alloc] initWithCapacity:50];
    }
    [currentStringValue appendString:string];
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
	if([elementName isEqualToString:@"string"])
	{
		[minerals addObject:currentStringValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"int"])
	{
		if(imageBool==TRUE)
		{
			imageCount=currentStringValue;
			imageBool=FALSE;
			currentStringValue=nil;
			return;
		}
		else if(analysisBool==TRUE)
		{
			analysisCount= currentStringValue;
			analysisBool=FALSE;
			currentStringValue=nil;
			return;
		}
		else //if both of the other bools are false, the int tag represents the subsample count
		{
			subsampleCount=currentStringValue;
			currentStringValue=nil;
			return;
		}
	}
	if([elementName isEqualToString:@"boolean"])
	{
		if([currentStringValue isEqualToString:@"false"])
		{
			bulkrock=@"No";
		}
		else
		{
			bulkrock=@"Yes";
		}
	}
}
//create a funtion that can be called from the subsample view controller that will return the number of subsamples and the number of chemical analyses
-(NSString*)getSubsampleCount
{
	[self getSubsampleInfo];
	//make an array to return that holds the image count and the subsample count
	return subsampleCount;
}

-(void)setData:(NSString*)sampleid:(NSString*)samplename
{
	sampleName= samplename;
	sampleID=sampleid;
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
	[sampleID release];
	[textView release];
	[imageCount release];
	[subsampleCount release];
	[analysisCount release];
	[subsampleResponse release];
	[myReturn release];
	[currentStringValue release];
	[minerals release];
	[bulkrock release];
	[sampleName release];
	
    [super dealloc];
}

@end
