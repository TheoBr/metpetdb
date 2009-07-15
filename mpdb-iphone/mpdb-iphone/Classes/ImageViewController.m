//
//  ImageViewController.m
//  Location
//
//  Created by Heather Buletti on 6/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "ImageViewController.h"


@implementation ImageViewController
@synthesize singleImageView, currentStringValue, sampleID, imageViewController;
@synthesize fullPath, nextButton, sampleController, sampleAnnotation;



-(void)viewDidLoad
{
	navBarItems=[[NSMutableArray alloc] init];
	buttonArray = [[NSMutableArray alloc] init];
	pagesDisplayed++;
	int x=0;
	width=22;
	height=40;
	int max;
	NSMutableArray *buttons=[[NSMutableArray alloc] init];
	CGRect toolBarFrame= CGRectMake (0, 377, self.view.bounds.size.width, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	[toolbar setBarStyle:1];
	
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
		fullPath=[[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb//image/?checksum=%@",[imagePaths objectAtIndex:x]];
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
		
		//if there are more than 9 images some need to be displayed on the following pages, make a button
		
		nextButton=[[UIBarButtonItem alloc] initWithTitle:@"Next Page" style:UIBarButtonItemStyleBordered target:self action:@selector(viewNextPage)];
		self.navigationItem.rightBarButtonItem= nextButton;
		//[buttons addObject:nextButton];
		//toolbar.items= buttons;
		//[self.view addSubview:toolbar];
	}
	
}


-(void)viewNextPage
{
	ImageViewController *viewController=[[ImageViewController alloc] initWithNibName:@"ImageView" bundle:nil];
	[viewController setVars:remainingCount:imageCount:imagePaths:imageIDs:pagesDisplayed];
	[viewController setData:sampleAnnotation :locations];
	self.imageViewController=viewController;
	UIView *newView= [imageViewController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:imageViewController animated:NO];
}
-(void)previousPage
{
	pagesDisplayed-=2;
	remainingCount= [imagePaths count] -((pagesDisplayed)*(9));
	imageCount-=18;
	//pagesDisplayed--;
	
	ImageViewController *viewController=[[ImageViewController alloc] initWithNibName:@"ImageView" bundle:nil];
	[viewController setVars:remainingCount :imageCount :imagePaths :imageIDs :pagesDisplayed];
	[viewController setData: sampleAnnotation :locations];
	self.imageViewController= viewController;
	UIView *newView= [imageViewController view];
	[self.view addSubview:newView];
}
	
-(void)enlarge
{
	int z;
	//loop through all the buttons and if one is highlighted, load a view with the selecte image enlarged
	for(z=0; z<[buttonArray count]; z++)
	{
		index= ((pagesDisplayed-1)*9) + z;
		UIButton *selected=[buttonArray objectAtIndex:z];
		//the corresponding object in the imageIDs array will be the id of the image
		selectedImage= [imageIDs objectAtIndex:index];
		if(selected.highlighted)
		{
			SingleImageView *viewController=[[SingleImageView alloc] initWithNibName:@"SingleImageView" bundle:nil];
			[viewController setData:selectedImage];
			self.singleImageView=viewController;
			[viewController release];
			UIView *ControllersView= [singleImageView view];
			[self.view addSubview:ControllersView];
			[self.navigationController pushViewController:singleImageView animated:NO];
		}
	}
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
-(void) setData:(AnnotationObjects*)selectedSample:(NSMutableArray*)mylocations{
	sampleAnnotation=selectedSample; 
	locations=mylocations; 
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
