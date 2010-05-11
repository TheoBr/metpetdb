//
//  InfoViewController.h
//  MetPetDB
//
//  Created by Heather Buletti on 7/23/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface InfoViewController : UIViewController {
	IBOutlet UIButton *wikiButton;

}
@property (nonatomic, retain) IBOutlet UIButton *wikiButton;
-(IBAction)goToWiki:(id)sender;

@end
