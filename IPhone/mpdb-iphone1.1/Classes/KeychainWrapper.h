//
//  KeychainWrapper.h
//  MetPetDB
//
//  Created by MetPetDB on 4/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Security/Security.h>

//Define an Objective-C wrapper class to hold Keychain Services code.
@interface KeychainWrapper : NSObject {
	
	NSString *serviceName;
}
@property (nonatomic, copy)NSString *serviceName;
@end
