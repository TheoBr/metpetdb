//
//  MapController.m
//  Location
//
//  Created by Heather Buletti on 5/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapController.h"
#import "TableController.h"
#import "uniqueSamples.h"
#import "SearchCriteriaController.h"


@implementation MapController
@synthesize sampleinfo, details, selectedID, navBar, satelliteView, mapViewButton, narrowSearch, mapView, infoButton, homeButton;
@synthesize tableController, rockTypeController, sampleTableController, mainViewController, mapTypeController;
@synthesize boxView, criteriaController, region, hybridView, selectedSample;


-(void)viewDidLoad{
	
	viewWidth=self.view.bounds.size.width;
	viewHeight= self.view.bounds.size.height;
	
	singlePoints=[[NSMutableArray alloc] init];
	multiplePoints=[[NSMutableArray alloc] init];
	buttonArray=[[NSMutableArray alloc] init];
	mapView=[[MKMapView alloc] initWithFrame:self.view.bounds];
	mapView.showsUserLocation=TRUE;
	mapView.mapType=MKMapTypeStandard;
	mapView.delegate=self;
	
	MKCoordinateRegion mapRegion;
	MKCoordinateSpan mapSpan;
	
	mapSpan.latitudeDelta= latitudeSpan+.05;
	mapSpan.longitudeDelta=longitudeSpan+.05;
	mapRegion.span=mapSpan;
	

	mapView.region= mapRegion;
	[mapView setCenterCoordinate:myLocation animated:YES];
	
	//go through the current annotations to make sure there are none with the same coordinates
//if they have coordinates, put them in an annotation indicating the number of samples at that point
	int z;
	int q;
	int count=0;
	//this array will contain only unique annotations, no duplicates
	NSMutableArray *uniqueLocations=[[NSMutableArray alloc] init];
	//add one location to the array so it is not empty

	[self.view addSubview:mapView];
	UIView *clearView=[[UIView alloc] init];
	clearView.backgroundColor=[UIColor clearColor];
	
	//if the region is null, then we are searching using a coordinate and a radius and should draw a search box
	//if(region==nil)
	//{
		//the following function creates the map annotations for the boundary points
		[self makeSearchBox];
		//the following functions adds those annotations to the map
		[mapView addAnnotations:boundaryAnnotations];
	//}
	[mapView addAnnotations:mySamples];
	
	if([mapType isEqualToString:@"map"])
	{
		[self switchToMap];
	}
	else if([mapType isEqualToString:@"hybrid"])
	{
		[self switchToHybrid];
	}
	else if([mapType isEqualToString:@"satellite"])
	{
		[self switchToSatellite];
	}
	[self makeToolbar];
	//the following function makes 4 pins to indicate the boundaries of the search box
	
	
	//make a clear view with a box that shows the region of samples that was specified.
	
/*	double totalSquarePixels=(viewWidth)*(viewHeight);
	double totalSquareDegrees=(latitudeSpan+1)*(longitudeSpan+1);
	double degreesInBox=(latitudeSpan)*(longitudeSpan);
	double pixelsInBox=(totalSquarePixels*degreesInBox)/(totalSquareDegrees);
	
	//find the dimension in pixels of each side of the box by taking the square root of the totalSquareDegrees in the box
	//the width in pixels of the mapView screen is 320, and the width is 377 instead of because of the toolbar
	double side= sqrt(pixelsInBox);
	//find the top right corner of the box so it can be displayed in the mapView
	double xCoordinate= (viewWidth-side)/2;
	double yCoordinate=(viewHeight-side)/2;
	
	boxView=[[UIView alloc] init];
	boxView.backgroundColor=[UIColor clearColor];
	CGRect frame=CGRectMake(xCoordinate, yCoordinate, side, side);
	UILabel *label=[[UILabel alloc] initWithFrame:frame];
	UIColor *boxColor=[[UIColor alloc] initWithHue:.7 saturation:.1 brightness:.1 alpha:.33];
	label.backgroundColor=boxColor;
	[boxView addSubview:label];
	[self.view addSubview:boxView];
	[mapView becomeFirstResponder];*/
}
-(void)makeSearchBox
{
	boundaryAnnotations= [[NSMutableArray alloc] init];
	//before making a pin, make sure the annotation does not have the same lat and long a sample
	//if it does, move the pin slightly
	for(int w=0; w<[mySamples count]; w++)
	{
		uniqueSamples *tempSample= [mySamples objectAtIndex:w];
		if(tempSample.coordinate.latitude == maxLat)
		{
			maxLat+= .0001;
		}
		if(tempSample.coordinate.latitude== minLat)
		{
			minLat-=.0001;
		}
		if(tempSample.coordinate.longitude== maxLong)
		{
			maxLong+= .0001;
		}
		if(tempSample.coordinate.longitude == minLong)
		{
			minLong-= .0001;
		}
	}
		for(int z=0; z< 4; z++)
	{
		uniqueSamples *newGroup=[uniqueSamples new];
		newGroup.title=@"Boundary Point";
		CLLocationCoordinate2D point;
		if(z==0)
		{
			point.latitude=maxLat;
			point.longitude=maxLong;
		}
		else if(z==1)
		{
			point.latitude=maxLat;
			point.longitude=minLong;
		}
		else if(z==2)
		{
			point.latitude=minLat;
			point.longitude=minLong;
		}
		else if(z==3)
		{
			point.latitude=minLat;
			point.longitude=maxLong;
		}
		newGroup.coordinate=point;
		[boundaryAnnotations addObject:newGroup];
	}
}
-(void)viewSamplesAsList
{
	bool sampleFlag=TRUE;
	SampleTableController *viewController = [[SampleTableController alloc] initWithNibName:@"SampleTableView" bundle:nil];
	[viewController setData:mySamples:sampleFlag];
	self.sampleTableController= viewController;
	[viewController release];
	UIView *ControllersView=[sampleTableController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleTableController animated:NO];	
}

