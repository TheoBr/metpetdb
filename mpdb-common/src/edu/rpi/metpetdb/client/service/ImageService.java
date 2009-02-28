package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.XrayImage;

public interface ImageService extends RemoteService {
	List<Image> allImages(final long subsampleId) throws MpDbException;
	Image details(long id) throws MpDbException;
	Image saveImage(Image image) throws ValidationException,
			LoginRequiredException, MpDbException;
	XrayImage saveImage(XrayImage xrayimg) throws ValidationException,
			LoginRequiredException, MpDbException;
	ImageOnGrid saveImageOnGrid(ImageOnGrid iog) throws ValidationException,
			LoginRequiredException, MpDbException;
	void delete(Image i);
	ImageOnGrid rotate(ImageOnGrid iog, int degrees);
}
