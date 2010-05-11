//
//  uniqueSamples.h
//  Location
//
//  Created by Heather Buletti on 6/4/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface uniqueSamples : NSObject <MKMapViewDelegate, MKAnnotation>{
	NSString *title;
	NSString *subtitle;
	NSString *countTitle;
	CLLocationCoordinate2D coordinate;	
	int count; //the number of samples that appear at a specific coordinate
	NSMutableArray *samples; //an array of samples that exist at this coordinate
	NSString *id;
	
}
@property (nonatomic, copy) NSString *countTitle;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *subtitle;
@property (nonatomic) CLLocationCoordinate2D coordinate;
@property (nonatomic) int count;
@property (nonatomic, retain) NSMutableArray *samples; 
@property (nonatomic, copy) NSString *id;

@end
