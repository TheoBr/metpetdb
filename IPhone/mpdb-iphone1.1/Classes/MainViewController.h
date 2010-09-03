#import "MyCLController.h"
#import "RadiusController.h"
#import "CoordInputController.h"
#import "InfoViewController.h"
#import "LoginViewController.h"
#import "CurrentSearchData.h"
#import "KeychainWrapper.h"


@class CurrentSearchData;
@class SecurityController;
@class LoginViewController;
@class InfoViewController;
@class RadiusController;
@class LocationViewController;
@class CoordInputController;
@class RegionViewController;
@class AlertViewController;
@interface MainViewController : UIViewController <CLLocationManagerDelegate, UITableViewDelegate, UITableViewDataSource>//, UIAlertViewDelegate>
{
	UIButton *signInButton;
	UIButton *signOutButton;
	SecurityController *security;
	UITableView *tableView;
	LoginViewController *loginController;
	RadiusController *radiusController;
	CoordInputController *coordController;
	RegionViewController *regionController;
	NSString *myLat;
	NSString *myLong;
	BOOL isCurrentlyUpdating;
	BOOL firstUpdate;
	CLLocation *myCoordinate;
	NSMutableArray *rows;
	InfoViewController *infoController;
	bool signedIn;
	NSString *username;
	UISegmentedControl *segControl;
	NSMutableArray *items;
	NSString *existingPassword;
	IBOutlet UILabel *usernameLabel;
	CLLocation *myLocation;
	int dontAllow; //this is set to true if the user selected don't allow
	IBOutlet UILabel *copyrightLabel;
	AlertViewController *alertController;
	CLLocation *coord;
	CurrentSearchData *currentSearchData;
	UITableViewCell *usernameCell;
	NSString *Uname;
	id dataSource;
	id delegate;
	MainViewController *mainViewController;
	UIAlertView *logoutAlertView;
}
@property(nonatomic, retain) UIAlertView *logoutAlertView;
@property(nonatomic, retain)MainViewController *mainViewController;
@property(nonatomic, retain) CurrentSearchData *currentSearchData;
@property (nonatomic, copy) NSString *existingPassword;
@property (nonatomic, retain) SecurityController *security;
@property(nonatomic, retain) UISegmentedControl *segControl;
@property (nonatomic, retain)LoginViewController *loginController;
@property(nonatomic, copy) NSString *username;
@property (nonatomic, retain) InfoViewController *infoController;
@property (nonatomic, retain) UITableView *tableView;
@property(nonatomic, retain) RadiusController *radiusController;
@property(nonatomic, retain)CoordInputController *coordController;
@property(nonatomic, retain)RegionViewController *regionController;
@property(nonatomic, retain)KeychainWrapper *keychain;
@property(nonatomic, copy) NSString *myLat;
@property(nonatomic, copy) NSString *myLong;
@property (nonatomic, retain) CLLocation *myCoordinate;

@property(nonatomic, assign) id<UITableViewDataSource> dataSource;
@property(nonatomic, assign) id<UITableViewDelegate> delegate;

@end
