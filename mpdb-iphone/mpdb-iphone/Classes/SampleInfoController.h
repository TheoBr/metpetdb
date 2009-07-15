//
//  SampleInfoController.h
//  Location
//
//  Created by Heather Buletti on 5/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapController.h"
#import "AnalysisSummary.h"
#import "CommentDisplayController.h"


@class MapController;
@class AnalysisSummary;
@class ImageViewController;
@class CommentDisplayController;
@interface SampleInfoController : UIViewController <UITextViewDelegate>{
	IBOutlet UILabel *titleLabel;
	NSData *sampleResponse;
	NSString *currentStringValue;
	NSString *sampleID;
	NSString *rocktype;
	bool rockFlag;
	bool idFlag;
	bool nameFlag;      
	bool mineralFlag;
	NSMutableArray *locations;
	MapController *mapController;
	NSMutableArray *minerals;
	NSString *outputText;
	NSString *titleText;
	UIBarButtonItem *backButton;
	IBOutlet UITextView *textView; //displays information about the sample below the title label
	AnnotationObjects *sampleAnnotation;
	UIToolbar *toolbar;
	NSMutableArray *buttons;
	UIBarButtonItem *subsampleButton;
	UIBarButtonItem *commentButton;
	AnalysisSummary *analysisSummary;
	ImageViewController *imageViewController;
	NSString *id; //this is the id number of the sample, which needs to be passed on to the subsample controller
	CommentDisplayController *commentDisplay;
	NSString *imageCount;
	UIButton *imageButton;
	NSData *imageResponse;
	NSMutableArray *imagePaths;
	NSMutableArray *imageIDs;
	int remainingCount;
}
@property(nonatomic, retain) UIButton *imageButton;
@property (nonatomic, retain) IBOutlet UILabel *titleLabel;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, copy) NSString *rocktype;
@property (nonatomic, retain) NSMutableArray *locations;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *outputText;
 @property (nonatomic, retain) UIBarButtonItem *backButton; 
@property (nonatomic, retain) UIBarButtonItem *commentButton;
@property (nonatomic, retain) IBOutlet UITextView *textView;
@property (nonatomic, retain) AnnotationObjects *sampleAnnotation;
@property (nonatomic, copy) NSString *titleText;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *subsampleButton;
@property (nonatomic retain) AnalysisSummary *analysisSummary;
@property (nonatomic, retain) ImageViewController *imageViewController;
@property (nonatomic, copy) NSString *id;
@property (nonatomic, retain) CommentDisplayController *commentDisplay;
@property(nonatomic, copy) NSString *imageCount;

-(IBAction)imagesDisplay:(id)sender;
-(void)backToMap;

@end
