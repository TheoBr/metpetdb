package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class SampleBulkUploadTest extends TestCase {

	public void setUp() {
		// Locate and set Valid Potential Minerals for the parser
		final Session s = DataStore.open();
		Collection<Mineral> minerals;
		try {
			minerals = (new MineralDAO(s).getAll());
			SampleParser.setMinerals(minerals);
		} catch (DAOException e1) {
			e1.printStackTrace();
		} catch (MpDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void testBug260() throws FileNotFoundException, MpDbException {
//		System.out.println("testUploadNewSamples");
//		final NewSampleParser sp = new NewSampleParser(new FileInputStream(
//				MpDbServlet.getFileUploadPath() + "compiledpublic_samp1.xls"));
//		sp.parse();
//		final Map<Integer, Sample> samples = sp.getSamples();
//		final Session session = DataStore.open();
//		session.beginTransaction();
//		final User u = new User();
//		u.setId(1);
//		final SampleDAO dao = new SampleDAO(session);
//		for (Entry<Integer, Sample> s : samples.entrySet()) {
//			s.getValue().setOwner(u);
//			s.getValue().setPublicData(false);
//			System.out.println(s.getValue().getAlias());
//			try {
//				dao.save(s.getValue());
//			} catch (Exception he) {
//				if (he instanceof ConstraintViolationException) {
//					if (((ConstraintViolationException) he).getConstraintName().equals("samples_nk_alias")) {
//						final DAOException e = new SampleAlreadyExistsException();
//						e.handleObject(s.getValue());
//						throw e;
//					}
//				}
//			}
//		}
//		session.getTransaction().commit();
//	}
	
	public void testLatLonError() throws FileNotFoundException, MpDbException {
		System.out.println("testLatLonError");
		final SampleParser sp = new SampleParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + "PrivateExampleUpload_samples.xls"));
		sp.parse();
		final Map<Integer, Sample> samples = sp.getSamples();
		final Session session = DataStore.open();
		session.beginTransaction();
		final User u = new User();
		u.setId(1);
		final SampleDAO dao = new SampleDAO(session);
		for (Entry<Integer, Sample> s : samples.entrySet()) {
			s.getValue().setOwner(u);
			s.getValue().setPublicData(false);
			System.out.println(s.getValue().getAlias());
			dao.save(s.getValue());
		}
		session.getTransaction().commit();
	}

}
