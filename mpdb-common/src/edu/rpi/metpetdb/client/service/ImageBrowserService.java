package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.GridDTO;

public interface ImageBrowserService extends RemoteService {
	Results bySampleId(long id, PaginationParameters parameters)
			throws NoSuchObjectException;
	GridDTO details(long id) throws NoSuchObjectException;
	ArrayList imagesOnGrid(long id) throws NoSuchObjectException;
	GridDTO saveGrid(GridDTO g) throws LoginRequiredException;
}
