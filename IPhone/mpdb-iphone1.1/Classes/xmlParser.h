//
//  parseXML.h
//  MetPetDB
//
//  Created by Heather Buletti on 10/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AnnotationObjects.h"
#import "uniqueSamples.h"
#import "CriteriaSummary.h"

@class AnnotationObjects;
@class uniqueSamples;
@class CriteriaSummary;


@interface xmlParser : UIViewController {
	//mySamples is the array of all samples that will be returned from the parseXML function
	NSMutableArray *mySamples;
	
	int totalCount;
	bool rockFlag;
	bool ownerFlag;
	bool nameFlag;
	bool mineralFlag;
	bool descriptionFlag;
	bool metamorphicFlag;
	bool uniqueFlag;
	bool locationFlag;
	bool criteriaRockFlag;
	bool criteriaMinFlag;
	bool criteriaMetFlag;
	bool criteriaOwnerFlag;
	NSString *description;
	NSString *sampleName;
	double longdouble;
	double latdouble;
	AnnotationObjects *newAnnotation;
	uniqueSamples *newSet;
	NSString *publicStatus;
	NSString *sampleID;
	NSMutableArray *addedLocations;
	NSMutableArray *currMetGrades;
	NSMutableArray *currMinerals;
	NSMutableArray *mins;
	NSMutableArray *metGrades;
	NSMutableArray *currPublicStatus;
	NSMutableArray *currOwner;
	NSMutableArray *currRockTypes;
	NSMutableArray *regions;
	NSString *currentOwner;
	NSString *rock;
	CriteriaSummary *criteria;
	NSString *currentStringValue;
}
@property (nonatomic, retain) NSString *description;
@property (nonatomic, retain) NSString *publicStatus;
@property(nonatomic, retain) NSString *sampleName;
@property(nonatomic, retain) NSString *rock;
@property (nonatomic, retain) NSString *currentOwner;
@property(nonatomic, retain) NSString *sampleID;
@property(nonatomic, copy)NSString *currentStringValue;

//create some static  methods that will parse xml data
//the first function returns an array of unique sample objects that represent sample annotations
-(NSMutableArray*)parseSamples:(NSData*)xmlData;
-(NSString*)blah;


@end
