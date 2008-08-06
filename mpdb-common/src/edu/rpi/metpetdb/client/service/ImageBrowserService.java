package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;

public interface ImageBrowserService extends RemoteService {
	GridDTO details(long id) throws DAOException;

	List<ImageOnGridDTO> imagesOnGrid(long id);

	GridDTO saveGrid(GridDTO g) throws LoginRequiredException, DAOException;
}
