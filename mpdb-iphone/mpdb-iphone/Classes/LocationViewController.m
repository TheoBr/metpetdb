//
//  untitled.m
//  Location
//
//  Created by Heather Buletti on 5/14/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationViewController.h"
#import "MainViewController.h"
#import "MapController.h"
#import "uniqueSamples.h"


@implementation LocationViewController
@synthesize  toplabel, sampleButton, toolbar, latitude, longitude, mylocationCoordinate, mylat, currentStringValue, publicStatus, currentRockType;
@synthesize sampleName, sampleID, mapController, regionName;


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}
-(void)viewDidLoad{
	toolbar.barStyle=UIBarStyleBlack;
	//initialize all the arrays used in this controller
	regions = [[NSMutableArray alloc] init];
	locations=[[NSMutableArray alloc] init];
	minerals=[[NSMutableArray alloc] init];
	metamorphicGrades=[[NSMutableArray alloc] init];
	rockTypes=[[NSMutableArray alloc] init];
	mineralFlag=FALSE;
	metamorphicFlag=FALSE;
	rockFlag=FALSE;
	nameFlag=TRUE;
	uniqueFlag=TRUE;
	
	
	locationFlag=TRUE;
	[self getLocationInfo];
	//sort the array of regions that is returned
	locationFlag=FALSE;
	regionSelector.showsSelectionIndicator=YES;
	regionSelector.dataSource = self;
	regionSelector.delegate=self;
	
	
	

}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)regionSelector{
	
	return 1;
}
- (NSInteger)pickerView:(UIPickerView *)regionSelector numberOfRowsInComponent:(NSInteger)component {
	return [regions count];
}
- (NSString *)pickerView:(UIPickerView *)regionSelector titleForRow:(NSInteger)row forComponent:(NSInteger)component {
	
	return [regions objectAtIndex:row];
}
- (void)pickerView:(UIPickerView *)regionSelector didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
	
	selectedRegion= [regions objectAtIndex:row];
}


