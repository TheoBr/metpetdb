package edu.rpi.metpetdb.server;

import java.net.URL;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;

import edu.rpi.metpetdb.server.DataStore;

/**
 * Test version of DataStore (mostly just uses a different database, therefore a
 * different hibernate.cfg.xml)
 * 
 * @author anthony
 * 
 */
public class DataStoreTest extends DataStore {
	
	public DataStoreTest() {
		super();
	}
	
	protected static synchronized Configuration getConfiguration() {
		if (config == null) {
			final Configuration cfg = new Configuration();
			final URL x = DataStore.class.getResource("dao/hibernate.cfg.xml");
			if (x == null)
				throw new MappingException("Missing dao/hibernate.cfg.xml.");
			cfg.configure(x);
			config = cfg;
		}
		return config;
	}

}
