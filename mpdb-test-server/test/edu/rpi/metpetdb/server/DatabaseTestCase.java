package edu.rpi.metpetdb.server;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.DefaultTableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.MObject;

public class DatabaseTestCase {

	private static String xmlFile = "test-data/test-sample-data.xml";

	private static IDataSet loadedDataSet;

	public static Session session;

	private static IDatabaseConnection conn;

	private static Connection hiberateConnection;

	public static final int PUBLIC_SAMPLE = 6;
	public static final long PUBLIC_SUBSAMPLE = 4;
	public static final int PUBLIC_CHEMICAL_ANALYSIS = 3;

	/**
	 * Tables excluded from the database backup
	 */
	private static final String excludedTables[] = {
			"geometry_columns", "spatial_ref_sys"
	};

	/**
	 * When true it backups/restores the database this is false when running the
	 * test suite
	 */
	public static boolean BACKUP_DATABASE = true;

	public DatabaseTestCase(final String xmlFile) {
		DatabaseTestCase.xmlFile = xmlFile;
	}

	@BeforeClass
	public static void beforeClass() {
		try {
			DataStore.initFactory();
			hiberateConnection = ((org.hibernate.engine.SessionFactoryImplementor) DataStore
					.getFactory()).getConnectionProvider().getConnection();
			conn = new DatabaseConnection(hiberateConnection);
			DatabaseConfig config = conn.getConfig();
			// We need this so that DbUnit can understand PostGIS geometry
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new PostGisDataTypeFactory());
			// config.setFeature(
			// "http://www.dbunit.org/features/batchedStatements", true);
			// Setup the excluded tables
			DefaultTableFilter tableFilter = new DefaultTableFilter();
			for (final String s : excludedTables) {
				tableFilter.excludeTable(s);
			}
			// IDataSet filteredTable = new FilteredDataSet(tableFilter, conn
			// .createDataSet());
			// DatabaseSequenceFilter sequenceFilter = new
			// DatabaseSequenceFilter(
			// conn, filteredTable.getTableNames());
			// Export the database excluding certain tables
			// IDataSet originalData = new FilteredDataSet(sequenceFilter, conn
			// .createDataSet());
			loadedDataSet = new FlatXmlDataSet(new File(xmlFile), false, true);
			DatabaseOperation.CLEAN_INSERT.execute(conn, loadedDataSet);
			hiberateConnection.commit();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setupSession(final int userId) {
		session.enableFilter("samplePublicOrUser").setParameter("userId",
				userId);
		session.enableFilter("subsamplePublicOrUser").setParameter("userId",
				userId);
		session.enableFilter("chemicalAnalysisPublicOrUser").setParameter(
				"userId", userId);
	}

	@Before
	public void setUp() {
		session = DataStore.open();
		session.beginTransaction();
		// setup an anonymous request
		final MpDbServlet.Req req = new MpDbServlet.Req();
		req.user = null;
		req.principals = new ArrayList<Principal>();
		MpDbServlet.testReq = req;
		setupSession(0);
	}

	@After
	public void tearDown() {
		session.getTransaction().commit();
		session.close();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		if (hiberateConnection != null)
			hiberateConnection.close();
		if (conn != null)
			conn.close();
	}

	@SuppressWarnings( {
		"unchecked"
	})
	protected <T extends MObject> T byId(final String name, final int id)
			throws NoSuchObjectException {
		final Query q = session.getNamedQuery(name + ".byId");
		q.setInteger("id", id);
		final Object r = q.uniqueResult();
		if (r == null)
			throw new NoSuchObjectException(name, String.valueOf(id));
		return (T) r;
	}

	/**
	 * Obtain a query to produce one page worth of rows.
	 * 
	 * @param name
	 *            name of the query that will produce the rows. The query must
	 *            be a named HQL query of <code>name/p.getParameter</code>. The
	 *            query must end in an order by clause.
	 * @param p
	 *            pagination parameters from the client. These will be used to
	 *            configure the query's result window before it gets returned,
	 *            allowing the database to more efficiently select the proper
	 *            rows.
	 * @return the single page object query.
	 */
	@SuppressWarnings( {
		"unchecked"
	})
	protected <T extends MObject> List<T> pageQuery(final String name,
			final String parameter, final boolean assending,
			final int firstResult, final int maxResults) {
		Query q;
		q = session.getNamedQuery(name + ".all/" + parameter);
		if (!assending)
			q = InitDatabase.getSession().createQuery(
					q.getQueryString() + " desc");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return (List<T>) q.list();
	}
}
