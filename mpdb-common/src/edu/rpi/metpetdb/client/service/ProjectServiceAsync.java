package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see ProjectService */
public interface ProjectServiceAsync {
	void details(int projectId, AsyncCallback<ProjectDTO> ac);

	void saveProject(ProjectDTO proj, AsyncCallback<ProjectDTO> ac);

	void samplesFromProject(PaginationParameters parameters, long id,
			AsyncCallback<Results<SampleDTO>> ac);

	void all(long userId, AsyncCallback<List<ProjectDTO>> ac);
}
