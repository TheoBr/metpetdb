//
// DDAnnotation.m
// MapKitDragAndDrop
//
// Created by digdog on 7/24/09.
// Copyright 2009 digdog software.
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//


#import "DDAnnotation.h"


#pragma mark -
#pragma mark DDAnnotation implementation

@implementation DDAnnotation

@synthesize coordinate, title, subtitle, placemark;


- (id)initWithCoordinate:(CLLocationCoordinate2D)_coordinate title:(NSString*)_title {
	if (self = [super init]) {
		[self changeCoordinate:_coordinate];
		self.title =_title;
		self.placemark = nil;
	}
	return self;
}



- (NSString *)subtitle {
	NSString* subtitle = nil;
	
	if (self.placemark) {
		subtitle = [NSString stringWithFormat:@"%@, %@", self.placemark.administrativeArea, self.placemark.country];
	} else {
		subtitle = [NSString stringWithFormat:@"%lf, %lf", self.coordinate.latitude, self.coordinate.longitude];
	}
	
	return subtitle;
}

#pragma mark -
#pragma mark Change coordinate

- (void)changeCoordinate:(CLLocationCoordinate2D)_coordinate {
	coordinate = _coordinate;
	
	// Try to reverse geocode here
	MKReverseGeocoder *reverseGeocoder = [[MKReverseGeocoder alloc] initWithCoordinate:self.coordinate];
	reverseGeocoder.delegate = self;
	[reverseGeocoder start];
}

#pragma mark -
#pragma mark MKReverseGeocoderDelegate methods

- (void)reverseGeocoder:(MKReverseGeocoder *)geocoder didFindPlacemark:(MKPlacemark *)placemark {
	[self notifyCalloutInfo:placemark];
	[geocoder release];
}

- (void)reverseGeocoder:(MKReverseGeocoder *)geocoder didFailWithError:(NSError *)error {
	[self notifyCalloutInfo:nil];
	[geocoder release];
}

#pragma mark -
#pragma mark MKAnnotationView Notification

- (void)notifyCalloutInfo:(MKPlacemark *)_placemark {
	[self willChangeValueForKey:@"subtitle"]; // Workaround for SDK 3.0, otherwise callout info won't update.
	self.placemark = _placemark;
	[self didChangeValueForKey:@"subtitle"]; // Workaround for SDK 3.0, otherwise callout info won't update.
	
	[[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"MKAnnotationCalloutInfoDidChangeNotification" object:self]];
}

#pragma mark -
#pragma mark Memory Management

- (void)dealloc {
	[title release];
	[placemark release];
	[super dealloc];
}

@end