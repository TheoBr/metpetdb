package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

/** @see ImageBrowserService */
public interface ImageBrowserServiceAsync {
	void details(long id, AsyncCallback<Grid> ac);

	void imagesOnGrid(long id, AsyncCallback<List<ImageOnGrid>> ac);

	void saveGrid(Grid g, AsyncCallback<Grid> ac);
	
	void saveAll(Collection<Grid> grids, AsyncCallback<Void> ac);
	
	void  allFromManySubsamples(final Collection<Long> subsampleIds, AsyncCallback<List<Grid>> ac);
}
