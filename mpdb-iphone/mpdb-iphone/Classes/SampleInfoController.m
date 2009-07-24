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


@implementation SampleInfoController
@synthesize currentStringValue, rocktype, sampleID, locations, mapController, outputText, backButton, sampleAnnotation, imageCount;
@synthesize toolbar, textView, titleLabel, titleText, subsampleButton, id, imageViewController, commentDisplay,  analysisSummary, imageButton;


-(void) viewDidLoad{
	
	imageIDs= [[NSMutableArray alloc] init];
	imagePaths=[[NSMutableArray alloc] init];
	
	[self makeImageButton];
	//make a label and a text view to display the sample information
		
	CGSize textViewSize= CGSizeMake(320, 500);
CGRect textViewFrame=	CGRectMake(0, 150, 320, 250);
	textView.frame= textViewFrame;
	textView.scrollEnabled=YES;
	textView.editable=NO;
	textView.delegate=self; 
	textView.font= [UIFont systemFontOfSize:14];

	mineralFlag=FALSE;
	minerals= [[NSMutableArray alloc] init];   
	
	//make variables to hold the various properties of the selected sample
	sampleID= sampleAnnotation.id;
	[self makeToolbar];
	
	NSString *rockType=sampleAnnotation.rockType;
	NSMutableArray *minerals=sampleAnnotation.minerals;
	NSMutableArray *metamorphicGrades= sampleAnnotation.metamorphicGrades;
	NSString *owner=sampleAnnotation.owner;
	minerals= sampleAnnotation.minerals;
	NSString *status= sampleAnnotation.publicData;
	NSString *latitude=[[NSString alloc] initWithFormat:@"%g", sampleAnnotation.coordinate.latitude];
	NSString *longitude=[[NSString alloc] initWithFormat:@"%g", sampleAnnotation.coordinate.longitude];
	
	//for the sample name, separate the publication number if it exists and display it on the next line
	//publication number is all characters that follow the :
	NSString *sampleName=sampleAnnotation.name;
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
		outputText=[[NSString alloc] initWithFormat:@"Publication Number: None \nOwner: %@ \nLatitude: %@ \nLongitude: %@ \nPublic: %@ \n", owner, latitude,longitude, status];
	}
	else
	{
		outputText=[[NSString alloc] initWithFormat: @"Publication Number: %@ \nOwner: %@ \nLatitude: %@ \nLongitude: %@ \nPublic: %@ \n", pubNum, owner, latitude,longitude, status];
	}
	NSString *mineralOutput=[[NSString alloc] init];
	if([minerals count]==0)
	{
		mineralOutput= [[NSString alloc] initWithString:@"Minerals Present: None Listed\n"];
	}
	else
	{
		int y; 
		mineralOutput=[[NSString alloc] initWithString:@"Minerals Present:\n"];
		for(y=0; y<[minerals count]; y++)
		{
			NSString *temp=[minerals objectAtIndex:y];
			NSString *temp1=[[NSString alloc] initWithFormat:@"     %@\n", temp];
			mineralOutput=[mineralOutput stringByAppendingString:temp1];
		}
	}
	outputText= [outputText stringByAppendingString:mineralOutput];
	if([metamorphicGrades count]==0)
	{
		outputText=[outputText stringByAppendingString:@"Metamorphic Grades: None Listed"];
	}
	else
	{
		int y;
		outputText=[outputText stringByAppendingString:@"Metamorphic Grades: \n"];
		for(y=0; y<[metamorphicGrades count]; y++)
		{
			NSString *temp=[metamorphicGrades objectAtIndex:y];
			NSString *temp1=[[NSString alloc] initWithFormat:@"     %@\n", temp];
			outputText=[outputText stringByAppendingString:temp1];

		}
	}
		textView.text= outputText;

	CGRect rect= CGRectMake(200, 25 , 100, 100);
	self.navigationItem.title=[[NSString alloc] initWithFormat:@"Sample %@", sampleName];
}
//the following function gets called by the back button

