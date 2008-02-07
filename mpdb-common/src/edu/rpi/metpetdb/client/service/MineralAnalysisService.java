package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralAnalysis;

public interface MineralAnalysisService extends RemoteService {
	Results all(PaginationParameters parameters, final long subsampleId);
	MineralAnalysis details(long id) throws NoSuchObjectException;
	MineralAnalysis saveMineralAnalysis(MineralAnalysis mineralAnalysis)
			throws ValidationException, LoginRequiredException;
	ArrayList all(long subsampleId) throws NoSuchObjectException;
	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}
