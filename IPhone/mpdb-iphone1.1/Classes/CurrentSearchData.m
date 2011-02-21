//
// This data represents the data that is currently being used to search and an object is passed throughout the views.
// The data such as minerals, rock types, metamorphic grades, and owners represents data that the user selected while refining the search
//

#import "CurrentSearchData.h"


@implementation CurrentSearchData
//@synthesize currentPublicStatus, region, locationVisible;//, originalCoordinates;
//@synthesize minerals, metamorphicGrades, owners, zoomed, latitudeSpan, longitudeSpan, zoomedCenter;
//constructor function to initialize all the variables

+(void)initialize
{

	rockTypes=[[NSMutableArray alloc] init];
	minerals=[[NSMutableArray alloc] init];
	metamorphicGrades=[[NSMutableArray alloc] init];
	owners=[[NSMutableArray alloc] init];
	originalCoordinates= [[NSMutableArray alloc] init];
	region=nil;
	currentPublicStatus=nil;
	mapType=nil;
	locationVisible=FALSE;
	zoomed=FALSE;
	//CLLocationCoordinate2D location = nil;
	//centerCoordinate;
	centerCoordinate.latitude = 0;
	centerCoordinate.longitude = 0;
	//centerCoordinate = location.coordinate;

}

+(void)setMinerals:(NSMutableArray *)val
{
	minerals = val;
}
+(NSMutableArray*)getMinerals
{
	return minerals;
}

//@property(nonatomic, retain) NSMutableArray *metamorphicGrades;
+(void)setMetamorphicGrades:(NSMutableArray *)val
{
	metamorphicGrades = val;
}
+(NSMutableArray*)getMetamorphicGrades
{
	return metamorphicGrades;
}

//@property(nonatomic, retain)  NSMutableArray *owners;
+(void)setOwners:(NSMutableArray *)val
{
	owners = val;
}

+(NSMutableArray*)getOwners
{
	return owners;
}


//@property(nonatomic, retain) NSMutableArray *originalCoordinates;

//@property BOOL locationVisible;
+(void)setLocationVisible:(BOOL)val
{
	locationVisible = val;
}
+(BOOL)getLocationVisible
{
	return locationVisible;
}
//@property BOOL zoomed;
+(void)setZoomed:(BOOL)val
{
	zoomed = val;
} 
+(BOOL)getZoomed
 {
	 return zoomed;
 }


//@property double latitudeSpan;
+(void)setLatitudeSpan:(double)val
{
	latitudeSpan = val;
}

+(double)getLatitudeSpan
{
	return latitudeSpan;
}

//@property double longitudeSpan;
+(void)setLongitudeSpan:(double)val
{
	longitudeSpan = val;
}

+(double)getLongitudeSpan
{
	return longitudeSpan;
}

//@property CLLocationCoordinate2D zoomedCenter;
+(void)setZoomedCenter:(CLLocationCoordinate2D)val
{
	zoomedCenter = val;
}

+(CLLocationCoordinate2D)getZoomedCenter
{
	return zoomedCenter;
}

//@property(nonatomic, copy) NSString *mapType;

//(nonatomic,copy) NSString *currentPublicStatus;
+(void)setCurrentPublicStatus:(NSString *)val
{
	currentPublicStatus = val;
}
+(NSString *)getCurrentPublicStatus
{
	return currentPublicStatus;
}

+(void)setRegion:(NSString *)val
{
	region = val;
}
+(NSString *)getRegion
{
	return region;
}

+(void) setOriginalCoordinates:(NSMutableArray *) val
{
	originalCoordinates = val;
}

+(NSMutableArray*) getOriginalCoordinates
{
	return originalCoordinates;
}


+(void) setCenterCoordinateLatitude:(double) val
{
	centerCoordinate.latitude = val;
}


+(void) setCenterCoordinateLongitude:(double) val
{
	centerCoordinate.longitude = val;
}

+(void) setCenterCoordinate:(CLLocationCoordinate2D)val
{
	centerCoordinate = val;
}

+(CLLocationCoordinate2D) getCenterCoordinate
{

	return centerCoordinate;
}

+(void) setMapType:(NSString *)val
{
	mapType = val;
}

+(NSString*) getMapType
{
	return mapType;
}

+(void) addRock:(NSString *) val
{
	[rockTypes addObject:val];
}

+(void) setRockTypes:(NSMutableArray *) val
{
	rockTypes = val;
}

+(NSMutableArray*) getRockTypes
{
	return rockTypes;
}
@end
