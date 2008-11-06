package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;

import junit.framework.TestCase;
import net.sf.hibernate4gwt.core.HibernateBeanManager;
import net.sf.hibernate4gwt.core.hibernate.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
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
		//super("test-data/test-sample-data.xml");
	}
	
	public void setUp() {
		// Locate and set Valid Potential Minerals for the parser
		final Session s = DataStore.open();
		final Collection<Mineral> minerals = (new MineralDAO(s).getAll());
		SampleParser.setMinerals(minerals);
		List<Element> elements = ((new ElementDAO(s)).getAll());
		List<Oxide> oxides = ((new OxideDAO(s)).getAll());
		AnalysisParser.setElementsAndOxides(elements, oxides);
		s.close();
	}
	
	@Test
	public void testUploadSamples() throws ServletException, InvalidFormatException, LoginRequiredException, FileNotFoundException, ValidationException {
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
		DatabaseObjectConstraints doc = DataStore.getInstance().getDatabaseObjectConstraints();
		final SampleParser sp = new SampleParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + "PrivateExampleUpload_samples.xls"));
		sp.parse();
		final List<Sample> samples = sp.getSamples();
		for(Sample s : samples) {
				doc.validate(s);
		}
	}
	
	@Test
	public void testUploadAnalyses() throws ServletException, InvalidFormatException, LoginRequiredException, ValidationException, IOException {
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
		DatabaseObjectConstraints doc = DataStore.getInstance().getDatabaseObjectConstraints();
		final AnalysisParser sp = new AnalysisParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + "PrivateExampleUpload_analyses.xls"));
		sp.parse();
		final List<ChemicalAnalysis> analyses = sp.getAnalyses();
		for(ChemicalAnalysis s : analyses) {
				doc.validate(s);
		}
	}

//	@Test
//	public void test_easy() {
//		try {
//			final SampleParser sp = new SampleParser(new FileInputStream(
//					"../mpdb-common/sample-data/easy_samples.xls"));
//			try {
//				sp.initialize();
//			} catch (final NoSuchMethodException nsme) {
//				nsme.printStackTrace();
//				fail("NoSuchMethodException");
//				// } catch (final ValidationException ve) {
//				// ve.printStackTrace();
//			} catch (final InvalidFormatException ife) {
//				ife.printStackTrace();
//				fail("InvalidFormatException");
//			}
//			// final List<List<String>> output = sp.validate(
//			// new HashSet<SampleParser.Index>(), new HashSet<Integer>(),
//			// new HashSet<Integer>());
//			// assertEquals(29, output.size());
//			sp.parse();
//			assertEquals(26, sp.getSamples().size());
//			// we'll test saving with a client-side test
//		} catch (final IOException ioe) {
//			fail("IO Exception");
//		}
//	}

//	@Test
//	public void test_valhalla() {
//		try {
//			final Query q = InitDatabase.getSession().getNamedQuery(
//					"User.byUsername");
//			q.setString("username", "anthony");
//			final User u = (User) q.uniqueResult();
//			final SampleParser sp = new SampleParser(new FileInputStream(
//					"../mpdb-common/sample-data/Valhalla_samples_upload.xls"));
//			try {
//				sp.initialize();
//			} catch (final NoSuchMethodException e) {
//				fail("NoSuchMethodException");
//			} catch (final InvalidFormatException ife) {
//				ife.printStackTrace();
//				fail("InvalidFormatException");
//			}
//			sp.parse();
//
//			final List<Sample> samples = sp.getSamples();
//
//		} catch (final IOException ioe) {
//			fail("IO Exception");
//		}
//	}

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
