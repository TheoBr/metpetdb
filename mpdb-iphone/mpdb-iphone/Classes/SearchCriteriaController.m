//
//  SearchCriteriaController.m
//  Location
//
//  Created by Heather Buletti on 6/18/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SearchCriteriaController.h"


#define TITLELABEL_TAG 1
#define SUBTITLELABEL_TAG 2
#define PHOTO_TAG 3

@implementation SearchCriteriaController
@synthesize tableController, toolbar, addButton, removeButton, clearButton, searchButton, mapController, region, editButton, group, newgroup;
@synthesize mapType, deleteButton;


-(void)viewDidLoad
{
	self.navigationItem.title=@"Search Criteria";
	int z;
	removing=FALSE; //the user has not yet selected the button to remove search criteria
	rowSelected=FALSE;
	canShowMap=TRUE;
	//create an array that will hold all the main titles of the rows and another that will hold the subtitles
	//the subtitles are the search criteria that have been selected thus far
	rowTitles = [[NSMutableArray alloc]init];
	rowSubtitles=[[NSMutableArray alloc] init];
		//the only criteria that is automatically displayed is the geographic location, the rest are only displayed if they have been added
	[rowTitles addObject:@"Geographic Location"];
	if(![region isEqualToString:nil])
	{
		NSString *regionName=[[NSString alloc] initWithFormat:@"Region: %@", region];
		[rowSubtitles addObject:regionName];
	}
	else
	{
		NSString *coordinate=[[NSString alloc] initWithFormat:@"Center Coordinate: %g, %g", myCoordinate.latitude, myCoordinate.longitude];
		[rowSubtitles addObject:coordinate];
	}
	if([currentRockTypes count]!=0)
	{
		[rowTitles addObject:@"Rock Type"];
		NSString *myRockTypes=[[NSString alloc] initWithFormat:@"%@",[currentRockTypes objectAtIndex:0]];
		
		for(z=1; z<[currentRockTypes count]; z++)
		{
			myRockTypes=[myRockTypes stringByAppendingFormat:@", %@",[currentRockTypes objectAtIndex:z]];
		}
		[rowSubtitles addObject:myRockTypes];
	}
	if([currentMinerals count]!=0)
	{
		[rowTitles addObject:@"Minerals"];
	 NSString *myMinerals=[[NSString alloc] initWithFormat:@"%@",[currentMinerals objectAtIndex:0]];
	 for(z=1; z<[currentMinerals count]; z++)
	 {
	 myMinerals=[myMinerals stringByAppendingFormat:@", %@",[currentMinerals objectAtIndex:z]];
	 }
	 [rowSubtitles addObject:myMinerals];
	}
	if([currentMetamorphicGrades count]!=0)
	{
		[rowTitles addObject:@"Metamorphic Grade"];
	 NSString *myMetGrades=[[NSString alloc] initWithFormat:@"%@",[currentMetamorphicGrades objectAtIndex:0]];
	 for(z=1; z<[currentMetamorphicGrades count]; z++)
	 {
	 myMetGrades=[myMetGrades stringByAppendingFormat:@", %@",[currentMetamorphicGrades objectAtIndex:z]];
	 }
	 [rowSubtitles addObject:myMetGrades];
	 
	}
	/*if([currentPublicStatus count]!=0)
	{
		[rowTitles addObject:@"Public/Private status"];
	 NSString *myStatus=[[NSString alloc] initWithFormat:@"%@",[currentPublicStatus objectAtIndex:0]];
	 for(z=1; z<[currentPublicStatus count]; z++)
	 {
	 myStatus=[myStatus stringByAppendingFormat:@", %@",[currentPublicStatus objectAtIndex:z]];
	 }
	 [rowSubtitles addObject:myStatus];
	}*/
	//for each of the rows in the table, add a boolean to indicate whether it has been selected for deletion and set them all initially to false
	delete= [[NSMutableArray alloc] init];
	/*int q;
	for(q=0; q<[rowTitles count]; q++)
	{
		[delete addObject:@"false"];
	}*/
		
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
											style:UITableViewStylePlain];
    tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	[self makeToolbar];
	
}
-(void)makeToolbar
{
	//make a toolbar at the bottom of the view and add a button to add search criteria
	buttons=[[NSMutableArray alloc] init];
	//editButton=[[UIBarButtonItem alloc] initWithTitle:@"Edit" style: UIBarButtonItemStyleBordered target:self action:@selector(edit)];
	//self.navigationItem.rightBarButtonItem=editButton;
	searchButton=[[UIBarButtonItem alloc] initWithTitle:@"Search" style:UIBarButtonItemStyleBordered target:self action:@selector(backToMap)];
	addButton= [[UIBarButtonItem alloc] initWithTitle:@"Add Criteria" style:UIBarButtonItemStyleBordered target:self action:@selector(addCriteria)];
	[buttons addObject:searchButton];
	[buttons addObject:addButton]; 
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
}
/*-(void)edit
{
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];	
	
	
	//if the user is editing, create a row that allows them to add criteria
	//[rowTitles addObject:@" Add Criteria"];
	//[rowSubtitles addObject:@""];
	
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	[self makeToolbar];
	
	removing=TRUE; //the user wants to remove search criteria so we want to register their row selections
	UIBarButtonItem *doneButton=[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleBordered target:self action:@selector(done)];
	self.navigationItem.rightBarButtonItem=doneButton;	
}*/
-(void)done
{
	int index= [rowTitles count]-1;
/*	if(index!=0)
	{
		[rowTitles removeObjectAtIndex:index];
	}*/
	removing=FALSE;
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	tableView.dataSource=self;
	tableView.delegate=self;
		[self.view addSubview:tableView];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];	
	self.navigationItem.rightBarButtonItem=nil;
	//editButton=[[UIBarButtonItem alloc] initWithTitle:@"Edit" style: UIBarButtonItemStyleBordered target:self action:@selector(edit)];
	//self.navigationItem.rightBarButtonItem=editButton;
	
	
	
}

