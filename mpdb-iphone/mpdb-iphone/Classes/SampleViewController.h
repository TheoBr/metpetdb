//
//  SampleViewController.h
//  Location
//
//  Created by Heather Buletti on 5/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapController.h"

@class MapController;
@interface SampleViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource, MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation> {
	IBOutlet UILabel *headerlabel;
	IBOutlet UIPickerView *sampleSelector;
	IBOutlet UILabel *output;
	IBOutlet UIToolbar *toolbar;
	IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *samples;
	NSMutableArray *locations;
	NSString *radius;
	NSString *sample;
	NSString *googlelink;
	NSString *latitude;
	NSString *longitude;
	IBOutlet MapController *mapController;
	NSString *temp;
	NSString *currentStringValue;
	NSData *myReturn;
	double latdouble;
	double longdouble;
	CLLocationCoordinate2D coord;
	NSString *sampleID;
	NSString *sampleName;
	NSString *rocktype;
	bool rockFlag;
	bool stringFlag;
	NSData *rockReturn;
	AnnotationObjects *newAnnotation;

	
}
@property (nonatomic, retain) UILabel *headerlabel;
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *sample;
@property (nonatomic, copy) NSString *googlelink;
@property (nonatomic, copy) NSString *latitude;
@property (nonatomic, copy) NSString *longitude;
@property (nonatomic, copy) NSString *radius;
@property (nonatomic, copy) NSString *temp;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, retain) IBOutlet MapController *mapController;
@property (nonatomic) CLLocationCoordinate2D coord;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *rocktype;





-(IBAction)loadMap:(id)sender;
-(void)openGoogleMapsApp;
-(void)getSamples;
-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx;
//-(CFHTTPMessageRef)buildMessage;
//-(CFHTTPMessageRef)performHTTPRequest:(CFHTTPMessageRef)request;


@end
