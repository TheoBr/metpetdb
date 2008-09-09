package edu.rpi.metpetdb.server.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.server.model.MObject;

public abstract class MpDbDAO<T extends MObject> {
	final protected Session sess;

	public MpDbDAO(Session session) {
		sess = session;
	}

	/**
	 * Fill takes an object, obtains the corresponding unique object from the
	 * db, and returns the object will all data filled in
	 * 
	 * @param inst
	 *            Partially completed object
	 * @return Filled object from database
	 * @throws DAOException
	 *             Generally corresponds to 'no corresponding object was found'
	 */
	abstract public T fill(T inst) throws DAOException;

	/**
	 * Save the specified object to the db. Inserting if the object is new,
	 * merging/updating if the object is not
	 * 
	 * @param inst
	 *            Object to save
	 * @return Object that was saved and is now in the db (merge may update)
	 * @throws DAOException
	 */
	abstract public T save(T inst) throws DAOException;

	/**
	 * Remove the specified object from the db.
	 * 
	 * @param inst
	 *            Object to remove
	 * @return
	 * @throws DAOException
	 */
	abstract public T delete(T inst) throws DAOException;

	public Set<T> fill(Set<T> s) {
		if (s == null)
			return null;

		final Iterator<T> itr = s.iterator();
		final HashSet<T> filled = new HashSet<T>();
		while (itr.hasNext()) {
			try {
				final T fill = fill(itr.next());
				itr.remove();
				filled.add(fill);
			} catch (final DAOException daoe) {
			}
		}
		s.addAll(filled);
		return s;
	}

	public boolean isNew(T inst) {
		try {
			// If the object in question can be fill()-ed successfully, then a
			// corresponding object is in the db already, thus the object is not
			// new
			fill(inst);
			return false;
		} catch (final DAOException daoe) {
			// If we can't fill() it, then it's new
			return true;
		}
	}

	/**
	 * Save the object to the db, inserting if new, merging/updating if not
	 * 
	 * @param u
	 * @return
	 * @throws DAOException
	 */
	protected T _save(T u) throws DAOException {
		if (u.mIsNew()) {
			insert(u);
		} else {
			try {
				u = update(merge(u));
			} catch (TransientObjectException toe) {
				throw toe;
			}
		}
		return u;
	}

	/**
	 * Insert the new object into the database on the next commit.
	 * 
	 * @param u
	 *            object to be inserted. This object must not already exist in
	 *            the database.
	 */
	protected  void insert(final T u) {
		sess.persist(u);
	}

	/**
	 * Deletes the object from the database on the next commit
	 * 
	 * @param u
	 *            object to be deleted, must already exist in the database
	 */
	protected  void _delete(final T u) {
		sess.delete(u);
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
	@SuppressWarnings( {
		"unchecked"
	})
	protected  T merge(final T u) {
		return (T) sess.merge(u);
	}

	/**
	 * Update the object in the database on the next commit.
	 * 
	 * @param u
	 *            object to be updated. This object must already exist in the
	 *            database and must also already exist in the session.
	 * @return always the reference <code>u</code>.
	 */
	protected  T update(final T u) {
		sess.update(u);
		return u;
	}

	/**
	 * Obtain a named query.
	 * 
	 * @param name
	 *            the name of the query to obtain.
	 * @return the previously defined query of the given name.
	 */
	protected Query namedQuery(final String name) {
		return sess.getNamedQuery(name);
	}

	/**
	 * @see{pageQuery
	 * @param name
	 * @param p
	 * @return
	 */
	protected Query pageQuery(final String name, final PaginationParameters p) {
		return pageQuery(name, p, -1);
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
	 * @param id
	 *            optional id for the query
	 * @return the single page object query.
	 */
	protected Query pageQuery(final String name, final PaginationParameters p,
			final long id) {
		Query q;
		q = sess.getNamedQuery(name + "/" + p.getParameter());
		if (!p.isAscending())
			q = sess.createQuery(q.getQueryString() + " desc");
		if (id != -1)
			q.setLong("id", id);

		q.setFirstResult(p.getFirstResult());
		q.setMaxResults(p.getMaxResults());
		return q;
	}

	/**
	 * @see{sizeQuery
	 * @param name
	 * @return
	 */
	protected Query sizeQuery(final String name) {
		return sizeQuery(name, -1);
	}

	/**
	 * Obtain a query to compute the total size of a result set.
	 * 
	 * @param name
	 *            name of the list query. The list query must be a named HQL
	 *            query of <code>name,size</code>.
	 * @param id
	 *            optional id for the query
	 * @return the result set counting query. Never null.
	 */
	protected Query sizeQuery(final String name, final long id) {
		Query q = sess.getNamedQuery(name + ",size");
		if (id != -1)
			q.setLong("id", id);
		return q;
	}
}
