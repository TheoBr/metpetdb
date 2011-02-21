//
//  postRequest.m
//  MetPetDB
//
//  Created by MetPetDB on 3/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

//This file builds the string that needs to be sent to the server as a post request using the current search criteria

#import "PostRequest.h"
#import "KeychainWrapper.h"
#import "constants.h"

@implementation PostRequest

//http://samana.cs.rpi.edu:8080/metpetwebtst/
//http://samana.cs.rpi.edu/metpetweb/

-(id)init
{
	[super init];
	return self;
}

-(NSData*)buildPostString
{

	//if the user is logged in, their username should be sent to the server
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		username = [[NSString alloc] initWithData:usernameData
										 encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	//NSString *urlString= [[NSString alloc] initWithFormat:@"searchIPhonePost.svc?"];
	NSString *urlString= [[NSString alloc] initWithFormat:@"%@searchIPhonePost.svc?", RootURL];
	
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString=[[NSString alloc] init];
	NSString *temp;
	int i; 
 	if(region!=nil)
	{
		postString=[postString stringByAppendingFormat:@"searchRegion= %@\n", region];
	}
	else
	{
		postString=[[NSString alloc] initWithFormat:@"north= %f\nsouth= %f\neast= %f\nwest= %f\n", [[coordinates objectAtIndex:0] doubleValue]+.001, [[coordinates objectAtIndex:1] doubleValue] -.001, [[coordinates objectAtIndex:2]doubleValue] + .001, [[coordinates objectAtIndex:3] doubleValue]- .001];
	}
	for(i=0; i< [currentRockTypes count]; i++)
	{
		postString=[postString stringByAppendingFormat:@"rockType= %@\n",[currentRockTypes objectAtIndex:i]];
	}
	for(i=0; i< [currentMinerals count]; i++)
	{
		NSString *m= [currentMinerals objectAtIndex:i];
		NSString *minSubstring=[[NSString alloc] init];
		bool removedChars=FALSE;
		for(int j=0; j<[m length]; j++)
		{
			NSString *character= [[NSString alloc] initWithFormat:@"%c", [m characterAtIndex:j]];
			if([character isEqualToString:@"("])
			{
				minSubstring= [m substringToIndex:j-1];
				removedChars=TRUE;
			}
		}
		if(removedChars==TRUE)
		{
			postString= [postString stringByAppendingFormat:@"mineral= %@\n", minSubstring];
		}
		else
		{
			postString=[postString stringByAppendingFormat:@"mineral= %@\n",[currentMinerals objectAtIndex:i]];
		}
		
	}
	for(i=0; i< [currentMetamorphicGrades count]; i++)
	{
		postString=[postString stringByAppendingFormat:@"metamorphicGrade= %@\n", [currentMetamorphicGrades objectAtIndex:i]];
	}
	for(i=0; i< [currentOwners count]; i++)
	{
		postString=[postString stringByAppendingFormat:@"owner= %@\n", [currentOwners objectAtIndex:i]];
	}
	
	if([currentPublicStatus isEqualToString:@"private"])
	{
		postString=[postString stringByAppendingString:@"sampleType= private\n"];
	}
	else if([currentPublicStatus isEqualToString:@"public"])
	{
		postString=[postString stringByAppendingString:@"sampleType= public\n"];
	}
	else if([currentPublicStatus isEqualToString:@"both"])
	{
		postString=[postString stringByAppendingString:@"sampleType= both\n"];
	}
	if(username!=nil)
	{
		postString=[postString stringByAppendingFormat:@"user= %@\n", username];
	}
	//postString = [postString stringByAppendingFormat:@"user= buleth@rpi.edu\n"];
	
	//we only want to specify a pagination parameter value if there we are not looking for search criteria
	if([criteriaSummary isEqualToString:@"true"])
	{
		postString=[postString stringByAppendingFormat:@"criteriaSummary= %@\n", criteriaSummary];	
	}
	else 
	{
		postString= [postString stringByAppendingFormat:@"pagination= %d\ncriteriaSummary= %@\n", pagination, criteriaSummary];
	}
	
	postString=[postString stringByAppendingFormat:@"currentLongitude= %.5g\n", currentLongitude];
	postString=[postString stringByAppendingFormat:@"currentLatitude= %.5g\n", currentLatitude];
	
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	NSError *error;
	postReturn = [NSURLConnection sendSynchronousRequest:myRequest
									   returningResponse:postReturn error:&error];
	if((postReturn == nil) && (error != nil))
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	/*
	NSFileManager *nsfm = [NSFileManager defaultManager];
	
   
	NSString *sfh = [[NSString alloc] init];
	
	sfh=[sfh stringByAppendingFormat:@"/Users/scball/Desktop/"];	
	sfh=[sfh  stringByAppendingFormat:@"%d", rand()];
	sfh=[sfh stringByAppendingFormat:@".txt"];
	
	[nsfm createFileAtPath:sfh contents:myData attributes: nil];
	*/
	

	return postReturn;
}


//this function gets passed all the current search data and sets the class variables accordingl
-(void)setData:(NSMutableArray*)minerals:(NSMutableArray*)rocks:(NSMutableArray*)owners:(NSMutableArray*)metgrades:
(NSString*)publicStatus:(NSString*)searchRegion:(NSMutableArray*)coords:(int)page:(NSString*)summary:(double)latitude:(double)longitude
{
	coordinates=coords;
	currentMinerals=minerals;
	currentRockTypes=rocks;
	currentOwners=owners;
	currentMetamorphicGrades=metgrades;
	region=searchRegion;
	currentPublicStatus=publicStatus;
	pagination= page;
	criteriaSummary=summary;
	currentLatitude=latitude;
	currentLongitude=longitude;
	
}







@end
