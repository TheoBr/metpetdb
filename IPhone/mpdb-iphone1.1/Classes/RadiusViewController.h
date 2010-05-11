//
//  RadiusViewController.h
//  Location
//
//  Created by Heather Buletti on 6/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface RadiusViewController : UIViewController {
	
	 IBOutlet UITextView *updateTextView;
	 IBOutlet UIBarButtonItem *startStopButton;
	 IBOutlet UIBarButtonItem *locationBarButton;
	 IBOutlet UIActivityIndicatorView *spinner;
	// IBOutlet SearchViewController *searchViewController;
	 BOOL isCurrentlyUpdating;
	 BOOL firstUpdate;
	 IBOutlet UILabel *output;
	 NSString *mylat;
	 NSString *mylong;
	CLLocation *coord;
}
	 
	 @property (nonatomic, retain) UITextView *updateTextView;
	 @property (nonatomic, retain) UIBarButtonItem *startStopButton;
	 @property (nonatomic,retain) UIBarButtonItem *locationBarButton;
	 @property (nonatomic, retain) UIActivityIndicatorView *spinner;
	 @property (nonatomic, retain) UIButton *searchButton;
	//@property(nonatomic, retain) IBOutlet SearchViewController *searchViewController;
	 @property (nonatomic, retain) UILabel *output;
	 @property (nonatomic, copy) NSString *LatConverted;
	 @property (nonatomic, copy) NSString *LongConverted;
	 @property (nonatomic, retain) CLLocation *coord;
	 @property (nonatomic, copy) NSString *mylat;
	 @property (nonatomic, copy) NSString *mylong;
	 
	 
	 
	 -(void)loadSearchViewController;
	 - (IBAction)startStopButtonPressed:(id)sender;
	 -(IBAction)changeLocation:(id)sender;

@end
