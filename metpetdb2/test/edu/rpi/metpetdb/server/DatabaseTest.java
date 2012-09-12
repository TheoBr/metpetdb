package edu.rpi.metpetdb.server;


import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import edu.rpi.metpetdb.server.dao.ElementDAO;
import edu.rpi.metpetdb.server.dao.OxideDAO;
import edu.rpi.metpetdb.server.dao.SampleDAO;
import edu.rpi.metpetdb.server.dao.UserDAO;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.server.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.server.model.Element;
import edu.rpi.metpetdb.server.model.Image;
import edu.rpi.metpetdb.server.model.ImageType;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.MineralType;
import edu.rpi.metpetdb.server.model.Oxide;
import edu.rpi.metpetdb.server.model.Reference;
import edu.rpi.metpetdb.server.model.RockType;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.Subsample;
import edu.rpi.metpetdb.server.model.SubsampleType;
import edu.rpi.metpetdb.server.model.User;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;
import edu.rpi.metpetdb.server.security.Util;

@ContextConfiguration
public class DatabaseTest extends AbstractTransactionalJUnit38SpringContextTests

{

	Logger logger = LoggerFactory.getLogger(DatabaseTest.class);
	
	public DatabaseTest()
	{
		super();		
	}
	
	
/*	public void XXXtestConnectivity() throws SQLException
	{
		BasicDataSource bds = (BasicDataSource)applicationContext.getBean("dataSource");
		
		Connection conn = bds.getConnection();
		
		ResultSet rs = conn.createStatement().executeQuery("select CURRENT_DATE;");
		
		String date = null;
		
		if (rs.next())
		date = rs.getString(1);
		
		logger.info(date);
		
		assertNotNull(date);
		
	}
	
	@Transactional
	public void XXXtestHibernate() 
	{
		User user = new User(0L, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());
		
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");
		
		userDAO.saveUser(user);
	
		
		Long persistedUserId = user.getUser_id();
		
		User persistedUser = userDAO.loadUser(persistedUserId);
		
		
		
		assertNotNull(persistedUser.getUser_id());
		assertEquals(persistedUserId, persistedUser.getUser_id());
	}
	
	public void XXXtestCascadeBehavior()
	{
		
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");
		
		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());
		
		Role role = new Role(null, "Member");
		
		user.addRole(role);
		
		userDAO.saveUser(user);
	
		
		Long persistedUserId = user.getUser_id();
		
		User persistedUser = userDAO.loadUser(persistedUserId);


		assertEquals(persistedUser.getRoles().size(), 1);
		
	}
	
	public void XXXtestPersistSample()
	{
	
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");

		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		userDAO.saveUser(user);

		
		SampleDAO sampleDAO = (SampleDAO)applicationContext.getBean("sampleDAO");
		
		//point.dimension = 2;
		///WGS 84
		//point.srid = 4326;
		//point.x = 43.25;
		//point.y = 44.24;
		
		
		Point point = new Point(new Coordinate(43.25, 44.56), new PrecisionModel(PrecisionModel.FLOATING),  4326 );
	
		Sample sample = new Sample(null, 1, "SESARNUMB", point , "NUMBER", new Timestamp(System.currentTimeMillis()), null,  Boolean.TRUE, "description", "country",  "location",  0f );
		sample.setOwner(user);
		sample.setRockType(new RockType(null, "Rocky Road"));
		sample.setCollector("Foo Bar");

		
		sampleDAO.saveSample(sample);
		
		Long persistedSampleId = sample.getSample_Id();
		
		
		Sample persistedSample = sampleDAO.loadSample(persistedSampleId);
		
		assertNotNull(persistedSample);
		
		assertNotNull(persistedSample.getSample_Id());
		

	}

	
	public void XXXtestPersistSampleWithImages()
	{
	
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");

		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		userDAO.saveUser(user);

		
		SampleDAO sampleDAO = (SampleDAO)applicationContext.getBean("sampleDAO");
		
		//point.dimension = 2;
		///WGS 84
		//point.srid = 4326;
		//point.x = 43.25;
		//point.y = 44.24;
		
		
		Point point = new Point(new Coordinate(43.25, 44.56), new PrecisionModel(PrecisionModel.FLOATING),  4326 );
	
		Sample sample = new Sample(null, 1, "SESARNUMB", point , "NUMBER", new Timestamp(System.currentTimeMillis()), null,  Boolean.TRUE, "description", "country",  "location",  0f );
		sample.setOwner(user);
		sample.setRockType(new RockType(null, "Rocky Road"));
		sample.setCollector("Foo Bar");

		Image image = new Image(0, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		image.setImageType(new ImageType(0L, "GOOD", "GOOD"));
		sample.addImage(image);
		
		sampleDAO.saveSample(sample);
		
		Long persistedSampleId = sample.getSample_Id();
		
		
		Sample persistedSample = sampleDAO.loadSample(persistedSampleId);
		
		assertNotNull(persistedSample);
		
		assertNotNull(persistedSample.getSample_Id());
		
		assertEquals(sample.getImages().size(), 1);

	}
	
	public void XXXtestPersistSampleWithImagesAndSubsamples()
	{
	
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");

		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		userDAO.saveUser(user);

		
		SampleDAO sampleDAO = (SampleDAO)applicationContext.getBean("sampleDAO");
		
		//point.dimension = 2;
		///WGS 84
		//point.srid = 4326;
		//point.x = 43.25;
		//point.y = 44.24;
		
		
		Point point = new Point(new Coordinate(43.25, 44.56), new PrecisionModel(PrecisionModel.FLOATING),  4326 );
	
		Sample sample = new Sample(null, 1, "SESARNUMB", point , "NUMBER", new Timestamp(System.currentTimeMillis()), null,  Boolean.TRUE, "description", "country",  "location",  0f );
		sample.setOwner(user);
		sample.setRockType(new RockType(null, "Rocky Road"));
		sample.setCollector("Foo Bar");

		Image image = new Image(0, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		image.setImageType(new ImageType(0L, "GOOD", "GOOD"));
		sample.addImage(image);
		
		
		Subsample subsample = new Subsample(0L, sample, 1, "SUBBY", Boolean.TRUE, user);
		subsample.setType( new SubsampleType(0L, "SUBSAMPTYPE"));
		
		Image subimage = new Image(0, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		subimage.setImageType(new ImageType(0L, "GOODSUB", "GOODSUB"));

		subsample.addImage(subimage);
		
		sample.addSubsample(subsample);
		
		
		sampleDAO.saveSample(sample);
		
		Long persistedSampleId = sample.getSample_Id();
		
		
		Sample persistedSample = sampleDAO.loadSample(persistedSampleId);
		
		assertNotNull(persistedSample);
		
		assertNotNull(persistedSample.getSample_Id());
		
		assertEquals(persistedSample.getImages().size(), 1);

		assertEquals(persistedSample.getSubsamples().size(), 1);
		

		assertEquals(persistedSample.getSubsamples().iterator().next().getImages().size(), 1);
	}

	public void XXXtestPersistSampleWithImagesAndSubsamplesAndChemAnalyses()
	{
	
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");

		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		userDAO.saveUser(user);

		
		SampleDAO sampleDAO = (SampleDAO)applicationContext.getBean("sampleDAO");
		
		//point.dimension = 2;
		///WGS 84
		//point.srid = 4326;
		//point.x = 43.25;
		//point.y = 44.24;
		
		
		Point point = new Point(new Coordinate(43.25, 44.56), new PrecisionModel(PrecisionModel.FLOATING),  4326 );
	
		Sample sample = new Sample(null, 1, "SESARNUMB", point , "NUMBER", new Timestamp(System.currentTimeMillis()), null,  Boolean.TRUE, "description", "country",  "location",  0f );
		sample.setOwner(user);
		sample.setRockType(new RockType(null, "Rocky Road"));
		sample.setCollector("Foo Bar");

		Image image = new Image(0, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		image.setImageType(new ImageType(0L, "GOOD", "GOOD"));
		sample.addImage(image);
		
		
		Subsample subsample = new Subsample(0L, sample, 1, "SUBBY", Boolean.TRUE, user);
		subsample.setType( new SubsampleType(0L, "SUBSAMPTYPE"));
		
		Image subimage = new Image(0, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		subimage.setImageType(new ImageType(0L, "GOODSUB", "GOODSUB"));

		subsample.addImage(subimage);
		
		
		
		Reference reference = new Reference(0L, "REF");
		Mineral mineral = new Mineral(0L, 0L, "MIN");
//	 ADD CONSTRAINT minerals_fk_real_mineral_id FOREIGN KEY (real_mineral_id)
		   
		ChemicalAnalysis chemicalAnalysis = new ChemicalAnalysis(null, 1, 1, 0.0, 0.0, null, subsample, "My Method", "location", "analyst", new Timestamp(System.currentTimeMillis()), new Short("0"), "description", reference, mineral, Boolean.TRUE, 0.0, Boolean.TRUE, user, subsample.getSubsample_Id(), 0.0, 0.0);

	
		subsample.getChemicalAnalysis().add(chemicalAnalysis);
		
		
		sample.addSubsample(subsample);
		
		
		Element ele = new Element(null, "FOO", "BAR", "BAZ", 100, 0.0f);
		ele.getMineralTypes().add(new MineralType("FOO", null));

		
		sampleDAO.saveSample(sample);
		
		Long persistedSampleId = sample.getSample_Id();
		
		
		Sample persistedSample = sampleDAO.loadSample(persistedSampleId);
		
		assertNotNull(persistedSample);
		
		assertNotNull(persistedSample.getSample_Id());
		
		assertEquals(persistedSample.getImages().size(), 1);

		assertEquals(persistedSample.getSubsamples().size(), 1);
		

		assertEquals(persistedSample.getSubsamples().iterator().next().getImages().size(), 1);
		
		assertEquals(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().size(), 1);
		
		
		
	}
	
	
	public void XXXtestPersistElement()
	{
	
		ElementDAO elementDAO = (ElementDAO)applicationContext.getBean("elementDAO");

		
		Element ele = new Element(null, "FOO", "BAR", "BAZ", 100, 0.0f);
		ele.getMineralTypes().add(new MineralType("FOO", null));


		elementDAO.saveElement(ele);
		
		Long elementId = ele.getElement_Id();
		
		Element persistedElement = elementDAO.loadElement(elementId);
		
		assertEquals(persistedElement.getMineralTypes().size(), 1);
		
	}*/
	
