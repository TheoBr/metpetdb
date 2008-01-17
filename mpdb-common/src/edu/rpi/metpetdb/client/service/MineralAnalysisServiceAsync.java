package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.MineralAnalysis;

public interface MineralAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveMineralAnalysis(MineralAnalysis mineralAnalysis, AsyncCallback ac);
	void all(long subsampleId, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);
}
