//
//  InfoViewController.m
//  MetPetDB
//
//  Created by Heather Buletti on 7/23/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "InfoViewController.h"


@implementation InfoViewController
@synthesize wikiButton;

-(IBAction)goToWiki:(id)sender
{
	NSURL *target = [[NSURL alloc] initWithString:@"http://wiki.cs.rpi.edu/trac/metpetdb/wiki/IphoneApp"];
    [[UIApplication sharedApplication] openURL:target];
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
