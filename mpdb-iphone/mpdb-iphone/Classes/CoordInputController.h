//
//  CoordInputView.h
//  Location
//
//  Created by Heather Buletti on 6/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RadiusController.h"

@class RadiusController;
@interface CoordInputController: UIViewController <UITextFieldDelegate, UIAlertViewDelegate> {
	RadiusController *radiusController;
	IBOutlet UITextField *latitudeTextField;
	IBOutlet UITextField *longitudeTextField;
	UIToolbar *toolbar;
	NSString *latitude;
	NSString *longitude;
}
@property(nonatomic, retain)RadiusController *radiusController;
@property(nonatomic, retain)IBOutlet UITextField *latitudeTextField;
@property(nonatomic, retain) IBOutlet UITextField *longitudeTextField;
@property(nonatomic, retain) UIToolbar *toolbar;
@property(nonatomic, copy) NSString *latitude;
@property(nonatomic, copy) NSString *longitude;
@end
