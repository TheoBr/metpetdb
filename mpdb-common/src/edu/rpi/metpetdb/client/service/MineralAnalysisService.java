package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;

public interface MineralAnalysisService extends RemoteService {
	Results all(PaginationParameters parameters, final long subsampleId);
	MineralAnalysisDTO details(long id) throws NoSuchObjectException;
	MineralAnalysisDTO saveMineralAnalysis(MineralAnalysisDTO mineralAnalysis)
			throws ValidationException, LoginRequiredException;
	ArrayList<MineralAnalysisDTO> all(long subsampleId) throws NoSuchObjectException;
	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}
