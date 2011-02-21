//
//  MapController.h
//  Location
//
//  Created by Heather Buletti on 5/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnnotationObjects.h"
#import "SampleInfoController.h"
#import "TableController.h"
#import "RockTypeController.h"
#import "SampleTableController.h"
#import "SearchCriteriaController.h"
#import "uniqueSamples.h"
#import "MainViewController.h"
#import "MapTypeController.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class MainViewController;
@class MapTypeController;
@class SampleInfoController;
@class uniqueSamples;
@class TableController;
@class RockTypeController;
@class SampleTableController;
@class SearchCriteriaController;
@class RadiusController;
@class CriteriaSummary;
@class CurrentSearchData;
@interface MapController : UIViewController <UIAlertViewDelegate, MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation, MyCLControllerDelegate> {
	MKMapView *mapView;
	NSMutableArray *singlePoints;
	NSMutableArray *mySamples;
	SampleInfoController *sampleinfo;
	MainViewController *mainViewController;
	MapTypeController *mapTypeController;
	UIButton *details;
	NSString *selectedID;
	NSMutableArray *buttonArray;
//	UIToolbar *toolbar;
	NSMutableArray *buttons;
	UINavigationBar *navBar;
	UIBarButtonItem *narrowSearch;
	UIBarButtonItem *satelliteView;
	UIBarButtonItem *hybridView;
	UIBarButtonItem *mapViewButton;
	UIBarButtonItem *homeButton;
	UIBarButtonItem *viewNextButton;
	UIButton *infoButton;
	TableController *tableController;
	NSMutableArray *myMinerals;
	NSMutableArray *myRockTypes;
	NSMutableArray *myMetamorphicGrades;
	RockTypeController *rockTypeController;
	SampleTableController *sampleTableController;
	NSMutableArray *multiplePoints; //array of uniqueSample objects
	NSMutableArray *multipleButtons; 
	double latitudeSpan;
	double longitudeSpan;
	double radiusDegrees;
	double radiusMeters;
	UIView *boxView;
	double viewWidth; //the width of the view, constant, 320
	double viewHeight; //the height of the view, constant, 480
	SearchCriteriaController *criteriaController;
	//CurrentSearchData *currentSearchData;
	NSMutableArray *currentRemovedSamples; //this array represents the samples that have been removed from the map as search criteria have been specified
	uniqueSamples *selectedSample;
	double maxLat, minLat, minLong, maxLong; //these will be used to make the points that will be used to make the search square;
	NSMutableArray *boundaryAnnotations;
	NSMutableArray *points; //this array holds the max and min lat and longs so the original search box on the map can be redrawn
	NSString *sampleCategory;
	UIActivityIndicatorView *indicator;
	int dontAllow;
	RadiusController *radiusController;
	bool mapBool;
	NSTimer *timer;
	CLLocation *user;
	int count;
	UIBarButtonItem *myLocationButton;
	NSInteger totalCount; //indicates the total number of samples returned in the search, not necessarily equal to the number being displayed
	int currentSampleCount;
	int cummulativeCount;
	NSData *myReturn;
	
	//the following variables are for parsing xml output
	bool rockFlag;
	bool ownerFlag;
	bool nameFlag;
	bool mineralFlag;
	bool descriptionFlag;
	bool metamorphicFlag;
	bool uniqueFlag;
	NSString *currentStringValue;
	NSString *description;
	NSString *sampleName;
	double longdouble;
	double latdouble;
	AnnotationObjects *newAnnotation;
	uniqueSamples *newSet;
	NSString *publicStatus;
	NSString *sampleID;
	NSMutableArray *addedLocations;
	NSMutableArray *currMetGrades;
	NSMutableArray *currMinerals;
	NSMutableArray *mins;
	NSMutableArray *metGrades;
	NSMutableArray *currPublicStatus;
	NSMutableArray *currOwner;
	NSMutableArray *currRockTypes;
	NSString *currentOwner;
	NSString *rock;
	CriteriaSummary *searchCriteria;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) NSString *description;
@property (nonatomic, retain) NSString *publicStatus;
@property(nonatomic, retain) NSString *sampleName;
@property(nonatomic, retain) NSString *rock;
@property (nonatomic, retain) NSString *currentOwner;
@property(nonatomic, retain) NSString *sampleID;
@property(nonatomic, retain)NSString *currentStringValue;
@property(nonatomic, retain) UIBarButtonItem *viewNextButton;
@property(nonatomic, retain) UIBarButtonItem *myLocationButton;
@property(nonatomic, retain) RadiusController *radiusController;
@property (nonatomic, retain) UIActivityIndicatorView *indicator;
@property(nonatomic, copy) NSString *sampleCategory;
@property (nonatomic, retain) MKMapView *mapView;
@property (nonatomic, retain) SampleInfoController *sampleinfo;
@property (nonatomic, retain) MainViewController *mainViewController;
@property (nonatomic, retain) MapTypeController *mapTypeController;
@property (nonatomic, retain) UIButton *details;
@property (nonatomic, copy) NSString *selectedID;
@property (nonatomic, retain) UINavigationBar *navBar;
@property (nonatomic, retain) UIBarButtonItem *narrowSearch;
@property (nonatomic, retain) UIBarButtonItem *satelliteView;
@property (nonatomic, retain) UIBarButtonItem *hybridView;
@property (nonatomic, retain) UIBarButtonItem *mapViewButton;
@property (nonatomic, retain) UIBarButtonItem *homeButton;
@property (nonatomic, retain) UIButton *infoButton;
@property (nonatomic, retain) TableController *tableController;
@property (nonatomic, retain) RockTypeController *rockTypeController;
@property (nonatomic, retain) SampleTableController *sampleTableController;
@property (nonatomic, retain) UIView *boxView;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, retain) uniqueSamples *selectedSample;
@property (nonatomic, copy) NSString *mapType;
@end