//This function is called when the search button is pressed and it loads the MapView
-(IBAction)loadMap:(id)sender{
	if(selectedRegion==nil)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Select a Region" message:@"Move the scroll wheel to select a region." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		locations=[[NSMutableArray alloc] init];
		//remove any spaces from the name of the selected region
		int t;
		bool added=FALSE;
		for(t=0; t<[selectedRegion length]; t++)
		{
			if(added==FALSE)
			{
				NSString *tempChar=[[NSString alloc] initWithFormat:@"%c",[selectedRegion characterAtIndex:t]];
				regionName=[[NSString alloc] initWithString:tempChar];
				added=TRUE;
			}
			else
			{
				if([selectedRegion characterAtIndex:t]!=' ')
				{
					NSString *tempChar=[[NSString alloc] initWithFormat:@"%c",[selectedRegion characterAtIndex:t]];
					regionName=[regionName stringByAppendingString:tempChar];
				}
				else //a space has been found, replace it with %20
				{
					regionName=[regionName stringByAppendingString:@"%20"];
				}
			}
		}
		
		[self getSamples];
	
		
	//before the map is loaded, find the center coordinate and span for the zoom
	//to find the center, average all the latitudes and then all the longitudes and that will be the coordinate
	int p,q;
	bool flag= FALSE;
	//place all the latitudes and longitudes in an array and then use all the numbers to take obtain the average lat and long which will be the center coordinate
	NSMutableArray *lats=[[NSMutableArray alloc] init];
	NSMutableArray *longs=[[NSMutableArray alloc] init];
	NSMutableArray *coords=[[NSMutableArray alloc] init];
	double averageLat=0;
	double averageLong=0;
	double maxLat, maxLong, minLat, minLong;
	for(p=0; p<[locations count]; p++)
	{
		uniqueSamples *sampleGroup=[uniqueSamples new];
		sampleGroup=[locations objectAtIndex:p];
		[coords addObject:sampleGroup];
			
		//initially set the max and min and compare all later ones to these numbers
		if(flag==FALSE)
		{
			maxLat=sampleGroup.coordinate.latitude;
			minLat= sampleGroup.coordinate.latitude;
			maxLong=sampleGroup.coordinate.longitude;
			minLong= sampleGroup.coordinate.longitude;
			flag=TRUE;
		}
		else
		{
			//set the max and min lats and longs so that the span can later be determined
			if(sampleGroup.coordinate.latitude > maxLat)
			{
				maxLat=sampleGroup.coordinate.latitude;
			}
			if(sampleGroup.coordinate.latitude < minLat)
			{
				minLat= sampleGroup.coordinate.latitude;
			}
			if(sampleGroup.coordinate.longitude > maxLong)
			{
				maxLong= sampleGroup.coordinate.longitude;
			}
			if(sampleGroup.coordinate.longitude < minLong)
			{
				minLong= sampleGroup.coordinate.longitude;
			}
		}
	}
	//obtain the average lat and long and make then the center coordinate
	int x,y;
	for(x=0; x<[coords count]; x++)
	{
		//AnnotationObjects *annot=[coords objectAtIndex:x];
		uniqueSamples *group=[coords objectAtIndex:x];
		CLLocationCoordinate2D coordinate=group.coordinate;
		double tempLat=coordinate.latitude;
		double tempLong=coordinate.longitude;
		averageLong= averageLong+ tempLong;
		averageLat= averageLat+ tempLat;
	}
	averageLat=averageLat/[coords count];
	averageLong=averageLong/[coords count];
	center.latitude= averageLat;
	center.longitude= averageLong;
	latSpan= maxLat - minLat;
	longSpan= maxLong- minLong;
	
	//show an alert if there are no samples in a chosen region
	if([locations count]==0)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Samples" message:@"Please choose a different region" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	else
	{
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		[viewController setData:locations:locations];
		[viewController setCoordinate:center:latSpan:longSpan];
		[viewController setRegion:regionName];
		self.mapController = viewController;
		[viewController release];
		UIView *ControllersView = [mapController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:mapController animated:NO];
	}
	}
}
-(void)getSamples{
	double temp= [longitude doubleValue];
	temp+=2;
	
	longitude=[[NSString alloc] initWithFormat:@"%f", temp];
	NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhone.svc?searchRegion='%@'", regionName];
	NSURL *myURL=[NSURL URLWithString:urlString];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
											  returningResponse:myReturn error:&error];

	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	
	NSURLResponse *response;
	
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
	
}
-(void)getLocationInfo{
	NSURL *myURL = [NSURL URLWithString:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhone.svc?regions=t"];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	NSURLResponse *response;
	NSError *error;
	locationResponse = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:&response error:&error];
	NSXMLParser *locationParser= [[NSXMLParser alloc] initWithData:locationResponse];
	[locationParser setDelegate:self];
	[locationParser parse];
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"set"])
	{
		//make sure this array is reset here so that when the back button is pressed the samples do not get added to the original array
		locations=[[NSMutableArray alloc]init];
	}
	if([elementName isEqualToString:@"sample"])
	{
		nameFlag=TRUE; //since we are now starting a new sample, we want to record a different name
		currentStringValue=nil;
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
		rockFlag=FALSE;
		currentStringValue= nil;
		return;
	}
	if([elementName isEqualToString:@"owner"])
	{
		ownerFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"minerals"])
	{
		//if the beginning of the minerals list is detected, set the flag to true and put the values inside
		//these string tags in the minerals array
		currentMinerals=[[NSMutableArray alloc]init];
		currentStringValue=nil;
		mineralFlag=TRUE;
	}
	if([elementName isEqualToString:@"metamorphicGrades"])
	{
		//if the beginning of the metamorphicGrade list is detected, set the flag to true and put the values inside
		//these string tags in the metamorphicGrades array
		currentMetamorphicGrades=[[NSMutableArray alloc] init];
		currentStringValue=nil;
		metamorphicFlag=TRUE;
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
		if(locationFlag==TRUE)
		{
			//each value from one of these tags is a string representing a region, add them to the array
			NSString *tempRegion= [[NSString alloc] initWithFormat:@"%@", currentStringValue];
			[regions addObject:tempRegion];
			[tempRegion release];
			currentStringValue = nil;
			return;
		}
		else if(ownerFlag==TRUE)
		{
			currentOwner=currentStringValue;
			ownerFlag=FALSE;
			return;
		}
		else if(nameFlag==TRUE)
		{
			sampleName= [[NSString alloc] initWithString:currentStringValue];
			currentStringValue=nil;
			nameFlag=FALSE;
			return;
		}
		
		else if(mineralFlag==TRUE)
		{
			//each value from one of these tags is a string representing a type of mineral found in a sample
			//for each new sample, loop through the existing mineral array to make sure it is not already in the array
			int x;
			bool flag=FALSE;
			[currentMinerals addObject:currentStringValue]; //build an array with all the minerals for each sample
			for(x=0; x<[minerals count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[minerals objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[minerals addObject:currentStringValue];
				currentStringValue=nil;
				return;
			}
		}
		else if(metamorphicFlag==TRUE)
		{
			int x;
			bool flag=FALSE;
			[currentMetamorphicGrades addObject:currentStringValue]; //build and array with the metamorphic grades for each sample
			for(x=0; x<[metamorphicGrades count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[metamorphicGrades objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[metamorphicGrades addObject:currentStringValue];
				currentStringValue=nil;
						return;
			}
		}
		
	}
	
    if ( [elementName isEqualToString:@"x"] ) {
		longdouble= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble=[[NSString alloc] initWithFormat: @"%.5f", longdouble];
		longdouble= [tempDouble doubleValue];
		currentStringValue = nil;
    }
	if([elementName isEqualToString:@"y"])
	{
		double latValue= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble2=[[NSString alloc] initWithFormat: @"%.5f", latValue];
		NSString *foo=[[NSString alloc] initWithString:tempDouble2];
		latdouble= [tempDouble2 doubleValue];
		currentStringValue = nil;
		
	}
	if([elementName isEqualToString:@"long"])
	{
		sampleID= [[NSString alloc] initWithString:currentStringValue];
		currentStringValue = nil;
		return;
	}
	
	if([elementName isEqualToString:@"sample"])
	{
		CLLocationDegrees latdegrees= (CLLocationDegrees)latdouble;
		CLLocationDegrees longdegrees= (CLLocationDegrees) longdouble;
		CLLocation *tempCoordinate= [[CLLocation alloc] initWithLatitude:latdegrees longitude:longdegrees];
		
		newAnnotation= [AnnotationObjects new];
		[newAnnotation setCoordinate:tempCoordinate.coordinate];
		[newAnnotation setTitle:[[NSString alloc] initWithString:sampleName]];
		[newAnnotation setId:[[NSString alloc] initWithString:sampleID]];
		[newAnnotation setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
		[newAnnotation setPublicData:publicStatus];
		[newAnnotation setRockType:currentRockType];
		[newAnnotation setMinerals:currentMinerals];
		[newAnnotation setOwner:currentOwner];
		[newAnnotation setName:sampleName];
		[newAnnotation setMetamorphicGrades:currentMetamorphicGrades];
		if(uniqueFlag==TRUE)
		{
			//automatically add the first sample to the set since there will not be any duplicates yet
			uniqueSamples *newSet=[uniqueSamples new];
			newSet.samples=[[NSMutableArray alloc] init];
			newSet.count=1;
			[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
			[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
			[newSet.samples addObject:newAnnotation];
			newSet.coordinate=tempCoordinate.coordinate;
			[newSet setId:sampleID];
			uniqueFlag=FALSE;
			[locations addObject:newSet];
		}
		else
		{
			int p;
			int q;
			bool added=FALSE;
			for(p=0; p<[locations count]; p++)
			{
				
				uniqueSamples *tempSample= [locations objectAtIndex:p];
				if((tempSample.coordinate.latitude ==tempCoordinate.coordinate.latitude) && (tempSample.coordinate.longitude == tempCoordinate.coordinate.longitude)) //this sample exists at a location where there is already a location
				{
					tempSample.count++;
					[tempSample setTitle:[[NSString alloc] initWithFormat:@"%d samples", tempSample.count]];
					[tempSample setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
					[tempSample.samples addObject:newAnnotation];
					[locations replaceObjectAtIndex:p withObject:tempSample];
					[tempSample setId:@"multiple samples."];
					added=TRUE;
				}
			}
			if(added==FALSE) //there is no sample at this location yet, so add it to the array
			{
				uniqueSamples *newSet=[uniqueSamples new];
				newSet.samples=[[NSMutableArray alloc] init];
				newSet.count=1;
				[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
				[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
				[newSet.samples addObject:newAnnotation];
				newSet.coordinate=tempCoordinate.coordinate;
				[newSet setId:sampleID];
				[locations addObject:newSet];
			}
		}
	}			
				
	
	if([elementName isEqualToString:@"rockType"])
	{
		if(rockFlag==FALSE)
		{
			currentRockType=currentStringValue;
			 int x;
			bool flag=FALSE;
			for(x=0; x<[rockTypes count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[rockTypes objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					rockFlag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[rockTypes addObject:currentStringValue];
				currentStringValue=nil;
				rockFlag=TRUE;
				return;
			}
		}
	}
	if([elementName isEqualToString:@"minerals"])
	{
		//the end of the mineral list for this sample has been reached
		mineralFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"metamorphicGrades"])
	{
		metamorphicFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"boolean"])//indicates the sample's status as public or private
	{
		publicStatus=[[NSString alloc] initWithString:currentStringValue];
	}
	
	currentStringValue = nil;
    [currentStringValue release];
	
}



-(void)setCoordinates:(NSString*)mylat:(NSString*)mylong{
	latitude=mylat;
	longitude=mylong;
}

- (void)dealloc {
    [super dealloc];
	[toplabel release];
	[sampleButton release];
	[toolbar release];
	[mapController release];

	[latitude release];
	[longitude release];
	[minerals release];
}
@end
