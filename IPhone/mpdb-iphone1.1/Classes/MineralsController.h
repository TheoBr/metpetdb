
#import <UIKit/UIKit.h>
#import "MapController.h"
#import "SearchCriteriaController.h"
#import "uniqueSamples.h"
#import "CriteriaSummary.h"
#import "CurrentSearchData.h"


@class CurrentSearchData;
@class SearchCriteriaController;
@class CriteriaSummary;
@interface MineralsController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>{
	SearchCriteriaController *criteriaController;
	IBOutlet UIPickerView *sampleSelector;
	IBOutlet UILabel *output;
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myMinerals;
	NSString *currentStringValue;
	AnnotationObjects *newAnnotation;
	NSMutableArray *myLocations;
	NSMutableArray *modifiedLocations; //stores the samples with any other search criteria that have been specified, not including minerals
	NSMutableArray *modifiedLocations2; //stores the samples with the minerals search criteria
	NSString *mineralName;
	UIToolbar *toolbar;
	UIBarButtonItem *refineButton;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;	NSMutableArray *points;
	NSString *min;
	int index; //represents the current position in the modified locations array where the object should be added, for multiple samples it replaces the last object added
	uniqueSamples *newgroup;
	bool modified;
	CriteriaSummary *searchCriteria;
//	CurrentSearchData *currentSearchData;
}
//@property(nonatomic, retain)CurrentSearchData *currentSearchData;
@property (nonatomic, retain) CriteriaSummary *searchCriteria;
@property (nonatomic, retain) uniqueSamples *newgroup;
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) UIToolbar *toolbar;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *mineralName;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property (nonatomic, copy) NSString *min;


-(IBAction)loadMap:(id)sender;

-(void)getSamples;
-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx;

@end

