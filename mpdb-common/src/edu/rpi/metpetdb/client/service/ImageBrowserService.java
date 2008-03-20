package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ImageBrowserService extends RemoteService {
	Results bySampleId(long id, PaginationParameters parameters)
			throws NoSuchObjectException;
	GridDTO details(long id) throws NoSuchObjectException;
	List<ImageOnGridDTO> imagesOnGrid(long id) throws NoSuchObjectException;
	GridDTO saveGrid(GridDTO g) throws LoginRequiredException;
}
