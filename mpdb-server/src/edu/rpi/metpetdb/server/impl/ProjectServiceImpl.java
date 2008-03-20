package edu.rpi.metpetdb.server.impl;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DuplicateValueException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.Project;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public ProjectDTO details(final int projectId) throws NoSuchObjectException {
		final ProjectDTO p = cloneBean(byId("Project", projectId));
		return p;
	}

	public ProjectDTO saveProject(ProjectDTO projectDTO) throws ValidationException,
			LoginRequiredException {
		doc.validate(projectDTO);
		if (projectDTO.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify projects you don't own.");
		Project project = mergeBean(projectDTO);
		try {
			if (project.mIsNew()) {
				project.getMembers().add(project.getOwner());
				insert(project);
			} else
				project = update(merge(project));
			commit();
			return cloneBean(project);
		} catch (ConstraintViolationException cve) {
			if ("projects_nk".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.Project_name, project.getName());
			throw cve;
		}
	}
	
	public Results samplesFromProject(PaginationParameters parameters, long id) {
		final String name = "Sample.forProject";
		return toResults(sizeQuery(name, id), pageQuery(name, parameters, id));
	}
}