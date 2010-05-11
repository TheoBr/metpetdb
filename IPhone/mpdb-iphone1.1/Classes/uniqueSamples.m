//
//  uniqueSamples.m
//  Location
//
//  Created by Heather Buletti on 6/4/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "uniqueSamples.h"


@implementation uniqueSamples
@synthesize title, subtitle, coordinate, count, samples, id;


- (void)dealloc {
	
	[title release];
	[subtitle release];
	[countTitle release];
	[samples release];
	[super dealloc];
}
@end
