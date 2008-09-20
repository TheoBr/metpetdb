package edu.rpi.metpetdb.server;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Query;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.MObject;

public class DatabaseTestCase extends TestCase {

	private final String xmlFile;

	private IDataSet loadedDataSet;

	/**
	 * When true it backups/restores the database this is false when running the
	 * test suite
	 */
	public static boolean BACKUP_DATABASE = true;

	private InitDatabase initDb = null;

	public DatabaseTestCase(final String xmlFile) {
		this.xmlFile = xmlFile;
		if (BACKUP_DATABASE) {
			initDb = new InitDatabase();
		}
	}

	@Override
	public void setUp() {
		if (BACKUP_DATABASE) {
			try {
				initDb.setUp();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		try {
			loadedDataSet = new FlatXmlDataSet(new FileInputStream(xmlFile));
			// Insert test data
			DatabaseOperation.INSERT.execute(InitDatabase.getConnection(),
					loadedDataSet);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		try {
			InitDatabase.getHibernateConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		try {
			// Delete test data
			DatabaseOperation.DELETE_ALL.execute(InitDatabase.getConnection(),
					loadedDataSet);
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (BACKUP_DATABASE) {
			try {
				initDb.tearDown();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			InitDatabase.getHibernateConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings( {
		"unchecked"
	})
	protected <T extends MObject> T byId(final String name, final int id)
			throws NoSuchObjectException {
		final Query q = InitDatabase.getSession().getNamedQuery(name + ".byId");
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
		q = InitDatabase.getSession().getNamedQuery(name + ".all/" + parameter);
		if (!assending)
			q = InitDatabase.getSession().createQuery(
					q.getQueryString() + " desc");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return (List<T>) q.list();
	}
}
