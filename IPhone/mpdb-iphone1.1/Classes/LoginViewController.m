//
//  LoginView.m
//  Location
//
//  Created by Heather Buletti on 5/8/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LoginViewController.h"
#import "MainViewController.h"


@implementation LoginViewController
@synthesize intro, usernametext, passwordtext, loginButton, toolbar, backButton, returnValue;
@synthesize mainViewController, tableView, security, currentStringValue, loginResponse;


-(void)viewDidLoad{
	currentStringValue=[[NSString alloc] init];
	loginResponse=[[NSString alloc] init];
	password=NULL;
	CGRect frame= CGRectMake(0, 80, 320, 160);
	
	tableView=[[UITableView alloc] initWithFrame:frame style:UITableViewStyleGrouped];
	tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight|UIViewAutoresizingFlexibleWidth;
	tableView.dataSource=self;
	tableView.delegate=self;
	[self.view addSubview:tableView];
	
	
	//toolbar.barStyle= UIBarStyleBlack;
	rowTitles= [[NSMutableArray alloc] init];
	[rowTitles addObject:@"Username:"];
	[rowTitles addObject:@"Password:"];
	
	CGRect picFrame= CGRectMake(95, 20, 126, 37);
	UIImageView *logoView= [[UIImageView alloc] initWithFrame:picFrame];
	UIImage *backgroundImage=[[UIImage alloc] initWithContentsOfFile:@"/Users/heatherbuletti/Documents/mpdb-logo.png"];
	logoView.image=backgroundImage;
	[self.view addSubview:logoView];
	
}

//This function makes the keyboard dissapear when the user presses the done button
-(BOOL) textFieldShouldReturn: (UITextField *) theTextField {
	if(theTextField==usernametext)
	{
		username= usernametext.text;
	}
	else if(theTextField==passwordtext)
	{
		password= passwordtext.text;
	}
	[theTextField resignFirstResponder];
	[self login];
	return YES;
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)table {
	return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return 2;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	UILabel *titleLabel;
	
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"My Identifier"];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"My Identifier"] autorelease];
		// cell.accessoryType = UITableViewCellAccessoryCheckmark;
		//int height= [tableView heightForRowAtIndexPath:indexPath];
        titleLabel = [[[UILabel alloc] initWithFrame:CGRectMake(25.0, 0.0, 100.0, 35)] autorelease];
		// titleLabel.font = [UIFont systemFontOfSize:15.0];
		titleLabel.font= [UIFont boldSystemFontOfSize:17.0];
        titleLabel.textAlignment = UITextAlignmentCenter;
		titleLabel.numberOfLines=2;
        titleLabel.textColor = [UIColor blackColor];
        titleLabel.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
		//set the background of the labels to be alternating colors
		[cell.contentView addSubview:titleLabel];
		
		if(indexPath.row==0)
		{
			usernametext = [[[UITextField alloc] initWithFrame:CGRectMake(125.0, 10.0, 180.0, 50.0)] autorelease];
			usernametext.keyboardType= UIKeyboardTypeEmailAddress;
			usernametext.autocapitalizationType=UITextAutocapitalizationTypeNone;
			usernametext.enablesReturnKeyAutomatically= YES;
			usernametext.font = [UIFont systemFontOfSize:15.0];
			usernametext.textAlignment = UITextAlignmentLeft;
			usernametext.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
			usernametext.autocorrectionType=UITextAutocorrectionTypeNo;
			usernametext.returnKeyType= UIReturnKeyGo;
			usernametext.placeholder=@"email@domain.com";
			
			usernametext.delegate=self;
			[cell.contentView addSubview:usernametext];
		}
		//if the password row is being typed in, make it secure and do not display the letters but a bullet instead
		if(indexPath.row==1)
		{
			passwordtext = [[[UITextField alloc] initWithFrame:CGRectMake(125.0, 10.0, 180.0, 50.0)] autorelease];
			passwordtext.font = [UIFont systemFontOfSize:15.0];
			passwordtext.textAlignment = UITextAlignmentLeft;
			passwordtext.autocapitalizationType= UITextAutocapitalizationTypeNone;
			passwordtext.autocorrectionType= UITextAutocorrectionTypeNo;
			passwordtext.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleHeight;
			passwordtext.delegate=self;
			passwordtext.enablesReturnKeyAutomatically=YES;
			[passwordtext setSecureTextEntry:YES];
			passwordtext.returnKeyType=UIReturnKeyGo;
			passwordtext.placeholder= @"password";
			[cell.contentView addSubview:passwordtext];
		}
		
        
		
		titleLabel.text = [rowTitles objectAtIndex:indexPath.row];
		
		cell.selectionStyle=UITableViewCellSelectionStyleNone;
		return cell;
		
	}
}
-(void)login
//-(IBAction)login:(id)sender
{
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:YES];
	if(passwordtext.text==NULL){
		password=NULL;
	}
	else{
		password= [[NSString alloc] initWithString:passwordtext.text];
	}
	if(usernametext.text==NULL){
		username=NULL;
	}
	else{
		username= [[NSString alloc] initWithString:usernametext.text];
	}
	
	
	//NSString *urlString= [[NSString alloc] initWithFormat:@"http://samana.cs.rpi.edu:8080/metpetwebtst/searchIPhonePost.svc?"];
	NSString *urlString= [[NSString alloc] initWithFormat:@"https://samana.cs.rpi.edu/metpetweb/searchIPhonePost.svc?"];
	
	NSURL *myURL=[NSURL URLWithString:urlString];
	NSMutableURLRequest *myRequest = [NSMutableURLRequest
									  requestWithURL:myURL];
	
	[myRequest setValue:@"text/plain" forHTTPHeaderField:@"Content-type"];
	[myRequest setHTTPMethod:@"POST"];
	NSString *postString= [[NSString alloc] initWithFormat:@"username= %@\npassword= %@\n",username, password ];
	//NSString *postString= [[NSString alloc] initWithFormat:@"username= sibel@cs.rpi.edu\npassword= sibeladali\n"];
	NSData *myData= [postString dataUsingEncoding:NSASCIIStringEncoding];
 	[myRequest setHTTPBody:myData];
	//[myRequest setValidatesSecureCertificate:NO]; 
	
	
	
	NSURLConnection *conn=[[NSURLConnection alloc] initWithRequest:myRequest delegate:self];
	[conn start];
	
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    myReturn= data;
	returnValue=[[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
	NSString *sub= [returnValue substringToIndex:6];
	apacheError=FALSE;
	if([sub isEqualToString:@"<html>"])
	{
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		apacheError=TRUE;
		return;
	}
	NSFileHandle *fh= [NSFileHandle fileHandleForWritingAtPath:@"/Users/heatherbuletti/Documents/test3.txt"];
	[fh writeData:myReturn];
	
}
- (void)connectionDidFinishLoading:(NSURLConnection *)connection{
	if(apacheError!=TRUE)
	{
		
		NSURLResponse *myResponse;
		NSXMLParser *myParser= [[NSXMLParser alloc] initWithData:myReturn];
		[myParser setDelegate:self];
		[myParser parse];
		//check for a server error
		
		if([loginResponse isEqualToString:@"authentication succeeded"])
		{
			//first delete the value in the keychain in case there is one remaining and then add a new one
			KeychainWrapper *keychain= [[KeychainWrapper alloc] init];
			[keychain newSearchDictionary:@"Username"];
			[keychain createKeychainValue:username forIdentifier:@"Username"];
			
			
			[self backToMain];
		}
		else
		{
			UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Invalid username or password." message:@"Please try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
			[alert show];
			username=nil;
			password=nil;
		}
	}
	[(MetPetDBAppDelegate *)[[UIApplication sharedApplication] delegate] setNetworkActivityIndicatorVisible:NO];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error{
	UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Network failure: unable to connect to internet." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alert show];
	
}
- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
    return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust];  
}  

- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
    [challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];  
	
}


//this function takes the user back to the main page if they have logged in
-(void)backToMain
{		
	MainViewController *viewController = [[MainViewController alloc] initWithNibName:@"MainView" bundle:nil];
	if([loginResponse isEqualToString:@"authentication succeeded"])
	{
		[viewController setSignIn:TRUE:username];
	}
	else
	{
		[viewController setSignIn:FALSE:NULL];
	}
	self.mainViewController = viewController;
	UIView *controllersview= [mainViewController view];
	[self.view addSubview:controllersview];
	[viewController release];
	[self.navigationController pushViewController:mainViewController animated:NO];
	
}

//the following 3 functions are used to parse the server response to login
//the server returns "authentication succeeded" or "authentication failed" depending on what happened
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}
	if([elementName isEqualToString:@"response"])
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
	if([elementName isEqualToString:@"html"])
	{ 
		//if a tag exists called html, an error was produced
		UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Unable to connect to server." message:@"Please try again later." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		return;
	}	
	if([elementName isEqualToString:@"response"])
	{
		loginResponse= currentStringValue;
		return;
	}
}
-(void) viewWillDisppear: (BOOL) animated{
	username=nil;
	password=nil;
}







- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning]; // Releases the view if it doesn't have a superview
    // Release anything that's not essential, such as cached data
}


- (void)dealloc {
    [super dealloc];
	[intro release];
	[usernametext release];
	[passwordtext release];
	[loginButton release];
	[toolbar release];
	[mainViewController release];
	[username release];
	[password release];
	[rowTitles release];
	[tableView release];
	[security release];
	[myReturn release];
	
	
}




@end
