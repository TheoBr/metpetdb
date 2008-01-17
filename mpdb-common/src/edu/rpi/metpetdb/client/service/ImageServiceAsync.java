package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public interface ImageServiceAsync {
	void allImages(long subsampleId, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveImage(Image image, AsyncCallback ac);
	void saveImageOnGrid(ImageOnGrid iog, AsyncCallback ac);
	void delete(Image i, AsyncCallback ac);
	void rotate(ImageOnGrid iog, int degrees, AsyncCallback ac);
}