-(void)backToMap
{
	if(canShowMap==FALSE)
	{
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No Criteria Removed" message:@"Select search criteria and then press clear to remove them, or view map without removing criteria." delegate:self cancelButtonTitle:@"Remove" otherButtonTitles:@"View Map"];
		[alert show];
	}
	else
	{
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
		//check to see if all the search data has been cleared and if so, pass the map the "original" array
		if([remainingLocations count]==0)
		{
			locations=original;
		}
		else
		{
			locations=remainingLocations;
		}
		for(p=0; p<[locations count]; p++)
		{
			uniqueSamples *sampleGroup=[uniqueSamples new];
			sampleGroup=[locations objectAtIndex:p];
			NSMutableArray *tempSamples=sampleGroup.samples;
			for(q=0; q<[tempSamples count]; q++)
			{
				AnnotationObjects *annotation=[AnnotationObjects new];
				annotation=[tempSamples objectAtIndex:q];
				[coords addObject:annotation];
				//initially set the max and min and compare all later ones to these numbers
				if(flag==FALSE)
				{
					maxLat=annotation.coordinate.latitude;
					minLat= annotation.coordinate.latitude;
					maxLong=annotation.coordinate.longitude;
					minLong= annotation.coordinate.longitude;
					flag=TRUE;
				}
				else
				{
					//set the max and min lats and longs so that the span can later be determined
					if(annotation.coordinate.latitude > maxLat)
					{
						maxLat=annotation.coordinate.latitude;
					}
					if(annotation.coordinate.latitude < minLat)
					{
						minLat= annotation.coordinate.latitude;
					}
					if(annotation.coordinate.longitude > maxLong)
					{
						maxLong= annotation.coordinate.longitude;
					}
					if(annotation.coordinate.longitude < minLong)
					{
						minLong= annotation.coordinate.longitude;
					}
				}
			}
		}
		//obtain the average lat and long and make then the center coordinate
		int x,y;
		for(x=0; x<[coords count]; x++)
		{
			AnnotationObjects *annot=[coords objectAtIndex:x];
			CLLocationCoordinate2D coordinate=annot.coordinate;
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
		
		
		MapController *viewController = [[MapController alloc] initWithNibName:@"MapView" bundle:nil];
		//check to see if all the search data has been cleared and if so, pass the map the "original" arr
		[viewController setData:original:remainingLocations];
		[viewController setType:mapType];
		[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
		[viewController setCoordinate:center:latSpan:longSpan:maxLat:minLat:maxLong:minLong];
		self.mapController = viewController;
		[viewController release];
		UIView *ControllersView = [mapController view];
		[self.view addSubview:ControllersView];
		[self.navigationController pushViewController:mapController animated:NO];
	}
}

-(void)deleteRow
{
		canShowMap=TRUE;
		removing=FALSE;
		self.navigationItem.rightBarButtonItem=nil;
		remainingLocations=[[NSMutableArray alloc] init];

	//loop through the bools to determine which subtitles should be set back to nil and which samples need to be added back to
	//the map now that the search criteria has been cleared
	int p, q;
	
	for(p=0; p<[delete count]; p++)
	{
		NSString *rowToDelete= [delete objectAtIndex:p];
		for(q=0; q<[rowTitles count]; q++)
		{
			if([[rowTitles objectAtIndex:q] isEqualToString:rowToDelete])
			{
				[rowTitles removeObjectAtIndex:q];
				[rowSubtitles removeObjectAtIndex:q];
			}
		}
		if([rowToDelete isEqualToString:@"Rock Type"])
		{
			currentRockTypes=[[NSMutableArray alloc] init];
		}
		if([rowToDelete isEqualToString:@"Minerals"])
		{
			currentMinerals=[[NSMutableArray alloc] init];
		}
		if([rowToDelete isEqualToString:@"Metamorphic Grade"])
		{
			currentMetamorphicGrades=[[NSMutableArray alloc] init];
		}
		/*if([rowToDelete isEqualToString:@"Public/Private status"])
		{
			currentPublicStatus= [[NSMutableArray alloc] init];
		}*/
			
	}
					
					
		int x, y, z, w;
		bool added=FALSE; //indicates whether anything has been added to the modifiedLocations array
		bool modified=FALSE;
				
		for(x=0; x<[original count]; x++) //first loop through the unique locations and remove any without the right rock type
		{
			modified=FALSE;
			group=[original objectAtIndex:x];
			newgroup=[uniqueSamples new]; //for each pre-existing group of samples at the same location we want to allocate a new one
			//in case any of them have the specified rock type
			newgroup.count=0;
			newgroup.samples= [[NSMutableArray alloc] init];
			NSMutableArray *groupAnnotations= group.samples;
			for(y=0; y<[groupAnnotations count]; y++)
			{
				AnnotationObjects *tempSample=[groupAnnotations objectAtIndex:y];
				NSMutableArray *tempMinerals= [[NSMutableArray alloc] init];
				tempMinerals=tempSample.minerals;
				NSMutableArray *tempMetGrades=[[NSMutableArray alloc] init];
				tempMetGrades=tempSample.metamorphicGrades;
				NSString *tempRockType=[[NSString alloc] initWithFormat:@"%@", tempSample.rockType];
				bool addSampleToMap=FALSE;
				//check each annotation to see if it remains on the map due to a specified rock type, mineral, metamorphic grade, or its public status
				for(z=0; z<[currentRockTypes count]; z++)
				{
					if([[currentRockTypes objectAtIndex:z] isEqualToString:tempRockType])
					{
						addSampleToMap=TRUE;
					}
				}
				for(z=0; z<[currentMinerals count]; z++)
				{
					for(w=0; w<[tempMinerals count]; w++)
					{
						if([[currentMinerals objectAtIndex:z] isEqualToString: [tempMinerals objectAtIndex:w]])
						{
							addSampleToMap=TRUE; //the mineral is contained in this sample, it remains on the map after the rock types are cleared
						}
					}
				}
				for(z=0; z<[currentMetamorphicGrades count]; z++)
				{
					for(w=0; w<[tempMetGrades count]; w++)
					{
						if([[currentMetamorphicGrades objectAtIndex:z] isEqualToString:[tempMinerals objectAtIndex:w]])
						{
							addSampleToMap=TRUE;
						}
					}
				}
				if(addSampleToMap==TRUE)
				{ 
					if([group.id isEqualToString:@"multiple samples"])
					{
						//modified=TRUE;
						//newgroup.count++;
						[newgroup.samples addObject:tempSample];
						newgroup.count=[newgroup.samples count];
						if(newgroup.count==1)
						{
							[newgroup setTitle:[[NSString alloc] initWithFormat:@"%d sample", newgroup.count]];
							newgroup.id= tempSample.id;
						}
						else
						{
							[newgroup setTitle:[[NSString alloc] initWithFormat:@"%d samples", newgroup.count]];
							newgroup.id=@"multiple samples";
						}
						newgroup.subtitle= @"Click for more info.";
						newgroup.coordinate=tempSample.coordinate;
						
						if(added==FALSE || modified==FALSE)
						{
							[remainingLocations addObject:newgroup];
							added=TRUE;
							modified=TRUE;
						}
						else if(modified==TRUE)
						{
							//replace the group of samples that was at this coordinate with the new group of samples
							int index; //represents the current position in the modified locations array where the object should be added, for multiple samples it replaces the last object added
							index=[remainingLocations count];
							index--;
							//[remainingLocations replaceObjectAtIndex:index withObject:newgroup];
							[remainingLocations removeObjectAtIndex:index];
							[remainingLocations addObject:newgroup];
						}
						
					}
					else //there are multiple samples at this location, so we have to subtract them from the original number if not the specified rock type
					{
						[remainingLocations addObject:group];
						added=TRUE;
					}
				}
			}
		}
	//if there are no search criteria remaining, the remainingLocations array is the same as the original array
	if([currentRockTypes count]==0 && [currentMinerals count]==0 && [currentMetamorphicGrades count]==0 && [currentPublicStatus count]==0)
	{
		remainingLocations=original;
	}
	
	//UIBarButtonItem *doneButton=[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleBordered target:self action:@selector(done)];
	//self.navigationItem.rightBarButtonItem=doneButton;	
	rowSelected=FALSE;
	removing=TRUE;
	
	
	UITableView *tableView= [[UITableView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]
														 style:UITableViewStylePlain];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	CGRect toolBarFrame= CGRectMake (0, 377, 320, 40);
	toolbar = [ [ UIToolbar alloc ] init ];
	toolbar.frame = toolBarFrame;
	toolbar.items=buttons;	
	[toolbar setBarStyle:1];
	[self.view addSubview:toolbar];
	
}
-(void)addCriteria
{
	//load the table containing the choices for search criteria so the user can add more
	TableController *viewController = [[TableController alloc] initWithNibName:@"TableView" bundle:nil];
	[viewController setData:original:remainingLocations:mapType:points];
	[viewController setCurrentSearchData:currentRockTypes :currentMinerals :currentMetamorphicGrades :currentPublicStatus:region:myCoordinate];
	 self.tableController = viewController;
	 [viewController release];
	 UIView *ControllersView = [tableController view];
	 [self.view addSubview:ControllersView];
	 [self.navigationController pushViewController:tableController animated:NO];	
	 removing=FALSE;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [rowTitles count];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UILabel *titleLabel, *subtitleLabel;

    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"My Identifier"];
	 UIImageView *checkmarkView;
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"My Identifier"] autorelease];
       // cell.accessoryType = UITableViewCellAccessoryCheckmark;
		//int height= [tableView heightForRowAtIndexPath:indexPath];
        titleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(0.0, 0.0, 100.0, 45)] autorelease];
        titleLabel.tag = TITLELABEL_TAG;
        titleLabel.font = [UIFont systemFontOfSize:15.0];
        titleLabel.textAlignment = UITextAlignmentCenter;
		titleLabel.numberOfLines=2;
        titleLabel.textColor = [UIColor blackColor];
        titleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
		//set the background of the labels to be alternating colors
				if(indexPath.row==0)
		{
			titleLabel.backgroundColor=[UIColor purpleColor];
		}
		else if(indexPath.row==1)
		{
			titleLabel.backgroundColor=[UIColor blueColor];
		}
		else if(indexPath.row==2)
		{
			titleLabel.backgroundColor=[UIColor greenColor];
		}
		else if(indexPath.row==3)
		{
			titleLabel.backgroundColor=[UIColor yellowColor];
		}
		else if(indexPath.row==4)
		{
			titleLabel.backgroundColor=[UIColor cyanColor];
		}
		else if(indexPath.row==5)
		{
			titleLabel.backgroundColor=[UIColor lightGrayColor];
		}
        [cell.contentView addSubview:titleLabel];
		
        subtitleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(110.0, 0.0, 180.0, 25.0)] autorelease];
        subtitleLabel.tag = SUBTITLELABEL_TAG;
		subtitleLabel.numberOfLines=2;
        subtitleLabel.font = [UIFont systemFontOfSize:12.0];
        subtitleLabel.textAlignment = UITextAlignmentLeft;
        subtitleLabel.textColor = [UIColor darkGrayColor];
        subtitleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
        [cell.contentView addSubview:subtitleLabel];
		
		
		checkmarkView = [[[UIImageView alloc] initWithFrame:CGRectMake(250.0, 8.0, 30.0, 30.0)] autorelease];
		checkmarkView.tag = PHOTO_TAG;
		checkmarkView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
		[cell.contentView addSubview:checkmarkView];
		
	}		
	else {
        titleLabel = (UILabel *)[cell.contentView viewWithTag:TITLELABEL_TAG];
        subtitleLabel = (UILabel *)[cell.contentView viewWithTag:SUBTITLELABEL_TAG];
		 checkmarkView = (UIImageView *)[cell.contentView viewWithTag:PHOTO_TAG];
    }
	//NSDictionary *aDict = [self.list objectAtIndex:indexPath.row];
    titleLabel.text = [rowTitles objectAtIndex:indexPath.row];
    subtitleLabel.text = [rowSubtitles objectAtIndex:indexPath.row];
	index= indexPath.row;
	
	cell.selectionStyle=UITableViewCellSelectionStyleNone;
	return cell;
	
}
//-(void)tableView:(UITableView *)tableView  accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath {}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
	/*if(removing==TRUE && (indexPath.row!=0))
	{
		if(indexPath.row==([rowTitles count]-1))
		{
			[self addCriteria];
		}*/
		if(indexPath.row!=0)
		{
			[delete addObject:[rowTitles objectAtIndex:indexPath.row]];
			CGRect frame= CGRectMake(240.0, 8.0, 60.0, 30.0);
			UIButton *button=[[UIButton alloc] initWithFrame:frame];
			deleteButton= [UIButton buttonWithType:UIButtonTypeRoundedRect];
			deleteButton.frame=frame;
			[deleteButton setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
			[deleteButton setTitle:@"Delete" forState:UIControlStateNormal];
			[deleteButton addTarget:self action:@selector(deleteRow) forControlEvents:UIControlEventTouchUpInside];
			[cell.contentView addSubview:deleteButton];
			//[self deleteRow];
			
		UIBarButtonItem	*doneButton=[[UIBarButtonItem alloc] initWithTitle:@"Cancel" style:UIBarButtonItemStyleBordered target:self action:@selector(done)];
			self.navigationItem.rightBarButtonItem=doneButton;
		}
}
//this is the data of the annotations from the original search result and the current annotations that are left
//this information needs to be passed to the table controller where more search criteria can be added
-(void)setData:(NSMutableArray*)originalData:(NSMutableArray*)sampleLocations:(NSString*)type:(NSMutableArray*)LatLongPoints
{
	mapType=type;
	original=originalData;
	remainingLocations= sampleLocations;
	points= LatLongPoints;
	
}

//this function will set the data that has been specified in the current search
-(void)setCurrentSearchData:(NSMutableArray*)rocks:(NSMutableArray*)mins:(NSMutableArray*)metgrades:(NSMutableArray*)public:(NSString*)aregion:(CLLocationCoordinate2D)coord
{
	currentRockTypes=rocks;
	currentMinerals=mins;
	currentMetamorphicGrades=metgrades;
	currentPublicStatus=public;
	myCoordinate=coord;
	region=aregion;
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
