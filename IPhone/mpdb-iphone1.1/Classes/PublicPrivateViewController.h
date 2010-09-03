

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
@interface PublicPrivateViewController : UIViewController <MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation> {
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myLocations;
	AnnotationObjects *newAnnotation;
	SearchCriteriaController *criteriaController;
	UIToolbar *toolbar;
	UIBarButtonItem *refineButton;
	NSMutableArray *modifiedLocations; //this array holds the rocks that are of the rock type the user specifies
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	CLLocationCoordinate2D myCoordinate;
	uniqueSamples *group;
	uniqueSamples *newgroup;
	NSString* mapType; //indicates map, hybrid or satellite
	NSMutableArray *points;
	CriteriaSummary *searchCriteria;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	NSMutableArray *originalCoordinates;
	UISegmentedControl *segControl;
	NSString *Uname;
	IBOutlet UIButton *okButton;
	CurrentSearchData *currentSearchData;
}
@property (nonatomic, retain) IBOutlet UIButton *okButton;
@property(nonatomic,copy) NSString *Uname;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property (nonatomic, retain) UISegmentedControl *segControl;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) uniqueSamples *group;
@property (nonatomic, retain) uniqueSamples *newgroup;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, retain) AnnotationObjects *newAnnotation;
@property (nonatomic, copy) NSString *rockName;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property(nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;

-(IBAction)backToCriteria;



@end
