//
//  RegionViewController.m
//  Location
//
//  Created by Heather Buletti on 6/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "RegionViewController.h"



@implementation RegionViewController
@synthesize latitude, longitude, mylocationCoordinate, mylat, currentStringValue, publicStatus, currentRockType;
@synthesize sampleName, sampleID, mapController, regionName, selectedRegion, tableView;

-(void)viewDidLoad
{
	
	//initialize all the arrays used in this controller
	letters=[[NSMutableArray alloc] init];
	tableObjects=[[NSMutableDictionary alloc] init];
	regions = [[NSMutableArray alloc] init];
	minerals=[[NSMutableArray alloc] init];
	sampleLocations= [[NSMutableArray alloc] init];
	metamorphicGrades=[[NSMutableArray alloc] init];
	rockTypes=[[NSMutableArray alloc] init];
	mineralFlag=FALSE;
	metamorphicFlag=FALSE;
	rockFlag=FALSE;
	nameFlag=TRUE;
	uniqueFlag=TRUE;
	
	
	locationFlag=TRUE;
	[self getLocationInfo];
	
	
	
	
	//the dictionary will hold values and the key will be the letter that the region begins with
	//make an array of all the regions of each letter as well as an array of the letters 
	char c;
	int x, y;
	for(c ='A'; c<='Z'; c++)
	{
		NSMutableArray *regionsForLetter=[[NSMutableArray alloc] init];
		for(x=0; x< [regions count]; x++)
		{
			NSString *tempRegion=[regions objectAtIndex:x];
			if([tempRegion characterAtIndex:0]==c) //add the region name to the array for that letter
			{
				[regionsForLetter addObject:tempRegion];
			}
		}
		//add the array of regions that start with that letter to the dictionary with the letter as the key
		if([regionsForLetter count]!=0)
		{
			[letters addObject:[NSString stringWithFormat:@"%c",c]];
			[tableObjects setObject:regionsForLetter forKey:[NSString stringWithFormat:@"%c",c]];
		}
	}
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return [letters count];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [[tableObjects objectForKey:[letters objectAtIndex:section]] count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	static NSString *MyIdentifier = @"MyIdentifier";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:MyIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithFrame:CGRectZero reuseIdentifier:MyIdentifier] autorelease];
	}
	
	// Set up the cell
	cell.text = [[tableObjects objectForKey:[letters objectAtIndex:indexPath.section]] objectAtIndex:indexPath.row];
	return cell;
}
- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
	//NSMutableArray *characters= [[NSMutableArray alloc]init];
	//for(char c = 'A'; c<='Z';c++)
	//	[characters addObject:[NSString stringWithFormat:@"%c",c]];
//	return characters;
	return letters;
}
- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index {
	NSInteger count = 0;
	for(NSString *character in letters)
	{
		if([character isEqualToString:title])
			return count;
		count ++;
	}
	return 0;// in case of some eror dont crash app
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	if([letters count]==0)
		return @"";
	return [letters objectAtIndex:section];
}
//if a row of the table is selected, load the map with the samples from that region
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *selectedCell=[tableView cellForRowAtIndexPath:indexPath];
	selectedRegion= selectedCell.text;
	locationFlag=FALSE;
	[self loadMap];
}

//This function is called when the search button is pressed and it loads the MapView
-(void)loadMap
{
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
		for(p=0; p<[sampleLocations count]; p++)
		{
			uniqueSamples *sampleGroup=[uniqueSamples new];
			sampleGroup=[sampleLocations objectAtIndex:p];
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
		if([sampleLocations count]==0)
		{
			UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Samples" message:@"Please choose a different region" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
		}
		else
		{
			MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
			[viewController setData:sampleLocations:sampleLocations];
			[viewController setCoordinate:center:latSpan:longSpan:maxLat:minLat:maxLong:minLong];
			[viewController setType:@"map"];
			[viewController setRegion:selectedRegion];
			self.mapController = viewController;
			[viewController release];
			UIView *ControllersView = [mapController view];
			[self.view addSubview:ControllersView];
			[self.navigationController pushViewController:mapController animated:NO];
		}
}
-(void)getSamples{
	double temp= [longitude doubleValue];
	temp+=2;
	
	longitude=[[NSString alloc] initWithFormat:@"%f", temp];
	NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?searchRegion='%@'", regionName];
	NSURL *myURL=[NSURL URLWithString:urlString];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	NSError *error;
	myReturn = [NSURLConnection sendSynchronousRequest:myRequest
									 returningResponse:myReturn error:&error];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
	
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/mpdb-iphone/searchResults.kml"];
	[fh writeData:myReturn];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	
	NSURLResponse *response;
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
	[myParser setDelegate:self];
	[myParser parse];
	
}
-(void)getLocationInfo{
	NSURL *myURL = [NSURL URLWithString:@"http://samana.cs.rpi.edu/metpetweb/searchIPhone.svc?regions=t"];
	
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	[myRequest setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"GET"];
	//[myRequest setHTTPBody:myData];
	
	
	NSURLResponse *response;
	NSError *error;
	locationResponse = [NSURLConnection sendSynchronousRequest:myRequest
											 returningResponse:&response error:&error];
	if(error!=NULL)
	{ 
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
	}
		
	
	NSString *localError= error.localizedDescription;
	
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/mpdb-iphone/mpdb-iphone/searchResults.kml"];
	[fh writeData:locationResponse];
	NSXMLParser *locationParser= [[NSXMLParser alloc] initWithData:locationResponse];
	[locationParser setDelegate:self];
	[locationParser parse];
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"set"])
	{
		//make sure this array is reset here so that when the back button is pressed the samples do not get added to the original array
		sampleLocations=[[NSMutableArray alloc]init];
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
			[sampleLocations addObject:newSet];
		}
		else
		{
			int p;
			int q;
			bool added=FALSE;
			for(p=0; p<[sampleLocations count]; p++)
			{
				
				uniqueSamples *tempSample= [sampleLocations objectAtIndex:p];
				if((tempSample.coordinate.latitude ==tempCoordinate.coordinate.latitude) && (tempSample.coordinate.longitude == tempCoordinate.coordinate.longitude)) //this sample exists at a location where there is already a location
				{
					tempSample.count++;
					[tempSample setTitle:[[NSString alloc] initWithFormat:@"%d samples", tempSample.count]];
					[tempSample setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
					[tempSample.samples addObject:newAnnotation];
					[sampleLocations replaceObjectAtIndex:p withObject:tempSample];
					[tempSample setId:@"multiple samples"];
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
				[sampleLocations addObject:newSet];
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
