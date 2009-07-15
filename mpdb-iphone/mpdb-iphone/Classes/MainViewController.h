#import "MyCLController.h"
#import "LocationViewController.h"
#import "RadiusController.h"
#import "CoordInputController.h"
#import "RegionViewController.h"

@class RadiusController;
@class LocationViewController;
@class CoordInputController;
@class RegionViewController;
@interface MainViewController : UIViewController <MyCLControllerDelegate, UITableViewDelegate, UITableViewDataSource>
{
	UITableView *tableView;
	IBOutlet UIButton *myLocationButton;
	IBOutlet UIButton *coordinateButton;
	IBOutlet UIButton *regionButton;
	LocationViewController *locationViewController;
	RadiusController *radiusController;
	CoordInputController *coordController;
	RegionViewController *regionController;
	NSString *myLat;
	NSString *myLong;
	BOOL isCurrentlyUpdating;
	BOOL firstUpdate;
	CLLocation *myCoordinate;
	NSMutableArray *rows;

}
@property (nonatomic, retain) UITableView *tableView;
@property(nonatomic, retain) IBOutlet UIButton *myLocationButton;
@property(nonatomic, retain) IBOutlet UIButton *coordinateButton;
@property(nonatomic, retain) IBOutlet UIButton *regionButton;
@property(nonatomic, retain) LocationViewController *locationViewController;
@property(nonatomic, retain) RadiusController *radiusController;
@property(nonatomic, retain)CoordInputController *coordController;
@property(nonatomic, retain)RegionViewController *regionController;
@property(nonatomic, copy) NSString *myLat;
@property(nonatomic, copy) NSString *myLong;
@property (nonatomic, retain) CLLocation *myCoordinate;


-(IBAction)useMyLocation:(id)sender;
-(IBAction)enterCoordinate:(id)sender;
-(IBAction)chooseRegion:(id)sender;
@end
