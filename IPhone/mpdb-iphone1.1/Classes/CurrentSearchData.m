//
// This data represents the data that is currently being used to search and an object is passed throughout the views.
// The data such as minerals, rock types, metamorphic grades, and owners represents data that the user selected while refining the search
//

#import "CurrentSearchData.h"


@implementation CurrentSearchData
@synthesize currentPublicStatus, mapType, region, locationVisible, originalCoordinates;
@synthesize rockTypes, minerals, metamorphicGrades, owners, centerCoordinate, zoomed, latitudeSpan, longitudeSpan, zoomedCenter;
//constructor function to initialize all the variables
-(CurrentSearchData*)init
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
}


@end
