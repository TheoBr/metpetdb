package edu.rpi.metpetdb.server.impl;

import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.client.service.GeoReferenceService;

public class GeoReferenceServiceImpl  extends MpDbServlet implements GeoReferenceService {
	public long getCount() {
		return new GeoReferenceDAO(this.currentSession()).getCount();
	}
}
