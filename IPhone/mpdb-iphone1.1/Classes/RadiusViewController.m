//
//  RadiusViewController.m
//  Location
//
//  Created by Heather Buletti on 6/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "RadiusViewController.h"
#import "MyCLController.h"


@implementation RadiusViewController

@synthesize updateTextView, startStopButton, spinner, output, coord, mylat, mylong;
@synthesize locationBarButton;

-(void)viewDidLoad{
	
	
	//	[startStopButton setTitle:NSLocalizedString(@"View My Location", @"View My Location")];
	[MyCLController sharedInstance].delegate = self;
	
    // Check to see if the user has disabled location services all together
    // In that case, we just print a message and disable the "Start" button
    if ( ! [MyCLController sharedInstance].locationManager.locationServicesEnabled ) {
        [self addTextToLog:NSLocalizedString(@"NoLocationServices", @"User disabled location services")];
        startStopButton.enabled = NO;
    }
	
	
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		isCurrentlyUpdating = NO;
		firstUpdate = YES;
		
		return self;
	}
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
	// Release anything that's not essential, such as cached data
}


// Appends some text to the main text view
// If this is the first update, it will replace the existing text
-(void)addTextToLog:(NSString *)text {
	if (firstUpdate) {
		updateTextView.text = text;
		[spinner stopAnimating];
		firstUpdate = NO;
	} else {
        if([text characterAtIndex:0]!='L')
        {
			updateTextView.text = [NSString stringWithFormat:@"%@-----\n\n%@", updateTextView.text, text];
			[updateTextView scrollRangeToVisible:NSMakeRange([updateTextView.text length], 0)]; // scroll to the bottom on updates
			[spinner stopAnimating];
        }
	}
}

// Called when the view is loading for the first time only
// If you want to do something every time the view appears, use viewWillAppear or viewDidAppear

#pragma mark ---- control callbacks ----


// Called when the "start/stop" button is pressed
-(IBAction)startStopButtonPressed:(id)sender {
    bool flag= false;
    if(flag==false)
    {
        if (isCurrentlyUpdating) {
			[spinner stopAnimating];
            [[MyCLController sharedInstance].locationManager stopUpdatingLocation];
            isCurrentlyUpdating = NO;
            [startStopButton setTitle:NSLocalizedString(@"View My Location", @"View  My Location")];
            [spinner stopAnimating];
			flag=true;
            
        } else {
            [[MyCLController sharedInstance].locationManager startUpdatingLocation];
            isCurrentlyUpdating = YES;
            [startStopButton setTitle:NSLocalizedString(@"Begin Search", @"Begin Search")];
            [spinner startAnimating];
			
        }
    }
    if(flag==true)
    {
		[self loadSearchViewController];
	}
}
-(void)loadSearchViewController{    
    SearchViewController *viewController = [[SearchViewController alloc] initWithNibName:@"SearchView" bundle:nil];
    self.searchViewController = viewController;
    [viewController release];
    UIView *searchControllersView = [searchViewController view];
    [self.view addSubview:searchControllersView];
	[[self searchViewController] setCoordinates:mylat:mylong];
	[self.navigationController pushViewController:searchViewController animated:NO];
	
}
	

#pragma mark ---- delegate methods for the MyCLController class ----

-(void)newLocationUpdate:(NSString *)text:(CLLocation*) coordinate{
	coord= coordinate;
	mylat= [[NSString alloc] initWithFormat:@"%g", coord.coordinate.latitude];
	mylong=[[NSString alloc] initWithFormat:@"%g", coord.coordinate.longitude];
    [self addTextToLog:text];
}


-(void)newError:(NSString *)text {
	[self addTextToLog:text];
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end
