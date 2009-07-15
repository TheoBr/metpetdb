//
//  SampleTableController.h
//  Location
//
//  Created by Heather Buletti on 6/5/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SampleInfoController.h"

@class SampleInfoController;
@interface SampleTableController : UIViewController<UITableViewDelegate, UITableViewDataSource> {
	IBOutlet UITableView *tableView;
	NSMutableArray *rows;
	NSMutableArray *names;
	NSMutableArray *owners;
	NSMutableArray *mySamples;
	NSString *sampleID;
	SampleInfoController *sampleInfo;
	AnnotationObjects *selectedSample;
	AnnotationObjects *tempSample;
	bool allSamples; //if this bool is true, then all the samples on the map are being displayed and the mySamples array is an 
					//array of uniqueSample objects rather than annotation objects
	NSString *tempName;
	NSString *tempOwner;
}
@property (nonatomic, retain) IBOutlet UITableView *tableView;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, retain) SampleInfoController *sampleInfo;
@property (nonatomic, retain) AnnotationObjects *tempSample;
@property (nonatomic, copy) NSString *tempName;
@property (nonatomic, copy) NSString *tempOwner;
@end
