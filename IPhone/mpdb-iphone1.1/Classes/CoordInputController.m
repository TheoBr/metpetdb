//
//  CoordInputView.m
//  Location
//
//  Created by Heather Buletti on 6/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "CoordInputController.h"

@implementation CoordInputController
@synthesize radiusController, longitudeTextField, latitudeTextField, latitude, longitude, currentPublicStatus, username, mapButton, dropMap, focusButton;

-(void)viewDidLoad
{
	latitude=nil;
	longitude=nil;
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	UIBarButtonItem *barButton=[[UIBarButtonItem alloc] initWithTitle:@"Continue" style:UIBarButtonItemStyleBordered target:self action:@selector(chooseRadius)];
	[buttons addObject:barButton];
/*	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar]; */
	
	[self.navigationController setToolbarHidden:NO animated:YES];
	[self.navigationController.toolbar setBarStyle:UIBarStyleBlack];
	[self setToolbarItems:buttons animated:YES];
	

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
		CLLocation *myLocation = [[CLLocation alloc] initWithLatitude:tempLat longitude:tempLong];
		
	//	myCoord.latitude=tempLat;
	//	myCoord.longitude=tempLong;
		
	// 	CLLocationCoordinate2D myCoord = myLocation.coordinate;
		
		[CurrentSearchData setCenterCoordinateLatitude:myLocation.coordinate.latitude];
		[CurrentSearchData setCenterCoordinateLongitude:myLocation.coordinate.longitude];
		
		//[currentSearchData setCenterCoordinate:myCoord];
	//	[viewController setData:currentSearchData];
		[viewController release];
		UIView *newview=[radiusController view];
		[self.view addSubview:newview];
		[self.navigationController pushViewController:radiusController animated:NO];
	}
}

-(IBAction)textFieldDoneEditing:(id)sender{
	[self.latitudeTextField resignFirstResponder];
	[self.longitudeTextField resignFirstResponder];
}

//this function should hide the keyboard when the user presses the done button in each of the text fields
-(BOOL) textFieldShouldReturn: (UITextField *) theTextField {
	
	latitude=[[NSString alloc] initWithString:latitudeTextField.text];
	longitude= [[NSString alloc] initWithString:longitudeTextField.text];
	
	//[theTextField resignFirstResponder];
	
	return YES;
}
/*
-(void)setData:(CurrentSearchData*)data
{
	currentSearchData=data;
}*/
-(IBAction)loadMap:(id)sender
{
	//MKAnnotationView *pin=[[MKAnnotationView alloc] init];
	
	//[self.view addSubview:[self mapView:mapView viewForAnnotation:pin]];
	DropMapViewController *viewController= [[DropMapViewController alloc] initWithNibName:@"dropMapView" bundle:nil];
//	[viewController setData:currentSearchData];
	self.dropMap= viewController;
	[viewController release];
	UIView *newView= [dropMap view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:dropMap animated:NO];
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
	[radiusController release];
	[dropMap release];
	[latitudeTextField release];
	[longitudeTextField release]; 
	[mapButton release];
	//[toolbar release];
	[latitude release];
	[longitude release];
	[currentPublicStatus release];
	[username release];
    [super dealloc];
}


@end
