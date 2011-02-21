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
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class CriteriaSummary;
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
//	UIToolbar *toolbar;
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
	NSData *myReturn;
	NSString *sampleName;
	
	//the following variables store information about the range of the map view so it can be reset to exactly how it was when the user presses the back button
	
	double latitudeSpan; //the span is a value in degrees that is used to set the zoom on the map view when it first appears
	double longitudeSpan;    
	double maxLat;
	double minLat;
	double maxLong;
	double minLong;
	
	//the following are more map variables
	bool visibleLocation;
	NSString *sampleCategory;
	NSMutableArray *samples;
	//NSString *mapType;
	
	CriteriaSummary *searchCriteria;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	NSString *Uname;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property(nonatomic, copy)NSString *Uname;
@property(nonatomic, retain)CriteriaSummary *searchCriteria;
@property (nonatomic, copy) NSString *sampleName;
@property(nonatomic, copy) NSString *region;
@property(nonatomic, copy) NSString *sampleCategory;
@property (nonatomic, retain) NSMutableArray *samples;
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
//@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *subsampleButton;
@property (nonatomic, retain) AnalysisSummary *analysisSummary;
@property (nonatomic, retain) ImageViewController *imageViewController;
@property (nonatomic, copy) NSString *id;
@property (nonatomic, retain) CommentDisplayController *commentDisplay;
@property(nonatomic, copy) NSString *imageCount;

-(IBAction)imagesDisplay:(id)sender;
-(void)backToMap;

@end
