package edu.rpi.metpetrest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.rpi.metpetrest.dao.SampleDAOImpl;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class MyCacheListener implements CacheEventListener {

	
	public Object clone()
	{
		return this;
		
	}
	
	private Logger logger = LoggerFactory.getLogger(MyCacheListener.class);

	 
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyElementEvicted(Ehcache arg0, Element arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyElementExpired(Ehcache arg0, Element arg1) {
		
	logger.info("creation/expiration/lastaccess/lastupdate" + new Date(arg1.getCreationTime()).toLocaleString() + "/" +  new Date(arg1.getExpirationTime()).toLocaleString() + "/" + new Date(arg1.getLastAccessTime()).toLocaleString() + "/" + new Date(arg1.getLastUpdateTime()).toLocaleString());
	}

	@Override
	public void notifyElementPut(Ehcache arg0, Element arg1)
			throws CacheException {
	//	logger.info("tti/ttl " + arg1.getTimeToIdle() + "/"+ arg1.getTimeToLive());
	}

	@Override
	public void notifyElementRemoved(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyElementUpdated(Ehcache arg0, Element arg1)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyRemoveAll(Ehcache arg0) {
		// TODO Auto-generated method stub

	}

}
