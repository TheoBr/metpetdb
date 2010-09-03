//
//  SampleViewController.h
//  Location
//
//  Created by Heather Buletti on 5/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableController.h"
#import "SearchCriteriaController.h"
#import "uniqueSamples.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class uniqueSamples;
@class SearchCriteriaController;
@class AnnotationObjects;
@class CriteriaSummary;
@interface RockTypeController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource, MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation> {
	IBOutlet UIPickerView *rockSelector;
	IBOutlet UILabel *output;
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myLocations;
	NSString *currentStringValue; 
	NSString *rockName;
	AnnotationObjects *newAnnotation;
	NSMutableArray *myRockTypes; //this is the data source for the picker view and the list of all the rock types of the returned samples
	SearchCriteriaController *criteriaController;
	UIToolbar *toolbar;
	UIBarButtonItem *refineButton;
	NSMutableArray *modifiedLocations; //this array holds the rocks that are of the rock type the user specifies
	//CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSString *tempRock;
	uniqueSamples *group;
	uniqueSamples *newgroup;
	NSString* mapType; //indicates map, hybrid or satellite
	NSMutableArray *points;
	CriteriaSummary *searchCriteria;
	CurrentSearchData *currentSearchData;
}
@property (nonatomic, retain) CurrentSearchData *currentSearchData;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) uniqueSamples *group;
@property (nonatomic, retain) uniqueSamples *newgroup;
@property (nonatomic, copy)NSString *tempRock;
@property (nonatomic, retain) UIPickerView *rockSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, retain) AnnotationObjects *newAnnotation;
@property (nonatomic, copy) NSString *rockName;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property(nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;
-(IBAction)refineSearch:(id)sender;


@end
