//
//  LoginView.h
//  Location
//
//  Created by Heather Buletti on 5/8/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyCLController.h"
#import "MetPetDBAppDelegate.h"
#import "KeychainWrapper.h"

@class MainViewController;
@class SecurityController;
@class MetPetDBAppDelegate;
@interface LoginViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
	NSString *currentStringValue;
	NSString *loginResponse;
	//IBOutlet UILabel *welcome;
	IBOutlet UILabel *intro;
	UITextField *usernametext;
	UITextField *passwordtext;
	IBOutlet UIBarButtonItem *loginButton;
	IBOutlet UIToolbar *toolbar;
	MainViewController *mainViewController;
	BOOL keyboardShown;
	NSString *username;
	NSString *password;
	NSMutableArray *rowTitles;
	UITableView *tableView;
	SecurityController *security;
	NSData *myReturn;
	UIBarButtonItem *backButton;
	NSString *returnValue;
	BOOL apacheError;
}
@property(nonatomic, copy)NSString *returnValue;
@property(nonatomic, copy)UIBarButtonItem *backButton;
@property(nonatomic, copy)NSString *currentStringValue;
@property(nonatomic, copy)NSString *loginResponse;
@property(nonatomic, retain) SecurityController *security;
@property (nonatomic, retain) UITableView *tableView;
@property (nonatomic, retain) UITextField *usernametext;
@property (nonatomic, retain) UITextField *passwordtext;
//@property (nonatomic, retain) UILabel *welcome;
@property (nonatomic, retain) UILabel *intro;
@property (nonatomic, retain) UIBarButtonItem *loginButton;
@property (nonatomic, retain) MainViewController *mainViewController;
@property(nonatomic, getter=isScrollEnabled) BOOL scrollEnabled;
@property (nonatomic, copy) NSString *username;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, retain) UIToolbar *toolbar;


@end
