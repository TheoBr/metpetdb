//
//  NarrowResultsController.h
//  Location
//
//  Created by Heather Buletti on 5/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface NarrowResultsController : UIViewController {
	NSString *currentStringValue;
	NSMutableArray *samples;
	NSData *rockReturn;
	IBOutlet UIPickerView *sampleSelector;
}
@property(nonatomic, copy) NSString *currentStringValue;


@end
