//
//  SearchCriteriaController.h
//  Location
//
//  Created by Heather Buletti on 6/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableController.h"
#import "MapController.h"
#import "uniqueSamples.h"
#import "CriteriaSummary.h"
#import "PostRequest.h"
#import "PublicPrivateViewController.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class TableController;
@class uniqueSamples;
@class MapController;
@class CriteriaSummary;
@class PublicPrivateViewController;
@interface SearchCriteriaController : UIViewController {
	//	IBOutlet UITableView *tableView; //this table will display the search criteria that have been chosen so far
	NSMutableArray *rowTitles;
	NSMutableArray *rowSubtitles;
	TableController *tableController;
	NSMutableArray *buttons;
	UIToolbar *toolbar;
	UIBarButtonItem *addButton;
	UIBarButtonItem *removeButton;
	UIBarButtonItem *clearButton;
	UIBarButtonItem *searchButton;
	UIBarButtonItem *editButton;
	UIBarButtonItem *doneButton;
	bool removing;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	bool rowSelected; //at least one row must be selected before the "clear" button will do anything
	NSMutableArray *locations; //this array represents the annotations that are currently left on the map after any search criteria have been entered
	//create 4 arrays that will hold the search criteria that has been specified thus far
	//if any of the arrays are empty, display none in the table
	//NSMutableArray *currentRockTypes;
	//NSMutableArray *currentMinerals;
	//NSMutableArray *currentMetamorphicGrades;
	//NSString *currentPublicStatus;
	//NSMutableArray *currentOwners;
	NSMutableArray *delete; //this array holds bools, if they are false the row should be deleted
	MapController *mapController;
	
	bool canShowMap; //this bool prevents the user from viewing the map before they have cleared any desired search criteria
	
	NSMutableArray *remainingSamples; //these are the locations that remain after one or more of the search criteria have been eliminated
	//NSString *region;
	//CLLocationCoordinate2D myCoordinate;
	int index;
	uniqueSamples *group;
	uniqueSamples *newgroup;
	int rowCount;
	NSString *mapType;
	UIButton *deleteButton;
	NSString *minimumLat;
	NSString *maximumLat;
	NSString *minimumLong;
	NSString *maximumLong;
	int num;
	NSString *newTag;
	//bool visibleLocation;
	CriteriaSummary *searchCriteria;
	NSData *sampleReturn;
	NSData *criteriaReturn;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	//NSMutableArray *originalCoordinates;
	NSData *postReturn;
	NSThread *myThread;
	NSAutoreleasePool *pool;
	bool edit;
	NSString *Uname;
	PublicPrivateViewController *publicPrivateController;
	//CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property(nonatomic, retain) PublicPrivateViewController *publicPrivateController;
@property(nonatomic, copy)NSString *Uname;
//@property(nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, retain) CriteriaSummary *searchCriteria;
//@property(nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, retain) TableController *tableController;
//@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *addButton;
@property (nonatomic, retain) UIBarButtonItem *removeButton;
@property (nonatomic, retain) UIBarButtonItem *clearButton;
@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, retain) UIBarButtonItem *editButton;
@property (nonatomic, retain) UIBarButtonItem *doneButton;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, retain) uniqueSamples *group;
@property (nonatomic, retain) uniqueSamples *newgroup;
@property (nonatomic, retain) UIButton *deleteButton;
@property (nonatomic, copy) NSString *minimumLat;
@property (nonatomic, copy) NSString *maximumLat;
@property (nonatomic, copy) NSString *minimumLong;
@property (nonatomic, copy) NSString *maximumLong;
@property (nonatomic, copy) NSString *newTag;

@end
