package edu.rpi.metpetdb.server.security;

import org.hibernate.HibernateException;
import org.hibernate.event.LoadEvent;
import org.hibernate.event.LoadEventListener;
import org.hibernate.event.def.DefaultLoadEventListener;

public class SecurityLoadListener extends DefaultLoadEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onLoad(final LoadEvent event,
			final LoadEventListener.LoadType loadType)
			throws HibernateException {
		super.onLoad(event, loadType);
	}

}
