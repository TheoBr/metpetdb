//
//  LoginView.h
//  Location
//
//  Created by Heather Buletti on 5/8/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyCLController.h"

@class MainViewController;
@interface LoginViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
	//IBOutlet UILabel *welcome;
	IBOutlet UILabel *intro;
	IBOutlet UITextField *usernametext;
	IBOutlet UITextField *passwordtext;
	IBOutlet UILabel *usernamelabel;
	IBOutlet UILabel *passwordlabel;
	IBOutlet UIBarButtonItem *loginButton;
	IBOutlet UIToolbar *toolbar;
	MainViewController *mainViewController;
	BOOL keyboardShown;
	NSString *username;
	NSString *password;
	
 }
@property (nonatomic, retain) UITextField *usernametext;
@property (nonatomic, retain) UITextField *passwordtext;
@property (nonatomic, retain) UILabel *usernamelabel;
@property (nonatomic, retain) UILabel *passwordlabel;
//@property (nonatomic, retain) UILabel *welcome;
@property (nonatomic, retain) UILabel *intro;
@property (nonatomic, retain) UIBarButtonItem *loginButton;
@property (nonatomic, retain) MainViewController *mainViewController;
@property(nonatomic, getter=isScrollEnabled) BOOL scrollEnabled;
@property (nonatomic, copy) NSString *username;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, retain) UIToolbar *toolbar;
-(IBAction)loadMainViewController:(id)sender;

@end
