package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Project;

/** @see ProjectService */
public interface ProjectServiceAsync {
	void details(int projectId, AsyncCallback ac);
	void saveProject(Project proj, AsyncCallback ac);
	void samplesFromProject(PaginationParameters parameters, long id, AsyncCallback ac);
}