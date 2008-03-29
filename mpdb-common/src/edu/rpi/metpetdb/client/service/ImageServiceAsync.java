package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;

public interface ImageServiceAsync {
	void allImages(long subsampleId, AsyncCallback<List<ImageDTO>> ac);

	void details(long id, AsyncCallback<ImageDTO> ac);

	void saveImage(ImageDTO image, AsyncCallback<ImageDTO> ac);

	void saveImageOnGrid(ImageOnGridDTO iog, AsyncCallback<ImageOnGridDTO> ac);

	void delete(ImageDTO i, AsyncCallback<Object> ac);

	void rotate(ImageOnGridDTO iog, int degrees, AsyncCallback<Object> ac);
}
