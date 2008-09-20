package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.XrayImage;

public interface ImageServiceAsync {
	void allImages(long subsampleId, AsyncCallback<List<Image>> ac);

	void details(long id, AsyncCallback<Image> ac);

	void saveImage(Image image, AsyncCallback<Image> ac);

	void saveImageOnGrid(ImageOnGrid iog, AsyncCallback<ImageOnGrid> ac);

	void saveImage(XrayImage img, AsyncCallback<XrayImage> ac);

	void delete(Image i, AsyncCallback<Object> ac);

	void rotate(ImageOnGrid iog, int degrees, AsyncCallback<Object> ac);
}
