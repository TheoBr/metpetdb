package edu.rpi.metpetdb.server.bulk.upload.image;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;

import net.sf.hibernate4gwt.core.HibernateBeanManager;
import net.sf.hibernate4gwt.core.hibernate.HibernateUtil;

import junit.framework.TestCase;
import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.BulkUploadImage;
import edu.rpi.metpetdb.server.bulk.upload.ImageParser;
import edu.rpi.metpetdb.server.bulk.upload.Parser;
import edu.rpi.metpetdb.server.dao.impl.ImageTypeDAO;

public class BulkUploadTest extends TestCase {

	public BulkUploadTest() {
		// super("test-data/test-sample-data.xml");
	}
	
	public void setUp() {
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
		try {
			Parser.setImageTypes(new ImageTypeDAO(DataStore.open()).getAll());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MpDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testNewUploadImages() throws ServletException,
			InvalidFormatException, LoginRequiredException, IOException,
			MpDbException {
		DataStore.getInstance().getDatabaseObjectConstraints();
		final ImageParser ap = new ImageParser(new FileInputStream(
				MpDbServlet.getFileUploadPath()
						+ "image_upload.xls"));
		ap.parse();
		final Map<Integer, BulkUploadImage> images = ap.getBulkUploadImages();
		for (Entry<Integer, BulkUploadImage> s : images.entrySet()) {
			try {
				DataStore.getInstance().getDatabaseObjectConstraints()
						.validate(s.getValue().getImage());
				DataStore.getInstance().getDatabaseObjectConstraints()
						.validate(s.getValue().getImageOnGrid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
