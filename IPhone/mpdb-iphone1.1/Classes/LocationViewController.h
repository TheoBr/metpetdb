//
//  untitled.h
//  Location
//
//  Created by Heather Buletti on 5/14/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"
#import "SampleViewController.h"
#import "MapController.h"

@class MapController;
@interface LocationViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>{
	IBOutlet UILabel *toplabel;
	IBOutlet UIPickerView *regionSelector;
	IBOutlet UIBarButtonItem *sampleButton;
	IBOutlet UIToolbar *toolbar;
	NSString *selectedRegion; //the region the user chooses from the picker view
	NSString *regionName;//the selected region without spaces
	NSString *latitude;
	NSString *longitude;
	NSMutableArray *regions; //this array will hold the names of all the regions in the database 
	CLLocation *mylocationCoordinate;
	CLLocationDegrees mylat;
	NSData *locationResponse;
	NSString *currentStringValue;
	NSData *myReturn;
	NSMutableArray *locations; //this array will hold the annotations for all the samples that are found in the region
	MapController *mapController;
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
}  

//@property(nonatomic, retain) UIPickerView *regionSelector;
@property(nonatomic, retain) UILabel *toplabel;
@property (nonatomic, retain) UIBarButtonItem *sampleButton;
@property (nonatomic, retain) UIToolbar *toolbar;
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

-(IBAction)loadMap:(id)sender;
-(void)setCoordinates:(CLLocation*)mylocation;

@end
