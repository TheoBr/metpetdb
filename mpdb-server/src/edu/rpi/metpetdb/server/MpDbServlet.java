package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.beanlib.transformer.CustomTransformersFactory;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.google.gwt.user.client.rpc.SerializationException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
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
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.bulk.upload.ImageParser;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageTypeDAO;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.impl.ImageServiceImpl;
import edu.rpi.metpetdb.server.security.Action;
import edu.rpi.metpetdb.server.security.ConvertSecurityException;
import edu.rpi.metpetdb.server.security.SessionEncrypter;
import edu.rpi.metpetdb.server.security.permissions.principals.AdminPrincipal;

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
public abstract class MpDbServlet extends PersistentRemoteService {
	private static final long serialVersionUID = 1L;

	/** The current thread's {@link Req}. */
	private static final ThreadLocal<Req> perThreadReq = new ThreadLocal<Req>();
	// req used for unit tests
	public static Req testReq = null;

	/** The server's object constraint instance. */
	protected DatabaseObjectConstraints doc;
	protected ObjectConstraints oc;

	protected static final Properties fileProps = new Properties();

	private static int autoLoginId = 11;
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
		DataStore.initFactory();
		final HibernateUtil hu = new HibernateUtil() {
			@Override
			public boolean isPersistentClass(Class<?> clazz) {
				if (clazz.equals(Results.class)) {
					return true;
				} else {
					return super.isPersistentClass(clazz);
				}
			}
		};
		hu.setSessionFactory(DataStore.getFactory());
		PersistentBeanManager.getInstance().setPersistenceUtil(hu);
		final StatelessProxyStore sps = new StatelessProxyStore();
		PersistentBeanManager.getInstance().setProxyStore(sps);
		CustomTransformersFactory.getInstance().addCustomBeanTransformer(
				GeometryCustomTransformer.class);
		DataStore.setBeanManager(PersistentBeanManager.getInstance());
		doc = DataStore.getInstance().getDatabaseObjectConstraints();
		if (doc == null) {
			throw new RuntimeException(
					"Unable to get constraints, check the tomct logs");
		}
		oc = DataStore.getInstance().getObjectConstraints();

