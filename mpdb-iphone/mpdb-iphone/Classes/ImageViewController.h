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
	UIToolbar *toolbar;
	AnnotationObjects *sampleAnnotation;
	NSMutableArray *locations;
}
@property (nonatomic, retain) AnnotationObjects *sampleAnnotation;
@property (nonatomic, retain) SampleInfoController *sampleController;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *nextButton;
@property (nonatomic, retain) UIBarButtonItem *prevButton;
@property(nonatomic, copy) NSString *fullPath;
@property(nonatomic, retain) SingleImageView *singleImageView;
@property(nonatomic, copy) NSString *currentStringValue;
@property(nonatomic, copy) NSString *sampleID;
@property(nonatomic, copy) NSString *selectedImage;
@property (nonatomic, retain) ImageViewController *imageViewController;
@end
     