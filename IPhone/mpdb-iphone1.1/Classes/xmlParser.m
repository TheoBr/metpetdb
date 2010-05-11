//
//  parseXML.m
//  MetPetDB
//
//  Created by Heather Buletti on 10/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "xmlParser.h"
#import "CriteriaSummary.h"


@implementation xmlParser
@synthesize publicStatus, sampleName, rock, currentOwner, sampleID, currentStringValue;


-(NSMutableArray*)parseSamples:(NSData*)xmlData
{
	currRockTypes=[[NSMutableArray alloc] init];
	locationFlag= FALSE;
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:xmlData];
	[myParser setDelegate:self];
	[myParser parse];
	return(mySamples);
}	
-(NSMutableArray*)parseRegions:(NSData*)xmlData
{
	
	locationFlag=TRUE;
	regions=[[NSMutableArray alloc] init];
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:xmlData];
	[myParser setDelegate:self];
	[myParser parse];
	locationFlag=FALSE;
	return(regions);
}
-(CriteriaSummary*)parseSearchCriteria:(NSData*)xmlData
{
	currRockTypes=[[NSMutableArray alloc] init];
	locationFlag=false;
	criteria= [[CriteriaSummary alloc] init];
	//allocate space for all of the search criteria attributes
	criteria.rockTypes=[[NSMutableArray alloc] init];
	criteria.minerals=[[NSMutableArray alloc] init];
	criteria.metamorphicGrades=[[NSMutableArray alloc] init];
	criteria.owners=[[NSMutableArray alloc] init];
	NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:xmlData];
	[myParser setDelegate:self];
	[myParser parse];
	criteria.totalCount= totalCount;
	return(criteria);
}



- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"set"])
	{
		//the array the holds all the samples must be initialized and all the booleans should be set here.
		mySamples= [[NSMutableArray alloc] init];
		currentStringValue=nil;
		return;
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
	if([elementName isEqualToString:@"number"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"minerals"])
	{
		//if the beginning of the minerals list is detected, set the flag to true and put the values inside
		//these string tags in the minerals array
		currMinerals=[[NSMutableArray alloc] init];
		currentStringValue=nil;
		mineralFlag=TRUE;
		return;
	}
	if([elementName isEqualToString:@"metamorphicGrades"])
	{
		//if the beginning of the metamorphicGrade list is detected, set the flag to true and put the values inside
		//these string tags in the metamorphicGrades array
		currMetGrades=[[NSMutableArray alloc] init];
		currentStringValue=nil;
		metamorphicFlag=TRUE;
		return;
	}
	if([elementName isEqualToString:@"description"])
	{
		description=nil;
		descriptionFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"int"])
	{
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"criteriaMinerals"])
	{
		criteriaMinFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"criteriaRockTypes"])
	{
		criteriaRockFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"criteriaMetGrades"])
	{
		criteriaMetFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"criteriaOwners"])
	{
		criteriaOwnerFlag=TRUE;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"id"])
	{
		currentStringValue=nil;
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


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
	if([elementName isEqualToString:@"int"])
	{
		totalCount=[currentStringValue intValue];
		criteria.totalCount=totalCount;
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"string"])
	{
		if(criteriaMinFlag==TRUE)
		{
			[criteria.minerals addObject:currentStringValue];
			currentStringValue=nil;
			return;
			
		}
		else if(criteriaMetFlag==TRUE)
		{
			[criteria.metamorphicGrades addObject:currentStringValue];
			currentStringValue=nil;
			return;
		}
		else if(criteriaOwnerFlag==TRUE)
		{
			[criteria.owners addObject:currentStringValue];
			currentStringValue=nil;
			return;
		}
		else if(locationFlag==TRUE)
		{
			NSString *tempRegion= [[NSString alloc] initWithFormat:@"%@", currentStringValue];
			[regions addObject:tempRegion];
			[tempRegion release];
			currentStringValue = nil;
			return;
		}			
		
		else if(nameFlag==TRUE)
		{
			sampleName= [[NSString alloc] initWithString:currentStringValue];
			currentStringValue=nil;
			nameFlag=FALSE;
			return;
		}
		else if(descriptionFlag==TRUE)
		{
			description= currentStringValue;
			descriptionFlag=FALSE;
			currentStringValue=nil;
			return;
		}
		else if(ownerFlag==TRUE)
		{
			currentOwner=currentStringValue;
			ownerFlag=FALSE;
			currentStringValue=nil;
			return;
		}
		else if(mineralFlag==TRUE)
		{
			//each value from one of these tags is a string representing a type of mineral found in a sample
			//for each new sample, loop through the existing mineral array to make sure it is not already in the array
			int x;
			bool flag=FALSE;
			for(x=0; x<[mins count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[mins objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[currMinerals addObject:currentStringValue];
				currentStringValue=nil;
				return;
			}
			currentStringValue=nil;
			return;
		}
		else if(metamorphicFlag==TRUE)
		{
			int x;
			bool flag=FALSE;
			for(x=0; x<[metGrades count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[metGrades objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					currentStringValue=nil;
					return;
				}
			}
			if(flag==FALSE)
			{
				[currMetGrades addObject:currentStringValue];
				currentStringValue=nil;
				return;
			}
		}
		currentStringValue=nil;
		return;
	}
	if ( [elementName isEqualToString:@"x"] ) {
		longdouble= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble=[[NSString alloc] initWithFormat: @"%.4f", longdouble];
		longdouble= [tempDouble doubleValue];
		currentStringValue = nil;
    }
	if([elementName isEqualToString:@"y"])
	{
		double latValue= [currentStringValue doubleValue];
		//truncate to 5 decimalplaces by converting to a string and then back to a double
		NSString *tempDouble2=[[NSString alloc] initWithFormat: @"%.4f", latValue];
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
		//	[newAnnotation setPublicData:currPublicStatus];
		[newAnnotation setRockType:[currRockTypes objectAtIndex:0]];
		[newAnnotation setMinerals:currMinerals];
		[newAnnotation setName:sampleName];
		[newAnnotation setOwner:currentOwner];
		[newAnnotation setMetamorphicGrades:currMetGrades];
		if(description!=nil)
		{
			[newAnnotation setDescription:description];
		}
		if(uniqueFlag==TRUE)
		{
			//automatically add the first sample to the set since there will not be any duplicates yet
			//	uniqueSamples *newSet=[uniqueSamples new];
			newSet=[uniqueSamples new];
			newSet.samples=[[NSMutableArray alloc] init];
			newSet.count=1;
			[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
			[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
			[newSet.samples addObject:newAnnotation];
			newSet.coordinate=tempCoordinate.coordinate;
			[newSet setId:sampleID];
			uniqueFlag=FALSE;
			[mySamples addObject:newSet];
		}
		else
		{
			int p;
			int q;
			bool added=FALSE;
			for(p=0; p<[mySamples count]; p++)
			{
				
				uniqueSamples *tempSample= [mySamples objectAtIndex:p];
				if((tempSample.coordinate.latitude ==tempCoordinate.coordinate.latitude) && (tempSample.coordinate.longitude == tempCoordinate.coordinate.longitude)) //this sample exists at a location where there is already a location
				{
					tempSample.count++;
					[tempSample setTitle:[[NSString alloc] initWithFormat:@"%d samples", tempSample.count]];
					[tempSample setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
					[tempSample.samples addObject:newAnnotation];
					[mySamples replaceObjectAtIndex:p withObject:tempSample];
					[tempSample setId:@"multiple samples"];
					added=TRUE;
				}
			}
			if(added==FALSE) //there is no sample at this location yet, so add it to the array
			{
				newSet=[uniqueSamples new];
				newSet.samples=[[NSMutableArray alloc] init];
				newSet.count=1;
				[newSet setTitle:[[NSString alloc] initWithFormat: @"%d sample", newSet.count]];
				[newSet setSubtitle:[[NSString alloc] initWithString: @"Click for more info"]];
				[newSet.samples addObject:newAnnotation];
				newSet.coordinate=tempCoordinate.coordinate;
				[newSet setId:sampleID];
				[mySamples addObject:newSet];
			}
		}
	}	
	if([elementName isEqualToString:@"maxLat"])
	{
		criteria.maxLat= [currentStringValue doubleValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"minLat"])
	{
		criteria.minLat= [currentStringValue doubleValue];
		currentStringValue= nil;
		return;
	}
	if([elementName isEqualToString:@"maxLong"])
	{
		criteria.maxLong= [currentStringValue doubleValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"minLong"])
	{
		criteria.minLong= [currentStringValue doubleValue];
		currentStringValue=nil;
		return;
	}
	if([elementName isEqualToString:@"rockType"])
	{
		if(criteriaRockFlag==TRUE)
		{
			[criteria.rockTypes addObject:currentStringValue];
			
		}
		else if(rockFlag==FALSE)
		{
			rock=currentStringValue;
			int x;
			bool flag=FALSE;
			for(x=0; x<[currRockTypes count]; x++)
			{
				NSString *temp=[[NSString alloc] initWithFormat:@"%@",[currRockTypes objectAtIndex:x]];
				if([currentStringValue isEqualToString:temp]) //do not add the current string value to the array
				{
					flag=TRUE;
					rockFlag=TRUE;
				}
			}
			if(flag==FALSE)
			{
				[currRockTypes addObject:currentStringValue];
				currentStringValue=nil;
				rockFlag=TRUE;
				return;
			}
		}
		currentStringValue=nil;
		return;
		
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
	/*	if([elementName isEqualToString:@"boolean"])//indicates the sample's status as public or private
	 {
	 publicStatus=[[NSString alloc] initWithString:currentStringValue];
	 }*/
	if([elementName isEqualToString:@"criteriaMinerals"])
	{
		criteriaMinFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"criteriaRockTypes"])
	{
		criteriaRockFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"criteriaMetGrades"])
	{
		criteriaMetFlag=FALSE;
		return;
	}
	if([elementName isEqualToString:@"criteriaOwners"])
	{
		criteriaOwnerFlag=FALSE;
		return;
	}
	
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
