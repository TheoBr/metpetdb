package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.impl.ImageServiceImpl;
import edu.rpi.metpetdb.server.security.SessionEncrypter;

/**
 * Basic remote service servlet for MpDp.
 * <p>
 * Supports quick access to a single Hibernate session for the calling thread,
 * as well as utility routines to hide the mundane query management required to
 * support the various service methods.
 * </p>
 * <p>
 * Automatically reuses the same session object for a single service request,
 * and ensures the session is closed (and its underlying connection gets
 * recycled) at the end of the request.
 * </p>
 */
public abstract class MpDbServlet extends RemoteServiceServlet {
	private static final long serialVersionUID = 1L;

	/** The current thread's {@link Req}. */
	private static final ThreadLocal perThreadReq = new ThreadLocal();

	/** The server's object constraint instance. */
	protected DatabaseObjectConstraints doc;
	protected ObjectConstraints oc;
	protected static final Properties fileProps = new Properties();

	public void init(final ServletConfig sc) throws ServletException {
		super.init(sc);
		DataStore.init();
		doc = DataStore.getDatabaseObjectConstraints();
		oc =  DataStore.getObjectConstraints();
		loadPropertyFiles();
		//TODO set this to your userid for automatic login
		//this.currentReq().userId = new Integer(1);
	}

	public void destroy() {
		doc = null;
		oc = null;
		DataStore.destroy();
		super.destroy();
	}

	private Req currentReq() {
		return (Req) perThreadReq.get();
	}
	
	private void loadPropertyFiles() {
		final String propFile = "files.properties";
		final InputStream i = ImageUploadServlet.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			throw new IllegalStateException("No " + propFile + " found");
		
		try {
			fileProps.load(i);
			i.close();
		} catch (IOException ioe) {
			throw new IllegalStateException("Cannot load " + propFile, ioe);
		}
		ImageUploadServlet.setBaseFolder(fileProps.getProperty("images.path"));
		ImageServiceImpl.setBaseFolder(fileProps.getProperty("images.path"));
	}

	/**
	 * Get the URL of this application.
	 * 
	 * @return the location of this application. This is the same as
	 *         {@link com.google.gwt.core.client.GWT#getModuleBaseURL()}.
	 */
	protected String getModuleBaseURL() {
		final StringBuffer u = getThreadLocalRequest().getRequestURL();
		u.setLength(u.lastIndexOf("/") + 1);
		return u.toString();
	}

	/**
	 * Configure (or forget) the current user.
	 * 
	 * @param u
	 *            the user to configure as the current application user. Null
	 *            will clear the current application user, if it had been known.
	 */
	protected void setCurrentUser(final User u) {
		final String v = u != null ? SessionEncrypter.crypt(u.getId()) : null;
		final Cookie c = new Cookie(MpDbConstants.USERID_COOKIE, v);
		c.setMaxAge(u != null ? -1 : 0); // Only for this browser session.
		getThreadLocalResponse().addCookie(c);
		currentReq().userId = u != null ? new Integer(u.getId()) : null;
	}

	/**
	 * Obtain the unique user id for the current application user.
	 * 
	 * @return the primary key identifying the current application user. Never
	 *         an invalid value.
	 * @throws LoginRequiredException
	 *             current application user cannot be determined as the user is
	 *             not actually logged in, or their authentication token is
	 *             invalid.
	 */
	protected int currentUser() throws LoginRequiredException {
		final Req r = currentReq();
		if (r.userId == null) {
			final Cookie[] cookieJar = getThreadLocalRequest().getCookies();
			if (cookieJar != null) {
				for (int k = cookieJar.length - 1; k >= 0; k--) {
					final Cookie c = cookieJar[k];
					if (MpDbConstants.USERID_COOKIE.equals(c.getName())) {
						r.userId = SessionEncrypter.verify(c.getValue());
						break;
					}
				}
			}
			if (r.userId == null)
				throw new LoginRequiredException();
		}
		return r.userId.intValue();
	}

	/**
	 * Obtain a session for the current thread.
	 * <p>
	 * If the current thread does not have a session, a new one will be
	 * allocated. The session will be automatically closed at the end of the
	 * current request.
	 * </p>
	 * 
	 * @return the active session for this thread. Always the same session
	 *         object within a single service request.
	 */
	protected Session currentSession() {
		return currentReq().currentSession();
	}

	/**
	 * Force a (possibly lazy) set to be loaded into memory.
	 * 
	 * @param s
	 *            the set in question to be loaded from the database. Must not
	 *            be null.
	 * @return either <code>s</code> or a copy of <code>s</code> if
	 *         <code>s</code> was not a normal java.util.HashSet. This copying
	 *         process is necessary to support the GWT serializer avoiding the
	 *         Hibernate specific PersistentSet type.
	 */
	protected static Set load(final Set s) {
		if (s == null)
			return s;
		return s instanceof HashSet ? s : new HashSet(s);
	}

