


#import "MainViewController.h"


@class MainViewController;
@interface MetPetDBAppDelegate : NSObject <UIApplicationDelegate> {
	IBOutlet UIWindow *window;
	MainViewController *mainController;
	UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) MainViewController *mainController;
@property (nonatomic, retain) UINavigationController *navigationController;

@end

