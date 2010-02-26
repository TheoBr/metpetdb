package edu.rpi.metpetdb.server.search;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.postgis.LinearRing;
import org.postgis.Point;

import com.thoughtworks.xstream.XStream;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.RegionDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.impl.SampleCommentServiceImpl;
import edu.rpi.metpetdb.server.impl.UserServiceImpl;
import edu.rpi.metpetdb.server.search.SearchIPhone.imageComparator;
import edu.rpi.metpetdb.server.search.SearchIPhone;


public class SearchIPhonePost extends HttpServlet {
	
	//make global variables out of all the search criteria and the session
	private static Session session;
	private static Set<String> owners= new HashSet();
	private static Set<RockType> rockTypes= new HashSet();
	private static Set<MetamorphicGrade> metamorphicGrades= new HashSet();
	private static Set<Mineral> minerals= new HashSet();
	private static String region= new String();
	private static PaginationParameters p;
	private static String criteria= "";
	//initialize the coordinates to -1 so we can see if they have been given values
	private static double north= -1;
	private static double south= -1;
	private static double east= -1;
	private static double west= -1;
	private static String username="";
	
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		//clear all of the variables so there are not left over values from previous requests
		owners= new HashSet();
		rockTypes= new HashSet();
		metamorphicGrades= new HashSet();
		minerals= new HashSet();
		region= "";
		p= null;
		criteria= "";
		
		response.setContentType("text/xml");
		int responseLength= request.getContentLength();
		byte[] postBytes= new byte[responseLength];
		ArrayList byteList= new ArrayList();
		int numbytes=0;
		List<Long> sampleIds = new ArrayList<Long>();
		session = DataStore.open();
		try{
		String postText= new String();
		while(numbytes!=-1)
		{
			numbytes= request.getInputStream().read(postBytes);
			
		}
		postText= new String(postBytes);
		//response.getWriter().write(postText);
		
		Scanner scanner = new Scanner(postText);
		int iterations=0;
		while(scanner.hasNextLine())
		{
			iterations++;
			Scanner lineScan= new Scanner(scanner.nextLine());
			//each individual search criteria will be on its own line in the format criteria = value
			//we can therefore use an equal sign as a delimiter
			lineScan.useDelimiter("= ");
			String criteriaType= new String();
			String value= new String();
			if(lineScan.hasNext())
			{
				criteriaType= lineScan.next();
				value= lineScan.next();
			}

		//test to see what the first word of the input is and call the functions in the rest of the 
		//file accordingly]
			else if(criteriaType.equals("password"))
			{
				String password= value;
				UserServiceImpl userImpl= new UserServiceImpl();
				User u= new User();
				u= userImpl.details(username);
				response.getWriter().write("<response>");
				if(UserServiceImpl.authenticate(u, password))
				{
					response.getWriter().write("authentication succeeded");
				}
				else
				{
					response.getWriter().write("authentication failed");
				}
				response.getWriter().write("</response>");
			}
			//if the user has signed in, create that user and pass it to the server
			if(criteriaType.equals("user"))
			{
				username=value;
			}
		//assign each of the search criteria to their respective variables using 
		//the scanner
		else if(criteriaType.equals("rockType"))
		{
			String tempRockType= value;
			RockType rt= new RockType(tempRockType);
			rockTypes.add(rt);
		}
		else if(criteriaType.equals("mineral"))
		{
			String tempMineral= value;
			Mineral min= new Mineral();
			min.setName(tempMineral);
			minerals.add(min);
		}
		else if(criteriaType.equals("metamorphicGrade"))
		{
			String tempMetGrade= value;
			MetamorphicGrade mg= new MetamorphicGrade(tempMetGrade);
			metamorphicGrades.add(mg);
		}
		else if(criteriaType.equals("owner"))
		{
			String tempOwner= value;
			owners  = new HashSet();
			owners.add(tempOwner);
		}
		else if(criteriaType.equals("criteriaSummary"))
		{
			criteria= value;
		}
		else if(criteriaType.equals("pagination"))
		{
			p= new PaginationParameters();
			int param= Integer.parseInt(value);
			p.setFirstResult(param);
			p.setMaxResults(5);
		}
		else if(criteriaType.equals("regions")){
			SearchIPhone.regions(response, session);
		}
		else if(criteriaType.equals("sampleID"))
		{
			sampleIds.add(Long.parseLong(value));
			SearchIPhone.sampleInfo(session, sampleIds,response);
		}
		else if(criteriaType.equals("comments"))
		{
			//the number following comments= will be the id of the sample we want comments for
			long id= Long.parseLong(value);
			SearchIPhone.comments(session, response, id);
		}
		else if(criteriaType.equals("subsampleInfo"))
		{
			//the number following subsampleInfo= will be the id of the sample we want comments for
			long id= Long.parseLong(value);
			SearchIPhone.subsampleInfo(session, response, id);
		}
		else if(criteriaType.equals("thumbnails"))
		{
			//the number following the thumbnails= will be the id of the sample we want thumbnails for
			long id=Long.parseLong(value);
			SearchIPhone.get_thumbnails(session, response, id);
		}
		else if(criteriaType.equals("largeImage"))
		{
			//the number following the largeImage= will be id of the image we want to enlarge
			long imageID= Long.parseLong(value);
			SearchIPhone.get_large_image(session, response, imageID);
		}
		else if(criteriaType.equals("addComment"))
		{
			//the number following addComment will be the id of the sample we want to add a comment to
			long id= Long.parseLong(scanner.next());
			//the text following the id is the string make a comment out of
			String comment= new String();
			while(scanner.hasNext())
			{
				comment+= scanner.next();
				comment+= " ";
			}
			SampleComment newComment= new SampleComment();
			SampleCommentServiceImpl commentImpl= new SampleCommentServiceImpl();
			commentImpl.save(newComment);
			response.getWriter().write("Comment Added");
		}
		//since there can only be one geographic search criteria,
		//the following are if-else statements
		if(criteriaType.equals("north")){
			north= Double.valueOf(value);
		}
		else if(criteriaType.equals("south")){
			south= Double.valueOf(value);
		}
		else if(criteriaType.equals("east")){
			east = Double.valueOf(value);
		}
		else if(criteriaType.equals("west")){
			west= Double.valueOf(value);
		}
		else if(criteriaType.equals("searchRegion"))
		{
			region= value;
		}
		}
		//the following statements will perform the actual database searches
		//and xml output from the searchIPhone file
		if(region!="") //if a region has been provided, call searchIPhone functions to search by region
		{
			if(criteria.equals("true"))
			{  
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response),response);
			}
		}
		//just test the value of the north value because all 4 coordinates are needed
		//to search based on geographic coordinates
		else if(north!= -1)
		{
			System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(north,south,east,west, session, owners, rockTypes, metamorphicGrades, minerals, region, username, p), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(north,south, east, west, session, owners, rockTypes, metamorphicGrades, minerals, region, username, p),response);
			}
		}
		//if search criteria were entered but a search region or search box was not, a seperate search must be done
		else if((minerals.size()!=0 || owners.size()!=0 || rockTypes.size()!=0 || metamorphicGrades.size()!=0))
		{
			for(RockType r : rockTypes )
				response.getWriter().write(r.getRockType());
			for(Mineral m : minerals)
				response.getWriter().write(m.getName());
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
			}
		}
		
		}
		catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		} finally {
			session.close();
		}
	}
	

}
