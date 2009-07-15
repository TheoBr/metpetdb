//
//  CoordInputView.m
//  Location
//
//  Created by Heather Buletti on 6/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "CoordInputController.h"


@implementation CoordInputController
@synthesize radiusController, longitudeTextField, latitudeTextField, toolbar, latitude, longitude;

-(void)viewDidLoad
{
	latitude=nil;
	longitude=nil;
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	UIBarButtonItem *barButton=[[UIBarButtonItem alloc] initWithTitle:@"Continue" style:UIBarButtonItemStyleBordered target:self action:@selector(chooseRadius)];
	[buttons addObject:barButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
}

-(void)chooseRadius
{
	//create an alert sheet in case the latitude or longitude that was entered was invalid
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Invaldid Input" message:@"Enter a latitude between -90 and 90 and a longitude between -180 and 180" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
	//if an invalid value was entered, display this sheet
	double tempLat=[latitude doubleValue];
	double tempLong=[longitude doubleValue];
	if((tempLat > 90) || (tempLat < -90) || (tempLong >180) || (tempLong <-180)||latitude==nil || longitude==nil)
	{
		[alert show];
		latitudeTextField.text=nil;
		longitudeTextField.text=nil;
	}
	else
	{
		RadiusController *viewController = [[RadiusController alloc] initWithNibName:@"SearchView" bundle:nil];
		self.radiusController=viewController;
		[viewController setData:latitude:longitude];
		[viewController release];
		UIView *newview=[radiusController view];
		[self.view addSubview:newview];
		[self.navigationController pushViewController:radiusController animated:NO];
	}
}

//this function should hide the keyboard when the user presses the done button in each of the text fields
-(BOOL) textFieldShouldReturn: (UITextField *) theTextField {

	latitude=[[NSString alloc] initWithString:latitudeTextField.text];
	longitude= [[NSString alloc] initWithString:longitudeTextField.text];
	
	[theTextField resignFirstResponder];

	return YES;
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
