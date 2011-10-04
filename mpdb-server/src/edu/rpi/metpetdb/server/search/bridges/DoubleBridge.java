package edu.rpi.metpetdb.server.search.bridges;

import org.apache.solr.util.NumberUtils;
import org.hibernate.search.bridge.StringBridge;

public class DoubleBridge implements StringBridge {

	public String objectToString(Object object) {
		if (object == null)
			return "";
		else
			return NumberUtils.double2sortableStr((Double) object);
	}
}