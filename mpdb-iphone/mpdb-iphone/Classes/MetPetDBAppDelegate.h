


#import "WelcomeViewController.h"


@class WelcomeViewController;
@interface MetPetDBAppDelegate : NSObject <UIApplicationDelegate> {
	IBOutlet UIWindow *window;
	WelcomeViewController *welcomeController;
	UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) WelcomeViewController *welcomeController;
@property (nonatomic, retain) UINavigationController *navigationController;

@end