	/**
	 * Insert the new object into the database on the next commit.
	 * 
	 * @param u
	 *            object to be inserted. This object must not already exist in
	 *            the database.
	 */
	protected void insert(final Object u) {
		currentSession().persist(u);
	}

	/**
	 * Deletes the object from the database on the next commit
	 * 
	 * @param u
	 *            object to be deleted, must already exist in the database
	 */

	protected void delete(final Object u) {
		currentSession().delete(u);
	}

	/**
	 * Reload the object from the database and merge client-side changes.
	 * 
	 * @param u
	 *            the object to be reloaded from the database. This instance
	 *            should contain the primary key information of an existing
	 *            object, and updated attributes for any values the client
	 *            modified. Unmodified attribues must match the current database
	 *            values to be considered unmodified.
	 * @return instance of the database record(s) that correspond to
	 *         <code>u</code>, but the returned object instance is actually a
	 *         member of the Hibernate session cache and therefore can be passed
	 *         off to {@link #update(Object)} to actually be modified.
	 */
	protected Object merge(final Object u) {
		return currentSession().merge(u);
	}

	/**
	 * Update the object in the database on the next commit.
	 * 
	 * @param u
	 *            object to be updated. This object must already exist in the
	 *            database and must also already exist in the session.
	 * @return always the reference <code>u</code>.
	 */
	protected Object update(final Object u) {
		currentSession().update(u);
		return u;
	}

	/**
	 * Commit all changes made to this Hibernate session.
	 * <p>
	 * Must be invoked by any RPC method implementation prior to the method
	 * returning, as otherwise our framework code will rollback the transaction
	 * and throw an exception back to the client.
	 * </p>
	 */
	protected void commit() {
		currentSession().getTransaction().commit();
	}

	/**
	 * Forget (throw away) all changes made to this Hibernate session.
	 * <p>
	 * Must be invoked by any RPC method implementation that is acting as a
	 * reader method that obtains data from the database, loads one or more
	 * collections, replaces those collections with their standard Java types,
	 * and returns the root of the graph to the client.
	 * </p>
	 * <p>
	 * Failure to perform this call before finishing the RPC method causes the
	 * framework to detect changes in memory to the Hibernate session, which
	 * makes the framework rollback the Hibernate transaction and throw an error
	 * to the client.
	 * </p>
	 */
	protected void forgetChanges() {
		currentSession().clear();
	}

	/**
	 * Obtain a named query.
	 * 
	 * @param name
	 *            the name of the query to obtain.
	 * @return the previously defined query of the given name.
	 */
	protected Query namedQuery(final String name) {
		return currentSession().getNamedQuery(name);
	}

	/**
	 * Obtain a query to produce one page worth of rows.
	 * 
	 * @param name
	 *            name of the query that will produce the rows. The query must
	 *            be a named HQL query of <code>name/p.getParameter</code>.
	 *            The query must end in an order by clause.
	 * @param p
	 *            pagination parameters from the client. These will be used to
	 *            configure the query's result window before it gets returned,
	 *            allowing the database to more efficiently select the proper
	 *            rows.
	 * @return the single page object query.
	 */
	protected Query pageQuery(final String name, final PaginationParameters p) {
		Query q;
		q = currentSession().getNamedQuery(name + "/" + p.getParameter());
		if (!p.isAscending())
			q = currentSession().createQuery(q.getQueryString() + " desc");
		q.setFirstResult(p.getOffset());
		q.setMaxResults(p.getMaxResults());
		return q;
	}

	protected Query pageQuery(final String name, final PaginationParameters p,
			final long id) {
		Query q;
		q = currentSession().getNamedQuery(name + "/" + p.getParameter());
		if (!p.isAscending())
			q = currentSession().createQuery(q.getQueryString() + " desc");
		q.setLong("id", id);
		q.setFirstResult(p.getOffset());
		q.setMaxResults(p.getMaxResults());
		return q;
	}

	/**
	 * Obtain a query to compute the total size of a result set.
	 * 
	 * @param name
	 *            name of the list query. The list query must be a named HQL
	 *            query of <code>name,size</code>.
	 * @return the result set counting query. Never null.
	 */
	protected Query sizeQuery(final String name) {
		return currentSession().getNamedQuery(name + ",size");
	}

	protected Query sizeQuery(final String name, final long id) {
		Query q = currentSession().getNamedQuery(name + ",size");
		q.setLong("id", id);
		return q;
	}

