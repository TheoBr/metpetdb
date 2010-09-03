//
//  AnnotationObjects.h
//  Location
//
//  Created by Heather Buletti on 5/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface AnnotationObjects: NSObject <MKMapViewDelegate, MKAnnotation>{
	CLLocationCoordinate2D coordinate;
	NSString *title;
	NSString *subtitle;
	NSString *id;
	NSString *publicData;
	NSString *rockType;
	NSMutableArray *metamorphicGrades;
	NSMutableArray *minerals;
	NSString *owner;
	NSString *name;
	NSString *description;
}
@property (nonatomic) CLLocationCoordinate2D coordinate;
@property (nonatomic, retain) NSString *description;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic, copy) NSString *id;
@property (nonatomic, copy) NSString *publicData;
@property (nonatomic, copy) NSString *rockType;
@property (nonatomic, retain) NSMutableArray *minerals;
@property (nonatomic, retain) NSMutableArray *metamorphicGrades;
@property (nonatomic, copy) NSString *owner;
@property (nonatomic, copy) NSString *name;
@end
