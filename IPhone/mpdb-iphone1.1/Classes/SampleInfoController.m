//
//  SampleInfoController.m
//  Location
//
//  Created by Heather Buletti on 5/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SampleInfoController.h"
#import "LoginViewController.h"
#import "CommentDisplayController.h"
#import "MetPetDBAppDelegate.h"
#import "KeychainWrapper.h"

@implementation SampleInfoController
@synthesize currentStringValue, rocktype, sampleID, locations, mapController, outputText, backButton, sampleAnnotation, imageCount;
@synthesize toolbar, textView, titleLabel, titleText, subsampleButton, id, imageViewController, commentDisplay,  analysisSummary, imageButton;
@synthesize sampleName;
@synthesize searchCriteria, Uname;

-(void) viewDidLoad
{
	
	backButton=[[UIBarButtonItem alloc] initWithTitle:@"Back To Map" style:UIBarButtonItemStyleBordered target:self action:@selector(backToMap)];
	self.navigationItem.leftBarButtonItem=backButton;
	imageIDs= [[NSMutableArray alloc] init];
	imagePaths=[[NSMutableArray alloc] init];
	
	[self makeImageButton];
	//make a label and a text view to display the sample information
	
	/*CGSize textViewSize= CGSizeMake(320, 500);
	 CGRect textViewFrame=	CGRectMake(0, 150, 320, 250);
	 textView.frame= textViewFrame;
	 textView.editable=NO;
	 textView.delegate=self; 
	 textView.font= [UIFont systemFontOfSize:14];
	 textView.scrollEnabled=YES;
	 textView.textColor=[UIColor whiteColor];
	 textView.canCancelContentTouches=YES;*/
	textView.font= [UIFont systemFontOfSize:14];
	
	mineralFlag=FALSE;
	minerals= [[NSMutableArray alloc] init];   
	
	//make variables to hold the v arious properties of the selected sample
	sampleID= sampleAnnotation.id;
	sampleName=sampleAnnotation.name;
	
	
	NSString *rockType=[sampleAnnotation rockType];
	minerals=[sampleAnnotation minerals];
	NSMutableArray *metamorphicGrades= [sampleAnnotation metamorphicGrades];
	NSString *owner=[sampleAnnotation owner];
	minerals= [sampleAnnotation minerals];
	NSString *status= [sampleAnnotation publicData];
	NSString *latitude=[[NSString alloc] initWithFormat:@"%.8g", sampleAnnotation.coordinate.latitude];
	NSString *longitude=[[NSString alloc] initWithFormat:@"%.8g", sampleAnnotation.coordinate.longitude];
	
	//for the sample name, separate the publication number if it exists and display it on the next line
	//publication number is all characters that follow the :
	
	NSString *shortName=[[NSString alloc] init];
	NSString *pubNum=nil;
	
	int x;
	int size= [sampleName length];
	for(x=0; x<size; x++)
	{
		if([sampleName characterAtIndex:x]==':')//everything after the colon will be part of the publication number 
		{
			x++;
			NSString *temp=[[NSString alloc] initWithFormat:@"%c",[sampleName characterAtIndex:x]];
			
			pubNum=[[NSString alloc] initWithString:temp];
			
			//[pubNum initWithString:[sampleName characterAtIndex:x]];
			//add the remaining characters to the pubnum variable
			x++;
			while(x<size)
			{
				NSString *foo=[[NSString alloc] initWithFormat:@"%c",[sampleName characterAtIndex:x]];
				
				pubNum=[pubNum stringByAppendingString:foo];
				x++;
			}
			break;
		}
		//if we are still before the :, we need to add the character to the short sample name
		NSString *foo=[[NSString alloc] initWithFormat:@"%c",[sampleName characterAtIndex:x]];
		shortName=[shortName stringByAppendingString:foo];
	}
	
	titleText=[[NSString alloc] initWithFormat:@"Sample: %@\nRock Type: %@ \n",shortName, rockType];
	
	titleLabel.text=titleText;
	
	if(pubNum==nil)//there is no publication number
	{
		outputText=[[NSString alloc] initWithFormat:@"Owner: %@ \nLatitude: %@ \nLongitude: %@ \n", owner, latitude,longitude];
	}
	else
	{
		outputText=[[NSString alloc] initWithFormat: @"Publication Number: %@ \nOwner: %@ \nLatitude: %@ \nLongitude: %@ \n", pubNum, owner, latitude,longitude];
	}
	NSString *mineralOutput=[[NSString alloc] init];
	if([minerals count]==0)
	{
		mineralOutput= [[NSString alloc] initWithString:@"Minerals Present: None Listed\n"];
	}
	else
	{
		int y; 
		mineralOutput=[[NSString alloc] initWithString:@"Minerals Present:  "];
		for(y=0; y<[minerals count]; y++)
		{
			NSString *temp=[minerals objectAtIndex:y];
			if(y==0)
			{
				mineralOutput=[mineralOutput stringByAppendingString:temp];
			}
			else
			{
				NSString *temp1=[[NSString alloc] initWithFormat:@", %@", temp];
				mineralOutput=[mineralOutput stringByAppendingString:temp1];
			}
			
		}
	}
	
	mineralOutput=[mineralOutput stringByAppendingString:@"\n"];
	outputText= [outputText stringByAppendingString:mineralOutput];
	if([metamorphicGrades count]==0)
	{
		outputText=[outputText stringByAppendingString:@"Metamorphic Grades: None Listed"];
	}
	else
	{
		int y;
		outputText=[outputText stringByAppendingString:@"Metamorphic Grades:  "];
		for(y=0; y<[metamorphicGrades count]; y++)
		{
			NSString *temp=[metamorphicGrades objectAtIndex:y];
			if(y==0)
			{
				outputText=[outputText stringByAppendingString:temp];
			}
			else
			{
				NSString *temp1=[[NSString alloc] initWithFormat:@"%@, ", temp];
				outputText=[outputText stringByAppendingString:temp1];
			}
			
		}
	}
	if(sampleAnnotation.description != nil)
	{
		NSString *description=[[NSString alloc] initWithFormat:@"\nDescription: %@", sampleAnnotation.description];
		outputText=[outputText stringByAppendingString:description];
	}
	textView.text= outputText;
	[self.view addSubview:textView];
	[self makeToolbar];
	
	
	
	
	CGRect rect= CGRectMake(200, 25 , 100, 100);
	self.navigationItem.title=[[NSString alloc] initWithFormat:@"Sample %@", sampleName];
	
	//if the user logged in, get their username so that it can later be passed to the server
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
}
//the following function gets called by the back button

