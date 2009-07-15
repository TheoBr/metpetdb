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
#import "PublicStatusController.h"
#import "MineralsController.h"

@class RockTypeController;
@class MetamorphicGradeController;
@class PublicStatusController;
@class MineralsController;
@class MapController;
@interface TableController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView *tableView;
	NSMutableArray *locations; //array of annotations representing the sample locations on the map
	NSMutableArray *rows;
	RockTypeController *rockTypeController;
	MetamorphicGradeController *metamorphicGradeController;
	MineralsController *mineralsController;
	PublicStatusController *publicStatusController;
	MapController *mapController;
	NSMutableArray *original; //this array will hold the original search data so that the search criteria can be cleared
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSString *region; //region that the user initially chose, must be displayed in the search criteria
	CLLocationCoordinate2D myCoordinate;
	NSString *mapType; //indicates map, hybrid or satellite
}
@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property(nonatomic, retain) RockTypeController *rockTypeController;
@property (nonatomic, retain) MetamorphicGradeController *metamorphicGradeController;
@property (nonatomic, retain) MineralsController *mineralsController;
@property (nonatomic, retain) PublicStatusController *publicStatusController;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;
@end
