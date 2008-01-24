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
import org.hibernate.Session;

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
		    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
		            new PostGisDataTypeFactory());
			// partial database export
	        QueryDataSet partialDataSet = new QueryDataSet(conn);
	        partialDataSet.addTable("samples", "SELECT * FROM samples");
	        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("partial.xml"));        

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

	public void testTrue() {
		assertTrue(true);
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
