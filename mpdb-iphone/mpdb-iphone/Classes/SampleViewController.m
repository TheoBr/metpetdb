
#import "SampleViewController.h"
#import "AnnotationObjects.h"


@implementation SampleViewController
@synthesize output, headerlabel, sampleSelector, googlelink, sample, latitude, longitude, mapController, temp, currentStringValue, sampleID, rocktype;
-(void)viewDidLoad{}

-(void)getSamples{
	//NSString *urlstring= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhone.svc?lat=%@&long=%@",latitude,longitude];
	//NSURL *myURL= [NSURL URLWithString:urlstring];
	NSURL *myURL = [NSURL URLWithString:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhone.svc?lat=40&long=-120"];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	
	NSURLResponse *response;
	NSError *error;
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/Location/searchResults.kml"];
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:&response error:&error];
	[fh writeData:myReturn];
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
	
}


- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
		if([elementName isEqualToString:@"sample"])
		{
			newAnnotation= [AnnotationObjects new];
			return;
		}
		if([elementName isEqualToString:@"x"])
		{
			currentStringValue=nil;
			return;
		}
		if([elementName isEqualToString:@"y"])
		{
			currentStringValue=nil;
			return;
		}
		if([elementName isEqualToString:@"long"])
		{
			currentStringValue=nil;
			return;
		}
		if([elementName isEqualToString:@"rockType"])
		{
			currentStringValue= nil;
			return;
		}
	if([elementName isEqualToString:@"string"])
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

    if ( [elementName isEqualToString:@"x"] ) {
		longdouble= [currentStringValue doubleValue];
		currentStringValue = nil;
    }
	if([elementName isEqualToString:@"y"])
	{
		latdouble= [currentStringValue doubleValue];
		currentStringValue = nil;
			}
	if([elementName isEqualToString:@"long"])
	{
		sampleID= [[NSString alloc] initWithString:currentStringValue];
		currentStringValue = nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		sampleName= [[NSString alloc] initWithString:currentStringValue];
		currentStringValue=nil;
		return;
	}
		
	if([elementName isEqualToString:@"sample"])
	{
		CLLocationDegrees latdegrees= (CLLocationDegrees)latdouble;
		CLLocationDegrees longdegrees= (CLLocationDegrees) longdouble;
		CLLocation *tempCoordinate= [[CLLocation alloc] initWithLatitude:latdegrees longitude:longdegrees];
		
		//AnnotationObjects *newAnnotation= [AnnotationObjects new];
		[newAnnotation setCoordinate:tempCoordinate.coordinate];
		[newAnnotation setTitle:[[NSString alloc] initWithString:sampleName]];
		[newAnnotation setId:[[NSString alloc] initWithString:sampleID]];
		[newAnnotation setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
		[locations addObject:newAnnotation];
	}
	if([elementName isEqualToString:@"rockType"])
	{
		if(stringFlag==FALSE)
		{
			NSString *tempRock= [[NSString alloc] initWithString:currentStringValue];
			[samples addObject:tempRock];
			[tempRock release];
			currentStringValue=nil;
		}
		else
		{
			sampleName= [[NSString alloc] initWithString:currentStringValue];
			currentStringValue=nil;
			return;
		}
	}
	   currentStringValue = nil;
    [currentStringValue release];
 
}


-(void)setRad:(NSString*)rad:(NSString*)lat:(NSString*)longx{
	radius=rad;
	latitude=lat;
	longitude=longx;
}


/*-(void)addAuthenticationToRequest:(CFHTTPMessageRef)request withResponse:(CFHTTPMessageRef)response {    
 CFHTTPAuthenticationRef authentication = CFHTTPAuthenticationCreateFromResponse(NULL, response);   
 [NSMakeCollectable(authentication) autorelease]; 
 CFStreamError err;   
 //Boolean success = CFHTTPMessageApplyCredentials(request, authentication, CFSTR(”username”), CFSTR(”password”), &err); }
 }
 */

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[output release];
	[headerlabel release];
	[sampleSelector release];
	[googlelink release];
	[sample release];
	[latitude release];
	[longitude release];
}
@end
