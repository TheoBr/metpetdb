package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ProjectNotFoundException;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.model.Project;

public class ProjectDAO extends MpDbDAO<Project> {

	public ProjectDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Project delete(Project inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Project fill(Project inst) throws DAOException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("Project.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (Project) q.uniqueResult();
		}

		throw new ProjectNotFoundException();
	}

	@Override
	public Project save(Project inst) throws DAOException {
		inst = _save(inst);
		return inst;
	}

	public List<Project> getForOwner(final long userId) {
		Query q = namedQuery("Project.byOwnerId");
		q.setLong("ownerId", userId);
		return (List<Project>) q.list();
	}

	public ResultsFromDAO<Project> getForOwner(final PaginationParameters p,
			final long ownerId) {
		final Query sizeQ = sizeQuery("Project.byOwnerId");
		final Query pageQ = pageQuery("Project.byOwnerId", p);
		sizeQ.setLong("ownerId", ownerId);
		pageQ.setLong("ownerId", ownerId);
		return getProjects(sizeQ, pageQ);
	}

	private ResultsFromDAO<Project> getProjects(Query sizeQuery, Query pageQuery) {
		final List<Project> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();

		return new ResultsFromDAO<Project>(size, l);
	}
}
