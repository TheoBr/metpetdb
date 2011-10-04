package edu.rpi.metpetdb.server.search.bridges;

import org.apache.solr.util.NumberUtils;
import org.hibernate.search.bridge.StringBridge;

public class FloatBridge implements StringBridge {

	public String objectToString(Object object) {
		if (object == null)
			return "";
		else
			return NumberUtils.float2sortableStr((Float) object);
	}
}
