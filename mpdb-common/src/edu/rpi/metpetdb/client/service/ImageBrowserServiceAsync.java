package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.GridDTO;

/** @see ImageBrowserService */
public interface ImageBrowserServiceAsync {
	void bySampleId(long id, PaginationParameters parameters, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void imagesOnGrid(long id, AsyncCallback ac);
	void saveGrid(GridDTO g, AsyncCallback ac);
}
