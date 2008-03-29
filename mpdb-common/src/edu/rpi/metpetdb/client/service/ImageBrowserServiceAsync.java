package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;

/** @see ImageBrowserService */
public interface ImageBrowserServiceAsync {
	void details(long id, AsyncCallback<GridDTO> ac);

	void imagesOnGrid(long id, AsyncCallback<List<ImageOnGridDTO>> ac);

	void saveGrid(GridDTO g, AsyncCallback<GridDTO> ac);
}
