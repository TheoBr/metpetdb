//
//  AddCommentController.m
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AddCommentController.h"
#import "KeychainWrapper.h"

@implementation AddCommentController
@synthesize titleText, sampleID, textView, toolbar, Uname;
-(void)viewDidLoad
{
	textCleared=FALSE;
	//scrollView.contentSize= self.view.bounds.size;
	CGRect frame=CGRectMake(20,32,280,40);
	UILabel *title=[[UILabel alloc] initWithFrame:frame];
	titleText=[[NSString alloc] initWithFormat: @"Add a Comment for Sample %@:", sampleID];
	title.text=titleText;
	title.backgroundColor=[UIColor blackColor];
	title.textColor=[UIColor whiteColor];
	title.textAlignment=UITextAlignmentCenter;
	[self.view addSubview:title];
	
	
	CGRect textViewFrame=	CGRectMake(0, 10, 320, 200);
	textView=[[UITextView alloc] initWithFrame: textViewFrame];
	textView.scrollEnabled=YES;
	textView.editable=YES;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];
	textView.backgroundColor=[UIColor whiteColor];
	textView.textColor=[UIColor blackColor];
	[self.view addSubview:textView];	
	textView.text=@"Click to add a comment.";
	textView.textColor=[UIColor blackColor];
	[self registerForKeyboardNotifications];
	
	//make a toolbar at the bottom of the view and add a button to add comments
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	UIBarButtonItem *addCommentButton=[[UIBarButtonItem alloc] initWithTitle:@"Upload Comment" style: UIBarButtonItemStyleBordered target:self action:@selector(add)];
	self.navigationItem.rightBarButtonItem=addCommentButton;
	/*[buttons addObject:addCommentButton];
	 CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	 toolbar = [ [ UIToolbar alloc ] init ];
	 toolbar.frame = toolBarFrame;
	 toolbar.items=buttons;	
	 [toolbar setBarStyle:1];
	 [self.view addSubview:toolbar];*/
	
	//if the user is logged in, get the username so the comment can be attached to the user who posted it
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	
}	

-(void)add
{
	[textView resignFirstResponder];
	NSString *newComment= [[NSString alloc] initWithString:textView.text];
	if([newComment length]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Comment Entered" message:@"A comment must be entered before it can be uploaded." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		//NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhonePost.svc?"];
		NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhonePost.svc?"];
		NSURL *myURL=[NSURL URLWithString:urlString];
		NSMutableURLRequest *myRequest = [NSMutableURLRequest
										  requestWithURL:myURL];
		[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
		[myRequest setHTTPMethod:@"POST"];
		//NSString *postString=[[NSString alloc] initWithFormat: @"addCommentSampleID= %d\n", sampleID];
		NSString *postString=[[NSString alloc] initWithString:@"user= buleth@rpi.edu\n"];
		postString=[postString stringByAppendingString:@"addCommentSampleID= 7\n"]; 
		postString=[postString stringByAppendingFormat:@"commentToAdd= %@\n", newComment];
		if(Uname!=NULL)
		{
			postString=[postString stringByAppendingFormat:@"user= %@\n", Uname];
		}
		//postString = [postString stringByAppendingFormat:@"user= buleth@rpi.edu\n"];
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
	}
	NSString *returnString=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if([returnString isEqualToString:@"Comment Added"])
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Comment Added" message:@"You successfully uploaded a comment for this sample." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	
}

- (void)textViewDidBeginEditing:(UITextView *)textView {  
    [self scrollViewToCenterOfScreen:textView];  
}  
- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWasShown:)
												 name:UIKeyboardDidShowNotification object:nil];
	
    [[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWasHidden:)
												 name:UIKeyboardDidHideNotification object:nil];
	
}
-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text

{
	if([textView becomeFirstResponder] && textCleared==FALSE)
	{
		textView.text=@"";
		textCleared=TRUE;
	}
	return YES;
}
- (void)scrollViewToCenterOfScreen:(UIView *)theView {  
    CGFloat viewCenterY = theView.center.y;  
    CGRect applicationFrame = [[UIScreen mainScreen] applicationFrame];  
	
    CGFloat availableHeight = applicationFrame.size.height -261; //keyboardBounds.size.height;    // Remove area covered by keyboard  
	
    CGFloat y = viewCenterY - availableHeight / 2.0;  
    if (y < 0) {  
        y = 0;  
    }  
	// scrollView.contentSize = CGSizeMake(applicationFrame.size.width, applicationFrame.size.height + keyboardBounds.size.height);  
    //[scrollView setContentOffset:CGPointMake(0, y) animated:YES];  
}  
- (void)keyboardNotification:(NSNotification*)notification {  
    NSDictionary *userInfo = [notification userInfo];  
    NSValue *keyboardBoundsValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];  
    [keyboardBoundsValue getValue:&keyboardBounds];  
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
	[sampleID release];
	[titleText release];
	[textView release];
	[toolbar release];
	[myReturn release];
	
    [super dealloc];
}


@end
