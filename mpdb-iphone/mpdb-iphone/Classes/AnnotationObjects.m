//
//  AnnotationObjects.m
//  Location
//
//  Created by Heather Buletti on 5/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AnnotationObjects.h"


@implementation AnnotationObjects
@synthesize coordinate, title, subtitle, id, publicData, rockType, minerals, metamorphicGrades, owner, name;

- (void)dealloc {
	
	[title release];
	[subtitle release];
	[publicData release];
	[rockType release];
	[owner release];
	[name release];
	[minerals release];
	[metamorphicGrades release];
	[super dealloc];
}
@end
