//
//  NarrowResultsController.m
//  Location
//
//  Created by Heather Buletti on 5/28/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "NarrowResultsController.h"


@implementation NarrowResultsController
@synthesize currentStringValue;
-(void)viewDidLoad{
	samples = [[NSMutableArray alloc] init];
	[self getRockList];
	sampleSelector.showsSelectionIndicator=YES;
	sampleSelector.dataSource = self;
	sampleSelector.delegate=self;
	
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)sampleSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)sampleSelector numberOfRowsInComponent:(NSInteger)component {
	return [samples count];
}
- (NSString *)pickerView:(UIPickerView *)sampleSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [samples objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)sampleSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	
	NSString *rockName= [samples objectAtIndex:row];
}

-(void)getRockList{
	//send http request to get a list of all the rock types stored in the database
	NSURL *myURL = [NSURL URLWithString:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?rockTypes=t"];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	NSURLResponse *response;
	NSError *error;
	rockReturn = [NSURLConnection sendSynchronousRequest:myRequest
									   returningResponse:&response error:&error];
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/Location/searchResults.kml"];
	[fh writeData:rockReturn];
	NSXMLParser *rockParser=[[NSXMLParser alloc] initWithData:rockReturn];
	[rockParser setDelegate:self];
	[rockParser parse];
	
}	
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"rockType"])
	{
		currentStringValue=nil;
		return;
	}
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    if (!currentStringValue) {
        // currentStringValue is an NSMutableString instance variable
        currentStringValue = [[NSMutableString alloc] initWithCapacity:50];
    }
	
	[currentStringValue appendString:string];
}
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
	if([elementName isEqualToString:@"rockType"])
	{
		NSString *tempRock= [[NSString alloc] initWithString:currentStringValue];
		[samples addObject:tempRock];
		[tempRock release];
		currentStringValue=nil;
	}
}
	
	
	- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
	[currentStringValue release];
	[rockReturn release];
	[samples release];
    [super dealloc];
}


@end
