#import "MetPetDBAppDelegate.h"

@implementation MetPetDBAppDelegate

@synthesize window;
@synthesize navigationController, mainController;

- (void)applicationDidFinishLaunching:(UIApplication *)application {
	MainViewController *viewController=[[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainController= viewController;
	[viewController release];
	navigationController= [[UINavigationController alloc] initWithRootViewController:mainController];
	[navigationController.navigationBar setBarStyle:1];


	[window addSubview:[navigationController view]];
    [window makeKeyAndVisible];
    
}
- (void)setNetworkActivityIndicatorVisible:(BOOL)setVisible {
    static NSInteger NumberOfCallsToSetVisible = 0;
    if (setVisible) 
        NumberOfCallsToSetVisible++;
    else 
        NumberOfCallsToSetVisible--;
	
    // The assertion helps to find programmer errors in activity indicator management.
    // Since a negative NumberOfCallsToSetVisible is not a fatal error, 
    // it should probably be removed from production code.
    NSAssert(NumberOfCallsToSetVisible >= 0, @"Network Activity Indicator was asked to hide more often than shown");
	
    // Display the indicator as long as our static counter is > 0.
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:(NumberOfCallsToSetVisible > 0)];
}
- (void)dealloc {
	
	[mainController release];
	[window release];
	[super dealloc];
}

@end
