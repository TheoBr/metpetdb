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
#import "AnnotationObjects.h"
#import "MetPetDBAppDelegate.h"


@implementation MapController
@synthesize sampleinfo, details, selectedID, navBar, satelliteView, mapViewButton, narrowSearch, mapView, infoButton, homeButton;
@synthesize tableController, rockTypeController, sampleTableController, mainViewController, mapTypeController, currentSearchData;
@synthesize boxView, criteriaController, hybridView, selectedSample, indicator, radiusController, myLocationButton, viewNextButton;
@synthesize sampleName, rock, currentOwner, sampleID, description, currentStringValue, publicStatus, searchCriteria, sampleCategory;


-(void)viewDidLoad{
	
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init]; 
	count=0;
	dontAllow=0;
	
	//if(currentSearchData.region!=nil)
	//{
	[self makeNavBar];
	//}
	
	mapBool=TRUE;
	[MyCLController sharedInstance].delegate = self;
	//back button is replaced by a "home button"
	homeButton=[[UIBarButtonItem alloc] initWithTitle:@"Home" style:UIBarButtonItemStyleBordered target:self action:@selector(returnHome)];
	self.navigationItem.leftBarButtonItem=homeButton;
	viewWidth=self.view.bounds.size.width;
	viewHeight= self.view.bounds.size.height;
	
	singlePoints=[[NSMutableArray alloc] init];
	points= [[NSMutableArray alloc] init];
	multiplePoints=[[NSMutableArray alloc] init];
	buttonArray=[[NSMutableArray alloc] init];
	mapView=[[MKMapView alloc] initWithFrame:self.view.bounds];
	if(currentSearchData.zoomed==TRUE)
	{
		//the user has changed the zoom level of the map, call the setSpan2 function which uses the span in the currentSearchData object
		//even if the user has not changed the zoom level, zoomed will be true when going back to the map after all the samples have been loaded
		[self setSpan2];
	}
	else
	{
		[self setSpan];
	}
	if(currentSearchData.locationVisible==TRUE)
	{
		mapView.showsUserLocation=TRUE;
		
	//	CLLocation *centerCoordinate= [[CLLocation alloc] initWithLatitude:currentSearchData.centerCoordinate.latitude longitude:currentSearchData.centerCoordinate.latitude];
		
		CLLocationCoordinate2D centerCoord = [CurrentSearchData getCenterCoordinate];
		
		double lat = centerCoord.latitude;
		double longi = centerCoord.longitude;
		
		[mapView setCenterCoordinate:centerCoord animated:YES];
	}
	mapView.mapType=MKMapTypeStandard;
	mapView.delegate=self;
	
	
	
	//go through the current annotations to make sure there are none with the same coordinates
	//if they have coordinates, put them in an annotation indicating the number of samples at that point
	
	//this array will contain only unique annotations, no duplicates
	[self.view addSubview:mapView];
	//if the region is null, then we are searching using a coordinate and a radius and should draw a search box
	if(currentSearchData.region==nil)
	{
		//the following function creates the map annotations for the boundary points
		
		//the following functions adds those annotations to the map
		[self makeSearchBox];
		//[mapView addAnnotations:boundaryAnnotations];
	}
	if([mySamples count]!=0)
	{
		[mapView addAnnotations:mySamples];
	}
	//load the map with the street view initially and then hybrid and satellite after the user specifies a different type
	
	NSString *mapType = [CurrentSearchData getMapType];
	
	
	if((mapType != nil) && [mapType isEqualToString:@"map"])
	{
		mapView.mapType=MKMapTypeStandard;
	}
	else if((mapType != nil) && [mapType isEqualToString:@"hybrid"])
	{
		mapView.mapType=MKMapTypeHybrid;
	}
	else if((mapType != nil) && [mapType isEqualToString:@"satellite"])
	{
		mapView.mapType=MKMapTypeSatellite;
	}


	[self makeToolbar];
	if(indicator!=nil)
	{
		[indicator stopAnimating];
		
	}
	
	//if the user is logged in, get the username so it can later be passed to the server
	NSString *Uname= [[NSString alloc] init];
	
	KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
	NSData *usernameData = [keychain searchKeychainCopyMatching:@"Username"];
	if (usernameData) {
		Uname = [[NSString alloc] initWithData:usernameData
									  encoding:NSUTF8StringEncoding];
		[usernameData release];
	}
	
}
-(void)makeNavBar
{
	currentSampleCount=0;
	for(int p=0; p<[mySamples count]; p++)
	{
		uniqueSamples *group= [mySamples objectAtIndex:p];
		NSMutableArray *samples= group.samples;
		currentSampleCount+=[samples count];
	}
	NSString *titleString;
	if(totalCount==currentSampleCount)
	{
		titleString=[[NSString alloc] initWithFormat:@"Displaying all %d samples", totalCount];
		self.navigationItem.rightBarButtonItem=nil;
	}
	else
	{
		titleString=[[NSString alloc] initWithFormat:@"Displaying %d / %d samples", currentSampleCount, totalCount];
		//UIImage *buttonImage= [[UIImage alloc] initWithContentsOfFile:@"/Users/heatherbuletti/Documents/button-next.png"];
		//UIImage *buttonImage= [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://samana.cs.rpi.edu:8080/metpetwebtst/images/button-next.png"]]];
		UIImage *buttonImage= [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://samana.cs.rpi.edu/metpetweb/images/button-next.png"]]];
		UIButton *b= [[UIButton alloc] init];
		b.frame=CGRectMake(0, 0, 45, 32);
		[b setBackgroundImage:buttonImage forState:UIControlStateNormal];
		[b addTarget:self action:@selector(viewMoreSamples) forControlEvents:UIControlEventTouchUpInside];
		self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:b];
		
	}
	
	UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 30)];
	[label setFont:[UIFont boldSystemFontOfSize:12.0]];
	[label setBackgroundColor:[UIColor clearColor]];
	[label setTextColor:[UIColor whiteColor]];
	[label setText:titleString];
	self.navigationItem.titleView= label;
	[label release];
	
}
-(void)setSpan
{
	//calculate the current span of the map that needs to be displayed
	bool flag=FALSE;
	double averageLat=0;
	double averageLong=0;
	NSMutableArray *coords=[[NSMutableArray alloc] init];
	MKCoordinateRegion mapRegion;
	MKCoordinateSpan mapSpan;
	averageLat= (searchCriteria.maxLat+searchCriteria.minLat)/2;
	averageLong=(searchCriteria.maxLong+ searchCriteria.minLong)/2;
	CLLocationCoordinate2D temp;
	temp.latitude=averageLat;
	temp.longitude=averageLong;
	
	CLLocation *averageCoordinate= [[CLLocation alloc] initWithLatitude:averageLat longitude:averageLong];
	
	[currentSearchData setCenterCoordinate: averageCoordinate.coordinate];
	//mapRegion.center= currentSearchData.centerCoordinate;
	latitudeSpan= searchCriteria.maxLat - searchCriteria.minLat;
	longitudeSpan= searchCriteria.maxLong- searchCriteria.minLong;
	//mapSpan.latitudeDelta= latitudeSpan;//+.01;
	//mapSpan.longitudeDelta=longitudeSpan;//+.01;
//	mapRegion.span=mapSpan;
//	mapView.region= mapRegion;
	mapSpan = MKCoordinateSpanMake(latitudeSpan, longitudeSpan);
	mapRegion = MKCoordinateRegionMake(temp, mapSpan);  

	
}	
-(void)setSpan2
{
	MKCoordinateRegion mapRegion;
	MKCoordinateSpan mapSpan;
	mapRegion.center= currentSearchData.zoomedCenter;
	mapSpan.latitudeDelta=currentSearchData.latitudeSpan;
	mapSpan.longitudeDelta=currentSearchData.longitudeSpan;
	mapRegion.span=mapSpan;
	mapView.region=mapRegion;
	
}
-(void)makeSearchBox
{
	boundaryAnnotations= [[NSMutableArray alloc] init];
	//before making a pin, make sure the annotation does not have the same lat and long a sample
	//if it does, move the pin slightly
	maxLat= searchCriteria.maxLat;
	maxLong= searchCriteria.maxLong;
	minLat= searchCriteria.minLat;
	minLong= searchCriteria.minLong;
	NSString *maxLatitude=[[NSString alloc] initWithFormat:@"%f", maxLat];
	NSString *minLatitude=[[NSString alloc] initWithFormat:@"%f", minLat];
	NSString *maxLongitude= [[NSString alloc] initWithFormat:@"%f", maxLong];
	NSString *minLongitude=[[NSString alloc] initWithFormat:@"%f", minLong];
	points= [[NSMutableArray alloc] init];
	[points addObject:maxLatitude];
	[points addObject:minLatitude];
	[points addObject:maxLongitude];
	[points addObject:minLongitude];
	
	for(int w=0; w<[mySamples count]; w++)
	{
		uniqueSamples *tempSample= [mySamples objectAtIndex:w];
		if(tempSample.coordinate.latitude == maxLat)
		{
			maxLat+= .001;
		}
		if(tempSample.coordinate.latitude==minLat)
		{
			minLat-=.001;
		}
		if(tempSample.coordinate.longitude== maxLong)
		{
			maxLong+= .001;
		}
		if(tempSample.coordinate.longitude == minLong)
		{
			minLong-= .001;
		}
	}	
	for(int z=0; z< 4; z++)
	{
		uniqueSamples *newGroup=[uniqueSamples new];
		newGroup.title=@"Boundary Point";
		CLLocationCoordinate2D point;
		point.latitude= searchCriteria.maxLat;
		point.longitude= searchCriteria.maxLong;
		newGroup.coordinate=point;
		[boundaryAnnotations addObject:newGroup];
	}
}
-(void)viewSamplesAsList
{
	SampleTableController *viewController = [[SampleTableController alloc] initWithNibName:@"SampleTableView" bundle:nil];
	[viewController setData:mySamples:TRUE];
	[viewController setSamples:mySamples:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	//[viewController setCoordinate:myLocation:latitudeSpan:longitudeSpan: maxLat:minLat: maxLong: minLong];
	
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
	
	UIBarButtonItem *allSamplesButton=[[UIBarButtonItem alloc] initWithTitle:@"View Samples as List" style:UIBarButtonItemStyleBordered target:self action:@selector(viewSamplesAsList)];
	UIButton* infoButton = [UIButton buttonWithType:UIButtonTypeInfoLight];
	//UIImage *backgroundImage = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://samana.cs.rpi.edu:8080/metpetwebtst/images/my-location.png"]]];
	UIImage *backgroundImage = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:@"http://samana.cs.rpi.edu/metpetweb/images/my-location.png"]]];
	//myLocationButton=[[UIBarButtonItem alloc] initWithImage:backgroundImage style:UIBarButtonItemStylePlain target:self action:@selector(viewMe)];
	//UIButton *locButton= [[UIButton alloc] initWithFrame:CGRectMake(280, 3, 33, 33)];
	//[locButton setImage:backgroundImage forState:UIControlStateNormal];
	//UIBarButtonItem *barLocButton= [[UIBarButtonItem alloc] initWithCustomView:locButton];
	//[locButton addTarget:self action:@selector(viewMe) forControlEvents:UIControlEventTouchUpInside];
	
	UIBarButtonItem *infoBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:infoButton];
	[infoButton addTarget:self action:@selector(infoButtonPressed) forControlEvents:UIControlEventTouchUpInside];
	[buttons addObject:narrowSearch];
	[buttons addObject:allSamplesButton];
	[buttons addObject:infoBarButtonItem];
	//[buttons addObject:myLocationButton];
	
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
	//[viewController setBool];
	self.mainViewController=viewController;
	[viewController release];
	UIView *newView=[mainViewController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:mainViewController animated:NO];
}
-(void)infoButtonPressed
{
	currentSearchData.zoomedCenter= mapView.region.center;
	currentSearchData.latitudeSpan=mapView.region.span.latitudeDelta;
	currentSearchData.longitudeSpan=mapView.region.span.longitudeDelta;
	currentSearchData.zoomed=TRUE;
	MapTypeController *viewController= [[MapTypeController alloc] initWithNibName:@"MapTypeView" bundle:nil];
	self.mapTypeController= viewController;
	[viewController setSamples:mySamples:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	UIView *newView= [mapTypeController view];
	[self.view addSubview:newView];
	[self.navigationController pushViewController:mapTypeController animated:YES];
}

-(IBAction)narrowSearchResults{
	/*if([mySamples count]==0)
	 {
	 UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Samples." message:@"This search did not return any samples, so the criteria cannot be refined." delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
	 [alert show];
	 }
	 else
	 {*/
	SearchCriteriaController *viewController= [[SearchCriteriaController alloc] initWithNibName:@"SearchCriteriaSummary" bundle:nil];
	[viewController setData:mySamples:searchCriteria];
	//[viewController setCurrentSearchData:searchCriteria :myLocation :region];
	[viewController setCurrentSearchData:currentSearchData];
	self.criteriaController=viewController;
	[viewController release];
	UIView *ControllersView =[criteriaController view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:criteriaController animated:NO];
	//}
}
-(void)makeAnnotations:(uniqueSamples*)newSet
{
	[mapView addAnnotation:newSet];
	[mySamples addObject:newSet];
}


-(void) showSampleInfo{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
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
	currentSearchData.zoomedCenter= mapView.region.center;
	currentSearchData.latitudeSpan=mapView.region.span.latitudeDelta;
	currentSearchData.longitudeSpan=mapView.region.span.longitudeDelta;
	currentSearchData.zoomed=TRUE;
	SampleInfoController *viewController = [[SampleInfoController alloc] initWithNibName:@"SampleInfo" bundle:nil];
	[viewController setSamples:mySamples:selectedSample:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
	self.sampleinfo = viewController;
	[viewController release];
	UIView *ControllersView=[sampleinfo view];
	[self.view addSubview:ControllersView];
	[self.navigationController pushViewController:sampleinfo animated:NO];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
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
	SampleTableController *viewController = [[SampleTableController alloc] initWithNibName:@"SampleTableView" bundle:nil];
	[viewController setData:selectedArray:FALSE];
	[viewController setSamples:mySamples:searchCriteria];
	[viewController setCurrentSearchData:currentSearchData];
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
	if([[annotation title] isEqualToString:@"Boundary Point"])
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


//the next few function call the pagination query

-(void)viewMoreSamples
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	
	PostRequest *post= [[PostRequest alloc] init];
	[post setData:[currentSearchData minerals] :[currentSearchData rockTypes] :[currentSearchData owners] :[currentSearchData metamorphicGrades] :currentSearchData.currentPublicStatus :
	 currentSearchData.region:[currentSearchData originalCoordinates]:currentSampleCount:@"false"];
	myReturn=[post buildPostString];
	
	NSURLResponse *response;
	xmlParser *x= [[xmlParser alloc] init];
	NSMutableArray *nextSamples=[[NSMutableArray alloc] init];
	nextSamples= [x parseSamples:myReturn];
	[x release];	
	
	//add the new samples that are returned to the samples currently being displayed
	for(int k=0; k<[nextSamples count]; k++)
	{
		[mySamples addObject:[nextSamples objectAtIndex:k]];
		
	}
	[self viewDidLoad];
	
	[self makeNavBar];
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
	
}



//the remaining functions load data into the map view controller

//this function sets variables in the map controller to contain all the information about the samples in the location range
-(void)setData:(NSMutableArray*)mylocations:(CriteriaSummary*)criteria
{
	totalCount=criteria.totalCount;
	mySamples=mylocations;
	//the searchCriteria object represents a summary of all the attributes of all objects that were displayed in the current search.
	//this could include rock type, minerals, metamorphic grades, and owners
	searchCriteria=criteria;
}

//this array must store 4 arrays with search criteria to be displayed in the search criteria summary
//the data will be passed in from the rocktypeController, metamorphicgradecontroller, mineralscontroller, and the ownerController
//based on what the user chooses as search criteria
-(void)setCurrentSearchData:(CurrentSearchData*)data
{
	currentSearchData= data;
	
}


-(void)setIndicator:(UIActivityIndicatorView*)activity
{
	indicator= activity;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}

- (void)dealloc {
	[mapView release];
	[singlePoints release];
	[mySamples release];
	[sampleinfo release];
	[mainViewController release];
	[mapTypeController release];
	[details release];
	[selectedID release];
	[buttonArray release];
	[toolbar release];
	[buttons release];
	[navBar release];
	[narrowSearch release];
	[satelliteView release];
	[hybridView release];
	[mapViewButton release];
	[homeButton release];
	[viewNextButton release];
	[infoButton release];
	[tableController release];
	[myMinerals release];
	[myRockTypes release];
	[myMetamorphicGrades release];
	[rockTypeController release];
	[sampleTableController release];
	[multiplePoints release];
	[multipleButtons release]; 
	[boxView release];
	[criteriaController release];
	
	[selectedSample release];
	[boundaryAnnotations release];
	[points release];
	[sampleCategory release];
	[indicator release];
	[radiusController release];
	
	[timer release];
	[user release];
	[myLocationButton release];
	[myReturn release];
	[currentStringValue release];
	[description release];
	[sampleName release];
	[newAnnotation release];
	[newSet release];
	[publicStatus release];
	[sampleID release];
	[addedLocations release];
	[currMetGrades release];
	[currMinerals release];
	[mins release];
	[metGrades release];
	[currPublicStatus release];
	[currOwner release];
	[currRockTypes release];
	[currentOwner release];
	[rock release];
	[searchCriteria release];
    [super dealloc];
}


@end
