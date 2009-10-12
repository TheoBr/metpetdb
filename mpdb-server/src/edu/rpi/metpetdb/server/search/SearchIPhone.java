package edu.rpi.metpetdb.server.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.RegionDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;


public class SearchIPhone extends HttpServlet{
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

	private Session session;
	private Set<String> owners= new HashSet();
	private Set<RockType> rockTypes= new HashSet();
	private Set<MetamorphicGrade> metamorphicGrades= new HashSet();
	private Set<Mineral> minerals= new HashSet();
	private String region= new String();
	private PaginationParameters p= new PaginationParameters();
	@Override

	/*protected void doPost(final HttpServletRequest request,
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
		if(scanner.hasNext("coordinates="))
		{
			scanner.next();
			double north= Double.valueOf(scanner.next().trim());
			double south= Double.valueOf(scanner.next().trim());
			double east= Double.valueOf(scanner.next().trim());
			double west= Double.valueOf(scanner.next().trim());
			
			System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
			outputSearchXML(search(north,south, east, west, session),response);
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
			regions.add(newRegion);
			outputSearchXML(search(regions, session),response);
		}
		else if(scanner.hasNext("sampleID="))
		{
			scanner.next();
			sampleIds.add(Long.parseLong(scanner.next().trim()));
			sampleInfo(sampleIds,response);
		}
		else if(scanner.hasNext("regions"))
		{
			regions(response);
		}
		else if(scanner.hasNext("rockTypes"))
		{
			rockTypes(response);
		}
		else if(scanner.hasNext("comments="))
		{
			scanner.next();
			//the number following comments= will be the id of the sample we want comments for
			long id= Long.parseLong(scanner.next().trim());
			comments(response, id);
		}
		else if(scanner.hasNext("subsampleInfo="))
		{
			scanner.next();
			//the number following subsampleInfo= will be the id of the sample we want comments for
			long id= Long.parseLong(scanner.next().trim());
			subsampleInfo(response, id);
		}
		else if(scanner.hasNext("thumbnails="))
		{
			scanner.next();
			//the number following the thumbnails= will be the id of the sample we want thumbnails for
			long id=Long.parseLong(scanner.next().trim());
			get_thumbnails(response, id);
		}
		else if(scanner.hasNext("largeImage="))
		{
			scanner.next();
			//the number following the largeImage= will be id of the image we want to enlarge
			long imageID= Long.parseLong(scanner.next().trim());
			get_large_image(response, imageID);
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
	}*/
	
	
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException  {
		response.setContentType("text/xml");
		
		List<Long> sampleIds = new ArrayList<Long>();
		session = DataStore.open();
		try{
			owners= new HashSet();
			rockTypes= new HashSet();
			metamorphicGrades= new HashSet();
			minerals= new HashSet();
			region= new String();
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
				p.setMaxResults(100);
			}
			else
			{
				p=null;	
			}
			if(request.getParameter(OWNER)!= null)
			{
				String tempOwners[]  = request.getParameterValues(OWNER);
				List list = Arrays.asList(tempOwners);
				owners  = new HashSet(list);
				//outputSearchXML(search(session), response);
			}
			
			if(request.getParameter(ROCK_TYPE)!= null)
			{	
				String tempRockTypes[]= request.getParameterValues(ROCK_TYPE);
				//loop through the strings of rock types and convert them to rock type objects
				for(int i=0; i<tempRockTypes.length; i++)
				{
					RockType rt= new RockType(tempRockTypes[i]);
					rockTypes.add(rt);
				}
				//outputSearchXML(search(session), response);
			}
			if(request.getParameter(METAMORPHIC_GRADE)!= null)
			{
				String tempMetGrades[]=request.getParameterValues(METAMORPHIC_GRADE);
				for(int i=0; i<tempMetGrades.length; i++)
				{
					MetamorphicGrade mg= new MetamorphicGrade(tempMetGrades[i]);
					metamorphicGrades.add(mg);
				}
				//outputSearchXML(search(session), response);
			}
			if(request.getParameter(MINERAL)!= null)
			{
				String tempMinerals[]= request.getParameterValues(MINERAL);
				for(int i=0; i<tempMinerals.length; i++)
				{
					Mineral min= new Mineral();
					min.setName(tempMinerals[i]);
					minerals.add(min);
				}
				//outputSearchXML(search(session), response);
			}
			
			// If there is a GET string for latitude and longitude then it is a search
			if (request.getParameter(NORTH_PARAMETER) != null && request.getParameter(SOUTH_PARAMETER) != null && request.getParameter(WEST_PARAMETER)!= null && request.getParameter(EAST_PARAMETER) != null) {
				double north = Double.parseDouble(request.getParameterValues (NORTH_PARAMETER)[0]);
				double south = Double.parseDouble(request.getParameterValues(SOUTH_PARAMETER)[0]);
				double west = Double.parseDouble(request.getParameterValues(WEST_PARAMETER)[0]);
				double east= Double.parseDouble(request.getParameterValues(EAST_PARAMETER)[0]);
				
				System.out.println("iPhone query: north = " + north + "south = " + south + "west = " + west + "east =" + east);
				outputSearchXML(search(north,south, east, west, session),response);
			} else if (request.getParameter(SEARCH_REGIONS) != null){
			//String region = new String();
				for (String s : request.getParameterValues(SEARCH_REGIONS)){
					if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
						region=(s.substring(1, s.length()-1));
					}
				}
				//outputSearchXML(search(region, session),response);
				outputSearchXML(search(session), response);
			}
			//if search criteria were entered but a search region or search box was not, a seperate search must be done
			else if(!minerals.isEmpty() || !owners.isEmpty() || !rockTypes.isEmpty() || !metamorphicGrades.isEmpty())
			{
				outputSearchXML(search(session), response);
			}
			else if (request.getParameter(SAMPLE_ID) != null){
				for (String id : request.getParameterValues(SAMPLE_ID))
					sampleIds.add(Long.parseLong(id));
				sampleInfo(sampleIds,response);
			} else if (request.getParameter(REGIONS) != null){
				if (request.getParameterValues(REGIONS)[0].equalsIgnoreCase("t")){
					regions(response);
				}
			} /*else if (request.getParameter(ROCK_TYPES) != null){
				if (request.getParameterValues(ROCK_TYPES)[0].equalsIgnoreCase("t")){
					rockTypes(response);
				}
			}*/
			else if (request.getParameter(COMMENTS) != null){
			
				long id= Long.parseLong(request.getParameterValues(COMMENTS)[0]);
				comments(response, id);
			}
			else if (request.getParameter(SUBSAMPLE_INFO)!= null)
			{
				//using the sample id, get a summary of the subsample and analysis information
				long id= Long.parseLong(request.getParameterValues(SUBSAMPLE_INFO)[0]);
				subsampleInfo(response, id);
			}
			else if (request.getParameter(THUMBNAILS)!=null)
			{
				long id= Long.parseLong(request.getParameterValues(THUMBNAILS)[0]);
				get_thumbnails(response, id);
			}
			else if (request.getParameter(LARGE_IMAGE)!=null)
			{
				//the id that is passed into the url here is the id of the image, not the sample
				long imageID= Long.parseLong(request.getParameterValues(LARGE_IMAGE)[0]);
				get_large_image(response, imageID);
			}
		}
		catch(Exception e){
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
	public class imageComparator implements Comparator {
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

	private void get_thumbnails(HttpServletResponse response, long id)
	{
		try{
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample=s.fill(sample);
			Set<edu.rpi.metpetdb.client.model.Image> images= sample.getImages();
			List<edu.rpi.metpetdb.client.model.Image> imageList= new ArrayList(images);
			
			
			final XStream x = new XStream();
			
			
			//after all the samples images have been output, display subsample images
			Set<Subsample> subsamples= sample.getSubsamples();
			//add the subsample images to the imageList before sorting
			for(Subsample sub : subsamples)
			{
				Set<edu.rpi.metpetdb.client.model.Image> subImages= sub.getImages();
				imageList.addAll(subImages);
			}
			java.util.Collections.sort(imageList, new imageComparator());
			response.getWriter().write("<thumbnails>");
				for(edu.rpi.metpetdb.client.model.Image im : imageList)
				{
					final String subImagePath= im.getChecksum64x64();
				
					response.getWriter().write("<image>");
					x.toXML(subImagePath, response.getWriter());
					response.getWriter().write("</image>");
					  response.getWriter().write("<imageID>");
				    x.toXML(im.getId(), response.getWriter());
				    response.getWriter().write("</imageID>");
				    response.getWriter().write("<imageType>");
				    x.toXML(im.getImageType(), response.getWriter());
				    response.getWriter().write("</imageType>");
				}
			 response.getWriter().write("<imageCount>");
			 x.toXML(sample.getImageCount(), response.getWriter());
			 response.getWriter().write("</imageCount>");
			response.getWriter().write("</thumbnails>");
		} catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}	}
	
	private void get_large_image(HttpServletResponse response, long imageID)
	{
		try{
			final XStream x = new XStream();
			response.getWriter().write("<image>");
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
			response.getWriter().write("<path>");
			x.toXML(imagePath, response.getWriter());
			response.getWriter().write("</path>");
			response.getWriter().write("<filename>");
			x.toXML(image.getFilename(), response.getWriter());
			response.getWriter().write("</filename>");
			response.getWriter().write("</image>");
		}	
		catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	
	private void comments(HttpServletResponse response, long id){
		try{
			final XStream x = new XStream();
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample = s.fill(sample);
			response.getWriter().write("<comments>");
			
			for (SampleComment sc : sample.getComments())
			 	x.toXML(sc.getText() , response.getWriter());
			response.getWriter().write("</comments>");
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	private void subsampleInfo(HttpServletResponse response, long id ){
		try{
			final XStream x= new XStream();
			SampleDAO s= new SampleDAO(session);
			Sample sample= new Sample();
			sample.setId(id);
			sample = s.fill(sample);
			//get the count of the subsamples for the sample
			Set<Subsample> subsamples= sample.getSubsamples();
			int count= subsamples.size();
				//new HashSet<Subsample>();
			response.getWriter().write("<subsamples>");
			x.toXML(count, response.getWriter()); //number of subsamples for the sample
			int totalImageCount=0;
			int totalAnalysisCount=0;
			for(Subsample sub : sample.getSubsamples())
			{
				totalImageCount+= sub.getImageCount();
				totalAnalysisCount+= sub.getAnalysisCount();
			}
			response.getWriter().write("<imageCount>");
			x.toXML(totalImageCount, response.getWriter());
			response.getWriter().write("</imageCount>");
			response.getWriter().write("<analysisCount>");
			x.toXML(totalAnalysisCount, response.getWriter());
			response.getWriter().write("</analysisCount>");
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
			response.getWriter().write("<materials>");
			for(String materialName : minerals) 
			{
				x.toXML(materialName, response.getWriter());
			}
			response.getWriter().write("</materials>");
			//convert the bulk rock value to xml
			response.getWriter().write("<bulkRock>");
			x.toXML(bulkRock, response.getWriter());
			response.getWriter().write("</bulkRock>");
			response.getWriter().write("</subsamples>");

		}
		catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	} 
	private void regions(HttpServletResponse response){
		try {
			RegionDAO service = new RegionDAO(session);
			session.enableFilter("hasSamplePublicOrUser").setParameter("userId", 0);
			final XStream x = new XStream();
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
	
	private void sampleInfo(List<Long> sampleIds, final HttpServletResponse response){
		try {
			SampleDAO s= new SampleDAO(session);
			final XStream x = new XStream();
			response.getWriter().write("<set>");
			for (Long id : sampleIds){
				Sample sample= new Sample();
				sample.setId(id);
				x.toXML(s.fill(sample),response.getWriter());
			}
			response.getWriter().write("</set>");
		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	private void outputSearchXML(final Results<Sample> results, final HttpServletResponse response){
		try{
			final XStream x = new XStream();

			response.getWriter().write("<set>");
			response.getWriter().write("<resultCount>");
			x.toXML(results.getCount(), response.getWriter());
			response.getWriter().write("</resultCount>");
			for (Sample sample : results.getList()){			
				response.getWriter().write("<sample>");
				response.getWriter().write(createXMLElement("number",x.toXML(sample.getNumber())));
				response.getWriter().write(createXMLElement("id",x.toXML(sample.getId())));
				response.getWriter().write(createXMLElement("rockType",x.toXML(sample.getRockType())));
				response.getWriter().write("<minerals>");
				for (SampleMineral m : sample.getMinerals())
					x.toXML(m.getName(),response.getWriter());
				response.getWriter().write("</minerals>");
				response.getWriter().write("<metamorphicGrades>");
				for (MetamorphicGrade m : sample.getMetamorphicGrades())
					x.toXML(m.getName(),response.getWriter());
				response.getWriter().write("</metamorphicGrades>");
				response.getWriter().write("<regions>");
				for(edu.rpi.metpetdb.client.model.Region r :sample.getRegions())
				{
					x.toXML(r.getName(), response.getWriter());
				}
				response.getWriter().write("</regions>");
				response.getWriter().write(createXMLElement("publicData",x.toXML(sample.isPublicData())));
				x.toXML(sample.getLocation(),response.getWriter());
				response.getWriter().write(createXMLElement("owner",x.toXML(sample.getOwner().getName())));
				response.getWriter().write("<description>");
				x.toXML(sample.getDescription(), response.getWriter());
				response.getWriter().write("</description>");
				
				response.getWriter().write("</sample>");
			}
			response.getWriter().write("</set>");
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
	private Results<Sample> search(Session session){
		
		try{
			//if any search criteria have been specified (owners, rocktypes, metamorphic grades, or minerals)
			//then set searchSample to have these attributes

			SearchSample ss = new SearchSample();
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
			if(p==null)
			{
				return SearchDb.sampleSearch(null, ss, null, session);
			}
			else
			{
				return SearchDb.sampleSearch(p, ss, null, session);
			}
		}
		catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	private Results<Sample> search(final Double north, final Double south, final Double east, final Double west, Session session){		
		try{
			SearchSample s = new SearchSample();
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
			//return search(s, session);
			return SearchDb.sampleSearch(null, s, null, session);

		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	private String createXMLElement(final String tag, final String value){
		return "<"+ tag + ">" + value + "</" + tag + ">";
	}
}
