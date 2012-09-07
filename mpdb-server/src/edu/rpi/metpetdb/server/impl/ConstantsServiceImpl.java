package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.service.ConstantsService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ConstantsDAO;

public class ConstantsServiceImpl extends MpDbServlet implements
		ConstantsService {

	public List<String> getOxidesAndElementsOrderedBySortOrder()
	{
		return new ConstantsDAO().getOrderedOxidesAndElements(this.currentSession());		
	}

}
