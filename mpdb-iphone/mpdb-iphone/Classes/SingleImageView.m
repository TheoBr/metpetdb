//
//  SingleImageView.m
//  Location
//
//  Created by Heather Buletti on 6/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SingleImageView.h"


@implementation SingleImageView
@synthesize imageView, toolbar, imageID, imagePath, currentStringValue, filename, scrollView;

-(void)viewDidLoad
{
	[self get_large_image];
	self.navigationItem.title=[[NSString alloc] initWithFormat:@"%@", filename];
	nameBool=FALSE;
	//make a server call to get the full size image by passing the image id
	NSString *fullPath=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb//image/?checksum=%@",imagePath];
	UIImage *image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:fullPath]]];
	[imageView setImage:image];
	[imageView setUserInteractionEnabled:YES];
	//make a toolbar at the bottom of the view
	/*NSMutableArray *buttons=[[NSMutableArray alloc] init];
	UIBarButtonItem *barButton=[[UIBarButtonItem alloc] initWithTitle:@"Add Image To My Library" style:UIBarButtonItemStyleBordered target:self action:@selector(addToLibrary)];
	[buttons addObject:barButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];*/
	//CGRect rect= CGRectMake(0, 0, imageView.frame.size.width, imageView.frame.size.height);
	//scrollView.frame=rect;//[[UIScreen mainScreen] applicationFrame]; 

	scrollView.contentMode = (UIViewContentModeScaleAspectFit);
    scrollView.autoresizingMask = ( UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight);
    scrollView.maximumZoomScale = 4;
	scrollView.minimumZoomScale=.25;
    scrollView.delegate = self;
	scrollView.bounces= NO;
	//scrollView.directionalLockEnabled= YES;
	scrollView.scrollEnabled=YES;
	//scrollView.pagingEnabled=YES;
	
	CGRect imageFrame= imageView.frame;
	/*if(imageView.frame.size.width > 300 && imageView.frame.size.height> 400)
	{
		CGRect frame= CGRectMake(-300, -300, 1000, 1000);
		scrollView.frame=frame;
	}
	else
	{	
		scrollView.contentSize=imageView.frame.size;
	}*/
	scrollView.contentSize= imageView.frame.size;

	[self.view addSubview:toolbar];
		
}
-(UIView *) viewForZoomingInScrollView:(UIScrollView *)scrollView {
	return imageView;
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
		//scrollView.frame=CGRectMake(-100, -100, 1000, 1000);
		//scrollView.contentSize= CGSizeMake(2000, 2000);
}

-(void)get_large_image
{
	NSString *urlString=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?large_image=%@", imageID];
	NSURL *myURL = [NSURL URLWithString:urlString];	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	NSURLResponse *response;
	NSError *error;
	imageResponse = [NSURLConnection sendSynchronousRequest:myRequest
										  returningResponse:&response error:&error];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	NSString *dataReceived=[[NSString alloc] initWithData:imageResponse encoding:NSASCIIStringEncoding];
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/MetPetDB/searchResults.kml"];
	[fh writeData:imageResponse];
	//NSString *data=[[NSString alloc] initWithData:imageResponse];
	
	NSXMLParser *subsampleParser= [[NSXMLParser alloc] initWithData:imageResponse];
	[subsampleParser setDelegate:self];
	[subsampleParser parse];
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict 
{
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"filename"])
	{
		nameBool=TRUE;
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


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName 
{
	if([elementName isEqualToString:@"string"])
	{
		if(nameBool==TRUE)
		{
			//currentStringValue holds the filename 
			filename= currentStringValue;
			currentStringValue= nil;
			nameBool=FALSE;
			return;
		}
		else
		{
			//currentStringValue now holds the path to the image
			imagePath= currentStringValue;
			currentStringValue=nil;
			return;
		}
	}
	currentStringValue=nil;
	return;
}



	
-(void)addToLibrary
{
}
- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

-(void)setData:(NSString*)id
{
	imageID=id;
}
	


- (void)dealloc {
    [super dealloc];
}


@end
