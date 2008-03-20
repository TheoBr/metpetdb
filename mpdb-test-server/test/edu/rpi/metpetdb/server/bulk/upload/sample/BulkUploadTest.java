package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.User;

public class BulkUploadTest extends DatabaseTestCase {

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
			final SampleParser sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/easy_samples.xls"));
			sp.initialize();
			final List<List<String>> output = sp.validate(
					new HashSet<SampleParser.Index>(), new HashSet<Integer>(),
					new HashSet<Integer>());
			assertEquals(29, output.size());
			sp.parse();
			assertEquals(29, sp.getSamples().size());
			// test saving with client-side test
		} catch (final IOException ioe) {
			fail("IO Exception");
		} catch (final InvalidFormatException ife) {
			ife.printStackTrace();
			fail("Invalid Format Exception");
		} catch (final HibernateException he) {
			he.printStackTrace();
			fail("Hibernate Exception");
		}
	}

	@Test
	public void test_valhalla() {
		try {
			final Query q = InitDatabase.getSession().getNamedQuery(
					"User.byUsername");
			q.setString("username", "anthony");
			final User u = (User) q.uniqueResult();
			final SampleParser sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/Valhalla_samples_upload.xls"));
			sp.initialize();
			sp.parse();

			assertEquals(29, sp.getSamples().size());
			final Query sq = InitDatabase.getSession().getNamedQuery(
					"Sample.all,size");
			assertEquals(38, ((Number) sq.uniqueResult()).intValue());
		} catch (final IOException ioe) {
			fail("IO Exception");
		} catch (final InvalidFormatException ife) {
			ife.printStackTrace();
			fail("Invalid Format Exception");
		}
	}
}
