//
//  LoginView.m
//  Location
//
//  Created by Heather Buletti on 5/8/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LoginViewController.h"
#import "MainViewController.h"


@implementation LoginViewController
@synthesize intro, usernamelabel, usernametext, passwordlabel, passwordtext, loginButton, toolbar;
@synthesize mainViewController;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		[self registerForKeyboardNotifications];
		[self.navigationController.navigationBar setBarStyle:1];
		return self;
	}
}
	
-(void)viewDidLoad{
	toolbar.barStyle= UIBarStyleBlack;
	[self registerForKeyboardNotifications];

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

// Called when the UIKeyboardDidShowNotification is sent.
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    if (keyboardShown)
        return;
	
    NSDictionary* info = [aNotification userInfo];
	
    // Get the size of the keyboard.
    NSValue* aValue = [info objectForKey:UIKeyboardBoundsUserInfoKey];
    CGSize keyboardSize = [aValue CGRectValue].size;
	
    // Resize the scroll view (which is the root view of the window)
  //  CGRect viewFrame = [scrollview frame];
    //viewFrame.size.height -= keyboardSize.height;
    //scrollview.frame = viewFrame;
	
    // Scroll the active text field into view.
	//CGRect textFieldRect = [passwordtext frame];
	//	[scrollview scrollRectToVisible:textFieldRect animated:YES];
	keyboardShown = YES;
	self.scrollEnabled= NO;
}


// Called when the UIKeyboardDidHideNotification is sent
- (void)keyboardWasHidden:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
	
    // Get the size of the keyboard.
    NSValue* aValue = [info objectForKey:UIKeyboardBoundsUserInfoKey];
    CGSize keyboardSize = [aValue CGRectValue].size;
	
    // Reset the height of the scroll view to its original value
//    CGRect viewFrame = [scrollview frame];
  //  viewFrame.size.height += keyboardSize.height;
    //scrollview.frame = viewFrame;
	//CGRect textFieldRect = [welcome frame];
	//[scrollview scrollRectToVisible:textFieldRect animated:YES];
	self.scrollEnabled=NO;
	
    keyboardShown = NO;
	[self registerForKeyboardNotifications];
	
}

//This function makes the keyboard dissapear when the user presses the done button
-(BOOL)textFieldShouldReturn: (UITextField *) theTextField {
	if (theTextField == usernametext) 
	{
		[usernametext resignFirstResponder];
	}
	else if(theTextField == passwordtext)
	{
		[passwordtext resignFirstResponder];
	}
	return YES;
}

//This method is called when the username text field becomes active
- (void)textFieldDidBeginEditing:(UITextField *)textField{
	if(textField== usernametext){
		[usernametext becomeFirstResponder];
	}
	if(textField ==passwordtext)
	{
		[passwordtext becomeFirstResponder];
	}
	}


-(IBAction)loadMainViewController:(id)sender{  
		password= [[NSString alloc] initWithString:passwordtext.text];
		username= [[NSString alloc] initWithString:usernametext.text];
		MainViewController *viewController = [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
		self.mainViewController = viewController;
		UIView *controllersview= [mainViewController view];
		[self.view addSubview:controllersview];
	[self.navigationController pushViewController:mainViewController animated:NO];
		
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];

	[intro release];
	[usernamelabel release];
	[passwordlabel release];
	[passwordtext release];
	[loginButton release];
	[mainViewController release];
	
}




@end
