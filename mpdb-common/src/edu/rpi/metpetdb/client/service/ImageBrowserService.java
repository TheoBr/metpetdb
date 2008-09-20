package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public interface ImageBrowserService extends RemoteService {
	Grid details(long id) throws DAOException;

	List<ImageOnGrid> imagesOnGrid(long id);

	Grid saveGrid(Grid g) throws LoginRequiredException, DAOException;
}
