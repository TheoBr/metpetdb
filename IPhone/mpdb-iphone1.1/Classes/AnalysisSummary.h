//
//  AnalysisTable.h
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface AnalysisSummary : UIViewController <UITextViewDelegate> {
	NSString *sampleID;
	IBOutlet UITextView *textView;
	NSString *imageCount;
	NSString *subsampleCount;
	NSString *analysisCount;
	NSData *subsampleResponse;
	NSData *myReturn;
	NSString *currentStringValue;
	bool imageBool; //if this is true, the current string value represents the image count
	bool analysisBool; //if this is true, the current string value represents the analysis count
	NSMutableArray *minerals; //array of the minerals that were analyzed in all the subsamples
	NSString *bulkrock;
	NSString *sampleName;
	NSString *Uname;
}
@property (nonatomic, copy) NSString *Uname;
@property (nonatomic, copy) NSString *sampleName;
@property (nonatomic, copy) NSString *sampleID;
@property (nonatomic, retain)IBOutlet UITextView *textView;
@property (nonatomic, copy) NSString *imageCount;
@property (nonatomic, copy) NSString *subsampleCount;
@property (nonatomic, copy) NSString *analysisCount;
@property (nonatomic, copy) NSString *currentStringValue;
@property (nonatomic, copy) NSString *bulkrock;
@end
