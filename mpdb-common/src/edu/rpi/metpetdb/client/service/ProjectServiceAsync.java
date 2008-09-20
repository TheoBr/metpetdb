package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see ProjectService */
public interface ProjectServiceAsync {

	void all(PaginationParameters p, long ownerId,
			AsyncCallback<Results<Project>> ac);

	void details(int projectId, AsyncCallback<Project> ac);

	void saveProject(Project proj, AsyncCallback<Project> ac);

	void samplesFromProject(PaginationParameters parameters, long id,
			AsyncCallback<Results<Sample>> ac);

	void all(long userId, AsyncCallback<List<Project>> ac);
}
