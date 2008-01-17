package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public interface ImageService extends RemoteService {
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.Image>
	 * @param subsampleId
	 * @return
	 * @throws NoSuchObjectException
	 */
	ArrayList allImages(final long subsampleId) throws NoSuchObjectException;
	Image details(long id) throws NoSuchObjectException;
	Image saveImage(Image image) throws ValidationException,
			LoginRequiredException;
	ImageOnGrid saveImageOnGrid(ImageOnGrid iog) throws ValidationException,
			LoginRequiredException;
	void delete(Image i);
	ImageOnGrid rotate(ImageOnGrid iog, int degrees);
}
