//
//  SingleImageView.h
//  Location
//
//  Created by Heather Buletti on 6/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface SingleImageView : UIViewController <UIScrollViewDelegate> {
	IBOutlet UIImageView *imageView;
	IBOutlet UIScrollView *scrollView;
	UIToolbar *toolbar;
	NSString *imageID;
	NSString *imagePath;
	NSData *imageResponse;
	NSString *currentStringValue;
	NSString *filename;
	bool nameBool; //when this is true, the currentStringValue will be the filename and not the pathname
}
@property (nonatomic, retain) IBOutlet UIScrollView *scrollView;
@property(nonatomic, retain) IBOutlet UIImageView *imageView;
@property (nonatomic, retain) UIToolbar *toolbar;
@property(nonatomic, copy) NSString *imageID;
@property (nonatomic, copy) NSString *imagePath;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *filename;
@end
 