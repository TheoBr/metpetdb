//
//  untitled.h
//  Location
//
//  Created by Heather Buletti on 5/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Sample : NSObject {
	NSString *owner;
	NSInteger *IGSN;
	NSString *number;
	NSString *dateCollected;
	NSString *rockType;
	NSString *publicStatus;
	NSString *latitude;
	NSString *longitude;
	NSString *locationError;
	NSString *country;
	NSString *description;
	NSString *collector;
	NSString *presentLocation;
	NSString *minerals;
	NSString *region;
	NSString *metamorphicGrade;
	NSString *publicationReferences;
	NSString *subsamples;
}
@property(nonatomic, copy) NSString *owner;
//@property(nonatomic, copy) NSInteger *IGSN;
@property(nonatomic, copy) NSString *number;
@property(nonatomic, copy) NSString *dateCollected;
@property(nonatomic, copy) NSString *rockType;
@property(nonatomic, copy) NSString *publicStatus;
@property(nonatomic, copy) NSString *latitude;
@property(nonatomic, copy) NSString *longitude;
@property(nonatomic, copy) NSString *locationError;
@property(nonatomic, copy) NSString *country;
@property(nonatomic, copy) NSString *description;
@property(nonatomic, copy) NSString *collector;
@property(nonatomic, copy) NSString *presentLocation;
@property(nonatomic, copy) NSString *minerals;
@property(nonatomic, copy) NSString *region;
@property(nonatomic, copy) NSString *metamorphicGrade;
@property(nonatomic, copy) NSString *publicationReferences;
@property(nonatomic, copy) NSString *subsamples;

@end
