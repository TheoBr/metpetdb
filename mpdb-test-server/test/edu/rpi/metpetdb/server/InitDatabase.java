package edu.rpi.metpetdb.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Sample;

public class InitDatabase extends TestCase {

	private Session s;

	private FileInputStream is;

	private IDatabaseConnection conn;

	private IDataSet dataSet;

	public InitDatabase() {

		DataStore.init();

		try {

			s = DataStore.open();

			conn = new DatabaseConnection(s.connection());
			DatabaseConfig config = conn.getConfig();
			//We need this so that DbUnit can understand PostGIS geometry
		    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
		            new PostGisDataTypeFactory());
	        
		    //Load sample data into the database
	        is = new FileInputStream("task-sample-data.xml");
	        dataSet = new FlatXmlDataSet(is);


		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Override
	protected void setUp() throws Exception {

		super.setUp();

		try {

			DatabaseOperation.INSERT.execute(conn, dataSet);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Test
	public void testLoad() {
		Query q = s.createQuery("from Sample s inner join fetch s.owner where s.id = " + 1);
		final Sample s = (Sample) q.uniqueResult();
		assertTrue(s.getId() == 1);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();

		try {

			DatabaseOperation.DELETE_ALL.execute(conn, dataSet);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
