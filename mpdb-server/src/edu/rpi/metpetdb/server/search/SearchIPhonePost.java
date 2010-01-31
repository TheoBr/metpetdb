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
	private static PaginationParameters p= new PaginationParameters();
	private static String criteria= "";

	
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		//clear all of the variables so there are not left over values from previous requests
		owners= new HashSet();
		rockTypes= new HashSet();
		metamorphicGrades= new HashSet();
		minerals= new HashSet();
		region= new String();
		p= new PaginationParameters();
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
		while(scanner.hasNextLine())
		{
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
			response.getWriter().write("Current line of input:\n");
			response.getWriter().write(criteriaType + " " + value);
	

		//test to see what the first word of the input is and call the functions in the rest of the 
		//file accordingly]
			if(criteriaType.equals("username"))
			{
			response.getWriter().write("<username>");
			String username= value;
			response.getWriter().write(username);
			response.getWriter().write("</username>");
			if(criteriaType.equals("password"))
			{
				response.getWriter().write("<password>");
				String password= value;
				response.getWriter().write(password);
				response.getWriter().write("</password>");
				UserServiceImpl userImpl= new UserServiceImpl();
				User u= new User();
				u= userImpl.details(username);
				if(UserServiceImpl.authenticate(u, password))
				{
					response.getWriter().write("authentication succeeded");
				}
				else
				{
					response.getWriter().write("authentication failed");
				}
			}
		}
		//assign each of the search criteria to their respective variables using 
		//the scanner
		if(criteriaType.equals("rockType"))
		{
			String tempRockType= value;
			response.getWriter().write(tempRockType);
			RockType rt= new RockType(tempRockType);
			rockTypes.add(rt);
			response.getWriter().write("Entire list of rock types added: \n");
			for(RockType r : rockTypes )
				response.getWriter().write(r.getRockType());
		}
		if(criteriaType.equals("mineral"))
		{
			String tempMineral= value;
			response.getWriter().write(tempMineral);
			Mineral min= new Mineral();
			min.setName(tempMineral);
			minerals.add(min);
		}
		if(criteriaType.equals("metamorphicGrade"))
		{
			String tempMetGrade= value;
			response.getWriter().write(tempMetGrade);
			MetamorphicGrade mg= new MetamorphicGrade(tempMetGrade);
			metamorphicGrades.add(mg);
		}
		if(criteriaType.equals("owner"))
		{
			String tempOwner= value;
			response.getWriter().write(tempOwner);
			owners  = new HashSet();
			owners.add(tempOwner);
		}
		if(criteriaType.equals("criteriaSummary"))
		{
			criteria= value;
		}
		if(criteriaType.equals("pagination"))
		{
			int param= Integer.parseInt(value);
			p.setFirstResult(param);
			p.setMaxResults(5);
		}
		if(criteriaType.equals("regions"))
		{
			SearchIPhone.regions(response, session);
		}
		if(criteriaType.equals("coordinates"))
		{
			double north= Double.valueOf(scanner.next());
			double south= Double.valueOf(scanner.next());
			double east= Double.valueOf(scanner.next());
			double west= Double.valueOf(scanner.next());
			if(scanner.hasNext("criteriaSummary="))
			{
				scanner.next();
				criteria= scanner.next();
			}
			
			System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(north,south,east,west, session, owners, rockTypes, metamorphicGrades, minerals, region, p), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(north,south, east, west, session, owners, rockTypes, metamorphicGrades, minerals, region, p),response);
			}
			
		}
		else if(criteriaType.equals("searchRegion"))
		{
			region= value;
			if(criteria.equals("true"))
			{  
				response.getWriter().write("Criteria was set to true!");
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, p, response), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, p, response),response);
				response.getWriter().write("not criteria output");
			}
		}
		//if search criteria were entered but a search region or search box was not, a seperate search must be done
		else if((!minerals.isEmpty() || !owners.isEmpty() || !rockTypes.isEmpty() || !metamorphicGrades.isEmpty()))
		{
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, p, response), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session, owners, rockTypes, metamorphicGrades, minerals, region, p, response), response);
			}
		}
		else if(criteriaType.equals("sampleID"))
		{
			sampleIds.add(Long.parseLong(value));
			SearchIPhone.sampleInfo(sampleIds,response);
		}
		else if(criteriaType.equals("comments"))
		{
			//the number following comments= will be the id of the sample we want comments for
			long id= Long.parseLong(value);
			SearchIPhone.comments(response, id);
		}
		else if(criteriaType.equals("subsampleInfo"))
		{
			//the number following subsampleInfo= will be the id of the sample we want comments for
			long id= Long.parseLong(value);
			SearchIPhone.subsampleInfo(response, id);
		}
		else if(criteriaType.equals("thumbnails"))
		{
			//the number following the thumbnails= will be the id of the sample we want thumbnails for
			long id=Long.parseLong(value);
			SearchIPhone.get_thumbnails(response, id);
		}
		else if(criteriaType.equals("largeImage"))
		{
			//the number following the largeImage= will be id of the image we want to enlarge
			long imageID= Long.parseLong(value);
			SearchIPhone.get_large_image(response, imageID);
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
		}
		}
		catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		} finally {
			session.close();
		}
	}
	

}
