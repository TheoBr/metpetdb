package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.server.DataStore;
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
			final SampleParser sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/easy_samples.xls"));
			try {
				sp.initialize();
			} catch (final NoSuchMethodException nsme) {
				nsme.printStackTrace();
				fail("NoSuchMethodException");
				// } catch (final ValidationException ve) {
				// ve.printStackTrace();
			} catch (final InvalidFormatException ife) {
				ife.printStackTrace();
			}
			// final List<List<String>> output = sp.validate(
			// new HashSet<SampleParser.Index>(), new HashSet<Integer>(),
			// new HashSet<Integer>());
			// assertEquals(29, output.size());
			sp.parse();
			assertEquals(26, sp.getSamples().size());
			// we'll test saving with a client-side test
		} catch (final IOException ioe) {
			fail("IO Exception");
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
			try {
				sp.initialize();
			} catch (final NoSuchMethodException e) {
				fail("NoSuchMethodException");
			} catch (final InvalidFormatException ife) {
				ife.printStackTrace();
			}
			sp.parse();

			final List<SampleDTO> samples = sp.getSamples();

		} catch (final IOException ioe) {
			fail("IO Exception");
		}
	}

	public void test_uploaded_files_table() {
		final Session s = DataStore.open();
		final Transaction t = s.beginTransaction();
		s
				.createSQLQuery(
						"INSERT INTO uploaded_files(uploaded_file_id, hash, filename, time) VALUES(nextval('uploaded_file_seq'), :hash, :filename, NOW())")
				.setParameter("hash", "hash").setParameter("filename",
						"filename").executeUpdate();
		t.commit();
	}
}
