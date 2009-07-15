//
//  AddCommentController.m
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AddCommentController.h"


@implementation AddCommentController
@synthesize titleText, sampleID, textView, scrollView;
-(void)viewDidLoad
{
	//scrollView.contentSize= self.view.bounds.size;
	CGRect frame=CGRectMake(20,32,280,40);
	UILabel *title=[[UILabel alloc] initWithFrame:frame];
	titleText=[[NSString alloc] initWithFormat: @"Add a Comment for Sample %@:", sampleID];
	title.text=titleText;
	title.backgroundColor=[UIColor blackColor];
	title.textColor=[UIColor whiteColor];
	title.textAlignment=UITextAlignmentCenter;
	[self.view addSubview:title];
	
	CGRect textViewFrame=	CGRectMake(0, 150, 320, 250);
	textView=[[UITextView alloc] initWithFrame: textViewFrame];
	textView.scrollEnabled=YES;
	textView.editable=YES;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];
	textView.backgroundColor=[UIColor whiteColor];
	textView.textColor=[UIColor blackColor];
	[self.view addSubview:textView];	
	[self registerForKeyboardNotifications];
	
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

	
- (void)scrollViewToCenterOfScreen:(UIView *)theView {  
    CGFloat viewCenterY = theView.center.y;  
    CGRect applicationFrame = [[UIScreen mainScreen] applicationFrame];  
	
    CGFloat availableHeight = applicationFrame.size.height -261; //keyboardBounds.size.height;    // Remove area covered by keyboard  
	
    CGFloat y = viewCenterY - availableHeight / 2.0;  
    if (y < 0) {  
        y = 0;  
    }  
    scrollView.contentSize = CGSizeMake(applicationFrame.size.width, applicationFrame.size.height + keyboardBounds.size.height);  
    [scrollView setContentOffset:CGPointMake(0, y) animated:YES];  
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
    [super dealloc];
}


@end
