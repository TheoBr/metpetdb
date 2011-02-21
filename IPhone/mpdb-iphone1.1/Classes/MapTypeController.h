//
//  untitled.h
//  MetPetDB
//
//  Created by Heather Buletti on 7/9/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapController.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class MapController;
@interface MapTypeController : UIViewController {
	UISegmentedControl *segControl;
	MapController *mapController;
	//all the following variables exist to maintain information from the map
	NSMutableArray *samples;
	//CLLocationCoordinate2D myLocation;
	double latitudeSpan;
	double longitudeSpan;
	NSString *sampleCategory;
	bool visibleLocation;
	CriteriaSummary *searchCriteria;
	//CurrentSearchData *currentSearchData;
}
// @property(nonatomic, retain) CurrentSearchData *currentSearchData;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property(nonatomic, copy) NSString *sampleCategory;
@property(nonatomic, retain) UISegmentedControl *segControl;
@property(nonatomic, retain) MapController *mapController;

-(IBAction)loadMap;
@end
