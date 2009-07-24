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



@class MainViewController;
@class MapTypeController;
@class SampleInfoController;
@class uniqueSamples;
@class TableController;
@class RockTypeController;
@class SampleTableController;
@class SearchCriteriaController;
@interface MapController : UIViewController <MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation> {
	MKMapView *mapView;
	NSMutableArray *singlePoints;
	NSMutableArray *mySamples;
	SampleInfoController *sampleinfo;
	MainViewController *mainViewController;
	MapTypeController *mapTypeController;
	UIButton *details;
	NSString *selectedID;
	NSMutableArray *buttonArray;
	UIToolbar *toolbar;
	NSMutableArray *buttons;
	UINavigationBar *navBar;
	UIBarButtonItem *narrowSearch;
	UIBarButtonItem *satelliteView;
	UIBarButtonItem *hybridView;
	UIBarButtonItem *mapViewButton;
	UIBarButtonItem *homeButton;
	UIButton *infoButton;
	TableController *tableController;
	NSMutableArray *myMinerals;
	NSMutableArray *myRockTypes;
	NSMutableArray *myMetamorphicGrades;
	RockTypeController *rockTypeController;
	NSMutableArray *originalData; //keep this array as a copy of the original search results so they can be restored, it is an array of arrays containing:
								  //an array of the unique sample locations, an array of minerals, an array of metamorphic grades, and an array of rock types
	SampleTableController *sampleTableController;
	NSMutableArray *multiplePoints; //array of uniqueSample objects
	NSMutableArray *multipleButtons; 
	CLLocationCoordinate2D myLocation;
	double latitudeSpan;
	double longitudeSpan;
	double radiusDegrees;
	UIView *boxView;
	double viewWidth; //the width of the view, constant, 320
	double viewHeight; //the height of the view, constant, 480
	SearchCriteriaController *criteriaController;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSMutableArray *currentRemovedSamples; //this array represents the samples that have been removed from the map as search criteria have been specified
	NSMutableArray *currentOwners;
	NSString *region; //region being searched in, needs to be displayed as a geographic crieria
	uniqueSamples *selectedSample;
	NSString *mapType; //this string can be either map, satellite, or hybrid
	double maxLat, minLat, minLong, maxLong; //these will be used to make the points that will be used to make the search square;
	NSMutableArray *boundaryAnnotations;
	NSMutableArray *points; //this array holds the max and min lat and longs so the original search box on the map can be redrawn
	
}
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