	public void testPersistSampleWithImagesAndSubsamplesAndChemAnalysesWithElement()
	{
	
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");

		User user = new User(null, 1, "foo@foo.com", "Foo Bar", "123 Foo St.", "Albany", "NY", "US", "11111", "Baz", "bar@bar.com", Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		userDAO.saveUser(user);

		
		SampleDAO sampleDAO = (SampleDAO)applicationContext.getBean("sampleDAO");
		
		//point.dimension = 2;
		///WGS 84
		//point.srid = 4326;
		//point.x = 43.25;
		//point.y = 44.24;
		
		
		Point point = new Point(new Coordinate(43.25, 44.56), new PrecisionModel(PrecisionModel.FLOATING),  4326 );
	
		Sample sample = new Sample(null, 1, "SESARNUMB", point , "NUMBER", new Timestamp(System.currentTimeMillis()), null,  Boolean.TRUE, "description", "country",  "location",  0f );
		sample.setOwner(user);
		sample.setRockType(new RockType(null, "Rocky Road"));
		sample.setCollector("Foo Bar");

		Image image = new Image(null, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		image.setImageType(new ImageType(null, "GOOD", "GOOD"));
		sample.addImage(image);
		
		
		Subsample subsample = new Subsample(null, sample, 1, "SUBBY", Boolean.TRUE, user);
		subsample.setType( new SubsampleType(null, "SUBSAMPTYPE"));
		
		Image subimage = new Image(null, 1,  100, 100, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), sample, UUID.randomUUID().toString(), UUID.randomUUID().toString(), 0, UUID.randomUUID().toString(), user, Boolean.TRUE );
		
		subimage.setImageType(new ImageType(null, "GOODSUB", "GOODSUB"));

		subsample.addImage(subimage);
		
		
		
		Reference reference = new Reference(0L, "REF");
		Mineral mineral = new Mineral(0L, 0L, "MIN");
//	 ADD CONSTRAINT minerals_fk_real_mineral_id FOREIGN KEY (real_mineral_id)
		   
		ChemicalAnalysis chemicalAnalysis = new ChemicalAnalysis(null, 1, 1, 0.0, 0.0, null, subsample, "My Method", "location", "analyst", new Timestamp(System.currentTimeMillis()), new Short("0"), "description", reference, mineral, Boolean.TRUE, 0.0, Boolean.TRUE, user, subsample.getSubsample_Id(), 0.0, 0.0);

	
		subsample.getChemicalAnalysis().add(chemicalAnalysis);
		
		
		sample.addSubsample(subsample);
		

		ChemicalAnalysisElement element = new ChemicalAnalysisElement( 0.1,  0.0, "ABS", 0.0, 0.0, "ppm");
		
	
		
		
		ChemicalAnalysisOxide oxide = new ChemicalAnalysisOxide( 0.1, 0.0, "REL", 0.0, 0.0, "ppm");
	
		
		//PERSIST ELEMENT
		
		ElementDAO elementDAO = (ElementDAO)applicationContext.getBean("elementDAO");

		
		Element ele = new Element(null, "FOO", "BAR", "BAZ", 100, 0.0f);
		ele.getMineralTypes().add(new MineralType("FOO", null));


		elementDAO.saveElement(ele);
		
		Long elementId = ele.getElement_Id();
		
		Element persistedElement = elementDAO.loadElement(elementId);
		
		assertEquals(persistedElement.getMineralTypes().size(), 1);
		
		
		//PERSIST OXIDE
		
		OxideDAO oxideDAO = (OxideDAO)applicationContext.getBean("oxideDAO");
		
		Oxide ox  = new Oxide(null, new Long(0), "SPEC",  0.0f, new Long(0), 0.0f);
		ox.getMineralTypes().add(new MineralType("BAR", null));
		
		ox.setElement(persistedElement);
		
		oxideDAO.saveOxide(ox);
		
		Long oxideId = ox.getOxide_Id();
		
		Oxide persistedOxide = oxideDAO.loadOxide(oxideId);
		
		assertEquals(persistedOxide.getMineralTypes().size(), 1);
		
		//PERSIST CHEM_ANAL

		
		try
		{
		
		sampleDAO.saveSample(sample);
		}
		catch (RuntimeException re)
		{
			re.printStackTrace();
			
			throw re;
		}

		Long persistedSampleId = sample.getSample_Id();
		
		
		Sample persistedSample = sampleDAO.loadSample(persistedSampleId);


		
		//PERSIST CHEM_ANAL_ELEMENT
		
		ChemicalAnalysisElement.Pk pk = new ChemicalAnalysisElement.Pk();
		
		pk.setChemical_analysis_id(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().getChemical_analysis_id());
		pk.setElement_id(persistedElement.getElement_Id());
		
		
		element.setPk(pk);

		
		ChemicalAnalysisOxide.Pk opk = new ChemicalAnalysisOxide.Pk();
		
		opk.setChemical_analysis_id(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().getChemical_analysis_id());
		opk.setOxide_id(persistedOxide.getOxide_Id());
		
		
		oxide.setPk(opk);

		element.setChemicalAnalysis(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next());
		oxide.setChemicalAnalysis(sample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next());
		persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().addChemicalAnalysisElement(element);
		persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().addChemicalAnalysisOxide(oxide);

		
		try
		{
		
		sampleDAO.saveSample(persistedSample);
		}
		catch (RuntimeException re)
		{
			re.printStackTrace();
			
			throw re;
		}


		
		assertNotNull(persistedSample);
		
		assertNotNull(persistedSample.getSample_Id());
		
		assertEquals(persistedSample.getImages().size(), 1);

		assertEquals(persistedSample.getSubsamples().size(), 1);
		

		assertEquals(persistedSample.getSubsamples().iterator().next().getImages().size(), 1);
		
		assertEquals(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().size(), 1);
		
		
		
		assertEquals(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().getChemicalAnalysisElements().size(), 1);
		assertEquals(persistedSample.getSubsamples().iterator().next().getChemicalAnalysis().iterator().next().getChemicalAnalysisOxides().size(), 1);

	}




}
