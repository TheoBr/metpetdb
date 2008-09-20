package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ProjectDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public Results<Project> all(final PaginationParameters p, final long ownerId) {
		return (new ProjectDAO(this.currentSession())).getForOwner(p, ownerId);
	}

	public List<Project> all(final long userId) {
		List<Project> lDAO = (new ProjectDAO(this.currentSession()))
				.getForOwner(userId);
		return (lDAO);
	}

	public Project details(final int projectId) throws DAOException {
		Project p = new Project();
		p.setId(projectId);
		p = (new ProjectDAO(this.currentSession())).fill(p);
		return (p);
	}

	public Project saveProject(Project project) throws ValidationException,
			LoginRequiredException, DAOException {
		doc.validate(project);
		if (project.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify projects you don't own.");
		project.getMembers().add(project.getOwner());

		project = (new ProjectDAO(this.currentSession())).save(project);
		commit();
		return (project);
	}

	// TODO: Code Dup? This the same as SampleServiceImpl.projectSamples()?
	public Results<Sample> samplesFromProject(PaginationParameters parameters,
			long id) {
		return (new SampleDAO(this.currentSession()).getProjectSamples(
				parameters, id));
	}
}
