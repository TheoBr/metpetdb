package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/**
 * Server operations to fetch and manipulate {@link Project}s.
 */
public interface ProjectService extends RemoteService {

	Results<Project> all(PaginationParameters p, final long ownerId)  throws MpDbException;
	/**
	 * Get the data for a project.
	 * 
	 * @param projectId
	 * 		the project to fetch the data for.
	 * @return requested project data.
	 * @throws MpDbException
	 * 		the project does not exist in the database.
	 */
	Project details(int projectId) throws MpDbException;

	List<Project> all(final long userId) throws MpDbException;

	/**
	 * Create or update an existing project.
	 * 
	 * @param proj
	 * 		the project to create, or the project to be updated.
	 * @return the same project, after any database edits have been applied.
	 * @throws LoginRequiredException
	 * 		current user is not logged in.
	 * @throws ValidationException
	 * 		something is wrong with the project specification.
	 * @throws MpDbException
	 * 		Error saving project to the database
	 */
	Project saveProject(Project proj) throws LoginRequiredException,
			ValidationException, MpDbException;

	Results<Sample> samplesFromProject(PaginationParameters parameters, long id)  throws MpDbException;
}
