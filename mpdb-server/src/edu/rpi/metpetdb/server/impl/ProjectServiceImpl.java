package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.dao.impl.ProjectDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.model.Project;
import edu.rpi.metpetdb.server.model.Sample;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public Results<ProjectDTO> all(final PaginationParameters p,
			final long ownerId) {
		ResultsFromDAO<Project> pDAO = (new ProjectDAO(this.currentSession()))
				.getForOwner(p, ownerId);
		List<ProjectDTO> l = cloneBean(pDAO.getList());
		return new Results<ProjectDTO>(pDAO.getCount(), l);
	}

	public List<ProjectDTO> all(final long userId) {
		List<Project> lDAO = (new ProjectDAO(this.currentSession()))
				.getForOwner(userId);
		return cloneBean(lDAO);
	}

	public ProjectDTO details(final int projectId) throws DAOException {
		Project p = new Project();
		p.setId(projectId);
		p = (new ProjectDAO(this.currentSession())).fill(p);
		return cloneBean(p);
	}

	public ProjectDTO saveProject(ProjectDTO projectDTO)
			throws ValidationException, LoginRequiredException, DAOException {
		doc.validate(projectDTO);
		if (projectDTO.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify projects you don't own.");

		Project project = mergeBean(projectDTO);
		project.getMembers().add(project.getOwner());

		project = (new ProjectDAO(this.currentSession())).save(project);
		commit();

		return cloneBean(project);
	}

	// TODO: Code Dup? This the same as SampleServiceImpl.projectSamples()?
	public Results<SampleDTO> samplesFromProject(
			PaginationParameters parameters, long id) {
		final ResultsFromDAO<Sample> l = (new SampleDAO(this.currentSession())
				.getProjectSamples(parameters, id));
		final List<SampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SampleDTO>(l.getCount(), lDTO);
	}
}
