#import <UIKit/UIKit.h>


@interface DropAnnotation: NSObject <MKMapViewDelegate, MKAnnotation>{
	CLLocationCoordinate2D coordinate;
	NSString *title;
	}
@property (nonatomic) CLLocationCoordinate2D coordinate;
@property (nonatomic, copy) NSString *title;

@end
