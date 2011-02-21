//
//  ImageViewController.h
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SingleImageView.h"
#import "SampleInfoController.h"
#import "AnnotationObjects.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class CriteriaSummary;
@class SingleImageView;
@class SampleInfoController;
@class AnnotationObjects;
@interface ImageViewController : UIViewController {
	SingleImageView *singleImageView;
	SampleInfoController *sampleController;
	NSMutableArray *buttonArray;
	NSString *currentStringValue;
	NSMutableArray *imageIDs; //stores all the image ID's for the images for this sample
	NSMutableArray *imagePaths; //stores all the image paths for all the images associated with this sample
	NSMutableArray *navBarItems;
	NSData *imageResponse;
	int imageCount;
	int remainingCount; //after each new page is displayed, subtract the number on the page to get the number remaining
	int width;
	int height;
	int tempCount;
	NSString *sampleID;
	NSString *selectedImage;
	ImageViewController *imageViewController;
	NSString *fullPath;
	bool firstPage;
	int pagesDisplayed;
	int index;
	UIBarButtonItem *nextButton;
//	UIToolbar *toolbar;
	AnnotationObjects *sampleAnnotation;
	NSMutableArray *locations;
	CGPoint startTouchPosition;
	IBOutlet UIPageControl *pageControl;
	UIActivityIndicatorView *progress;
	UIBarButtonItem *backButton;
	//the following variables store information about the range of the map view so it can be reset to exactly how it was when the user presses the back button
	
	double latitudeSpan; //the span is a value in degrees that is used to set the zoom on the map view when it first appears
	double longitudeSpan;    
	double maxLat;
	double minLat;
	double maxLong;
	double minLong;
	
	//the following are more map variables
	NSMutableArray *samples;
	CriteriaSummary *searchCriteria;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property(nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) NSMutableArray *samples;
@property(nonatomic, getter=isNetworkActivityIndicatorVisible) 
BOOL networkActivityIndicatorVisible;
@property(nonatomic, retain) UIBarButtonItem *backButton;
@property (nonatomic, retain) UIActivityIndicatorView *progress;
@property (nonatomic, retain)IBOutlet UIPageControl *pageControl;
@property (nonatomic, retain) AnnotationObjects *sampleAnnotation;
@property (nonatomic, retain) SampleInfoController *sampleController;
//@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *nextButton;
@property (nonatomic, retain) UIBarButtonItem *prevButton;
@property(nonatomic, copy) NSString *fullPath;
@property(nonatomic, retain) SingleImageView *singleImageView;
@property(nonatomic, copy) NSString *currentStringValue;
@property(nonatomic, copy) NSString *sampleID;
@property(nonatomic, copy) NSString *selectedImage;
@property (nonatomic, retain) ImageViewController *imageViewController;
@end
