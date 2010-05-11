

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "DropMapViewController.h"

@interface DDAnnotationView:  MKPinAnnotationView {
	
    BOOL _isMoving;
    CGPoint _startLocation;
    CGPoint _originalCenter;
    MKMapView* _mapView;
	CLLocationCoordinate2D newCoordinate;
	NSString *title;
}
@property (nonatomic, copy) NSString *title;
@property CLLocationCoordinate2D newCoordinate;
@property (nonatomic, assign) MKMapView* mapView;

@end