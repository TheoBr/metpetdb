
#import "DDAnnotationView.h"
#import "DDAnnotation.h"

@interface DDAnnotationView ()
@property (nonatomic, assign) BOOL isMoving;
@property (nonatomic, assign) CGPoint startLocation;
@property (nonatomic, assign) CGPoint originalCenter;
@end


#pragma mark -
#pragma mark DDAnnotationView implementation

@implementation DDAnnotationView

@synthesize isMoving = _isMoving;
@synthesize startLocation = _startLocation;
@synthesize originalCenter = _originalCenter;
@synthesize mapView = _mapView;
@synthesize newCoordinate, title;

- (id)initWithAnnotation:(id <MKAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier {
	if (self = [super initWithAnnotation:annotation reuseIdentifier:reuseIdentifier]) {
		self.enabled = YES;
		self.canShowCallout = YES;
		self.multipleTouchEnabled = NO;
		self.animatesDrop = YES;
		
        UIButton *rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        self.rightCalloutAccessoryView = rightButton;
	}
	return self;
}


#pragma mark -
#pragma mark Handling events

// Reference: iPhone Application Programming Guide > Device Support > Displaying Maps and Annotations > Displaying Annotations > Handling Events in an Annotation View

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
	// The view is configured for single touches only.
    UITouch* aTouch = [touches anyObject];
    self.startLocation = [aTouch locationInView:[self superview]];
	
	self.originalCenter=self.center;
    [super touchesBegan:touches withEvent:event];
}

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
	
    UITouch* aTouch = [touches anyObject];
    CGPoint newLocation = [aTouch locationInView:[self superview]];
    CGPoint newCenter;
	
	// If the user's finger moved more than 5 pixels, begin the drag.
    if ((abs(newLocation.x - self.startLocation.x) > 5.0) || (abs(newLocation.y - self.startLocation.y) > 5.0)) {
		self.isMoving = YES;
	}
	
	// If dragging has begun, adjust the position of the view.
    if (self.isMoving) {
        newCenter.x = self.originalCenter.x + (newLocation.x - self.startLocation.x);
        newCenter.y = self.originalCenter.y + (newLocation.y - self.startLocation.y);
        self.center = newCenter;
    } else {
		// Let the parent class handle it.
        [super touchesMoved:touches withEvent:event];
	}
	//newCoordinate = [self.mapView convertPoint:newLocation toCoordinateFromView:self.superview];
}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
	//self.canShowCallout=YES;
    if (self.isMoving) {
        // Update the map coordinate to reflect the new position.
        CGPoint newCenter = self.center;
        DDAnnotation* theAnnotation = (DDAnnotation *)self.annotation;
		
		newCoordinate = [self.mapView convertPoint:newCenter toCoordinateFromView:self.superview];
		//[theAnnotation changeCoordinate:newCoordinate];
		DDAnnotation *oldAnnotation= [[DDAnnotation alloc] initWithCoordinate:[self.mapView convertPoint:_originalCenter toCoordinateFromView:self.superview] title:@"Drag to move pin"];
		[oldAnnotation changeCoordinate:newCoordinate];
		
        // Clean up the state information.
        self.startLocation = CGPointZero;
        self.originalCenter = CGPointZero;
        self.isMoving = NO;
    } else {
        [super touchesEnded:touches withEvent:event];
	}
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
	
    if (self.isMoving) {
        // Move the view back to its starting point.
        self.center = self.originalCenter;
		
        // Clean up the state information.
        self.startLocation = CGPointZero;
        self.originalCenter = CGPointZero;
        self.isMoving = NO;
    } else {
        [super touchesCancelled:touches withEvent:event];
	}
}
@end