
#import <UIKit/UIKit.h>
#import "SearchCriteriaController.h"
#import "AnnotationObjects.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class SearchCriteriaController;
@class CriteriaSummary;
@interface MetamorphicGradeController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource, MKReverseGeocoderDelegate, MKMapViewDelegate, MKAnnotation> {
	IBOutlet UIPickerView *sampleSelector;
	IBOutlet UILabel *output;
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myMetamorphicGrades;
	NSString *currentStringValue;
	AnnotationObjects *newAnnotation;
	UIToolbar *toolbar;
	NSMutableArray *mylocations;
	NSMutableArray *modifiedLocations; //this array will hold the samples with the user-specified metamorphic grade and they will be displayed on the map
	UIBarButtonItem *refineButton;
	SearchCriteriaController *criteriaController;
	NSString *gradeName;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSMutableArray *points;
	NSMutableArray *modifiedLocations2;
	int index;
	NSString *grade;
	uniqueSamples *newgroup;
	uniqueSamples *group;
	int totalSamples;
	CriteriaSummary *searchCriteria;
	//if coordinates are used as a search criteria, an array must be kept with the original north, south, east, west
	NSMutableArray *originalCoordinates;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, copy)CurrentSearchData *currentSearchData;
@property (nonatomic, copy) NSString *currentPublicStatus;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) uniqueSamples *group;
@property(nonatomic, retain) uniqueSamples *newgroup;
@property(nonatomic, copy) NSString *grade;
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *gradeName;
@property(nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property(nonatomic, retain) SearchCriteriaController *criteriaController;

-(IBAction)backToCriteria:(id)sender;

-(void)getSamples;
-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx;

@end