		loadAutomaticLogin();
		try {
			initBulkUpload();
		} catch (MpDbException e) {
			e.printStackTrace();
		}
		// System.setProperty("java.security.auth.login.config", MpDbServlet.
		// class.getClassLoader().getResource("jaas.properties").getPath());
	}

	/**
	 * Initializes the static properties of the bulk upload process
	 * 
	 * @throws DAOException
	 */
	public void initBulkUpload() throws MpDbException {
		// Locate and set Valid Potential Minerals for the parser
		final Session s = DataStore.open();
		try {
			final Collection<Mineral> minerals = (new MineralDAO(s).getAll());
			SampleParser.setMinerals(minerals);
			List<Element> elements = ((new ElementDAO(s)).getAll());
			List<Oxide> oxides = ((new OxideDAO(s)).getAll());
			AnalysisParser.setOxides(oxides);
			AnalysisParser.setElements(elements);
			ImageParser.setImageTypes((new ImageTypeDAO(s).getAll()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	}

	public void destroy() {
		doc = null;
		oc = null;
		DataStore.destoryFactory();
		super.destroy();
	}

	public static Req currentReq() {
		if (testReq != null)
			return testReq;
		return perThreadReq.get();
	}

	public void loadAutomaticLogin() {
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
		if (enabled) {
			MpDbServlet.autoLoginId = Integer.parseInt(fileProps
					.getProperty("userid"));
		}
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
	protected void setCurrentUser(final User u,
			final Collection<Principal> principals) {
		final String v = u != null ? SessionEncrypter.crypt(u.getId()) : null;
		final Cookie c = new Cookie(MpDbConstants.USERID_COOKIE, v);
		c.setMaxAge(u != null ? -1 : 0); // Only for this browser session.
		getThreadLocalResponse().addCookie(c);
		currentReq().userId = u != null ? new Integer(u.getId()) : null;
		getThreadLocalRequest().getSession().setAttribute("principals",
				principals);
		getThreadLocalRequest().getSession().setAttribute("user", u);
		if (u == null || principals == null)
			// clear the session when we don't have a current user
			getThreadLocalRequest().getSession().invalidate();
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
	protected int currentUserId() throws LoginRequiredException {
		if (autoLoginId != -1) {
			System.out.println("using autologin id of " + autoLoginId);
			// autologins get full privileges
			final Collection<Principal> principals = new HashSet<Principal>();
			principals.add(new AdminPrincipal());
			getThreadLocalRequest().getSession().setAttribute("principals",
					principals);
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
	 * Only returns a user id if someone is logged in, otherwise it returns 0
	 * 
	 * @return
	 */
	protected int currentUserIdIfExists() {
		int userId = 0;
		try {
			userId = currentUserId();
		} catch (Exception e) {

		}
		return userId;
	}

	protected User currentUser() throws LoginRequiredException, MpDbException {
		if (currentReq() != null)
			return currentReq().currentUser(currentUserId());
		else
			throw new LoginRequiredException();
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
		if (currentReq() != null)
			return currentReq().currentSession();
		else
			return DataStore.open();
	}

	/**
	 * Commit all changes made to this Hibernate session.
	 * <p>
	 * Must be invoked by any RPC method implementation prior to the method
	 * returning, as otherwise our framework code will rollback the transaction
	 * and throw an exception back to the client.
	 * </p>
	 * 
	 * @throws MpDbException
	 */
	protected void commit() throws MpDbException {
		try {
			currentSession().getTransaction().commit();
		} catch (CallbackException e) {
			forgetChanges();
			throw ConvertSecurityException.convertToException(e);
		} catch (final HibernateException he) {
			throw handleHibernateException(he);
		}
	}

	protected DAOException handleHibernateException(
			final HibernateException he, final MObject object) {
		final DAOException e = handleHibernateException(he);
		e.handleObject(object);
		return e;
	}

	protected DAOException handleHibernateException(final HibernateException he) {

		// Sometimes we can map specific sql exceptions to specific dao
		// exceptions, do that if we can
		if (he instanceof ConstraintViolationException) {
			final ConstraintViolationException cve = (ConstraintViolationException) he;
			final String constraintName = cve.getConstraintName();

			if ("projects_nk_alias".equals(constraintName))
				return new ProjectAlreadyExistsException();
			else if ("samples_nk_number".equals(constraintName))
				return new SampleAlreadyExistsException();
			else if ("users_nk_username".equals(constraintName))
				return new UserAlreadyExistsException();
			else if ("subsamples_nk_name".equals(constraintName))
				return new SubsampleAlreadyExistsException();
			else if ("chemical_analyses_nk_spot_id".equals(constraintName))
				return new ChemicalAnalysisAlreadyExistsException();
			else if ("images_nk_filename_subsample".equals(constraintName)
					|| "images_nk_filename_sample".equals(constraintName))
				return new ImageAlreadyExistsException();
		}

		// If we have no idea what the exception means, should it be passed to
		// the end user? I say so, that way they have something meaningful to
		// bring to the dev team (instead of just' it didn't work sometime
		// yesterday afternoon')
		return new GenericDAOException(formatExceptionMessage(he));
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
		r.principals = (Collection<Principal>) this.getThreadLocalRequest()
				.getSession().getAttribute("principals");
		r.setUser((User) this.getThreadLocalRequest().getSession()
				.getAttribute("user"));
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

	public SearchSample getSearchSample() {
		return (SearchSample) this.getThreadLocalRequest().getSession()
				.getAttribute("searchSamp");
	}

	public void setSearchSample(final SearchSample searchSamp) {
		this.getThreadLocalRequest().getSession().setAttribute("searchSamp",
				searchSamp);
	}

	public SearchSample getLastSearchedSearchSample() {
		return (SearchSample) this.getThreadLocalRequest().getSession()
				.getAttribute("lastSearchSamp");
	}

	public void setLastSearchedSearchSample(final SearchSample searchSamp) {
		this.getThreadLocalRequest().getSession().setAttribute(
				"lastSearchSamp", searchSamp);
	}

	public PaginationParameters getLastSearchPagination() {
		return (PaginationParameters) this.getThreadLocalRequest().getSession()
				.getAttribute("lastSearchPagination");
	}

	public void setLastSearchPagination(final PaginationParameters p) {
		this.getThreadLocalRequest().getSession().setAttribute(
				"lastSearchPagination", p);
	}

	public static final class Req {
		Session session;
		Integer userId = null;
		public User user;
		public Action action;
		public Collection<Principal> principals;

		public void setUser(final User u) {
			user = u;
			if (u == null)
				userId = null;
			else
				userId = u.getId();
		}

		User currentUser(int userId) throws MpDbException {
			if (user == null) {
				user = new User();
				user.setId(userId);
				user = new UserDAO(currentSession()).fill(user);
			}
			return user;
		}

		Session currentSession() {
			if (session == null) {
				session = DataStore.open();
				// enable filters to make it so loading public data does not
				// load the private
				// data unless the current user is the owner of the private data
				int userIdToUser = userId == null ? 0 : userId;
				session.enableFilter("samplePublicOrUser").setParameter(
						"userId", userIdToUser);
				session.enableFilter("subsamplePublicOrUser").setParameter(
						"userId", userIdToUser);
				session.enableFilter("chemicalAnalysisPublicOrUser")
						.setParameter("userId", userIdToUser);
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
