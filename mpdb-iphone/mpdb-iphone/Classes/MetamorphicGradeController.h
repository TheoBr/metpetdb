
#import <UIKit/UIKit.h>
#import "SearchCriteriaController.h"
#import "AnnotationObjects.h"

@class SearchCriteriaController;
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
	NSMutableArray *original;
	CLLocationCoordinate2D center;
	double latSpan;
	double longSpan;
	NSMutableArray *currentRockTypes;
	NSMutableArray *currentMinerals;
	NSMutableArray *currentMetamorphicGrades;
	NSMutableArray *currentPublicStatus;
	NSMutableArray *currentOwners;
	NSString *region;
	CLLocationCoordinate2D myCoordinate;
	NSString* mapType; //indicates map, hybrid or satellite
	NSMutableArray *points;
	NSMutableArray *modifiedLocations2;
	int index;
	
}
@property (nonatomic, retain) UIPickerView *sampleSelector;
@property (nonatomic, retain) UILabel *output;
//@property (nonatomic, retain) UIBarButtonItem *searchButton;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *gradeName;
@property(nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *refineButton;
@property(nonatomic, retain) SearchCriteriaController *criteriaController;
@property(nonatomic, copy) NSString *region;
@property (nonatomic, copy) NSString *mapType;





-(IBAction)refineSearch:(id)sender;

-(void)getSamples;
-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx;

@end
