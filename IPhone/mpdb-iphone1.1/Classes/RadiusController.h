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
#import "MapController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"
#import "xmlParser.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"

@class CurrentSearchData;
@class MapController;
@class CriteriaSummary;
@interface RadiusController : UIViewController <NSObject, UIPickerViewDelegate, UIPickerViewDataSource>{
	IBOutlet UIPickerView *radiusPicker;
	IBOutlet UIBarButtonItem *searchButton;
	IBOutlet UILabel *outputlabel;
	IBOutlet UIToolbar *toolbar;
	BOOL isCurrentlyUpdating;
	BOOL firstUpdate;
	NSString *radius;
	NSString *latitude;
	NSString *longitude;
	NSString *north;
	NSString *south;
	NSString *east;
	NSString *west;
	CLLocation *mylocationCoordinate;
	//CLLocationCoordinate2D centerCoordinate;
	MapController *mapController;
	NSMutableArray *locations;
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
	NSMutableArray *radii; //holds the varius radii that will populate the picker view
	double radiusDegrees; //represents the radius in degrees
	double radiusMeters;
	double latitudeDegrees;
	double longitudeDegrees;
	double n, s, e, w;
	UIActivityIndicatorView *indicator;
	NSTimer *timer;
	bool selected;
	bool descriptionFlag;
	NSString *description;
	NSThread *myThread;
	uniqueSamples *newSet;
	NSXMLParser *myParser;
	CriteriaSummary *searchCriteria;
	NSData *myReturn;
	CurrentSearchData *currentSearchData;
	NSMutableArray *tempArray;
}
@property(nonatomic, retain) CurrentSearchData *currentSearchData;
@property(nonatomic, copy)NSString *north;
@property(nonatomic, copy)NSString *south;
@property(nonatomic, copy)NSString *east;
@property(nonatomic, copy)NSString *west;
@property(nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) NSString *description;
@property(nonatomic, retain) IBOutlet UIProgressView *progressView;
@property(nonatomic, retain) NSTimer *timer;
@property(nonatomic, retain) UIActivityIndicatorView *loading;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, retain) UIActivityIndicatorView *indicator;
@property (nonatomic, retain) IBOutlet UIPickerView *radiusPicker;
@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, retain) UILabel *outputlabel;
@property (nonatomic, copy) NSString *radius;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) CLLocation *mylocationCoordinate;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *publicStatus;
@property (nonatomic, copy) NSString *currentRockType;
@property (nonatomic, copy) NSString *currentOwner;

-(IBAction)showMap:(id)sender;
-(void)setCoordinates:(CLLocation*)mylocation;

-(void)getSamples:(NSMutableArray*)coordinates;

@end
