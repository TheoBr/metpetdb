//
//  ImageViewController.m
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "ImageViewController.h"
#import "MetPetDBAppDelegate.h"
#import "constants.h"

#define HORIZ_SWIPE_DRAG_MIN  12
#define VERT_SWIPE_DRAG_MAX    4

@implementation ImageViewController
@synthesize singleImageView, currentStringValue, sampleID, imageViewController;
@synthesize fullPath, nextButton, sampleController, sampleAnnotation, pageControl, progress, backButton;
@synthesize searchCriteria;



-(void)viewDidLoad
{	
	//backButton=[[UIBarButtonItem alloc] initWithTitle:@"Back To Sample" style:UIBarButtonItemStyleBordered target:self action:@selector(backToSample)];
	//self.navigationItem.leftBarButtonItem=backButton;
	double num= [imagePaths count]/9.0;
	int numPages=ceil(num);
	if([imagePaths count] <=9)
	{
		pageControl.numberOfPages=1;
	}
	else
	{
		pageControl.numberOfPages=numPages;
	}
	pageControl.currentPage=pagesDisplayed;
	pagesDisplayed++;
	
	[pageControl addTarget:self action:@selector(turnPage) forControlEvents:UIControlEventValueChanged];
	
	if(pagesDisplayed>1)//after the first page where the back button goes back to the sample, we dont need a back button
	{
		[self.navigationItem setHidesBackButton:YES];
	}
	
	navBarItems=[[NSMutableArray alloc] init];
	buttonArray = [[NSMutableArray alloc] init];
	
	int x=0;
	width=22;
	height=40;
	int max;
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
/*	CGRect toolBarFrame= CGRectMake (0, 377, self.view.bounds.size.width, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	[toolbar setBarStyle:1];*/
	
	[self.navigationController setToolbarHidden:NO animated:YES];
	[self.navigationController.toolbar setBarStyle:UIBarStyleBlack];	
	[self setToolbarItems:buttons animated:YES];

	
	if(remainingCount>9)
	{
		max=9;
		//if there are more than 9 images some need to be displayed on the following pages, make a button
		//UIBarButtonItem *nextButton=[[UIBarButtonItem alloc] initWithTitle:@"Next Page" style:UIBarButtonItemStyleBordered target:self action:@selector(viewNextPage)];
		//self.navigationItem.rightBarButtonItem= nextButton;
		remainingCount-=9;
	}
	else
	{
		max=remainingCount;
		remainingCount=0;
		//since all the thumbnails views will not be filled, make sure there are no remaining images in them
		
	}
	for(x=imageCount; x<(imageCount+max); x++)
	{
		fullPath=[[NSString alloc] initWithFormat:@"%@/image/?checksum=%@", RootURL, [imagePaths objectAtIndex:x]];
		
		UIImage *image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:fullPath]]];
		CGRect rect=CGRectMake(width, height, 64.0, 64.0);
		UIButton *button=[[UIButton alloc] initWithFrame:rect];
		[button setImage:image forState:UIControlStateNormal];
		[button addTarget:self action:@selector(enlarge) forControlEvents:UIControlEventTouchUpInside];
		[self.view addSubview:button];
		width+=106;
		if(width>=320)
		{
			height+=120;
			width=22;
		}
		//add all the buttons to an array 
		[buttonArray addObject:button];
	}
	imageCount+=max;
	if(imageCount >=10)
	{
		//UIBarButtonItem *sampleButton=[[UIBarButtonItem alloc] initWithTitle:@"Back to Sample" style:UIBarButtonItemStyleBordered target:self action:@selector(backToSample)];
		/*UIBarButtonItem *prevButton=[[UIBarButtonItem alloc] initWithTitle:@"Previous Page" style:UIBarButtonItemStyleBordered target:self action: @selector(previousPage)];
		 [buttons addObject:prevButton];
		 toolbar.items=buttons;	
		 [self.view addSubview:toolbar];	*/
	}
	if(remainingCount>0)
	{
	}
	
}
- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
	
    UITouch *touch = [touches anyObject];
	
	startTouchPosition = [touch locationInView:self.view];
	
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event

