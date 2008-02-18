package edu.rpi.metpetdb.server.impl;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DuplicateValueException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.Project;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public ProjectDTO details(final int projectId) throws NoSuchObjectException {
		final ProjectDTO p = (ProjectDTO) clone(byId("Project", projectId));
		p.setMembers(load(p.getMembers()));
		p.setSamples(load(p.getSamples()));
		forgetChanges();
		return p;
	}

	public ProjectDTO saveProject(ProjectDTO projectDTO) throws ValidationException,
			LoginRequiredException {
		doc.validate(projectDTO);
		if (projectDTO.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify projects you don't own.");
		Project project = (Project) merge(projectDTO);
		try {
			if (project.mIsNew()) {
				project.getMembers().add(project.getOwner());
				insert(project);
			} else
				project = (Project) update(merge(project));
			commit();
			project.setMembers(load(project.getMembers()));
			project.setSamples(load(project.getSamples()));
			forgetChanges();
			return (ProjectDTO) clone(project);
		} catch (ConstraintViolationException cve) {
			if ("projects_nk".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.Project_name, projectDTO.getName());
			throw cve;
		}
	}
	
	public Results samplesFromProject(PaginationParameters parameters, long id) {
		final String name = "Sample.forProject";
		return toResults(sizeQuery(name, id), pageQuery(name, parameters, id));
	}
}