package edu.rpi.metpetdb.server.model;

import net.sf.hibernate4gwt.pojo.gwt.LazyGwtPojo;

/**
 * Base class for all data model beans.
 * <p>
 * All data model beans must derive from this class, allowing validation
 * routines to access all relevant properties through the {@link #mGet(int)} and
 * {@link #mSet(int, Object)} reflection-work-a-like methods. This strategy is
 * required as GWT does not support reflection natively and we don't want to
 * bother with writing our own GWT class generator implementations.
 * </p>
 */
public abstract class MObject extends LazyGwtPojo {

	private static final long serialVersionUID = 1L;

	/**
	 * Is this a new object instance?
	 * 
	 * @return true if this object has not yet been written to the database.
	 */
	public abstract boolean mIsNew();

}
