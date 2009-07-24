//
//  SearchViewController.h
//  LocateMe
//
//  Created by Heather Buletti on 5/3/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyCLController.h"
#import "MainViewController.h"
#import "LocationViewController.h"
#import "MapController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"


@class MapController;
@interface RadiusController : UIViewController <NSObject, UIPickerViewDelegate, UIPickerViewDataSource>{
	IBOutlet UIPickerView *radiusPicker;
	UIBarButtonItem *searchButton;
	IBOutlet UILabel *outputlabel;
	IBOutlet UIToolbar *toolbar;
	BOOL isCurrentlyUpdating;
	BOOL firstUpdate;
	NSString *radius;
	NSString *latitude;
	NSString *longitude;
	CLLocation *mylocationCoordinate;
	MapController *mapController;
	NSString *currentStringValue;
	NSMutableArray *locations;
	NSData *myReturn;
	AnnotationObjects *newAnnotation;
	double latdouble;
	double longdouble;
	NSString *sampleID;
	NSString *sampleName;
	//the following arrays hold information about the all the samples that meet the specified geographic criteria
	NSMutableArray *minerals;
	NSMutableArray *metamorphicGrades;
	NSMutableArray *rockTypes;
	NSString *currentRockType;
	bool mineralFlag;
	bool metamorphicFlag;
	bool nameFlag;
	bool rockFlag;
	bool uniqueFlag;
	bool ownerFlag;
	NSString *publicStatus;
	NSMutableArray *currentMinerals; //represents the minerals contained in just the current sample and should be added to the new annotation
	NSMutableArray *currentMetamorphicGrades; //represents the metamorphic grades contained in just the current sample and should be added to
	NSString *currentOwner;
	NSMutableArray *radii; //holds the varius radii that will populate the picker view
	double radiusDegrees; //represents the radius in degrees
	double radiusMeters;
	double latitudeDegrees;
	double longitudeDegrees;
	double n, s, e, w;
	bool blah;
	UIActivityIndicatorView *indicator;
}

@property (nonatomic, retain) IBOutlet UIPickerView *radiusPicker;
@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, retain) UILabel *outputlabel;
@property (nonatomic, copy) NSString *radius;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) CLLocation *mylocationCoordinate;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *publicStatus;
@property (nonatomic, copy) NSString *currentRockType;
@property (nonatomic, copy) NSString *currentOwner;
@property (nonatomic, retain) UIActivityIndicatorView *indicator;

//-(IBAction)showMap:(id)sender;
-(void)setCoordinates:(CLLocation*)mylocation;



@end
