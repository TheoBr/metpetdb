package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;

import junit.framework.TestCase;
import net.sf.hibernate4gwt.core.HibernateBeanManager;
import net.sf.hibernate4gwt.core.hibernate.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;

public class BulkUploadTest extends TestCase {

	public BulkUploadTest() {
		// super("test-data/test-sample-data.xml");
	}

	public void setUp() {
		// Locate and set Valid Potential Minerals for the parser
		final Session s = DataStore.open();
		Collection<Mineral> minerals;
		try {
			minerals = (new MineralDAO(s).getAll());
			SampleParser.setMinerals(minerals);
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MpDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Element> elements;
		List<Oxide> oxides;
		try {
			elements = ((new ElementDAO(s)).getAll());
			oxides = ((new OxideDAO(s)).getAll());
			AnalysisParser.setElements(elements);
			AnalysisParser.setOxides(oxides);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MpDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s.close();
		HibernateBeanManager.getInstance().setPersistenceUtil(
				new HibernateUtil() {
					@Override
					public boolean isPersistentClass(Class<?> clazz) {
						if (clazz.equals(Results.class)) {
							return true;
						} else {
							return super.isPersistentClass(clazz);
						}
					}
				});
		HibernateBeanManager.getInstance().setSessionFactory(
				DataStore.getFactory());
		DataStore.setBeanManager(HibernateBeanManager.getInstance());
	}


	@Test
	public void testUploadNewSamples() throws FileNotFoundException,
			MpDbException {
		System.out.println("testUploadNewSamples");
		final SampleParser sp = new SampleParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + "samples_tests4.xls"));
		sp.parse();
		final Map<Integer, Sample> samples = sp.getSamples();
		final Session session = DataStore.open();
		session.beginTransaction();
		final User u = new User();
		u.setId(1);
		for (Entry<Integer, Sample> s : samples.entrySet()) {
			try {
				System.out.println(s.getValue().getAlias());
				DataStore.getInstance().getDatabaseObjectConstraints()
						.validate(s.getValue());
			} catch (ValidationException e) {
				e.printStackTrace();
			}

		}
	}


	public void testNewUploadAnalyses() throws ServletException,
			InvalidFormatException, LoginRequiredException, IOException,
			MpDbException {
		final AnalysisParser ap = new AnalysisParser(new FileInputStream(
				MpDbServlet.getFileUploadPath()
						+ "PrivateExampleUpload_analyses.xls"));
		ap.parse();
		final Map<Integer, ChemicalAnalysis> analyses = ap
				.getChemicalAnalyses();
		for (Entry<Integer, ChemicalAnalysis> s : analyses.entrySet()) {
			try {
			DataStore.getInstance().getDatabaseObjectConstraints().validate(
					s.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// @Test
	// public void test_easy() {
	// try {
	// final SampleParser sp = new SampleParser(new FileInputStream(
	// "../mpdb-common/sample-data/easy_samples.xls"));
	// try {
	// sp.initialize();
	// } catch (final NoSuchMethodException nsme) {
	// nsme.printStackTrace();
	// fail("NoSuchMethodException");
	// // } catch (final ValidationException ve) {
	// // ve.printStackTrace();
	// } catch (final InvalidFormatException ife) {
	// ife.printStackTrace();
	// fail("InvalidFormatException");
	// }
	// // final List<List<String>> output = sp.validate(
	// // new HashSet<SampleParser.Index>(), new HashSet<Integer>(),
	// // new HashSet<Integer>());
	// // assertEquals(29, output.size());
	// sp.parse();
	// assertEquals(26, sp.getSamples().size());
	// // we'll test saving with a client-side test
	// } catch (final IOException ioe) {
	// fail("IO Exception");
	// }
	// }

	// @Test
	// public void test_valhalla() {
	// try {
	// final Query q = InitDatabase.getSession().getNamedQuery(
	// "User.byUsername");
	// q.setString("username", "anthony");
	// final User u = (User) getResult(q);
	// final SampleParser sp = new SampleParser(new FileInputStream(
	// "../mpdb-common/sample-data/Valhalla_samples_upload.xls"));
	// try {
	// sp.initialize();
	// } catch (final NoSuchMethodException e) {
	// fail("NoSuchMethodException");
	// } catch (final InvalidFormatException ife) {
	// ife.printStackTrace();
	// fail("InvalidFormatException");
	// }
	// sp.parse();
	//
	// final List<Sample> samples = sp.getSamples();
	//
	// } catch (final IOException ioe) {
	// fail("IO Exception");
	// }
	// }

	public void test_uploaded_files_table() {
		final Session s = DataStore.open();
		final Transaction t = s.beginTransaction();
		s
				.createSQLQuery(
						"INSERT INTO uploaded_files(uploaded_file_id, hash, filename, time) VALUES(nextval('uploaded_files_seq'), :hash, :filename, NOW())")
				.setParameter("hash", "hash").setParameter("filename",
						"filename").executeUpdate();
		t.commit();
	}
}
