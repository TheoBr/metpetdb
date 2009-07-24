package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ImageServiceAsync {
	void details(long id, AsyncCallback<Image> ac);
	
	void details(List<Long> ids, AsyncCallback<List<Image>> ac);

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
	void allImages(long subsampleId, PaginationParameters p, AsyncCallback<Results<Image>> ac);
	
	void allImageIds(long subsampleId, AsyncCallback<Map<Object,Boolean>> ac);
	
	void makePublicBySubsampleId(ArrayList<Subsample> selectedSubsamples, AsyncCallback<Void> ac);

	void makePublicBySampleId(ArrayList<Sample> samples, AsyncCallback<Void> ac);
	
	 void makeMobileImages(AsyncCallback<Void> ac);
}
