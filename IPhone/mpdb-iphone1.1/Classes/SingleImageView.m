//
//  SingleImageView.m
//  Location
//
//  Created by Heather Buletti on 6/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SingleImageView.h"
#import "MetPetDBAppDelegate.h"
#import "KeychainWrapper.h"
#define HORIZ_SWIPE_DRAG_MIN  12
#define VERT_SWIPE_DRAG_MAX    4


@implementation SingleImageView
@synthesize imageView, toolbar, imageID, imagePath, currentStringValue, filename, scrollView, singleImage;


-(void)viewDidLoad
{
	[self get_large_image];
	//NSString *fullPath=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst//image/?checksum=%@",imagePath];
	NSString *fullPath=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/image/?checksum=%@",imagePath];
//	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test2.txt"];//
//	[fh writeData:[fullPath dataUsingEncoding:NSASCIIStringEncoding]];
//	NSData *newData=[[NSData alloc] init];
//	newData=[fullPath dataUsingEncoding:NSASCIIStringEncoding];
//	[fh writeData:newData];
	
	
	
	UIImage *image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:fullPath]]];
	
	//self.navigationItem.title=[[NSString alloc] initWithFormat:@"%@", filename];
	
	
	[imageView setUserInteractionEnabled:YES];
	CGRect frame= CGRectMake(0,0, 320, 480);
	imageView.frame=frame;
	[imageView setImage:image];
	
	//make a toolbar at the bottom of the view
	/*NSMutableArray *buttons=[[NSMutableArray alloc] init];
	 UIBarButtonItem *barButton=[[UIBarButtonItem alloc] initWithTitle:@"Add Image To My Library" style:UIBarButtonItemStyleBordered target:self action:@selector(addToLibrary)];
	 [buttons addObject:barButton];
	 CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	 toolbar = [ [ UIToolbar alloc ] init ];
	 toolbar.frame = toolBarFrame;
	 toolbar.items=buttons;	
	 [toolbar setBarStyle:1];*/
	CGRect rect= CGRectMake(0, 0, imageView.frame.size.width, imageView.frame.size.height);
	scrollView.frame=rect;//[[UIScreen mainScreen] applicationFrame]; 
	
	scrollView.contentMode = (UIViewContentModeScaleAspectFit);
    scrollView.autoresizingMask = ( UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight);
    scrollView.maximumZoomScale = 4;
	scrollView.minimumZoomScale=.25;
    scrollView.delegate = self;
	scrollView.bounces= NO;
	scrollView.userInteractionEnabled=YES;
	//scrollView.directionalLockEnabled= YES;
	scrollView.scrollEnabled=YES;
	
	
	CGRect imageFrame= imageView.frame;
	scrollView.contentSize= imageView.frame.size;
	nextButton=[[UIBarButtonItem alloc] initWithTitle:@"Next" style:UIBarButtonItemStyleBordered target:self action:@selector(nextImage)];
	self.navigationItem.rightBarButtonItem=nextButton;
	
	[self.view addSubview:toolbar];
	//if the user is logged in, get the username so it can later be passed to the server
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
	int size= [images count];
	if(position >= (size -1)){
		self.navigationItem.rightBarButtonItem=nil;
	}
	
}

-(UIView *) viewForZoomingInScrollView:(UIScrollView *)scrollView {
	return imageView;
}
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
	return YES;
}	

-(void)nextImage
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	nextButton.enabled=NO;
	position++;
	if(position < [images count])
	{
		SingleImageView *viewController= [[SingleImageView alloc] initWithNibName:@"SingleImageView" bundle:nil];
		[viewController setData:position :images];
		self.singleImage= viewController;
		[viewController release];
		UIView *newView= [singleImage view];
		[self.view addSubview:newView];
		int size= [images count];
		if(position >= (size -1)){
			self.navigationItem.rightBarButtonItem=nil;
		}
		
		//[self.navigationController pushViewController:singleImage animated:NO];
	}
	else
	{
		self.navigationItem.rightBarButtonItem=nil;
	}
	nextButton.enabled=YES;
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)previousImage
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	//[self.navigationController popViewControllerAnimated:NO];
	if(position >=1)
	{
		position--;
		
		SingleImageView *viewController= [[SingleImageView alloc] initWithNibName:@"SingleImageView" bundle:nil];
		[viewController setData:position :images];
		self.singleImage= viewController;
		[viewController release];
		UIView *newView= [singleImage view];
		[self.view addSubview:singleImage];
		[self.navigationController pushViewController:singleImage animated:NO];
	}
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}

-(void)get_large_image
{
	//send the username and password to the server to be authenticated
	//NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhonePost.svc?"];
	NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhonePost.svc?"];
	
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString=[[NSString alloc] initWithFormat: @"largeImage= %@\n", imageID];
	if(Uname!=nil)
	{
		postString= [[NSString alloc] stringByAppendingFormat:@"user= %@\n", Uname];
	}
	//postString = [postString stringByAppendingFormat:@"user= buleth@rpi.edu\n"];
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	NSString *returnValue=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if((myReturn == nil) && (error != nil))
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
//	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test2.txt"];
//	[fh writeData:myReturn];
	
	NSURLResponse *response;
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
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
//pass the function the array of image paths and the current position in the array to display

-(void)setData:(NSInteger)arrayPosition:(NSMutableArray*)imageIDs
{
	position=arrayPosition;
	images=imageIDs;
	imageID= [images objectAtIndex:position];
	
}
/*-(void)setData:(NSString*)id
 {
 imageID=id;
 }*/



- (void)dealloc {
	/*[imageView release];
	 [scrollView release];
	 [toolbar release];
	 [imageID release];
	 [imagePath release];
	 [images release];
	 [imageResponse release];
	 [currentStringValue release];
	 [filename release];
	 [singleImage release];
	 [myReturn release];
	 
	 [super dealloc];*/
}


@end
