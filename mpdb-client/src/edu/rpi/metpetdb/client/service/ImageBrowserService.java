package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public interface ImageBrowserService extends RemoteService {
	Grid details(long id) throws MpDbException;

	List<ImageOnGrid> imagesOnGrid(long id) throws MpDbException;

	Grid saveGrid(Grid g) throws LoginRequiredException, MpDbException;
	
	void saveAll(Collection<Grid> grids)
		throws ValidationException, LoginRequiredException, MpDbException;
	
	List<Grid> allFromManySubsamples(final Collection<Long> subsampleIds) throws MpDbException;
}
