package edu.rpi.metpetdb.server.search;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;
import org.postgis.LinearRing;
import org.postgis.Point;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.impl.UserServiceImpl;


public class JSONSearchIPhone extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final String NORTH_PARAMETER = "north";
	private static final String SOUTH_PARAMETER = "south";
	private static final String WEST_PARAMETER=  "west";
	private static final String EAST_PARAMETER= "east";
	private static final String USERNAME_PARAMETER ="username";
	private static final String PASSWORD_PARAMETER ="password";
	private static final String SAMPLE_ID = "sampleId";
	private static final String REGIONS = "regions";
	private static final String SEARCH_REGIONS = "searchRegion";
	private static final String COMMENTS = "comments";
	private static final String SUBSAMPLE_INFO="subsampleInfo";
	private static final String THUMBNAILS="thumbnails";
	private static final String LARGE_IMAGE="large_image";
	private static final String ROCK_TYPE= "rockType";
	private static final String MINERAL= "mineral";
	private static final String METAMORPHIC_GRADE="metamorphicGrade";
	private static final String OWNER="owner";  
	private static final String PAGINATION="pagination";
	private static final String CRITERIA="criteriaSummary";

	private static Session session;
	public static Set<String> owners= new HashSet();
	public static Set<RockType> rockTypes= new HashSet();
	public static Set<MetamorphicGrade> metamorphicGrades= new HashSet();
	public static Set<Mineral> minerals= new HashSet();
	public static String region= new String();
	public static PaginationParameters p= new PaginationParameters();
	public static String criteria= "";
	public static String username="";
	public static int publicPrivate=0; //this variable is not appropriately set in the get request, only used here becase
	//the post search functions require it

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException  {
		response.setContentType("text/javascript");
		
		List<Long> sampleIds = new ArrayList<Long>();
		session = DataStore.open();
		try{
			owners= new HashSet();
			rockTypes= new HashSet();
			metamorphicGrades= new HashSet();
			minerals= new HashSet();
			region= "";
			p= new PaginationParameters();
			//make sets of the various search criteria so they can be passed to the various search functions
			if(request.getParameter(PAGINATION)!=null)
			{
				//the pagination parameter that is passed as an argument is the first result we want, as the max results number is fixed
				//there should never be more than one value for the pagination param, so can convert tempParams[0] to an int
				//and it will be the first result we want
				String tempParams[]= request.getParameterValues(PAGINATION);
				int param= Integer.parseInt(tempParams[0]);
				p.setFirstResult(param);
				p.setMaxResults(50);
			}
			else
			{
				p=null;	
			}
			
			if(request.getParameter(OWNER)!= null)
			{
				String tempOwner="";
				for (String s : request.getParameterValues(OWNER)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
						tempOwner=(s.substring(1, s.length()-1));
					}
				}
				//String tempOwners[]  = request.getParameterValues(OWNER);
				//List list = Arrays.asList(tempOwners);
				owners  = new HashSet();
				owners.add(tempOwner);
				//outputSearchXML(search(session), response);
			}
			
			if(request.getParameter(ROCK_TYPE)!= null)
			{	
				String tempRockType="";
				for (String s : request.getParameterValues(ROCK_TYPE)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){

						tempRockType=(s.substring(1, s.length()-1));
					}
				}
				RockType rt= new RockType(tempRockType);
				rockTypes.add(rt);
				//outputSearchXML(search(session), response);
			}
			if(request.getParameter(METAMORPHIC_GRADE)!= null)
			{
				String tempMetGrade="";
				for (String s : request.getParameterValues(METAMORPHIC_GRADE)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
						tempMetGrade=(s.substring(1, s.length()-1));
					}
				}
				MetamorphicGrade mg= new MetamorphicGrade(tempMetGrade);
				metamorphicGrades.add(mg);
				//outputSearchXML(search(session), response);
			}
			if(request.getParameter(MINERAL)!= null)
			{
				String tempMineral="";
				for (String s : request.getParameterValues(MINERAL)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
						tempMineral=(s.substring(1, s.length()-1));
					}
				}
					Mineral min= new Mineral();
					min.setName(tempMineral);
					minerals.add(min);
				//outputSearchXML(search(session), response);
			}
			if(request.getParameter(CRITERIA)!= null)
			{
				criteria= request.getParameterValues(CRITERIA)[0];
			}
			// If there is a GET string for latitude and longitude then it is a search
			if (request.getParameter(NORTH_PARAMETER) != null && request.getParameter(SOUTH_PARAMETER) != null && request.getParameter(WEST_PARAMETER)!= null && request.getParameter(EAST_PARAMETER) != null) {
				double north = Double.parseDouble(request.getParameterValues (NORTH_PARAMETER)[0]);
				double south = Double.parseDouble(request.getParameterValues(SOUTH_PARAMETER)[0]);
				double west = Double.parseDouble(request.getParameterValues(WEST_PARAMETER)[0]);
				double east= Double.parseDouble(request.getParameterValues(EAST_PARAMETER)[0]);
				
				System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
				if(criteria.equals("true"))
				{
					getSearchCriteria(search(north,south,east,west, session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p), response);
				}
				else
				{
					outputSearchJSON(search(north,south, east, west, session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p),response);
				}
			} 
			else if (request.getParameter(SEARCH_REGIONS) != null){
			//String region = new String();
				for (String s : request.getParameterValues(SEARCH_REGIONS)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
						region=(s.substring(1, s.length()-1));
					}
				}
				if(criteria.equals("true"))
				{
					getSearchCriteria(search(session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
				}
				else
				{
					outputSearchJSON(search(session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
				}
			}
			//if search criteria were entered but a search region or search box was not, a seperate search must be done
			else if(!minerals.isEmpty() || !owners.isEmpty() || !rockTypes.isEmpty() || !metamorphicGrades.isEmpty())
			{
				if(criteria.equals("true"))
				{
					getSearchCriteria(search(session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
				}
				else
				{
					outputSearchJSON(search(session, publicPrivate, owners, rockTypes, metamorphicGrades, minerals, region, username, p, response), response);
				}
			}
			else if (request.getParameter(SAMPLE_ID) != null){
				for (String id : request.getParameterValues(SAMPLE_ID))
					sampleIds.add(Long.parseLong(id));
				sampleInfo(session, sampleIds,response);
			} else if (request.getParameter(REGIONS) != null){
				if (request.getParameterValues(REGIONS)[0].equalsIgnoreCase("t")){
					regions(response, session);
				}
			} /*else if (request.getParameter(ROCK_TYPES) != null){
				if (request.getParameterValues(ROCK_TYPES)[0].equalsIgnoreCase("t")){
					rockTypes(response);
				}
			}*/
			else if (request.getParameter(COMMENTS) != null){
			
				long id= Long.parseLong(request.getParameterValues(COMMENTS)[0]);
				comments(session, response, id);
			}
			else if (request.getParameter(SUBSAMPLE_INFO)!= null)
			{
				//using the sample id, get a summary of the subsample and analysis information
				long id= Long.parseLong(request.getParameterValues(SUBSAMPLE_INFO)[0]);
				subsampleInfo(session, response, id);
			}
			else if (request.getParameter(THUMBNAILS)!=null)
			{
				long id= Long.parseLong(request.getParameterValues(THUMBNAILS)[0]);
				get_thumbnails(session, response, id);
			}
			else if (request.getParameter(LARGE_IMAGE)!=null)
			{
				//the id that is passed into the url here is the id of the image, not the sample
				long imageID= Long.parseLong(request.getParameterValues(LARGE_IMAGE)[0]);
				get_large_image(session, response, imageID);
			}
		}
		catch(Exception e){
			System.out.print(e.getMessage());
			throw new IllegalStateException(e.getMessage());
		} finally {
			session.close();
		}
		}
		
	/*private void rockTypes(HttpServletResponse response){
		try {
			DatabaseObjectConstraints doc = DataStore.getInstance().getDatabaseObjectConstraints();		
			final XStream x = new XStream();
			x.toXML(doc.Sample_rockType.getValues(),response.getWriter());
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		} 
	}*/
	public static class imageComparator implements Comparator {
		/*  public int compare(edu.rpi.metpetdb.client.model.Image image1, edu.rpi.metpetdb.client.model.Image image2) {
			  if((Math.abs((image1.getImageType().getId())-5))>= (Math.abs((image2.getImageType().getId())-5)))
			  {
				  return 0;
			  }
			  else
			  {
				  return 1;
			  }	  
		  }*/
		public int compare(Object obj1, Object obj2) {
			edu.rpi.metpetdb.client.model.Image image1= (edu.rpi.metpetdb.client.model.Image) obj1;
			edu.rpi.metpetdb.client.model.Image image2= (edu.rpi.metpetdb.client.model.Image) obj2;
			if((Math.abs((image1.getImageType().getId())-5))<= (Math.abs((image2.getImageType().getId())-5)))
			  {
				  return 0;
			  }
			  else
			  {
				  return 1;
			  }	  
		}
	}
	
	//DONE
	public static void get_thumbnails(Session session, HttpServletResponse response, long id)
	{
		try{
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample=s.fill(sample);
			Set<edu.rpi.metpetdb.client.model.Image> images= sample.getImages();
			List<edu.rpi.metpetdb.client.model.Image> imageList= new ArrayList(images);
			
			final XStream x = new XStream();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
			
			//after all the samples images have been output, display subsample images
			Set<Subsample> subsamples= sample.getSubsamples();
			//add the subsample images to the imageList before sorting
			for(Subsample sub : subsamples)
			{
				Set<edu.rpi.metpetdb.client.model.Image> subImages= sub.getImages();
				imageList.addAll(subImages);
			}
			java.util.Collections.sort(imageList, new imageComparator());
			xml_str = "<thumbnails>";
			for(edu.rpi.metpetdb.client.model.Image im : imageList)
			{
				final String subImagePath= im.getChecksum64x64();
			
				xml_str += "<image>" + x.toXML(subImagePath) + "</image>";
				xml_str += "<imageID>" + x.toXML(im.getId()) + "</imageID>";
				xml_str += "<imageType>" + x.toXML(im.getImageType()) + "</imageType>";
			}
			xml_str += "<imageCount>" + x.toXML(sample.getImageCount()) + "</imageCount>";
			xml_str += "</thumbnails>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
			
		} catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}	}
	//DONE
	public static void get_large_image(Session session, HttpServletResponse response, long imageID)
	{
		try{
			final XStream x = new XStream();
			PrintWriter pw = response.getWriter();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
			
			xml_str = "<image>";
			ImageDAO i= new ImageDAO(session);
			edu.rpi.metpetdb.client.model.Image image = new edu.rpi.metpetdb.client.model.Image();
			image.setId(imageID);
			image = i.fill(image);
			//final String checksum = image.getChecksumHalf();
			//final String folder = checksum.substring(0, 2);
			//final String subfolder = checksum.substring(2, 4);
			//final String filename = checksum.substring(4, checksum.length());
			final String imagePath;
			if(image.getChecksumMobile()== null || image.getChecksumMobile().equals(""))
			{
				imagePath =image.getChecksumHalf();
			}
			else
			{
				imagePath = image.getChecksumMobile();
			}
			xml_str += "<path>" + x.toXML(imagePath) + "</path>";
			xml_str += "<filename>" + x.toXML(image.getFilename()) + "</filename>";
			xml_str += "</image>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
		}	
		catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	//DONE
	public static void comments(Session session, HttpServletResponse response, long id){
		try{
			final XStream x = new XStream();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
			
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample = s.fill(sample);
			//int commentCount= sample.getComments().size();
			xml_str = "<comments>";
			//response.getWriter().write("<commentCount>");
			//response.getWriter().write(commentCount);
			//response.getWriter().write("</commentCount>");
			xml_str += x.toXML(sample.getComments().size());
			for (SampleComment sc : sample.getComments())
				xml_str += x.toXML(sc.getText());
			xml_str += "</comments>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	//DONE
	public static void subsampleInfo(Session session, HttpServletResponse response, long id ){
		try{
			final XStream x= new XStream();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
			
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample = s.fill(sample);
			//get the count of the subsamples for the sample
			Set<Subsample> subsamples= sample.getSubsamples();
			int count= subsamples.size();
				//new HashSet<Subsample>();
			xml_str = "<subsamples>";
			xml_str += x.toXML(count); //number of subsamples for the sample
			int totalImageCount=0;
			int totalAnalysisCount=0;
			for(Subsample sub : sample.getSubsamples())
			{
				totalImageCount+= sub.getImageCount();
				totalAnalysisCount+= sub.getAnalysisCount();
			}
			
			xml_str += "<imageCount>" + x.toXML(totalImageCount) + "</imageCount>";
			xml_str += "<analysisCount>" + x.toXML(totalAnalysisCount) + "</analysisCount>";
			Set<String> minerals= new HashSet<String>(); //a set of the string names of all the minerals that have been analyzed
			Boolean bulkRock= false;
			for(Subsample sub: sample.getSubsamples())
			{
			Set<ChemicalAnalysis> chem= sub.getChemicalAnalyses();
				for(ChemicalAnalysis chemAnalysis : chem)
				{
					Mineral min= chemAnalysis.getMineral();
					
					if(chemAnalysis.getMineral()!=null) //this analysis was done on a mineral
					{
						minerals.add(min.getName());
					}
					else //a bulk rock analysis was done
					{
						bulkRock=true;
					}
				}
			}
			//convert the array of analysis materials to xml
			xml_str += "<materials>";
			for(String materialName : minerals) 
			{
				xml_str += x.toXML(materialName);
			}
			xml_str += "</materials>";
			//convert the bulk rock value to xml
			xml_str += "<bulkRock>" + x.toXML(bulkRock) + "</bulkRock>";
			xml_str += "</subsamples>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
		}
		catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	} 
	//DONE
	public static void regions(HttpServletResponse response, Session session){
		try {
			RegionDAO service = new RegionDAO(session);
			User u= new User();
			int userID;
			/*if(username!="")
			{
				u.setName(username);
				userID= u.getId();
				session.enableFilter("hasSamplePublicOrUser").setParameter("userId", userID);
			}
			else
			{*/
				session.enableFilter("hasSamplePublicOrUser").setParameter("userId", 0);
			//}
			final XStream x = new XStream(new JsonHierarchicalStreamDriver());
			//Set<String> regionNames=service.viewableNamesForUser(0);
			Object[] regionNames= service.allNames();
			List<String>regionList= new ArrayList<String>();
			for (Object region : regionNames){
				regionList.add((String) region);
			}

			java.util.Collections.sort(regionList);
			//x.toXML(service.viewableNamesForUser(0),response.getWriter());
			x.toXML(regionList, response.getWriter());
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	//DONE
	public static void sampleInfo(Session session, List<Long> sampleIds, final HttpServletResponse response){
		try {
			SampleDAO s= new SampleDAO(session);
			final XStream x = new XStream();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
			xml_str += "<set>";
			for (Long id : sampleIds){
				Sample sample= new Sample();
				sample.setId(id);
				xml_str += x.toXML(s.fill(sample));
			}
			xml_str += "</set>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	//DONE
	public static void getSearchCriteria(final Results<Sample> results, final HttpServletResponse response) {
		try{
			final XStream x = new XStream();
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";
		xml_str = "<searchCriteria>";
		xml_str += "<resultCount>" + x.toXML(results.getCount()) + "</resultCount>";
		
		
		//create a list of all minerals in all samples returned from search
		Set <SampleMineral> currentMinerals = new HashSet();
		List sampleList= results.getList();
		Double maxLong= -180.0; //initialize to something smaller than it could possibly be so something is guaranteed to be larger
		Double maxLat= -90.0;
		Double minLong= 180.0;  //initialize to somthing bigger than it could be so something is guaranteed to be smaller
		Double minLat= 90.0;
		
		for(int i=0; i<sampleList.size(); i++)
		{
			Sample samp= (Sample) sampleList.get(i);
			if(((Point)samp.getLocation()).y > maxLat)
			{
				maxLat=((Point)samp.getLocation()).y;
			}
			if(((Point)samp.getLocation()).x > maxLong)
			{
				maxLong=((Point)samp.getLocation()).x;
			}
			if(((Point)samp.getLocation()).y < minLat)
			{
				minLat=((Point)samp.getLocation()).y;
			}
			if(((Point)samp.getLocation()).x < minLong)
			{
				minLong=((Point)samp.getLocation()).x;
			}
			Set<SampleMineral> mins= samp.getMinerals();
			for(SampleMineral m : mins)
			{
				boolean flag=false;
				for(SampleMineral currMin : currentMinerals)
				{
					if(currMin.getMineral().getId() == m.getMineral().getId())
					{
						flag=true;
					}
				}
				if(flag==false)
				{
					currentMinerals.add(m);
				}
			}
		}
		//return xml for all the minerals
		xml_str += "<criteriaMinerals>";
		for(SampleMineral min : currentMinerals)
		{
			xml_str += x.toXML(min.getName());
			//response.getWriter().write(min.getName());
		}
		xml_str += "</criteriaMinerals>";
		
		//create a list of all metamorphic grades in all samples returned from search
		
		Set <MetamorphicGrade> currentMetGrades = new HashSet();
		for(int i=0; i<sampleList.size(); i++)
		{
			Sample samp= (Sample) sampleList.get(i);
			Set<MetamorphicGrade> metGrades= samp.getMetamorphicGrades();
			for(MetamorphicGrade m : metGrades)
			{
				boolean flag=false;
				for(MetamorphicGrade met : currentMetGrades)
				{
					if(met.getId() == m.getId())
					{
						flag=true;
					}
				}
				if(flag==false)
				{
					currentMetGrades.add(m);
				}
			}
		}
		xml_str += "<criteriaMetGrades>";
		//return xml for all the metamorhpic grades
		for(MetamorphicGrade met : currentMetGrades)
		{
			xml_str += x.toXML(met.getName());
		}
		xml_str += "</criteriaMetGrades>";
		
		
		//create a list of all rock types of all samples returned from the search
		Set <RockType> currentRockTypes = new HashSet();
		for(int i=0; i<sampleList.size(); i++)
		{
			Sample samp= (Sample) sampleList.get(i);
			RockType rock= samp.getRockType();
			boolean flag=false;
			for(RockType r : currentRockTypes)
			{
				if(r.getId()== rock.getId())
				{
					flag=true;
				}
			}
			if(flag==false)
			{
				currentRockTypes.add(rock);
			}
		}
		//return xml for all the rock types
		xml_str += "<criteriaRockTypes>";
		for(RockType rt : currentRockTypes)
		{
			xml_str += x.toXML(rt);
		}
		xml_str += "</criteriaRockTypes>";
		
		//create a list of all owners of all samples returned from the search
		Set <String> currentOwners = new HashSet(); 
		for(int i=0; i<sampleList.size(); i++)
		{
			Sample samp= (Sample) sampleList.get(i);
			String owner= samp.getOwner().getName();
			boolean flag=false;
			for(String o : currentOwners)
			{
				if(o.equals(owner))
				{
					flag=true;
				}
			}
			if(flag==false)
			{
				currentOwners.add(owner);
			}
		}
		//return xml for all the rock types
		xml_str += "<criteriaOwners>";
		for(String o : currentOwners)
		{
			xml_str += x.toXML(o);
		}
		xml_str += "</criteriaOwners>";
		xml_str += "<maxLat>" + x.toXML(maxLat) + "</maxLat>";
		xml_str += "<minLat>" + x.toXML(minLat) + "</minLat>";
		xml_str += "<maxLong>" + x.toXML(maxLong) + "</maxLong>";
		xml_str += "<minLong>" + x.toXML(minLong) + "</minLong>";
		
		xml_str += "</searchCriteria>";
		
		JSONObject objtemp = XML.toJSONObject(xml_str);
		response.getWriter().write(objtemp.toString(2));
		response.getWriter().flush();
	}
		
		catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
		
	}
	//DONE
	public static void outputSearchJSON(final Results<Sample> results, final HttpServletResponse response){
		try{
			final XStream x = new XStream(new DomDriver());
			//final XStream x_json = new XStream(new JsonHierarchicalStreamDriver());
			String xml_str = "";

			xml_str += "<set>";
			xml_str += "<resultCount>" + x.toXML(results.getCount()) + "</resultCount>";
		
			for (Sample sample : results.getList()){			
				xml_str += "<sample>";
				xml_str += createXMLElement("number",x.toXML(sample.getNumber()));
				xml_str += createXMLElement("id",x.toXML(sample.getId()));
				xml_str += createXMLElement("rockType",x.toXML(sample.getRockType()));
				xml_str += "<minerals>";
				for (SampleMineral m : sample.getMinerals())
					xml_str += x.toXML(m.getName());
				xml_str += "</minerals>";
				xml_str += "<metamorphicGrades>";
				for (MetamorphicGrade m : sample.getMetamorphicGrades())
					xml_str += x.toXML(m.getName());
				xml_str += "</metamorphicGrades>";
				xml_str += "<regions>";
				for(edu.rpi.metpetdb.client.model.Region r :sample.getRegions())
				{
					xml_str += x.toXML(r.getName());
				}
				xml_str += "</regions>";
				xml_str += createXMLElement("publicData",x.toXML(sample.isPublicData()));
				xml_str += x.toXML(sample.getLocation());
				xml_str += createXMLElement("owner",x.toXML(sample.getOwner().getName()));
				xml_str += "<description>";
				xml_str += x.toXML(sample.getDescription());
				xml_str += "</description>";
				
				xml_str += "</sample>";
			}
			xml_str += "</set>";
			
			JSONObject objtemp = XML.toJSONObject(xml_str);
			response.getWriter().write(objtemp.toString(2));
			response.getWriter().flush();
		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	/*private Results<Sample> search(final Collection<String> regions, Session session){
		try{
			SearchSample s = new SearchSample();
			for (String r : regions){
				System.out.print(r);
				s.addRegion(r);
			}	
			return search(s, session);
		}
		catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}
		
	}*/
	
	//private Results<Sample> search(final SearchSample s, Session session){
	//private Results<Sample> search(final String region, Session session){
	

	
	public static Results<Sample> search(Session session, int publicStatus, Set<String> owners, Set<RockType> rockTypes,
			Set<MetamorphicGrade> metamorphicGrades, Set<Mineral> minerals, String region, String username, PaginationParameters p, final HttpServletResponse response){
		
		try{
			//if any search criteria have been specified (owners, rocktypes, metamorphic grades, or minerals)
			//then set searchSample to have these attributes
			SearchSample ss = new SearchSample();
			User u= new User();
			if(region!=null && region.length()!=0)
			{
				ss.addRegion(region);
			}
			if(!owners.isEmpty())
			{
				ss.setOwners(owners);
			}
			if(!rockTypes.isEmpty())
			{
				ss.setPossibleRockTypes(rockTypes);
			}
			if(!metamorphicGrades.isEmpty())
			{
				ss.setMetamorphicGrades(metamorphicGrades);
			}
			if(!minerals.isEmpty())
			{
				ss.setMinerals(minerals);
			}
			// 0 = public and private, 1 = public only, 2 = private only
			if(username!="")
			{
				UserServiceImpl Uimp= new UserServiceImpl();
				u= Uimp.details(username);
				u = new UserDAO(session).fill(u);
				ss.setGetPublic(publicStatus);
			}
			if(p==null)
			{
				return SearchDb.sampleSearch(null, ss, u, session);
			}
			else
			{
				return SearchDb.sampleSearch(p, ss, u, session);
			}
		}
		catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}	
	public static Results<Sample> search(final Double north, final Double south, final Double east, final Double west, Session session, int publicStatus,
				Set<String> owners, Set<RockType> rockTypes, Set<MetamorphicGrade> metamorphicGrades, Set<Mineral> minerals, String region, String username, PaginationParameters p){		
		try{
			SearchSample s = new SearchSample();
			User u= new User();
			final LinearRing[] ringArray = new LinearRing[1];
			final Point[] points = new Point[5];
			final Point p1 = new Point();
			p1.x = west;
			p1.y = south;

			final Point p2 = new Point();
			p2.x = west;
			p2.y = north;

			final Point p3 = new Point();
			p3.x = east;
			p3.y = north;

			final Point p4 = new Point();
			p4.x = east;
			p4.y = south;

			
			points[0] = p1;
			points[1] = p2;
			points[2] = p3;
			points[3] = p4;
			points[4] = p1;
			final LinearRing ring = new LinearRing(points);
			ringArray[0] = ring;
			org.postgis.Polygon boundingBox = new org.postgis.Polygon(ringArray);
			boundingBox.srid = MpDbConstants.WGS84;
			boundingBox.dimension = 2;
			
			s.setBoundingBox(boundingBox);
			//return search(s, session);'
		
			if(!owners.isEmpty())
			{
				s.setOwners(owners);
			}
			if(!rockTypes.isEmpty())
			{
				s.setPossibleRockTypes(rockTypes);
			}
			if(!metamorphicGrades.isEmpty())
			{
				s.setMetamorphicGrades(metamorphicGrades);
			}
			if(!minerals.isEmpty())
			{
				s.setMinerals(minerals);
			}
			if(username!="")
			{
				UserServiceImpl Uimp= new UserServiceImpl();
				u= Uimp.details(username);
				u = new UserDAO(session).fill(u);
				s.setGetPublic(publicStatus);
			}
			if(p==null)
			{
				return SearchDb.sampleSearch(null, s, u, session);
			}
			else
			{
				return SearchDb.sampleSearch(p, s, u, session);
			}
			//return SearchDb.sampleSearch(null, s, null, session);

		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	public static String createXMLElement(final String tag, final String value){
		return "<"+ tag + ">" + value + "</" + tag + ">";
	}
	
}
