//
//  SubsampleInfo.h
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface ChemicalAnalysis: UIViewController <UITextViewDelegate> {
	IBOutlet UITextView *textView;
	IBOutlet UILabel *titleLabel;

}
@property (nonatomic, retain) IBOutlet UITextView *textView;
@property (nonatomic, retain) IBOutlet UILabel *titleLabel;

@end
