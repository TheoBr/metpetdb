#import "MetPetDBAppDelegate.h"

@implementation MetPetDBAppDelegate

@synthesize window;
@synthesize navigationController, welcomeController;

- (void)applicationDidFinishLaunching:(UIApplication *)application {

	
	WelcomeViewController *viewController=[[WelcomeViewController alloc] initWithNibName:@"WelcomeView" bundle:nil];
	self.welcomeController = viewController;
	[viewController release];
	UIView *newView= [welcomeController view];
	navigationController= [[UINavigationController alloc] initWithRootViewController:welcomeController];
	[navigationController.navigationBar setBarStyle:1];


	[window addSubview:[navigationController view]];
    [window makeKeyAndVisible];
    
}

- (void)dealloc {
	
	[welcomeController release];
	[window release];
	[super dealloc];
}

@end