-(void)makeToolbar{
	//make an array of the buttons to be added to the toolbar in the mapview
	buttons=[[NSMutableArray alloc] init];
	
	//initialize an object of commentDisplayController and analysisSummary so the counts for each can be obtained
	CommentDisplayController *commentController= [[CommentDisplayController alloc] initWithNibName:@"CommentDisplay" bundle:nil];
	[commentController setData:sampleID:sampleName:FALSE];
	//NSString *commentCount= [commentController getCommentCount];
	self.commentDisplay= commentController;
	[commentController release];
	
	AnalysisSummary *analysisController = [[AnalysisSummary alloc] initWithNibName:@"ChemicalAnalysis" bundle:nil];
	[analysisController setData:sampleID:sampleName];
	//NSString *subsampleCount= [analysisController getSubsampleCount];
	self.analysisSummary= analysisController;
	[analysisController release];
	
	
	NSString *subsampleCount= [analysisSummary getSubsampleCount];
	NSString *commentCount= [commentDisplay getCommentCount];
	NSString *subsampleTitle=[[NSString alloc] initWithFormat:@"%@ Subsamples", subsampleCount];
	NSString *commentTitle= [[NSString alloc] initWithFormat:@"%@ Comments", commentCount];
	//NSString *temp=@"lala";
	//NSString *subsampleTitle= [[NSString alloc] initWithFormat:@"Hello %@",temp ];
	//NSString *commentTitle= [[NSString alloc] initWithFormat:@"Hello %@", temp];
	
	subsampleButton=[[UIBarButtonItem alloc] initWithTitle: subsampleTitle style: UIBarButtonItemStyleBordered target: self action: @selector(viewSubsamples)];
	commentButton=[[UIBarButtonItem alloc] initWithTitle:commentTitle style: UIBarButtonItemStyleBordered target:self action: @selector(viewComments)];
	[buttons addObject:subsampleButton];
	[buttons addObject:commentButton];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
}
-(void)makeImageButton
{
	//although we are calling the get_thumbnails function, the only information we are interested in is the image count
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	[self get_thumbnails];
	NSString *buttonTitle=[[NSString alloc] initWithFormat:@"%@ images", imageCount];
	CGRect frame= CGRectMake(210, 5, 90, 30);
	imageButton=[UIButton buttonWithType:UIButtonTypeRoundedRect];
	imageButton.frame=frame;
	[imageButton setTitle:buttonTitle forState:UIControlStateNormal];
	[imageButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
	[self.view addSubview:imageButton];
	[imageButton addTarget:self action:@selector(imagesDisplay) forControlEvents:UIControlEventTouchUpInside];
	//[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)get_thumbnails
{
	sampleID= sampleAnnotation.id;
	//send the username and password to the server to be authenticate
	//NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhonePost.svc?"];
	NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhonePost.svc?"];
	
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString=[[NSString alloc] initWithFormat: @"thumbnails= %@\n", sampleID];
	if(Uname!=NULL)
	{
		postString= [[NSString alloc] stringByAppendingFormat:@"user= %@\n", Uname];
	}
	//postString = [postString stringByAppendingFormat:@"user= buleth@rpi.edu\n"];
	//NSString *postString=[[NSString alloc] initWithFormat:@"thumbnails= 7\n"];
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	NSString *returnValue=[[NSString alloc] initWithData:myReturn encoding:NSASCIIStringEncoding];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test2.txt"];
	[fh writeData:myReturn];
	
	NSURLResponse *response;
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"int"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"long"])
	{
		currentStringValue=nil;
		return;
	}
	currentStringValue=nil;
	return;
}
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
	if (!currentStringValue) {
		// currentStringValue is an NSMutableString instance variable
		currentStringValue = [[NSMutableString alloc] initWithCapacity:50];
	}
	[currentStringValue appendString:string];
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
	
	if([elementName isEqualToString:@"int"])
	{
		imageCount=currentStringValue;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"long"])
	{
		[imageIDs addObject: currentStringValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		//currentStringValue now holds the path to the image
		[imagePaths addObject:currentStringValue];
		remainingCount=[imagePaths count];
		
		currentStringValue=nil;
		return;
	}
	currentStringValue=nil;
	return;
	
	currentStringValue=nil;
	return;
}

