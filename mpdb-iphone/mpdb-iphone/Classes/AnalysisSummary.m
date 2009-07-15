//
//  AnalysisSummary.m
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "AnalysisSummary.h"


@implementation AnalysisSummary
@synthesize sampleID, textView, imageCount, analysisCount, subsampleCount, currentStringValue, bulkrock;
-(void)viewDidLoad{
	minerals= [[NSMutableArray alloc] init];
	imageBool=FALSE;
	analysisBool=FALSE;
	subsampleCount=nil;
	[self getSubsampleInfo];
	
	CGSize textViewSize= CGSizeMake(320, 500);
	CGRect textViewFrame=	CGRectMake(0, 100, 320, 250);
	textView.frame= textViewFrame;
	textView.scrollEnabled=YES;
	textView.editable=NO;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];
	textView.textColor=[UIColor whiteColor];
	NSString *titleText=[[NSString alloc] initWithFormat:@"Subsample Info for Sample# %@",sampleID];
	CGRect labelFrame= CGRectMake(0,0,320, 100);
	UILabel *title=[[UILabel alloc] initWithFrame:labelFrame];
	title.textColor=[UIColor whiteColor];
	title.textAlignment= UITextAlignmentCenter;
	title.backgroundColor=[UIColor blackColor];
	title.text=titleText;
	[self.view addSubview:title];
	if([subsampleCount isEqualToString:@"0"])
	{
		NSString *output= [[NSString alloc] initWithString:@"No subsamples exist for this sample."];
		textView.text= output;
	}
	else
	{
		NSString *output=[[NSString alloc] initWithFormat: @"Number of Subsamples: %@ \nNumber of Subsample Images: %@ \nNumber of Analyses Available: %@ \n", subsampleCount, imageCount, analysisCount];
		output=[output stringByAppendingString:@"Minerals Analyzed:"];
		int x;
		for(x=0; x<[minerals count]; x++)
		{
			output=[output stringByAppendingString:@"\n     "];
			output=[output stringByAppendingString:[minerals objectAtIndex:x]];
		}
		output= [output stringByAppendingFormat:@"\nBulk Rock Analysis: %@", bulkrock];
		textView.text=output;
	}
	//[self.view addSubview:textView];
}
-(void)getSubsampleInfo
{
	NSString *urlString=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?subsampleInfo=%@", sampleID];
	NSURL *myURL = [NSURL URLWithString:urlString];	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	NSURLResponse *response;
	NSError *error;
	subsampleResponse = [NSURLConnection sendSynchronousRequest:myRequest
											returningResponse:&response error:&error];
	NSString *dataReceived=[[NSString alloc] initWithData:subsampleResponse encoding:NSASCIIStringEncoding];
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/Location/searchResults.kml"];
	[fh writeData:subsampleResponse];
	
	NSXMLParser *subsampleParser= [[NSXMLParser alloc] initWithData:subsampleResponse];
	[subsampleParser setDelegate:self];
	[subsampleParser parse];
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"subsamples"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"imageCount"])
	{
		imageBool=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"analysisCount"])
	{
		analysisBool=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"boolean"])
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
	if([elementName isEqualToString:@"string"])
	{
		[minerals addObject:currentStringValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"int"])
	{
		if(imageBool==TRUE)
		{
			imageCount=currentStringValue;
			imageBool=FALSE;
			currentStringValue=nil;
			return;
		}
		else if(analysisBool==TRUE)
		{
			analysisCount= currentStringValue;
			analysisBool=FALSE;
			currentStringValue=nil;
			return;
		}
		else //if both of the other bools are false, the int tag represents the subsample count
		{
			subsampleCount=currentStringValue;
			currentStringValue=nil;
			return;
		}
	}
	if([elementName isEqualToString:@"boolean"])
	{
		if([currentStringValue isEqualToString:@"false"])
		{
			bulkrock=@"No";
		}
		else
		{
			bulkrock=@"Yes";
		}
	}
}
//create a funtion that can be called from the subsample view controller that will return the number of subsamples and the number of chemical analyses
-(NSString*)getSubsampleCount
{
	[self getSubsampleInfo];
	//make an array to return that holds the image count and the subsample count
	return subsampleCount;
}

-(void)setData:(NSString*)sample
{
	sampleID=sample;
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
    [super dealloc];
}

@end
