//
//  AddCommentController.h
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AddCommentController : UIViewController <UITextViewDelegate>{
	NSString *sampleID;
	NSString *titleText;
	IBOutlet UITextView *textView;
	IBOutlet UIScrollView *scrollView;
	CGRect keyboardBounds;
}
@property(nonatomic, copy)NSString *sampleID;
@property (nonatomic, copy) NSString *titleText;
@property (nonatomic, retain) IBOutlet UITextView *textView;
@property (nonatomic, retain) IBOutlet UIScrollView *scrollView;
@end
