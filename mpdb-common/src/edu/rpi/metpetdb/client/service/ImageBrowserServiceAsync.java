package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

/** @see ImageBrowserService */
public interface ImageBrowserServiceAsync {
	void bySampleId(long id, PaginationParameters parameters, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void imagesOnGrid(long id, AsyncCallback ac);
	void saveGrid(GridDTO g, AsyncCallback ac);
}
