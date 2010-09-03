//
//  currentSearchData.h
//  MetPetDB
//
//  Created by MetPetDB on 3/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface CurrentSearchData : NSObject {
	NSMutableArray *rockTypes; //rock types that the user has selected while refining the search
	NSMutableArray *minerals; //minerals that the user has selected while refining the search
	NSMutableArray *metamorphicGrades; //metamorphic grades the user has selected while  refining the search
	NSMutableArray *owners; //owners the user has selected while refining the search
	NSString *mapType; //the type of the map: street view, hybrid, or satellite
	NSString *currentPublicStatus; //this value represents whether public, private, or both types of samples are being displayed
	NSString *region; //the name of the region the user is searching in if they have selected a region, otherwise it is nil
	CLLocationCoordinate2D centerCoordinate; //the center coordinate the user input to search around, if a region was used this does not have a value
	BOOL locationVisible; //this is only true if the user is searching using their current location, otherwise it is false
	NSMutableArray *originalCoordinates; //the north, south, east and west coordinates of the original search
	BOOL zoomed; //if the user has zoomed in on a sample, maintain that zoom by setting the lat and long span to the 
	//values of the zoom after the user returns to the map
	double latitudeSpan;
	double longitudeSpan;
	

	
	CLLocationCoordinate2D zoomedCenter; //after the user has zoomed, the new center point is stored as the center of the map
}
CLLocationCoordinate2D centerCoordinate;
NSString *mapType;

@property(nonatomic,retain) NSMutableArray *rockTypes;
@property(nonatomic, retain) NSMutableArray *minerals;
@property(nonatomic, retain) NSMutableArray *metamorphicGrades;
@property(nonatomic, retain)  NSMutableArray *owners;
//@property(nonatomic, retain) NSMutableArray *originalCoordinates;
@property BOOL locationVisible;
@property BOOL zoomed;
@property double latitudeSpan;
@property double longitudeSpan;
@property CLLocationCoordinate2D zoomedCenter;
//@property(nonatomic, copy) NSString *mapType;
@property(nonatomic,copy) NSString *currentPublicStatus;
@property(nonatomic, copy) NSString *region;

+(void)setOriginalCoordinates:(NSMutableArray *)val;
+(NSMutableArray*)getOriginalCoordinates;

+(void)setCenterCoordinateLatitude:(double)val;
+(void)setCenterCoordinateLongitude:(double)val;

+(CLLocationCoordinate2D)getCenterCoordinate;

+(void)setMapType:(NSString*)val;
+(NSString*)getMapType;


@end
