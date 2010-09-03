//
//  OwnerViewController.h
//  MetPetDB
//
//  Created by Heather Buletti on 7/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableController.h"
#import "SearchCriteriaController.h"
#import "uniqueSamples.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class uniqueSamples;
@class SearchCriteriaController;
@class AnnotationObjects;
@class CriteriaSummary;

@interface OwnerViewController : UIViewController {
	IBOutlet UIPickerView *sampleSelector;
	IBOutlet UILabel *output;
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myLocations;
	NSString *currentStringValue; 
	NSString *ownerName;
	AnnotationObjects *newAnnotation;
	NSMutableArray *owners; //this is the list of owners that will be displayed in the picker view
	SearchCriteriaController *criteriaController;
	UIToolbar *toolbar;
	UIBarButtonItem *refineButton;
	NSMutableArray *modifiedLocations; //this array holds the rocks that are of the rock type the user specifies
	double latSpan;
	double longSpan;
	NSString *tempOwner;
	uniqueSamples *group;
	uniqueSamples *newgroup;
	NSString* mapType; //indicates map, hybrid or satellite
	NSMutableArray *points;
	//bool buttonVisible;
	CriteriaSummary *searchCriteria;
	CurrentSearchData *currentSearchData;
}
@property(nonatomic, retain) CurrentSearchData *currentSearchData;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) uniqueSamples *group;
@property (nonatomic, retain) uniqueSamples *newgroup;
@property (nonatomic, copy)NSString *tempOwner;
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, retain) AnnotationObjects *newAnnotation;
@property (nonatomic, copy) NSString *ownerName;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property(nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;

-(IBAction)refineSearch:(id)sender;


@end
