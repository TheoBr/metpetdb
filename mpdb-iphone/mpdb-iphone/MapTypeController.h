//
//  untitled.h
//  MetPetDB
//
//  Created by Heather Buletti on 7/9/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapController.h"

@class MapController;
@interface MapTypeController : UIViewController {
	UISegmentedControl *segControl;
	MapController *mapController;
	NSString *mapType;
	//all the following variables exist to maintain information from the map
	NSMutableArray *samples;
	NSMutableArray *originalData;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSMutableArray *currentOwners;
	NSString *region;
	CLLocationCoordinate2D myLocation;
	double latitudeSpan;
	double longitudeSpan;
	double maxLat, minLat, maxLong, minLong;
}
@property(nonatomic, retain) UISegmentedControl *segControl;
@property(nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *mapType;

@end
