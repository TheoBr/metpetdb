//
//  RegionViewController.h
//  Location
//
//  Created by Heather Buletti on 6/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapController.h"
#import "AnnotationObjects.h"
#import "uniqueSamples.h"
#import "xmlParser.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"





@class CurrentSearchData;
@class xmlParser;
@class MapController;
@class MetPetDBAppDelegate;
@interface RegionViewController : UIViewController {
	MapController *mapController;
	IBOutlet UITableView *tableView;
	NSMutableArray *letters;
	NSMutableDictionary *tableObjects;
	NSMutableArray *regions;
	NSMutableArray *sampleLocations;
	NSString *selectedRegion; //the region the user chooses from the picker view
	NSString *regionName;//the selected region without spaces
	NSString *latitude;
	NSString *longitude;
	CLLocation *mylocationCoordinate;
	CLLocationDegrees mylat;
	NSData *regionsResponse;
	NSString *currentStringValue;
	NSData *myReturn;
	NSMutableArray *locations; //this array will hold the annotations for all the samples that are found in the region
	double latdouble;
	double longdouble;
	//the following arrays hold information about the all the samples that meet the specified geographic criteria
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSThread *myThread;
	NSString *sampleCategory;
	CriteriaSummary *criteria;
	NSString *Uname;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property(nonatomic, copy)NSString *Uname;
@property(nonatomic, copy) CriteriaSummary *criteria;
@property(nonatomic, copy) NSString *sampleCategory;
@property(nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic,retain) CLLocation *mylocationCoordinate;
@property (nonatomic) CLLocationDegrees mylat;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *selectedRegion;
@property (nonatomic, retain) NSString *regionName;
@property (nonatomic, retain) IBOutlet UIImageView *imageView;

-(void)setCoordinates:(CLLocation*)mylocation;
+(NSMutableArray*)parseSamples:(NSData*)xmlData;


@end