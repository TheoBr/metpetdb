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


@class MapController;
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
	NSData *locationResponse;
	NSString *currentStringValue;
	NSData *myReturn;
	NSMutableArray *locations; //this array will hold the annotations for all the samples that are found in the region
	bool locationFlag;
	NSString *sampleName;
	NSString *sampleID;
	double latdouble;
	double longdouble;
	AnnotationObjects *newAnnotation;
	//the following arrays hold information about the all the samples that meet the specified geographic criteria
	NSMutableArray *minerals;
	NSMutableArray *metamorphicGrades;
	NSMutableArray *rockTypes;
	NSString *currentRockType;
	bool nameFlag;
	bool mineralFlag;
	bool metamorphicFlag;
	bool rockFlag;
	bool ownerFlag;
	bool uniqueFlag;
	NSString *publicStatus;
	NSMutableArray *currentMinerals; //represents the minerals contained in just the current sample and should be added to the new annotation
	NSMutableArray *currentMetamorphicGrades; //represents the metamorphic grades contained in just the current sample and should be added to the new annotation
	NSString *currentOwner; //represents the owner of each sample before this information gets stored in an annotation object
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	bool htmlFlag;
}
@property(nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic,retain) CLLocation *mylocationCoordinate;
@property (nonatomic) CLLocationDegrees mylat;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, retain) AnnotationObjects *newAnnotation;
@property (nonatomic, retain) NSString *publicStatus;
@property (nonatomic, copy) NSString *currentRockType;
@property (nonatomic, copy) NSString *selectedRegion;
@property (nonatomic, copy) NSString *regionName;
@property (nonatomic, retain) IBOutlet UIImageView *imageView;

-(void)setCoordinates:(CLLocation*)mylocation;


@end
