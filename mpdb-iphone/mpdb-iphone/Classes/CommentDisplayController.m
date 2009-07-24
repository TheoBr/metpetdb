//
//  CommentDisplay.m
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "CommentDisplayController.h"


@implementation CommentDisplayController
@synthesize sampleID, titleText, addCommentButton, toolbar, addComment, textView, currentStringValue;

-(void)viewDidLoad
{
	//make the title label
	CGRect frame=CGRectMake(20,32,280,40);
	UILabel *title=[[UILabel alloc] initWithFrame:frame];
	titleText=[[NSString alloc] initWithFormat: @"Sample %@ Comments:", sampleID];
	title.text=titleText;
	title.backgroundColor=[UIColor blackColor];
	title.textColor=[UIColor whiteColor];
	title.textAlignment=UITextAlignmentCenter;
	[self.view addSubview:title];
	
	//make a toolbar at the bottom of the view and add a button to add comments
	/*NSMutableArray *buttons=[[NSMutableArray alloc] init];
	addCommentButton=[[UIBarButtonItem alloc] initWithTitle:@"Add a Comment" style: UIBarButtonItemStyleBordered target:self action: @selector(addComment)];
	[buttons addObject:addCommentButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];*/
	[self getComments];
	
}	
-(void)getComments
{
	NSString *urlString=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?comments=%@", sampleID];
	NSURL *myURL = [NSURL URLWithString:urlString];	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	NSURLResponse *response;
	NSError *error;
	commentResponse = [NSURLConnection sendSynchronousRequest:myRequest
											 returningResponse:&response error:&error];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	NSString *dataReceived=[[NSString alloc] initWithData:commentResponse encoding:NSASCIIStringEncoding];
	NSXMLParser *commentParser= [[NSXMLParser alloc] initWithData:commentResponse];
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
			
-(void)addComment
{
	AddCommentController *viewController=[[AddCommentController alloc] initWithNibName:@"AddComment" bundle:nil];
	[viewController setData:sampleID];
	self.addComment=viewController;
	[viewController release];
	UIView *ControllersView=[addComment view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:addComment animated:NO];
}
	
//create a function that returns the number of comments for the samples that can be called by the sampleInfo controller
-(NSString*)getCommentCount
{
	[self getComments];
	NSString *commentCount= [[NSString alloc] initWithFormat:@"%d", [comments count]];
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

-(void)setData:(NSString*)sample
{
	sampleID=sample;
}

- (void)dealloc {
    [super dealloc];
}


@end
