package edu.rpi.metpetdb.server.dao.permissions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.CannotLoadRoleChangeException;
import edu.rpi.metpetdb.client.error.security.CannotLoadPrivateDataException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageComment;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.MineralType;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.DatabaseTestCase;

/**
 * Verifies that the loading permissions for Anonymous users are correct, since Members
 * have the same loading permissions this tests them as well
 * @author anthony
 *
 */
public class AnonymousLoadingPermissionsTest extends DatabaseTestCase {

	public AnonymousLoadingPermissionsTest() {
		super("test-data/test-sample-data.xml");
	}

	@Before
	public void setUp() {
		super.setUp();
	}

	/**
	 * Attemps to load a private sample of another user and should fail with an
	 * exception, it is a NoSuchObjectException because the extra where clauses
	 * prohibit loading of other user's data
	 * 
	 * @throws Throwable
	 */
	@Test(expected = NoSuchObjectException.class)
	public void loadPrivateSample() throws Throwable {
		try {
			super.byId("Sample", 1);
		} catch (CallbackException c) {
			session.clear();
			throw c.getCause();
		}
	}

	/**
	 * @see AnonymousLoadingPermissionsTest#loadPrivateSample()
	 * @throws Throwable
	 */
	@Test(expected = NoSuchObjectException.class)
	public void loadPrivateSubsample() throws Throwable {
		try {
			super.byId("Subsample", 1);
		} catch (CallbackException c) {
			session.clear();
			throw c.getCause();
		}
	}

	/**
	 * @see AnonymousLoadingPermissionsTest#loadPrivateSample()
	 * @throws Throwable
	 */
	@Test(expected = NoSuchObjectException.class)
	public void loadPrivateChemicalAnalysis() throws Throwable {
		try {
			super.byId("ChemicalAnalysis", 1);
		} catch (CallbackException c) {
			session.clear();
			throw c.getCause();
		}
	}

	/**
	 * Tries to load a user's data
	 * @throws NoSuchObjectException 
	 * 
	 * @throws Throwable
	 */
	@Test
	public void loadOtherUser() throws NoSuchObjectException  {
		final User u = super.byId("User", 1);
		assertEquals(1, u.getId());
	}

	/**
	 * Attemps loading a public sample
	 * 
	 * @throws Throwable
	 */
	@Test
	public void loadPublicSample() throws Throwable {
		try {
			super.byId("Sample", PUBLIC_SAMPLE);
		} catch (CallbackException c) {
			session.clear();
			throw c.getCause();
		}
	}

	/**
	 * Tests that loading a region works
	 */
	@Test
	public void loadRegion() {
		final Session s = DataStore.open();
		final Region r = (Region) s.getNamedQuery("Region.byName")
				.setParameter("name", "test region").uniqueResult();
		assertEquals("test region", r.getName());
		s.close();
	}

	@Test
	public void loadMineral() throws NoSuchObjectException {
		final Mineral m = super.byId("Mineral", 1);
		assertEquals("Tectosilicates", m.getName());
	}

	@Test
	public void loadRockType() {
		final Session s = DataStore.open();
		final RockType rt = (RockType) s.getNamedQuery("RockType.byRockType")
				.setParameter("rockType", "Slate").uniqueResult();
		assertEquals("Slate", rt.getRockType());
		s.close();
	}

	@Test
	public void loadMetamorphicGrade() {
		final Session s = DataStore.open();
		final MetamorphicGrade mg = (MetamorphicGrade) s.getNamedQuery(
				"MetamorphicGrade.byName").setParameter("name", "Great Shape")
				.uniqueResult();
		assertEquals("Great Shape", mg.getName());
		s.close();
	}

	/**
	 * This sample comment should not be loaded because it is part of a private
	 * sample
	 * 
	 * @throws Throwable
	 */
	@Test(expected = CannotLoadPrivateDataException.class)
	public void loadPrivateSampleComment() throws Throwable {
		try {
			super.byId("SampleComment", 1);
		} catch (CallbackException c) {
			session.clear();
			throw c.getCause();
		}
	}

	/**
	 * This sample comment is part of a public sample so it can be loaded
	 * 
	 * @throws Throwable
	 */
	@Test
	public void loadPublicSampleComment() throws Throwable {
		final SampleComment sc = super.byId("SampleComment", 2);
		assertEquals("wowo", sc.getText());
	}
	
	/**
	 * Tests loading an image comment from an image that is private
	 * @throws Throwable
	 */
	@Test(expected = CannotLoadPrivateDataException.class)
	public void loadPrivateImageComment() throws Throwable {
		try {
			super.byId("ImageComment", 1);
		} catch(CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}
	
	/**
	 * this one attemps to load an image comment on a public image
	 * @throws Throwable
	 */
	@Test
	public void loadPublicImageComment() throws Throwable {
		final ImageComment ic = super.byId("ImageComment", 2);
		assertEquals("Zoey", ic.getText());
	}

	@Test
	public void loadReference() {
		final Session s = DataStore.open();
		final Reference ref = (Reference) s.getNamedQuery("Reference.byName")
				.setParameter("name", "this publication").uniqueResult();
		assertEquals("this publication", ref.getName());
		s.close();
	}

	@Test(expected = CannotLoadPrivateDataException.class)
	public void loadPrivateImage() throws Throwable {
		try {
			super.byId("Image", 1);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}

	@Test
	public void loadPublicImage() throws NoSuchObjectException {
		final Image i = super.byId("Image", 2);
		assertTrue(i.isPublicData());
	}
	
	@Test
	public void loadElement() {
		final Session s = DataStore.open();
		final Element element = (Element) s.getNamedQuery("Element.byName")
				.setParameter("name", "aluminium").uniqueResult();
		assertEquals("Aluminium", element.getName());
		s.close();
	}
	
	@Test
	public void loadOxide() {
		final Session s = DataStore.open();
		final Oxide oxide = (Oxide) s.getNamedQuery("Oxide.bySpecies")
				.setParameter("species", "Al2O3").uniqueResult();
		assertEquals("Al2O3", oxide.getSpecies());
		s.close();
	}
	
	@Test
	public void loadImageType() {
		final Session s = DataStore.open();
		final ImageType imageType = (ImageType) s.getNamedQuery("ImageType.byImageType")
				.setParameter("imageType", "Map").uniqueResult();
		assertEquals("Map", imageType.getImageType());
		s.close();
	}
	
	@Test
	public void loadSubsampleType() {
		final Session s = DataStore.open();
		final SubsampleType subsampleType = (SubsampleType) s.getNamedQuery("SubsampleType.bySubsampleType")
				.setParameter("subsampleType", "Thin Section").uniqueResult();
		assertEquals("Thin Section", subsampleType.getSubsampleType());
		s.close();
	}
	
	@Test
	public void loadMineralType() {
		final Session s = DataStore.open();
		final MineralType mineralType = (MineralType) s.getNamedQuery("MineralType.byName")
				.setParameter("name", "Silicates").uniqueResult();
		assertEquals("Silicates", mineralType.getName());
		s.close();
	}

	@Test
	public void loadRole() throws NoSuchObjectException {
		final Role r = super.byId("Role", 1);
		assertEquals("Member", r.getRoleName());
	}
	
	@Test(expected = CannotLoadRoleChangeException.class)
	public void loadPendingRole() throws Throwable {
		try {
			super.byId("PendingRole", 1);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}
	
	//TODO implement a test for loading a project
	//TODO implement a test of grids (image map)
	//TODO implement a test for image on grid (image map)
}
