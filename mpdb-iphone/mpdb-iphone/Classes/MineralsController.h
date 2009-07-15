
#import <UIKit/UIKit.h>
#import "MapController.h"
#import "SearchCriteriaController.h"

@class SearchCriteriaController;
@interface MineralsController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource>{
	SearchCriteriaController *criteriaController;
	IBOutlet UIPickerView *sampleSelector;
	IBOutlet UILabel *output;
	//IBOutlet UIBarButtonItem *searchButton;
	NSMutableArray *myMinerals;
	NSString *currentStringValue;
	AnnotationObjects *newAnnotation;
	NSMutableArray *myLocations;
	NSMutableArray *modifiedLocations; //stores the locations of the samples that contain the specified mineral
	NSString *mineralName;
	UIToolbar *toolbar;
	UIBarButtonItem *refineButton;
	NSMutableArray *original;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals; //this array will be modified by this controller to contain the minerals the user specifies 
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSString *region;
	CLLocationCoordinate2D myCoordinate;
	NSString *mapType;
}
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) UIToolbar *toolbar;
@property (nonatomic, retain) MapController *mapController;
@property (nonatomic, copy) NSString *mineralName;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property (nonatomic, copy) NSString *region;
@property (nonatomic, retain) SearchCriteriaController *criteriaController;
@property (nonatomic, copy) NSString *mapType;


-(IBAction)loadMap:(id)sender;

-(void)getSamples;
-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx;

@end
 
