package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Grid;

public interface ImageBrowserService extends RemoteService {
	Results bySampleId(long id, PaginationParameters parameters)
			throws NoSuchObjectException;
	Grid details(long id) throws NoSuchObjectException;
	ArrayList imagesOnGrid(long id) throws NoSuchObjectException;
	Grid saveGrid(Grid g) throws LoginRequiredException;
}
