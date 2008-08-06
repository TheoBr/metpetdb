package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;

public interface ImageService extends RemoteService {
	List<ImageDTO> allImages(final long subsampleId);
	ImageDTO details(long id) throws DAOException;
	ImageDTO saveImage(ImageDTO image) throws ValidationException,
			LoginRequiredException, DAOException;
	XrayImageDTO saveImage(XrayImageDTO xrayimg) throws ValidationException,
			LoginRequiredException, DAOException;
	ImageOnGridDTO saveImageOnGrid(ImageOnGridDTO iog)
			throws ValidationException, LoginRequiredException, DAOException;
	void delete(ImageDTO i);
	ImageOnGridDTO rotate(ImageOnGridDTO iog, int degrees);
}