-(void)touchesBegan:(NSSet *)touches withEvent: (UIEvent *)event
{
	CGRect frame=CGRectMake(0, 0, viewWidth, 50);
	UILabel *label=[[UILabel alloc] initWithFrame:frame];
	label.backgroundColor=[UIColor redColor];
	[mapView addSubview:label];
}
	
-(void)makeToolbar{
	//make an array of the buttons to be added to the toolbar in the mapview
	buttons=[[NSMutableArray alloc] init];
	multipleButtons=[[NSMutableArray alloc] init];
	narrowSearch = [ [ UIBarButtonItem alloc ] initWithTitle:@"Refine Search" style: UIBarButtonItemStyleBordered target: self action: @selector(narrowSearchResults)];
	hybridView= [[UIBarButtonItem alloc] initWithTitle:@"Hybrid" style:UIBarButtonItemStyleBordered target:self action:@selector(switchToHybrid)];
	satelliteView= [[UIBarButtonItem alloc] initWithTitle:@"Satellite" style:UIBarButtonItemStyleBordered target:self action:@selector(switchToSatellite)];
	mapViewButton= [[UIBarButtonItem alloc] initWithTitle:@"Street" style:UIBarButtonItemStyleBordered target:self action:@selector(switchToMap)];
	homeButton=[[UIBarButtonItem alloc] initWithTitle:@"Home" style:UIBarButtonItemStyleBordered target:self action:@selector(returnHome)];
	UIBarButtonItem *allSamplesButton=[[UIBarButtonItem alloc] initWithTitle:@"View Samples as List" style:UIBarButtonItemStyleBordered target:self action:@selector(viewSamplesAsList)];
	UIButton* infoButton = [UIButton buttonWithType:UIButtonTypeInfoLight];
	[infoButton addTarget:self action:@selector(infoButtonPressed) forControlEvents:UIControlEventTouchUpInside];
	UIBarButtonItem *infoBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:infoButton];
	self.navigationItem.rightBarButtonItem = infoBarButtonItem;
	[buttons addObject:narrowSearch];
	[buttons addObject:allSamplesButton];
	[buttons addObject:homeButton];
	
	CGRect toolBarFrame= CGRectMake (0, 377, viewWidth, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[mapView addSubview:toolbar];
}
-(void)returnHome
{
	MainViewController *viewController= [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	self.mainViewController=viewController;
	[viewController release];
	UIView *newView=[mainViewController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:mainViewController animated:NO];
}
-(void)infoButtonPressed
{
	MKCoordinateRegion currentRegion= mapView.region;
	MKCoordinateSpan currentSpan= currentRegion.span;
	latitudeSpan= currentSpan.latitudeDelta;
	longitudeSpan= currentSpan.longitudeDelta;
	
	MapTypeController *viewController= [[MapTypeController alloc] initWithNibName:@"MapTypeView" bundle:nil];
	self.mapTypeController= viewController;
	[viewController setSamples:mySamples:originalData];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myLocation];
	[viewController setCoordinate:myLocation:latitudeSpan:longitudeSpan: maxLat:minLat: maxLong: minLong];
	UIView *newView= [mapTypeController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:mapTypeController animated:YES];
}

