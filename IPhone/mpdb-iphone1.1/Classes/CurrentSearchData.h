//
//  currentSearchData.h
//  MetPetDB
//
//  Created by MetPetDB on 3/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface CurrentSearchData : NSObject {
}


CLLocationCoordinate2D zoomedCenter; //after the user has zoomed, the new center point is stored as the center of the map
NSMutableArray *minerals; //minerals that the user has selected while refining the search
NSMutableArray *metamorphicGrades; //metamorphic grades the user has selected while  refining the search
NSMutableArray *owners; //owners the user has selected while refining the search
NSString *mapType; //the type of the map: street view, hybrid, or satellite
NSString *currentPublicStatus; //this value represents whether public, private, or both types of samples are being displayed
NSString *region; //the name of the region the user is searching in if they have selected a region, otherwise it is nil
CLLocationCoordinate2D centerCoordinate; //the center coordinate the user input to search around, if a region was used this does not have a value
BOOL locationVisible; //this is only true if the user is searching using their current location, otherwise it is false
BOOL zoomed; //if the user has zoomed in on a sample, maintain that zoom by setting the lat and long span to the 
//values of the zoom after the user returns to the map
double latitudeSpan;
double longitudeSpan;
NSMutableArray *originalCoordinates; //the north, south, east and west coordinates of the original search
NSMutableArray *rockTypes; //rock types that the user has selected while refining the search
NSString *mapType;

//+(void)initSearchData;
//@property(nonatomic,retain) NSMutableArray *rockTypes;
//@property(nonatomic, copy) NSMutableArray *rockTypes;

//@property(nonatomic, retain) NSMutableArray *minerals;
+(void)setMinerals:(NSMutableArray *)val;
+(NSMutableArray*)getMinerals;

//@property(nonatomic, retain) NSMutableArray *metamorphicGrades;
+(void)setMetamorphicGrades:(NSMutableArray *)val;
+(NSMutableArray*)getMetamorphicGrades;

//@property(nonatomic, retain)  NSMutableArray *owners;
+(void)setOwners:(NSMutableArray *)val;
+(NSMutableArray*)getOwners;


//@property(nonatomic, retain) NSMutableArray *originalCoordinates;

//@property BOOL locationVisible;
+(void)setLocationVisible:(BOOL)val;
+(BOOL)getLocationVisible;

//@property BOOL zoomed;
+(void)setZoomed:(BOOL)val;
+(BOOL)getZoomed;


//@property double latitudeSpan;
+(void)setLatitudeSpan:(double)val;
+(double)getLatitudeSpan;

+(void)setOriginalCoordinates:(NSMutableArray *)val;
+(NSMutableArray*)getOriginalCoordinates;

//@property double longitudeSpan;
+(void)setLongitudeSpan:(double)val;
+(double)getLongitudeSpan;

//@property CLLocationCoordinate2D zoomedCenter;
+(void)setZoomedCenter:(CLLocationCoordinate2D*)val;
+(CLLocationCoordinate2D)getZoomedCenter;

//@property(nonatomic, copy) NSString *mapType;

//(nonatomic,copy) NSString *currentPublicStatus;
+(void)setCurrentPublicStatus:(NSString *)val;
+(NSString *)getCurrentPublicStatus;

//@property(nonatomic, copy) NSString *region;
+(void)setRegion:(NSString *)val;
+(NSString *)getRegion;


+(void)setOriginalCoordinates:(NSMutableArray *)val;
+(NSMutableArray*)getOriginalCoordinates;

+(void)setCenterCoordinateLatitude:(double)val;
+(void)setCenterCoordinateLongitude:(double)val;

+(void)setCenterCoordinate:(CLLocationCoordinate2D*)val;
+(CLLocationCoordinate2D)getCenterCoordinate;

+(void)setMapType:(NSString*)val;
+(NSString*)getMapType;

+(void)setAddRock:(NSString *)val;
+(void)setRockTypes:(NSMutableArray *)val;
+(NSMutableArray*)getRockTypes;


@end
