package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import net.sf.hibernate4gwt.core.HibernateBeanManager;
import net.sf.hibernate4gwt.core.hibernate.HibernateUtil;
import net.sf.hibernate4gwt.gwt.HibernateRemoteService;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.google.gwt.user.client.rpc.SerializationException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.dao.ChemicalAnalysisAlreadyExistsException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.dao.ImageAlreadyExistsException;
import edu.rpi.metpetdb.client.error.dao.ProjectAlreadyExistsException;
import edu.rpi.metpetdb.client.error.dao.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.dao.SubsampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.dao.UserAlreadyExistsException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;
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
public abstract class MpDbServlet extends HibernateRemoteService {
	private static final long serialVersionUID = 1L;

	/** The current thread's {@link Req}. */
	private static final ThreadLocal<Req> perThreadReq = new ThreadLocal<Req>();

	/** The server's object constraint instance. */
	protected DatabaseObjectConstraints doc;
	protected ObjectConstraints oc;

	protected static final Properties fileProps = new Properties();

	private static int autoLoginId = -1;
	private static final String fileUploadPath;

	static {
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
		fileUploadPath = fileProps.getProperty("fileUpload.path") + "/";
	}

	public static String getFileUploadPath() {
		return fileUploadPath;
	}

	/**
	 * This is called when the servlet is initially loaded. It first sets up
	 * hibernate4gwt to be used with this project, then it initializes the
	 * DataStore (which handles the database specific things). Lastly it fetches
	 * the object constraints for the client.
	 */
	public void init(final ServletConfig sc) throws ServletException {
		super.init(sc);
		// Setup hibernate4gwt

		// Setup copying of java beans
		// DirectoryClassMapper cloneMapper = new DirectoryClassMapper();
		// cloneMapper.setRootDomainPackage("edu.rpi.metpetdb.client.model");
		// cloneMapper.setRootClonePackage("edu.rpi.metpetdb.client.model");
		// cloneMapper.setCloneSuffix("");
		// //
		// HibernateBeanManager.getInstance().setClassMapper(cloneMapper);
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
		DataStore.initFactory();
		doc = DataStore.getInstance().getDatabaseObjectConstraints();
		oc = DataStore.getInstance().getObjectConstraints();

		loadAutomaticLogin();
		initBulkUpload();
	}

	/** Initializes the static properties of the bulk upload process */
	public void initBulkUpload() {
		// Locate and set Valid Potential Minerals for the parser
		final Session s = DataStore.open();
		final Collection<Mineral> minerals = (new MineralDAO(s).getAll());
		SampleParser.setMinerals(minerals);
		List<Element> elements = ((new ElementDAO(s))
				.getAll());
		List<Oxide> oxides = ((new OxideDAO(s))
				.getAll());
		AnalysisParser.setElementsAndOxides(elements, oxides);
		s.close();
	}

	public void destroy() {
		doc = null;
		oc = null;
		DataStore.destoryFactory();
		super.destroy();
	}

	private Req currentReq() {
		return perThreadReq.get();
	}

	private void loadAutomaticLogin() {
		final String propFile = "autologin.properties";
		final InputStream i = MpDbServlet.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			return;
		try {
			fileProps.load(i);
			i.close();
		} catch (IOException ioe) {
			return;
		}
		final boolean enabled = (Integer.parseInt(fileProps
				.getProperty("enabled")) == 1 ? true : false);
		if (enabled)
			MpDbServlet.autoLoginId = Integer.parseInt(fileProps
					.getProperty("userid"));
	}

	/**
	 * Get the URL of this application.
	 * 
	 * @return the location of this application. This is the same as {@link
	 * 	com.google.gwt.core.client.GWT#getModuleBaseURL()}.
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
	 * 		the user to configure as the current application user. Null will
	 * 		clear the current application user, if it had been known.
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
	 * 	an invalid value.
	 * @throws LoginRequiredException
	 * 		current application user cannot be determined as the user is not
	 * 		actually logged in, or their authentication token is invalid.
	 */
	protected int currentUser() throws LoginRequiredException {
		if (autoLoginId != -1) {
			System.out.println("using autologin id of " + autoLoginId);
			return autoLoginId;
		}
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
	 * 	object within a single service request.
	 */
	protected Session currentSession() {
		return currentReq().currentSession();
	}

	/**
	 * Commit all changes made to this Hibernate session.
	 * <p>
	 * Must be invoked by any RPC method implementation prior to the method
	 * returning, as otherwise our framework code will rollback the transaction
	 * and throw an exception back to the client.
	 * </p>
	 */
	protected void commit() throws DAOException {
		try {
			currentSession().getTransaction().commit();
		} catch (final HibernateException he) {
			handleHibernateException(he);
		}
	}

	protected void handleHibernateException(final HibernateException he)
			throws DAOException {

		// Sometimes we can map specific sql exceptions to specific dao
		// exceptions, do that if we can
		if (he instanceof ConstraintViolationException) {
			final ConstraintViolationException cve = (ConstraintViolationException) he;
			final String constraintName = cve.getConstraintName();

			if ("projects_nk_alias".equals(constraintName))
				throw new ProjectAlreadyExistsException();
			else if ("samples_nk_alias".equals(constraintName))
				throw new SampleAlreadyExistsException();
			else if ("users_nk_username".equals(constraintName))
				throw new UserAlreadyExistsException();
			else if ("subsamples_nk_name".equals(constraintName))
				throw new SubsampleAlreadyExistsException();
			else if ("chemical_analyses_nk_spot_id".equals(constraintName))
				throw new ChemicalAnalysisAlreadyExistsException();
			else if ("images_nk_filename".equals(constraintName))
				throw new ImageAlreadyExistsException();
		}

		// If we have no idea what the exception means, should it be passed to
		// the end user? I say so, that way they have something meaningful to
		// bring to the dev team (instead of just' it didn't work sometime
		// yesterday afternoon')
		throw new GenericDAOException(formatExceptionMessage(he));
	}

	public static String formatExceptionMessage(final HibernateException he) {
		String s = "";
		s = s + he.getLocalizedMessage() + "\n";
		if (he.getCause() != null) {
			s = s + "Cause: \n" + he.getCause().getLocalizedMessage() + "\n";
			if (he.getCause().getCause() != null) {
				s = s + "Cause: \n"
						+ he.getCause().getCause().getLocalizedMessage() + "\n";
			}
		}
		return s;
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
