//
//  WelcomeViewController.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "WelcomeViewController.h"


@implementation WelcomeViewController
//@synthesize beginButton, mainViewController;
@synthesize mainViewController, button;
-(void)viewDidLoad
{
	UIImageView *rockView;
	rockView = [[[UIImageView alloc] initWithFrame:CGRectMake(15.0, 10.0, 288.0, 126.0)] autorelease];
	//rockView = [[[UIImageView alloc] initWithFrame:CGRectMake(0.0, 50.0, 320.0, 250.0)] autorelease];
	rockView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
	//UIImage *image=[[UIImage alloc] initWithContentsOfFile:@"/Users/heatherbuletti/Documents/MetPetDB/MPDBlogo.jpg"];
	NSString *logo=[[NSString alloc] initWithString:@"http://samana.cs.rpi.edu:8080/metpetwebtst/images/logo-mask.png"];
	UIImage *image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:logo]]];
	rockView.image= image;
	[self.view addSubview:rockView];
	
	CGRect frame= CGRectMake(110, 300, 100, 30);
	//UIButton *button=[[UIButton alloc] initWithFrame:frame];
		button= [UIButton buttonWithType:UIButtonTypeRoundedRect];
	button.frame=frame;

	[button setTitle:@"Begin" forState:UIControlStateNormal];
		//button.imageView=imageView;
	[button addTarget:self action:@selector(begin) forControlEvents:UIControlEventTouchUpInside];
	[self.view addSubview:button];
	
}
//-(IBAction) begin:(id)sender
-(void) begin
{
	MainViewController *viewController=[[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainViewController = viewController;
	[viewController release];
	UIView *mainView= [mainViewController view];
	[self.view addSubview:mainView];
	[self.navigationController pushViewController:mainViewController animated:NO];
	
}

- (void)dealloc {
    [super dealloc];
}


@end
