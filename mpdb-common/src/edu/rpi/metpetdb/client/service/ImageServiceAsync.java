package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;

public interface ImageServiceAsync {
	void allImages(long subsampleId, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveImage(ImageDTO image, AsyncCallback ac);
	void saveImageOnGrid(ImageOnGridDTO iog, AsyncCallback ac);
	void delete(ImageDTO i, AsyncCallback ac);
	void rotate(ImageOnGridDTO iog, int degrees, AsyncCallback ac);
}
