package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;

public interface ImageService extends RemoteService {
	ArrayList<ImageDTO> allImages(final long subsampleId) throws NoSuchObjectException;
	ImageDTO details(long id) throws NoSuchObjectException;
	ImageDTO saveImage(ImageDTO image) throws ValidationException,
			LoginRequiredException;
	ImageOnGridDTO saveImageOnGrid(ImageOnGridDTO iog) throws ValidationException,
			LoginRequiredException;
	void delete(ImageDTO i);
	ImageOnGridDTO rotate(ImageOnGridDTO iog, int degrees);
}