-(void)viewSubsamples
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	AnalysisSummary *viewController = [[AnalysisSummary alloc] initWithNibName:@"ChemicalAnalysis" bundle:nil];
	//pass the analysis table the subsample ID so a list of subsamples can be obtainined
	[viewController setData:sampleID:sampleName];
	
	self.analysisSummary= viewController;
	[viewController release];
	UIView *ControllersView = [analysisSummary view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:analysisSummary animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)viewComments
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	CommentDisplayController *viewController= [[CommentDisplayController alloc] initWithNibName:@"CommentDisplay" bundle:nil];
	[viewController setData:sampleID:sampleName:TRUE];
	self.commentDisplay= viewController;
	[viewController release];
	UIView *ControllersView=[commentDisplay view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:commentDisplay animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}

-(void)imagesDisplay
{
	if([imagePaths count] ==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Images" message:@"There are no images or subsample images for this sample." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
		ImageViewController *viewController=[[ImageViewController alloc] initWithNibName:@"ImageView" bundle:nil];
		[viewController setVars: remainingCount : 0:imagePaths :imageIDs:0]; 
		[viewController setSamples:sampleAnnotation:locations:searchCriteria];
		[viewController setCurrentSearchData:currentSearchData];
		//[viewController setCoordinate:myLocation:latitudeSpan:longitudeSpan: maxLat:minLat: maxLong: minLong];
		self.imageViewController=viewController;
		[viewController release];
		UIView *ControllersView = [imageViewController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:imageViewController animated:NO];
		[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	}
}

-(void)backToMap{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	MapController *viewController=[[MapController alloc] initWithNibName:@"MapView" bundle:nil];
	//[viewController setType:mapType];
	[viewController setData:locations:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	[mapController makeNavBar];
	self.mapController= viewController;
	[viewController release];
	UIView *newView=[mapController view];
	[self.view addSubview:newView];
	[mapController makeNavBar];
	[self.navigationController pushViewController:mapController animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}


-(void)setSamples:(NSMutableArray*)mySamples:(AnnotationObjects*)selectedSample:(CriteriaSummary*)criteria
{
	sampleAnnotation= selectedSample;
	locations=mySamples;
	//buttonVisible=button;
	searchCriteria=criteria;
	//originalCoordinates=original;
}

-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
}
- (void)dealloc {
	[titleLabel release];
	[sampleResponse release];
	[currentStringValue release];
	[sampleID release];
	[rocktype release];
	[locations release];
	[mapController release];
	[minerals release];
	[outputText release];
	[titleText release];
	[backButton release];
	[textView release];
	[sampleAnnotation release];
	[toolbar release];
	[buttons release];
	[subsampleButton release];
	[commentButton release];
	[analysisSummary release];
	[imageViewController release];
	[commentDisplay release];
	[imageCount release];
	[imageButton release];
	[imageResponse release];
	[imagePaths release];
	[imageIDs release];
	[myReturn release];
	[sampleName release];
	[samples release];
	
    [super dealloc];
}


@end
