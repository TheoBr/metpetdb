//
//  postRequest.h
//  MetPetDB
//
//  Created by MetPetDB on 3/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//



#import <Foundation/Foundation.h>


@interface PostRequest : NSObject {
	NSString *currentPublicStatus;
	NSString *region;
	NSString *postString;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentOwners;
	NSString *username;
	NSString *postReturn;
	int pagination;
	NSString* criteriaSummary;
	NSMutableArray *coordinates;
	double currentLatitude;
	double currentLongitude;

	
}
@property(nonatomic, copy)NSString *currentPublicStatus;
@property(nonatomic, copy)NSString *region;
@property(nonatomic, copy)NSString *username;
@property(nonatomic, copy)NSString *postString;
@property(nonatomic, copy)NSString *criteriaSummary;
@property(nonatomic)double currentLatitude;
@property(nonatomic)double currentLongitude;
@end


