package edu.rpi.metpetdb.server;

import java.io.FileOutputStream;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.ForwardOnlyResultSetTableFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.DefaultTableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.junit.Test;

public class InitDatabase extends TestCase {

	private static Session s;

	private static IDatabaseConnection conn;

	private IDataSet originalData;

	/**
	 * Tables excluded from the database backup
	 */
	private static final String excludedTables[] = { "geometry_columns",
			"spatial_ref_sys" };

	public InitDatabase() {
		DataStore.initFactory();

		s = DataStore.open();

		try {
			conn = new DatabaseConnection(DataStore.getConfiguration()
					.buildSettings().getConnectionProvider().getConnection());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static IDatabaseConnection getConnection() {
		return conn;
	}

	public static Session getSession() {
		return s;
	}

	private void backupDatabase() {

		try {
			DatabaseConfig config = conn.getConfig();
			// We need this so that DbUnit can understand PostGIS geometry
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new PostGisDataTypeFactory());
			// Change the table factory for more performance when backup up the
			// database
			config.setProperty(DatabaseConfig.PROPERTY_RESULTSET_TABLE_FACTORY,
					new ForwardOnlyResultSetTableFactory());

			// Setup the excluded tables
			DefaultTableFilter tableFilter = new DefaultTableFilter();
			for (final String s : excludedTables) {
				tableFilter.excludeTable(s);
			}
			IDataSet filteredTable = new FilteredDataSet(tableFilter, conn
					.createDataSet());
			DatabaseSequenceFilter sequenceFilter = new DatabaseSequenceFilter(
					conn, filteredTable.getTableNames());
			// Export the database excluding certain tables
			originalData = new FilteredDataSet(sequenceFilter, conn
					.createDataSet());
			FlatXmlDataSet.write(originalData, new FileOutputStream(
					"test-data/test-backup.xml"));
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testConnection() {
		assertTrue(conn != null);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		backupDatabase();
		try {
			DatabaseOperation.DELETE_ALL.execute(conn, originalData);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		try {
			DatabaseOperation.INSERT.execute(conn, originalData);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