	/**
	 * Locate an object by its unique identifier.
	 * 
	 * @param name
	 *            simple type name of the object. The object must have defined a
	 *            named HQL query of <code>name.byId</code>.
	 * @param id
	 *            the unique identifier of the object.
	 * @return the single instance, after loading it from the database. Never
	 *         null.
	 * @throws NoSuchObjectException
	 *             the object specified was not returned. It does not exist in
	 *             the database. This error probably should be thrown back to
	 *             the UI layer, so it can display a proper error message.
	 */
	protected Object byId(final String name, final int id)
			throws NoSuchObjectException {
		final Query q = currentSession().getNamedQuery(name + ".byId");
		q.setInteger("id", id);
		final Object r = q.uniqueResult();
		if (r == null)
			throw new NoSuchObjectException(name, String.valueOf(id));
		return r;
	}

	/**
	 * Locate an object by its unique identifier.
	 * 
	 * @param name
	 *            simple type name of the object. The object must have defined a
	 *            named HQL query of <code>name.byId</code>.
	 * @param id
	 *            the unique identifier of the object.
	 * @return the single instance, after loading it from the database. Never
	 *         null.
	 * @throws NoSuchObjectException
	 *             the object specified was not returned. It does not exist in
	 *             the database. This error probably should be thrown back to
	 *             the UI layer, so it can display a proper error message.
	 */
	protected Object byId(final String name, final long id)
			throws NoSuchObjectException {
		final Query q = currentSession().getNamedQuery(name + ".byId");
		q.setLong("id", id);
		final Object r = q.uniqueResult();
		if (r == null)
			throw new NoSuchObjectException(name, String.valueOf(id));
		return r;
	}

	/**
	 * Locate an object by its unique identifier.
	 * 
	 * @param name
	 *            simple type name of the object. The object must have defined a
	 *            named HQL query of <code>name.byAttribute</code>.
	 * @param attribute
	 *            name of the attribute to query.
	 * @param id
	 *            the unique identifier of the object.
	 * @return the single instance, after loading it from the database. Never
	 *         null.
	 * @throws NoSuchObjectException
	 *             the object specified was not returned. It does not exist in
	 *             the database. This error probably should be thrown back to
	 *             the UI layer, so it can display a proper error message.
	 */
	protected Object byKey(final String name, final String attribute,
			final String id) throws NoSuchObjectException {
		final Query q = currentSession().getNamedQuery(
				name + ".by" + attribute.substring(0, 1).toUpperCase()
						+ attribute.substring(1));
		q.setString(attribute, id);
		final Object r = q.uniqueResult();
		if (r == null)
			throw new NoSuchObjectException(name, String.valueOf(id));
		return r;
	}

	protected Object byKeySet(final String name, final String attribute,
			final long id) throws NoSuchObjectException {
		final Number sz = (Number) sizeQuery(
				name + ".by" + attribute.substring(0, 1).toUpperCase()
						+ attribute.substring(1), id).uniqueResult();
		if (sz.intValue() > 0) {
			final Query q = currentSession().getNamedQuery(
					name + ".by" + attribute.substring(0, 1).toUpperCase()
							+ attribute.substring(1));
			q.setLong(attribute, id);
			final Object r = q.list();
			return r;
		} else {
			return new ArrayList();
		}
	}

	/**
	 * Create a results wrapper for pagination.
	 * 
	 * @param szQuery
	 *            the query that will compute the total number of rows.
	 * @param objQuery
	 *            the query that will generate the current page's row data.
	 * @return the results object to return back to the UI layer.
	 */
	protected Results toResults(final Query szQuery, final Query objQuery) {
		final Number sz = (Number) szQuery.uniqueResult();
		return new Results(sz.intValue(), sz.intValue() > 0
				? objQuery.list()
				: new ArrayList());
	}

	public String processCall(final String payload)
			throws SerializationException {
		final Req r = new Req();
		perThreadReq.set(r);
		String response = "Internal server error.";
		try {
			response = super.processCall(payload);
		} finally {
			perThreadReq.set(null);
			r.finishRequest(response);
		}
		return response;
	}

	static final class Req {
		Session session;
		Integer userId;

		Session currentSession() {
			if (session == null) {
				session = DataStore.open();
				session.beginTransaction();
			}
			return session;
		}

		void finishRequest(final String response) {
			if (session == null)
				return;
			try {
				if (session.isDirty() && session.getTransaction().isActive()) {
					session.getTransaction().rollback();
					if (response.startsWith("{OK}"))
						throw new HibernateException("BAD: session dirty!");
				}
			} finally {
				session.close();
			}
		}
	}
}
