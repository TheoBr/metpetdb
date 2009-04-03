package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ImageServiceAsync {
	void allImages(long subsampleId, AsyncCallback<List<Image>> ac);

	void details(long id, AsyncCallback<Image> ac);

	void saveImage(Image image, AsyncCallback<Image> ac);

	void saveImageOnGrid(ImageOnGrid iog, AsyncCallback<ImageOnGrid> ac);

	void saveImage(XrayImage img, AsyncCallback<XrayImage> ac);

	void delete(Image i, AsyncCallback<Void> ac);

	void rotate(ImageOnGrid iog, int degrees, AsyncCallback<ImageOnGrid> ac);

	/**
	 * Used to fetch the images eligable for addition to an image map, aka
	 * images not already on the map
	 * 
	 * @param subsampleId
	 * @param ac
	 * @return
	 */
	void allForImageMap(long subsampleId, PaginationParameters p, AsyncCallback<Results<Image>> ac);
}
