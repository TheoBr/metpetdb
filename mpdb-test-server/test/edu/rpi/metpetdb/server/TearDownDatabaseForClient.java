package edu.rpi.metpetdb.server;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
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
import org.hibernate.Transaction;

/**
 * Basically same thing as SetupDatabaseForClient, but in reverse
 * 
 * @author anthony
 * 
 */
public class TearDownDatabaseForClient {

	private static Session s;

	private static IDatabaseConnection conn;

	private static IDataSet originalData;
	
	private static Connection hibernateConnection;

	public static void main(String[] args) {
		DataStore.initFactory();

		s = DataStore.open();
		Transaction tx = s.beginTransaction();

		try {
			hibernateConnection = ((org.hibernate.engine.SessionFactoryImplementor) DataStore
					.getFactory()).getConnectionProvider()
					.getConnection();
			conn = new DatabaseConnection(hibernateConnection);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (DatabaseUnitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (args.length > 0)
			tearDown(args[0]);
		else
			tearDown(".");

		try {
			try {
				hibernateConnection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tx.commit();
			s.close();
			conn.close();
			System.out.println("Database has been restored");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tables excluded from the database backup
	 */
	private static final String excludedTables[] = { "geometry_columns",
			"spatial_ref_sys" };

	private static void loadDatabase(final String path) {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void tearDown(final String path) {
		// Delete test data
		loadDatabase(path);
		try {
			DatabaseOperation.DELETE_ALL.execute(conn, originalData);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// put in backed up data
		final IDataSet loadedDataSet;
		try {
			loadedDataSet = new FlatXmlDataSet(new FileInputStream(path + "/"
					+ "test-data/test-backup.xml"));
			// Insert test data
			DatabaseOperation.INSERT.execute(conn, loadedDataSet);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
