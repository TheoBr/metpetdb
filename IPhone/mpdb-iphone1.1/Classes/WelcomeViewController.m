//
//  WelcomeViewController.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "WelcomeViewController.h"
#include <CommonCrypto/CommonCryptor.h>


@implementation WelcomeViewController
//@synthesize beginButton, mainViewController;
@synthesize mainViewController, button;
-(void)viewDidLoad
{
	UIImageView *rockView;
	//rockView = [[[UIImageView alloc] initWithFrame:CGRectMake(15.0, 10.0, 288.0, 126.0)] autorelease];
	//rockView = [[[UIImageView alloc] initWithFrame:CGRectMake(0.0, 50.0, 320.0, 250.0)] autorelease];
	//NSString *logo=[[NSString alloc] initWithString:@"http://samana.cs.rpi.edu/metpetweb/images/logo-mask.png"];
	//UIImage *image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:logo]]];
	//rockView.image= image;
	//[self.view addSubview:rockView];
	//UIImage *backgroundImage= [[UIImage alloc] initWithContentsOfFile:@"iTunesArtwork.jpg"];
	//UIImage *backgroundImage=[[UIImage alloc] initWithContentsOfFile:@"/Users/heatherbuletti/Documents/mpdb-iphone/mpdb-iphone/iTunesArtwork.jpg"];
	UIImage *backgroundImage = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://samana.cs.rpi.edu/metpetweb/images/iTunesArtwork.jpg"]]];
	UIImageView *logoView=[[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 480)];
	logoView.image=backgroundImage;
	[self.view addSubview:logoView];
	
	UILabel *topLabel= [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 300, 200)];	
	topLabel.text=@"MetPetDB is a database for metamorphic geochemistry that is being designed and built by a global community of metamorphic petrologists in collaboration with computer scientists at Rensselaer Polytechnic Institute as part of the National Cyberinfrastructure Initiative and supported by the National Science Foundation.";
	topLabel.textAlignment=UITextAlignmentCenter;
	topLabel.numberOfLines=10;
	topLabel.backgroundColor=[UIColor clearColor];
	topLabel.font=[UIFont boldSystemFontOfSize:14];
	[self.view addSubview:topLabel];
	
	CGRect frame= CGRectMake(110, 360, 100, 30);
	//UIButton *button=[[UIButton alloc] initWithFrame:frame];
		button= [UIButton buttonWithType:UIButtonTypeRoundedRect];
	button.frame=frame;

	[button setTitle:@"Begin" forState:UIControlStateNormal];
		//button.imageView=imageView;
	[button addTarget:self action:@selector(begin) forControlEvents:UIControlEventTouchUpInside];
	[self.view addSubview:button];
	//[self testSymmetricEncryption];
	
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
