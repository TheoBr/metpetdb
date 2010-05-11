//This class represents an object containing a summary of all the criteria
//contained in the samples returned from a given search
#import <Foundation/Foundation.h>


@interface CriteriaSummary : NSObject {
	NSMutableArray *rockTypes;
	NSMutableArray *minerals;
	NSMutableArray *metamorphicGrades;
	NSMutableArray *owners;
	int totalCount;
	double maxLat;
	double minLat;
	double maxLong;
	double minLong;
}
@property double minLat;
@property double maxLat;
@property double minLong;
@property double maxLong;
@property int totalCount;
@property(nonatomic, retain) NSMutableArray *rockTypes;
@property(nonatomic, retain) NSMutableArray *minerals;
@property(nonatomic, retain) NSMutableArray *metamorphicGrades;
@property(nonatomic, retain) NSMutableArray *owners;

@end