-(IBAction)narrowSearchResults{
	
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:originalData:mySamples:mapType:points];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myLocation];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
}

-(void)switchToHybrid
{
	mapView.mapType=MKMapTypeHybrid;
}
	
//this function will switch the current map view to satellite view
-(void)switchToSatellite{
	mapView.mapType=MKMapTypeSatellite;
}
//if the current map is in satellite or hybrid, this function switches it back to map view
//if the current map is already in street view, this function has no effect
-(void)switchToMap{
	mapView.mapType=MKMapTypeStandard;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}
-(void) showSampleInfo{
	int x;
	int size= [buttonArray count];
	NSString *id=[[NSString alloc] init];
	AnnotationObjects *selectedSample=[AnnotationObjects new];
	//go through the array containing all the annotation points to determine which one was chosen
	//and pass its information to the next veiw controller
	for(x=0; x<[buttonArray count]; x++)
	{
		UIButton *tempButton= [[UIButton alloc] init];
		tempButton=[buttonArray objectAtIndex:x];
		if(tempButton.highlighted){
			selectedSample=[singlePoints objectAtIndex:x];
		}
		
	}
	SampleInfoController *viewController = [[SampleInfoController alloc] initWithNibName:@"SampleInfo" bundle:nil];
	[viewController setData:selectedSample:mySamples];
	self.sampleinfo = viewController;
	[viewController release];
	UIView *ControllersView=[sampleinfo view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleinfo animated:NO];
}
-(void) sampleTable
{
	int y;
	uniqueSamples *selectedLocation=[uniqueSamples new];
	NSMutableArray *selectedArray=[[NSMutableArray alloc] init]; //represents the array of samples that are all located at the same point
	//determine which button was pressed and display the corresponding samples from the multiple samples array
	for(y=0; y<[multipleButtons count]; y++)
	{
		selectedLocation=[multiplePoints objectAtIndex:y];
		UIButton *tempButton=[[UIButton alloc] init];
		tempButton=[multipleButtons objectAtIndex:y];
		if(tempButton.highlighted)
		{
			selectedArray=selectedLocation.samples;
		}
	}
	bool sampleFlag=FALSE;
	SampleTableController *viewController = [[SampleTableController alloc] initWithNibName:@"SampleTableView" bundle:nil];
	[viewController setData:selectedArray:FALSE];
	self.sampleTableController= viewController;
	[viewController release];
	UIView *ControllersView=[sampleTableController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleTableController animated:NO];
}

