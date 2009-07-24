//
//  PublicStatusController.h
//  Location
//
//  Created by Heather Buletti on 5/29/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SearchCriteriaController.h"

@class SearchCriteriaController;
@interface PublicStatusController : UIViewController {
	UISegmentedControl *segcontrol;
	NSMutableArray *items;
	IBOutlet UILabel *label;
	NSMutableArray *myLocations;
	NSMutableArray *modifiedLocations;
	UIToolbar *toolbar;
	SearchCriteriaController *criteriaController;
	NSMutableArray *original;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSMutableArray *currentOwners;
	NSString *region;
	CLLocationCoordinate2D myCoordinate;
	NSString* mapType; //indicates map, hybrid or satellite
	NSMutableArray *points;
}
@property (nonatomic,retain) UISegmentedControl *segcontrol;
@property (nonatomic, retain) IBOutlet UILabel *label;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;
@end
 