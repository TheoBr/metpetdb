//
//  CommentDisplay.h
//  Location
//
//  Created by Heather Buletti on 6/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddCommentController.h"

@class AddCommentController;
@interface CommentDisplayController : UIViewController <UITextViewDelegate, UITextFieldDelegate>  {
	NSString *sampleID;
	NSString *titleText;
	UIToolbar *toolbar;
	UIBarButtonItem *addCommentButton;
	AddCommentController *addComment;
	UITextView *textView;
	NSData *commentResponse;
	NSData *myReturn;
	NSMutableArray *comments; //this array holds all the comments for the sample
	NSString *currentStringValue; //this holds each comment as it is put in the comments array
	NSString *sampleName;
	NSString *commentCount;
	bool callCommentFunction;
	NSString *Uname;
}
@property(nonatomic, copy) NSString *Uname;
@property(nonatomic, copy) NSString *commentCount;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *sampleID;
//@property (nonatomic, retain)IBOutlet UITextView *textView;
@property (nonatomic, copy) NSString *titleText;
@property (nonatomic, retain) UIToolbar *toolbar;
@property (nonatomic, retain) UIBarButtonItem *addCommentButton;
@property (nonatomic, retain) AddCommentController *addComment;
@property (nonatomic, retain) IBOutlet UITextView *textView;
@property(nonatomic, copy) NSString *currentStringValue;
@end
