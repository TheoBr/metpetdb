package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ProjectDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public Results<Project> all(final PaginationParameters p, final long ownerId) throws MpDbException {
		return (new ProjectDAO(this.currentSession())).getForOwner(p, ownerId);
	}

	public List<Project> all(final long userId) throws MpDbException {
		List<Project> lDAO = (new ProjectDAO(this.currentSession()))
				.getForOwner(userId);
		return (lDAO);
	}

	public Project details(final int projectId) throws MpDbException {
		Project p = new Project();
		p.setId(projectId);
		p = (new ProjectDAO(this.currentSession())).fill(p);
		return (p);
	}

	public Project saveProject(Project project) throws ValidationException,
			LoginRequiredException, MpDbException {
		doc.validate(project);
		project.getMembers().add(project.getOwner());

		project = (new ProjectDAO(this.currentSession())).save(project);
		commit();
		return (project);
	}

	// TODO: Code Dup? This the same as SampleServiceImpl.projectSamples()?
	public Results<Sample> samplesFromProject(PaginationParameters parameters,
			long id) throws MpDbException {
		return (new SampleDAO(this.currentSession()).getProjectSamples(
				parameters, id));
	}

	public Results<User> allProjectMembers(PaginationParameters p, int id)
			throws MpDbException {
		return (new ProjectDAO(this.currentSession())).getMembersForProject(p, id);
	}
}
