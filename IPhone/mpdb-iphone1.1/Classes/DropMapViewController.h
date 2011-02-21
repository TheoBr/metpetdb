//
//  DropMapViewController.h
//  MetPetDB
//
//  Created by Heather Buletti on 9/2/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DDAnnotation.h"
#import "DDAnnotationView.h"
#import "RadiusController.h"
#import "CurrentSearchData.h"


@class DDAnnotationView;
@class RadiusController;
@class AnnotationObjects;
@class CurrentSearchData;

@interface DropMapViewController : UIViewController< MKReverseGeocoderDelegate, MKMapViewDelegate> {
	MKMapView *mapView;
//	UIToolbar *toolbar;
	double viewWidth;
	CLLocationCoordinate2D selectedCoordinate;
	MKAnnotationView *pin;
	DDAnnotationView *annotationView;
	NSString *currentPublicStatus;
	bool locationVisible;
	RadiusController *radiusController;
	AnnotationObjects *annotObj;
	UIBarButtonItem *navButton;
	UILabel *latLongLabel;
	UIBarButtonItem *barButton;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, retain) UIBarButtonItem *barButton;
@property (nonatomic, retain) UILabel *latLongLabel;
@property (nonatomic, retain) UIBarButtonItem *navButton;
@property(nonatomic, retain) AnnotationObjects *annotObj;
@property(nonatomic, retain) RadiusController *radiusController;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property (nonatomic, retain) DDAnnotationView *annotationView;
//@property(nonatomic, retain)UIToolbar *toolbar;

//-(IBAction)loadMap;
@end
