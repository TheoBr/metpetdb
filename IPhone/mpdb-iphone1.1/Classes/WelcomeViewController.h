//
//  WelcomeViewController.h
//  MetPetDB
//
//  Created by Heather Buletti on 7/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainViewController.h"

@class MainViewController;
@interface WelcomeViewController : UIViewController {
	//IBOutlet UIButton *beginButton;
	MainViewController *mainViewController;
	UIButton *button;
	NSString *resultString;

}
@property (nonatomic, retain) MainViewController *mainViewController;
//@property (nonatomic, retain) IBOutlet UIButton *beginButton;
@property (nonatomic, retain) UIButton *button;
@property (nonatomic, copy) NSString *resultString;

//  -(IBAction) begin:(id)sender;
@end