{
	
    UITouch *touch = [touches anyObject];
	
    CGPoint currentTouchPosition = [touch locationInView:self.view];
	
    // If the swipe tracks correctly.
	
    if (fabsf(startTouchPosition.x - currentTouchPosition.x) >= HORIZ_SWIPE_DRAG_MIN)		
    {
		
        // It appears to be a swipe.
		
        if (startTouchPosition.x < currentTouchPosition.x) //leftward swipe, go to previous page of images
		{
			[self previousPage];
		}
        else //rightward swipe, go to next page of images
		{
			if(remainingCount >0)
			{
				[self viewNextPage];
			}
		}
		
    }
}

-(void)turnPage
{
	if(pageControl.currentPage > pagesDisplayed-1)
	{
		[self viewNextPage];
	}
	else if(pageControl.currentPage < pagesDisplayed-1)
	{
		[self previousPage];
	}
}

-(void)viewNextPage
{
	//if the current page on the page control is greater than the number of pages displayed, go to the next page
	//otherwise, view the previous page
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	ImageViewController *viewController=[[ImageViewController alloc] initWithNibName:@"ImageView" bundle:nil];
	[viewController setVars:remainingCount:imageCount:imagePaths:imageIDs:pagesDisplayed];
	[viewController setSamples:sampleAnnotation:locations:searchCriteria];
	//[viewController setCurrentSearchData:currentSearchData];
	
	self.imageViewController=viewController;
	UIView *newView= [imageViewController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:imageViewController animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)previousPage
{
	[self.navigationController popViewControllerAnimated:NO];
}

-(void)backToSample
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	SampleInfoController *viewController = [[SampleInfoController alloc] initWithNibName:@"SampleInfo" bundle:nil];
	//[viewController setData:selectedSample:mySamples];
	[viewController setSamples:locations:sampleAnnotation:searchCriteria];
//	[viewController setCurrentSearchData:currentSearchData];
	self.sampleController = viewController;
	[viewController release];
	UIView *ControllersView=[sampleController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleController animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}


-(void)enlarge
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	int z;
	//loop through all the buttons and if one is highlighted, load a view with the selecte image enlarged
	for(z=0; z<[buttonArray count]; z++)
	{
		index= ((pagesDisplayed-1)*9) + z;
		UIButton *selected=[buttonArray objectAtIndex:z];
		//the corresponding object in the imageIDs array will be the id of the image
		//selectedImage= [imageIDs objectAtIndex:index];
		if(selected.highlighted)
		{
			SingleImageView *viewController=[[SingleImageView alloc] initWithNibName:@"SingleImageView" bundle:nil];
			[viewController setData:index:imageIDs];
			self.singleImageView=viewController;
			[viewController release];
			UIView *ControllersView= [singleImageView view];
			[self.view addSubview:ControllersView];
			[self.navigationController pushViewController:singleImageView animated:NO];
		}
	}
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}
-(void)setVars:(int) remaining:(int)count:(NSMutableArray*)paths:(NSMutableArray*) IDs:(int)pages
{
	pagesDisplayed=pages;
	remainingCount=remaining;
	imageCount=count;
	imagePaths=paths;
	imageIDs=IDs;
}
//this is the data that needs to be preserved to return to the sample information page instead of having to scroll through all the image pages first
-(void) setSamples:(AnnotationObjects*)selectedSample:(NSMutableArray*)mylocations:(CriteriaSummary*)criteria
{
	sampleAnnotation=selectedSample; 
	locations=mylocations; 
	searchCriteria=criteria;
}
/*-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData=data;
	
}*/
/*-(void)setCoordinate:(CLLocationCoordinate2D)center:(double)latSpan:(double) longSpan:(double)latmax:(double)latmin:(double)longmax:(double)longmin
 {
 //this function gets called the first time the map loads and it provides the center coordinate and zoom for the initial map view
 myLocation=center;
 //the span is a value in degrees that is used to set the zoom on the map view when it first appears
 latitudeSpan= latSpan; 
 longitudeSpan= longSpan;    
 maxLat=latmax;
 minLat= latmin;
 maxLong= longmax;
 minLong= longmin;
 }*/



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
	/*[singleImageView release];
	 [sampleController release];
	 [buttonArray release];
	 [currentStringValue release];
	 [imageIDs release];
	 [imagePaths release];
	 [navBarItems release];
	 [imageResponse release];
	 [sampleID release];
	 [imageViewController release];
	 [fullPath release];
	 [nextButton release];
	 [toolbar release];
	 [sampleAnnotation release];
	 [locations release];
	 [pageControl release];
	 [progress release];
	 [backButton release];
	 [samples release];
	 [super dealloc];*/
}


@end