- (MKAnnotationView *) mapView: (MKMapView *) mapView viewForAnnotation: (id<MKAnnotation>) annotation {
	MKPinAnnotationView *pin = (MKPinAnnotationView *) [mapView dequeueReusableAnnotationViewWithIdentifier: [annotation title]];
	if (pin == nil)
	{
		pin = [[[MKPinAnnotationView alloc] initWithAnnotation: annotation reuseIdentifier: [annotation title]] autorelease];
	}
	else
	{
		pin.annotation = annotation;
	}
	if([[annotation title] isEqualToString:@"Current Location"])
	{
		pin.pinColor = MKPinAnnotationColorRed;
		pin.animatesDrop = YES;
		pin.canShowCallout = TRUE;
		
		return pin;
	}
	else if([[annotation title] isEqualToString:@"Boundary Point"])
	{
				
		pin.pinColor= MKPinAnnotationColorPurple;
		pin.animatesDrop=YES;
		pin.canShowCallout= TRUE;
		return pin;
	}
	else
	{
		pin.pinColor = MKPinAnnotationColorGreen;
		pin.animatesDrop = YES;
		pin.canShowCallout = TRUE;
		//AnnotationObjects *selectedSample= [AnnotationObjects new];
		selectedSample= [uniqueSamples new];
		selectedSample= annotation;
		AnnotationObjects *aSample=[AnnotationObjects new];
		aSample=[selectedSample.samples objectAtIndex:0];
		details= [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
		if([selectedSample.id isEqualToString: @"multiple samples"])
		{
			[details addTarget:self action:@selector(sampleTable) forControlEvents:UIControlEventTouchUpInside];
			[multiplePoints addObject:selectedSample];
			[multipleButtons addObject:details]; 
		}
		else
		{
			selectedSample.title= aSample.name;
			NSString *selected=[[NSString alloc] initWithString:selectedSample.id];
			//[singlePoints addObject:selectedSample];
			[singlePoints addObject:aSample];
			[details addTarget:self action:@selector(showSampleInfo) forControlEvents:UIControlEventTouchUpInside];
			//put all the buttons in an array and make a corresponding array that hold the ids at the same index for the single sample locations
			[buttonArray addObject:details];
		}			
		
		
		pin.rightCalloutAccessoryView=details;
		return pin;
	}
}

//this function sets variables in the map controller to contain all the information about the samples in the location range
-(void)setData:(NSMutableArray*)original: (NSMutableArray*)mylocations
{
	mySamples=mylocations;
	originalData=original;
}
	
-(void)setCoordinate:(CLLocationCoordinate2D)center:(double)latSpan:(double) longSpan:(double)upperLat:(double)lowerLat:(double)upperLong:(double)lowerLong
{
	//this function gets called the first time the map loads and it provides the center coordinate and zoom for the initial map view
	myLocation=center;
	//the span is a value in degrees that is used to set the zoom on the map view when it first appears
	latitudeSpan= latSpan;
	longitudeSpan= longSpan;
	maxLat= upperLat;
	minLat= lowerLat;
	maxLong= upperLong;
	minLong= lowerLong;
}
//this array must store 4 arrays with search criteria to be displayed in the search criteria summary
//the data will be passed in from the rocktypeController, metamorphicgradecontroller, mineralscontroller, and the publicstatuscontroller
-(void)setCurrentSearchData:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metGrades:(NSMutableArray*)public:(NSString*)aRegion:(CLLocationCoordinate2D)coord
{
	currentRockTypes=rocks;
	currentMinerals=mins;
	currentMetamorphicGrades=metGrades;
	currentPublicStatus=public;
	myLocation=coord;
	region=aRegion;
}
-(void)setRegion:(NSString*)selectedRegion
{
	region=selectedRegion;
}
//the type of the map is changed depending on which "type" is passed in
-(void)setType:(NSString*)typeString
{
	mapType=typeString;
}

- (void)dealloc {
    [super dealloc];
}


@end
