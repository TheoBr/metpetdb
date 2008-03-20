package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

/** @see ProjectService */
public interface ProjectServiceAsync {
	void details(int projectId, AsyncCallback ac);
	void saveProject(ProjectDTO proj, AsyncCallback ac);
	void samplesFromProject(PaginationParameters parameters, long id, AsyncCallback ac);
}