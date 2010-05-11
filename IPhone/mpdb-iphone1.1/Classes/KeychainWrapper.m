//
//  KeychainWrapper.m
//  MetPetDB
//
//  Created by MetPetDB on 4/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "KeychainWrapper.h";

@implementation KeychainWrapper
@synthesize serviceName;

//search the keychain for a value 
- (NSData *)searchKeychainCopyMatching:(NSString *)identifier {
    NSMutableDictionary *searchDictionary = [self newSearchDictionary:identifier];
	
    // Add search attributes
    [searchDictionary setObject:(id)kSecMatchLimitOne forKey:(id)kSecMatchLimit];
	
    // Add search return types
    [searchDictionary setObject:(id)kCFBooleanTrue forKey:(id)kSecReturnData];
	
    NSData *result = nil;
    OSStatus status = SecItemCopyMatching((CFDictionaryRef)searchDictionary,
										  (CFTypeRef *)&result);
	
    [searchDictionary release];
    return result;
}
- (NSMutableDictionary *)newSearchDictionary:(NSString *)identifier {
	serviceName= @"MetPetDB";
    NSMutableDictionary *searchDictionary = [[NSMutableDictionary alloc] init];  
	
    [searchDictionary setObject:(id)kSecClassGenericPassword forKey:(id)kSecClass];
	
    NSData *encodedIdentifier = [identifier dataUsingEncoding:NSUTF8StringEncoding];
    [searchDictionary setObject:encodedIdentifier forKey:(id)kSecAttrGeneric];
    [searchDictionary setObject:encodedIdentifier forKey:(id)kSecAttrAccount];
    [searchDictionary setObject:serviceName forKey:(id)kSecAttrService];
	
    return searchDictionary; 
}
//deleting the value in the keychain
- (void)deleteKeychainValue:(NSString *)identifier {
	
	NSMutableDictionary *searchDictionary = [self newSearchDictionary:identifier];
	SecItemDelete((CFDictionaryRef)searchDictionary);
	[searchDictionary release];
}
- (BOOL)createKeychainValue:(NSString *)password
			  forIdentifier:(NSString *)identifier {
	NSMutableDictionary *dictionary = [self newSearchDictionary:identifier];
	
	NSData *passwordData = [password dataUsingEncoding:NSUTF8StringEncoding];
	[dictionary setObject:passwordData forKey:(id)kSecValueData];
	
	OSStatus status = SecItemAdd((CFDictionaryRef)dictionary, NULL);
	[dictionary release];
	
	if (status == errSecSuccess) {
		return YES;
	}
	return NO;
}
//updating the value in a keychain
/*- (BOOL)updateKeychainValue:(NSString *)password
 forIdentifier:(NSString *)identifier {
 
 NSMutableDictionary *searchDictionary = [self newSearchDictionary:identifier];
 NSMutableDictionary *updateDictionary = [[NSMutableDictionary alloc] init];
 NSData *passwordData = [password dataUsingEncoding:NSUTF8StringEncoding];
 [updateDictionary setObject:passwordData forKey:(id)kSecValueData];
 
 OSStatus status = SecItemUpdate((CFDictionaryRef)searchDictionary,
 (CFDictionaryRef)updateDictionary);
 
 [searchDictionary release];
 [updateDictionary release];
 
 if (status == errSecSuccess) {
 return YES;
 }
 return NO;
 }*/

@end