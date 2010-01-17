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
	

		//test to see what the first word of the input is and call the functions in the rest of the 
		//file accordingly
		response.getWriter().write(postText);
			if(scanner.hasNext("username="))
			{
			scanner.next();
			response.getWriter().write("<username>");
			String username=scanner.next().trim();
			response.getWriter().write(username);
			response.getWriter().write("</username>");
			if(scanner.hasNext("password="))
			{
				response.getWriter().write("<password>");
				scanner.next();
				String password= scanner.next().trim();
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
		while(scanner.hasNext("rockType="))
		{
			scanner.next();
			String tempRockType= "";
			tempRockType=scanner.next().trim();
			RockType rt= new RockType(tempRockType);
			rockTypes.add(rt);
		}
		while(scanner.hasNext("mineral="))
		{
			scanner.next();
			String tempMineral="";
			tempMineral=scanner.next().trim();
			Mineral min= new Mineral();
			min.setName(tempMineral);
			minerals.add(min);
		}
		while(scanner.hasNext("metamorphicGrade="))
		{
			scanner.next();
			String tempMetGrade="";
			tempMetGrade=scanner.next().trim();
			MetamorphicGrade mg= new MetamorphicGrade(tempMetGrade);
			metamorphicGrades.add(mg);
		}
		while(scanner.hasNext("owner="))
		{
			scanner.next();
			String tempOwner="";
			owners  = new HashSet();
			owners.add(tempOwner);
		}
		if(scanner.hasNext("criteriaSummary="))
		{
			scanner.next();
			criteria= scanner.next().trim();
		}
		if(scanner.hasNext("pagination="))
		{
			scanner.next();
			int param= Integer.parseInt(scanner.next().trim());
			p.setFirstResult(param);
			p.setMaxResults(5);
		}
		if(scanner.hasNext("regions"))
		{
			SearchIPhone.regions(response);
		}
		if(scanner.hasNext("coordinates="))
		{
			scanner.next();
			double north= Double.valueOf(scanner.next().trim());
			double south= Double.valueOf(scanner.next().trim());
			double east= Double.valueOf(scanner.next().trim());
			double west= Double.valueOf(scanner.next().trim());
			
			System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(north,south,east,west, session), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(north,south, east, west, session),response);
			}
		}
		else if(scanner.hasNext("searchRegion="))
		{
			scanner.next();
			Set<String> regions = new HashSet<String>();
			String newRegion= new String();
			while(scanner.hasNext())
			{
				newRegion+= scanner.next().trim();
				if(scanner.hasNext())
				{
					newRegion+=" ";
				}
			}
			response.getWriter().write(newRegion);
			regions.add(newRegion);
			response.getWriter().write(criteria);
			if(criteria.equals("true"))
			{
				response.getWriter().write("Criteria was set to true!");
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session),response);
				response.getWriter().write("not criteria output");
			}
		}
		//if search criteria were entered but a search region or search box was not, a seperate search must be done
		else if(!minerals.isEmpty() || !owners.isEmpty() || !rockTypes.isEmpty() || !metamorphicGrades.isEmpty())
		{
			if(criteria.equals("true"))
			{
				SearchIPhone.getSearchCriteria(SearchIPhone.search(session), response);
			}
			else
			{
				SearchIPhone.outputSearchXML(SearchIPhone.search(session), response);
			}
		}
		else if(scanner.hasNext("sampleID="))
		{
			scanner.next();
			sampleIds.add(Long.parseLong(scanner.next().trim()));
			SearchIPhone.sampleInfo(sampleIds,response);
		}
		else if(scanner.hasNext("comments="))
		{
			scanner.next();
			//the number following comments= will be the id of the sample we want comments for
			long id= Long.parseLong(scanner.next().trim());
			SearchIPhone.comments(response, id);
		}
		else if(scanner.hasNext("subsampleInfo="))
		{
			scanner.next();
			//the number following subsampleInfo= will be the id of the sample we want comments for
			long id= Long.parseLong(scanner.next().trim());
			SearchIPhone.subsampleInfo(response, id);
		}
		else if(scanner.hasNext("thumbnails="))
		{
			scanner.next();
			//the number following the thumbnails= will be the id of the sample we want thumbnails for
			long id=Long.parseLong(scanner.next().trim());
			SearchIPhone.get_thumbnails(response, id);
		}
		else if(scanner.hasNext("largeImage="))
		{
			scanner.next();
			//the number following the largeImage= will be id of the image we want to enlarge
			long imageID= Long.parseLong(scanner.next().trim());
			SearchIPhone.get_large_image(response, imageID);
		}
		else if(scanner.hasNext("addComment"))
		{
			scanner.next();
			//the number following addComment will be the id of the sample we want to add a comment to
			long id= Long.parseLong(scanner.next().trim());
			//the text following the id is the string make a comment out of
			String comment= new String();
			while(scanner.hasNext())
			{
				comment+= scanner.next().trim();
				comment+= " ";
			}
			SampleComment newComment= new SampleComment();
			SampleCommentServiceImpl commentImpl= new SampleCommentServiceImpl();
			commentImpl.save(newComment);
			response.getWriter().write("Comment Added");
		}
		}
		catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		} finally {
			session.close();
		}
	}
	

}
