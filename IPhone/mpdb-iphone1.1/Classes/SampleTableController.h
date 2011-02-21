//
//  SampleTableController.h
//  Location
//
//  Created by Heather Buletti on 6/5/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SampleInfoController.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class CriteriaSummary;
@class SampleInfoController;
@interface SampleTableController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView *tableView;
	NSMutableArray *rows;
	NSMutableArray *names;
	NSMutableArray *owners;
	NSMutableArray *mySamples;
	NSString *sampleID;
	SampleInfoController *sampleInfo;
	AnnotationObjects *selectedSample;
	AnnotationObjects *tempSample;
	bool allSamples; //if this bool is true, then all the samples on the map are being displayed and the mySamples array is an 
	//array of uniqueSample objects rather than annotation objects
	NSString *locations;
	NSString *tempName;
	NSString *tempOwner;
//	UIToolbar *toolbar;
	int currentSampleCount;
	
	//the following variables store information about the range of the map view so it can be reset to exactly how it was when the user presses the back button
	
	double latitudeSpan; //the span is a value in degrees that is used to set the zoom on the map view when it first appears
	double longitudeSpan;    
	double maxLat;
	double minLat;
	double maxLong;
	double minLong;
	
	//the following are more map variables
	//bool visibleLocation;
	NSString *sampleCategory;
	NSMutableArray *samples;
	//NSString *mapType;
	CriteriaSummary *searchCriteria;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	//NSMutableArray *originalCoordinates;
	//CurrentSearchData *currentSearchData;
}
//@property (nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, copy) CriteriaSummary *searchCriteria;
@property(nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;
@property(nonatomic, copy) NSString *sampleCategory;
@property (nonatomic, retain) NSMutableArray *samples;
@property (nonatomic, retain) NSMutableArray *currentRockTypes;
@property (nonatomic, retain) NSMutableArray *currentMinerals;
@property (nonatomic, retain) NSMutableArray *currentMetamorphicGrades;

@property (nonatomic, retain) NSMutableArray *currentOwners;

//@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, retain) SampleInfoController *sampleInfo;
@property (nonatomic, retain) AnnotationObjects *tempSample;
@property (nonatomic, copy) NSString *tempName;
@property (nonatomic, copy) NSString *tempOwner;
@end
