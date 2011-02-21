//
//  CommentDisplay.m
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "CommentDisplayController.h"
#import "KeychainWrapper.h"
#import "constants.h"

@implementation CommentDisplayController
@synthesize sampleID, titleText, addCommentButton, addComment, textView, currentStringValue, sampleName, commentCount, Uname;

-(void)viewDidLoad
{
	//make the title label
	CGRect frame=CGRectMake(20,32,280,40);
	UILabel *title=[[UILabel alloc] initWithFrame:frame];
	titleText=[[NSString alloc] initWithFormat: @"Comments for Sample\n %@", sampleName];
	title.text=titleText;
	title.backgroundColor=[UIColor blackColor];
	title.textColor=[UIColor whiteColor];
	title.textAlignment=UITextAlignmentCenter;
	[self.view addSubview:title];
	
	//make a toolbar at the bottom of the view and add a button to add comments
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	//addCommentButton=[[UIBarButtonItem alloc] initWithTitle:@"Add a Comment" style: UIBarButtonItemStyleBordered target:self action: @selector(addComment)];
	//[buttons addObject:addCommentButton];
	
/*	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar]; */
	
	[self.navigationController setToolbarHidden:NO animated:YES];
	[self.navigationController.toolbar setBarStyle:UIBarStyleBlack];	
	[self setToolbarItems:buttons animated:YES];
	
	
	//only call the getComment function if the boolean is true
	if(callCommentFunction== TRUE)
	{
		[self getComments];
	}
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	
}	
-(void)getComments
{
	//send the username and password to the server to be authenticated
	NSString *urlString= [[NSString alloc] initWithFormat:@"%@searchIPhonePost.svc?", RootURL];
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString=[[NSString alloc] initWithFormat: @"comments= %@\n", sampleID];
	//NSString *postString=@"comments= 7\n";
	if(Uname!=nil)
	{
		postString= [[NSString alloc] stringByAppendingFormat:@"user= %@\n", Uname];
	}
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	NSString *returnValue=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if((myReturn == nil) && (error != nil))
	{ 
		NSString* errorStr = [[NSString alloc] init];
		
		errorStr = error.domain;
		
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet."@"Network failure: unable to connect to internet." message:errorStr delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
//	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test4.txt"];
//	[fh writeData:myReturn];
	
	NSString *dataReceived=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	NSXMLParser *commentParser= [[NSXMLParser alloc] initWithData:myReturn];
	[commentParser setDelegate:self];
	[commentParser parse];
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"comments"])
	{
		//make sure this array is reset here so that when the back button is pressed the samples do not get added to the original array
		comments=[[NSMutableArray alloc]init];
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"int"]){
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
		[comments addObject:currentStringValue]; //add each comment to the array
		currentStringValue=nil;
		return;
	}
	else if([elementName isEqualToString:@"comments"])
	{
		//there are no more comments for this sample, display the comments in the comments array
		[self showComments];
	}
	else if([elementName isEqualToString:@"int"]) //the value between the int tags represents the number of comments for the sample
	{
		commentCount=currentStringValue;
		currentStringValue=nil;
		return;
	}
	
}
-(void)showComments
{
	
	CGRect textViewFrame=	CGRectMake(0, 150, 320, 200);
	textView=[[UITextView alloc] initWithFrame: textViewFrame];
	textView.scrollEnabled=YES;
	textView.editable=NO;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];
	textView.textColor=[UIColor whiteColor];
	
	int x;
	NSString *output=[[NSString alloc] init];
	if([comments count]==0)
	{
		output=[[NSString alloc] initWithString:@"There are no comments for this sample."];
	}
	else
	{
		for(x=0; x<[comments count]; x++)
		{
			NSString *commentTitle=[[NSString alloc] initWithFormat:@"Comment #%d: \n\t %@\n", x+1, [comments objectAtIndex:x]];
			output=[output stringByAppendingString:commentTitle];
		}
	}
	textView.text=output;
	textView.backgroundColor=[UIColor blackColor];
	textView.textColor=[UIColor whiteColor];
	[self.view addSubview:textView];
}

/*-(void)addComment
 {
 AddCommentController *viewController=[[AddCommentController alloc] initWithNibName:@"AddComment" bundle:nil];
 [viewController setData:sampleID];
 self.addComment=viewController;
 [viewController release];
 UIView *ControllersView=[addComment view];
 [self.view addSubview:ControllersView];
 [self.navigationController pushViewController:addComment animated:NO];
 }*/

//create a function that returns the number of comments for the samples that can be called by the sampleInfo controller
-(NSString*)getCommentCount
{
	[self getComments];
	//commentCount= [[NSString alloc] initWithFormat:@"%d", [comments count]];
	return commentCount;
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

-(void)setData:(NSString*)sampleid:(NSString*)samplename:(bool)getSamples
{
	callCommentFunction= getSamples;
	sampleName= samplename;
	sampleID=sampleid;
	
}

- (void)dealloc {
	[sampleID release];
	[titleText release];
//	[toolbar release];
	[addCommentButton release];
	[addComment release];
	[textView release];
	[commentResponse release];
	[myReturn release];
	[comments release];
	[currentStringValue release];
	[sampleName release];
	
    [super dealloc];
}


@end
