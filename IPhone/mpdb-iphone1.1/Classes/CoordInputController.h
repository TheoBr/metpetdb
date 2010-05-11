//
//  CoordInputView.h
//  Location
//
//  Created by Heather Buletti on 6/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RadiusController.h"
#import "DropMapViewController.h" 
#import "CurrentSearchData.h"

@class RadiusController;
@class DropMapViewController;
@interface CoordInputController: UIViewController <UITextFieldDelegate, UIAlertViewDelegate, MKMapViewDelegate> {
	RadiusController *radiusController;
	DropMapViewController *dropMap;
	IBOutlet UITextField *latitudeTextField;
	IBOutlet UITextField *longitudeTextField;
	IBOutlet UIButton *mapButton;
	UIToolbar *toolbar;
	NSString *latitude;
	NSString *longitude;
	NSString *currentPublicStatus;
	NSString *username;
	bool locationVisible;
	CurrentSearchData *currentSearchData;
}
@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, retain) DropMapViewController *dropMap;
@property (nonatomic, retain) IBOutlet UIButton *mapButton;
@property(nonatomic, copy) NSString *username;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, retain)RadiusController *radiusController;
@property(nonatomic, retain)IBOutlet UITextField *latitudeTextField;
@property(nonatomic, retain) IBOutlet UITextField *longitudeTextField;
@property(nonatomic, retain) UIToolbar *toolbar;
@property(nonatomic, copy) NSString *latitude;
@property(nonatomic, copy) NSString *longitude;

-(IBAction)loadMap:(id)sender;

@end
