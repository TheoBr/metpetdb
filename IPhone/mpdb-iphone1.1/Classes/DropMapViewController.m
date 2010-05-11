//
//  DropMapViewController.m
//  MetPetDB
//
//  Created by Heather Buletti on 9/2/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "DropMapViewController.h"
#import "DDAnnotation.h"
#import "DDAnnotationView.h"



@implementation DropMapViewController
@synthesize toolbar, currentPublicStatus, radiusController, annotObj, navButton, latLongLabel, barButton;
-(void)viewDidLoad
{
	
	mapView=[[MKMapView alloc] initWithFrame:self.view.bounds];
	mapView.delegate=self;
	[self.view addSubview:mapView];
	
	barButton=[[UIBarButtonItem alloc] initWithTitle:@"Select Coordinate" style:UIBarButtonItemStyleBordered target:self action:@selector(loadMap)];
	navButton=[[UIBarButtonItem alloc] initWithTitle:@"Drop Pin" style:UIBarButtonItemStyleBordered target:self action:@selector(dropPin)];
	self.navigationItem.rightBarButtonItem=navButton;
	NSMutableArray *buttons= [[NSMutableArray alloc] init];
	[buttons addObject:barButton];
	viewWidth=self.view.bounds.size.width;
	CGRect toolBarFrame= CGRectMake (0, 377, viewWidth, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[mapView addSubview:toolbar];
	barButton.enabled=NO;
}
-(void)dropPin
{
	pin=[[MKAnnotationView alloc] init];
	[self.view addSubview:[self mapView:mapView viewForAnnotation:pin]];
	barButton.enabled=YES;
	navButton.enabled=NO;/*
						  latLongLabel=[[UILabel alloc] initWithFrame:CGRectMake(5, 20, 100, 30)];
						  latLongLabel.text=[[NSString alloc] initWithFormat:@"%@g, %@g", selectedCoordinate.latitude, selectedCoordinate.longitude];
						  //[self.view addSubview:latLongLabel];*/
	
	
}
-(void)loadMap
{
	//selectedCoordinate= annotationView.newCoordinate;
	selectedCoordinate=	[mapView convertPoint:annotationView.center toCoordinateFromView:self.view];	
	RadiusController *viewController = [[RadiusController alloc] initWithNibName:@"SearchView" bundle:nil];
	[currentSearchData setCenterCoordinate:selectedCoordinate];
	[viewController setData:currentSearchData];
	
	self.radiusController=viewController;
	[viewController release];
	UIView *newview=[radiusController view];
	[self.view addSubview:newview];
	[self.navigationController pushViewController:radiusController animated:NO];
	
	
}


- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	
	
	annotationView = (DDAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:@"Pin"];
	annotationView.canShowCallout=YES;
	if (annotationView == nil) {
		annotationView = [[[DDAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Pin"] autorelease];
	}
	// Dragging annotation will need _mapView to convert new point to coordinate;
	annotationView.mapView = mapView;
	//annotationView.title=@"Drag to Move";
	annotationView.center=CGPointMake(160, 240);
	selectedCoordinate=	[mapView convertPoint:annotationView.center toCoordinateFromView:self.view];
	
	//selectedCoordinate= annotationView.newCoordinate;
	return annotationView;
	
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
-(void)setData:(CurrentSearchData*)data
{
	currentSearchData=data;
	
}


- (void)dealloc {
	[mapView release];
	[toolbar release];
	[pin release];
	[annotationView release];
	[currentPublicStatus release];
	[radiusController release];
	[annotObj release];
	[navButton release];
	[latLongLabel release];
	[barButton release];
	
    [super dealloc];
}


@end