-(void)makeToolbar{
	//make an array of the buttons to be added to the toolbar in the mapview
	buttons=[[NSMutableArray alloc] init];
	
	//initialize an object of commentDisplayController and analysisSummary so the counts for each can be obtained
	CommentDisplayController *commentController= [[CommentDisplayController alloc] initWithNibName:@"CommentDisplay" bundle:nil];
	[commentController setData:sampleID];
	self.commentDisplay= commentController;
	[commentController release];
	
	AnalysisSummary *analysisController = [[AnalysisSummary alloc] initWithNibName:@"ChemicalAnalysis" bundle:nil];
	[analysisController setData:sampleID];
	self.analysisSummary= analysisController;
	[analysisController release];
	
	
	NSString *subsampleCount= [analysisSummary getSubsampleCount];
	NSString *commentCount= [commentDisplay getCommentCount];
	NSString *subsampleTitle=[[NSString alloc] initWithFormat:@"%@ Subsamples", subsampleCount];
	NSString *commentTitle= [[NSString alloc] initWithFormat:@"%@ Comments", commentCount];
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
	[self get_thumbnails];
	NSString *buttonTitle=[[NSString alloc] initWithFormat:@"%@ images", imageCount];
	CGRect frame= CGRectMake(210, 5, 90, 30);
	imageButton=[UIButton buttonWithType:UIButtonTypeRoundedRect];
	imageButton.frame=frame;
	[imageButton setTitle:buttonTitle forState:UIControlStateNormal];
	[imageButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
	[self.view addSubview:imageButton];
	[imageButton addTarget:self action:@selector(imagesDisplay) forControlEvents:UIControlEventTouchUpInside];
	}
-(void)get_thumbnails
{
	sampleID= sampleAnnotation.id;
	NSString *urlString=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?thumbnails=%@", sampleID];
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
	
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/MetPetDB/searchResults.kml"];
	[fh writeData:imageResponse];
	NSString *dataReceived=[[NSString alloc] initWithData:imageResponse encoding:NSASCIIStringEncoding];
	NSXMLParser *subsampleParser= [[NSXMLParser alloc] initWithData:imageResponse];
	[subsampleParser setDelegate:self];
	[subsampleParser parse];
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
	AnalysisSummary *viewController = [[AnalysisSummary alloc] initWithNibName:@"ChemicalAnalysis" bundle:nil];
	//pass the analysis table the subsample ID so a list of subsamples can be obtainined
	[viewController setData:sampleID];
	
	self.analysisSummary= viewController;
	[viewController release];
	UIView *ControllersView = [analysisSummary view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:analysisSummary animated:NO];
}
-(void)viewComments
{
	CommentDisplayController *viewController= [[CommentDisplayController alloc] initWithNibName:@"CommentDisplay" bundle:nil];
	[viewController setData:sampleID];
	self.commentDisplay= viewController;
	[viewController release];
	UIView *ControllersView=[commentDisplay view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:commentDisplay animated:NO];
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
		ImageViewController *viewController=[[ImageViewController alloc] initWithNibName:@"ImageView" bundle:nil];
		[viewController setVars: remainingCount : 0:imagePaths :imageIDs:0]; 
		[viewController setData:sampleAnnotation :locations];
		self.imageViewController=viewController;
		[viewController release];
		UIView *ControllersView = [imageViewController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:imageViewController animated:NO];
	}
}

-(void) setData:(AnnotationObjects*)selectedSample:(NSMutableArray*)mylocations{
	sampleAnnotation=selectedSample;
	locations=mylocations;
}

-(void)backToMap{
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		[viewController setData:locations];
		self.mapController = viewController;
		[viewController release];
		UIView *ControllersView = [mapController view];
		[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:mapController animated:NO];
	//[self.navigationController popViewController:self animated:NO];
}
								  


- (void)dealloc {
    [super dealloc];
}


@end
