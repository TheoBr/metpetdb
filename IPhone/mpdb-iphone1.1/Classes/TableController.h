//
//  TableController.h
//  Location
//
//  Created by Heather Buletti on 5/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RockTypeController.h"
#import "MetamorphicGradeController.h"
#import "MineralsController.h"
#import "OwnerViewController.h"
#import "CriteriaSummary.h"
#import "PublicPrivateViewController.h"
#import "CurrentSearchData.h"

@class CurrentSearchData;
@class RockTypeController;
@class OwnerViewController;
@class MetamorphicGradeController;
@class MineralsController;
@class MapController;
@class CriteriaSummary;
@class PublicPrivateViewController;
@interface TableController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView *tableView;
	NSMutableArray *locations; //array of annotations representing the sample locations on the map
	NSMutableArray *rows;
	RockTypeController *rockTypeController;
	MetamorphicGradeController *metamorphicGradeController;
	MineralsController *mineralsController;
	MapController *mapController;
	OwnerViewController *ownerController;
	PublicPrivateViewController *publicPrivateController;
	//CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	//NSMutableArray *currentRockTypes;
	//NSMutableArray *currentMinerals;
	//NSMutableArray *currentMetamorphicGrades;
	//NSMutableArray *currentOwners;
	//NSString *region; //region that the user initially chose, must be displayed in the search criteria
	//CLLocationCoordinate2D myCoordinate;
	//NSString *mapType; //indicates map, hybrid or satellite
	//bool visibleLocation;
	//bool buttonVisible;
	CriteriaSummary *searchCriteria;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	//NSMutableArray *originalCoordinates;
	NSString *Uname;
	//NSString *currentPublicStatus;
	CurrentSearchData *currentSearchData;
}
@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, copy) NSString *Uname;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property(nonatomic, retain) RockTypeController *rockTypeController;
@property (nonatomic, retain) MetamorphicGradeController *metamorphicGradeController;
@property (nonatomic, retain) MineralsController *mineralsController;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, retain) PublicPrivateViewController *publicPrivateController;
@property (nonatomic, retain) OwnerViewController *ownerController;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;
@end
