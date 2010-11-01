package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Image;

/** @see ConstantsService */
public interface ConstantsServiceAsync {
	
	void getOxidesAndElementsOrderedBySortOrder( AsyncCallback<List<String>> ac);
}
