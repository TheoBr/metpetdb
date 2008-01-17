package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Grid;

/** @see ImageBrowserService */
public interface ImageBrowserServiceAsync {
	void bySampleId(long id, PaginationParameters parameters, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void imagesOnGrid(long id, AsyncCallback ac);
	void saveGrid(Grid g, AsyncCallback ac);
}
