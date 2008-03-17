package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.IOException;

import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.User;

public class BulkUploadTest extends DatabaseTestCase {

	private SampleParser sp;

	public BulkUploadTest() {
		super("test-data/test-sample-data.xml");

	}

	@Test
	public void test_easy() {
		try {
			final Query q = InitDatabase.getSession().getNamedQuery(
					"User.byUsername");
			q.setString("username", "anthony");
			final User u = (User) q.uniqueResult();
			sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/easy_samples.xls"), u);
			sp.initialize();
		} catch (IOException ioe) {
			fail("IO Exception");
		} catch (InvalidFormatException ife) {
			fail("Invalid Format Exception");
		}

		assertEquals(sp.getSamples().size(), 29);
		final Query sq = InitDatabase.getSession().getNamedQuery(
		"Sample.all,size");
		assertEquals(34, ((Number)sq.uniqueResult()).intValue());
		// 29 + the five from the sample data
	}

	@Test
	public void test_valhalla() {
		try {
			final Query q = InitDatabase.getSession().getNamedQuery(
					"User.byUsername");
			q.setString("username", "anthony");
			final User u = (User) q.uniqueResult();
			sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/Valhalla_samples_upload.xls"), u);
			sp.initialize();
		} catch (IOException ioe) {
			fail("IO Exception");
		} catch (InvalidFormatException ife) {
			fail("Invalid Format Exception");
		}

		assertEquals(sp.getSamples().size(), 29);
		final Query sq = InitDatabase.getSession().getNamedQuery(
		"Sample.all,size");
		assertEquals(34, ((Number)sq.uniqueResult()).intValue());
		
		// 29 + the five from the sample data
	}
}
