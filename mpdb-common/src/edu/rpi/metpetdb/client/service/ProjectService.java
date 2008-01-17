package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Project;

/**
 * Server operations to fetch and manipulate {@link Project}s.
 */
public interface ProjectService extends RemoteService {
	/**
	 * Get the data for a project.
	 * 
	 * @param projectId
	 *            the project to fetch the data for.
	 * @return requested project data.
	 * @throws NoSuchObjectException
	 *             the project does not exist in the database.
	 */
	Project details(int projectId) throws NoSuchObjectException;

	/**
	 * Create or update an existing project.
	 * 
	 * @param proj
	 *            the project to create, or the project to be updated.
	 * @return the same project, after any database edits have been applied.
	 * @throws LoginRequiredException
	 *             current user is not logged in.
	 * @throws ValidationException
	 *             something is wrong with the project specification.
	 */
	Project saveProject(Project proj) throws LoginRequiredException,
			ValidationException;
	
	Results samplesFromProject(PaginationParameters parameters, long id);
}
