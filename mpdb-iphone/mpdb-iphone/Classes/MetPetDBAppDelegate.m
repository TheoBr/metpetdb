#import "MetPetDBAppDelegate.h"

@implementation MetPetDBAppDelegate

@synthesize window;
@synthesize navigationController, mainController;

- (void)applicationDidFinishLaunching:(UIApplication *)application {
	MainViewController *viewController=[[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainController= viewController;
	[viewController release];
	UIView *newView= [mainController view];
	navigationController= [[UINavigationController alloc] initWithRootViewController:mainController];
	[navigationController.navigationBar setBarStyle:1];


	[window addSubview:[navigationController view]];
    [window makeKeyAndVisible];
    
}

- (void)dealloc {
	
	[mainController release];
	[window release];
	[super dealloc];
}

@end
