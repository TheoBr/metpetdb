package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ProjectNotFoundException;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ProjectDAO extends MpDbDAO<Project> {

	public ProjectDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Project delete(Project inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Project fill(Project inst) throws MpDbException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("Project.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (Project) getResult(q);
		}

		throw new ProjectNotFoundException();
	}

	@Override
	public Project save(Project inst) throws MpDbException {
		inst = _save(inst);
		return inst;
	}

	public List<Project> getForOwner(final long userId) throws MpDbException{
		Query q = namedQuery("Project.byOwnerId");
		q.setLong("ownerId", userId);
		return (List<Project>) getResults(q);
	}

	public Results<Project> getForOwner(final PaginationParameters p,
			final long ownerId) throws MpDbException {
		final Query sizeQ = sizeQuery("Project.byOwnerId");
		final Query pageQ = pageQuery("Project.byOwnerId", p);
		sizeQ.setLong("ownerId", ownerId);
		pageQ.setLong("ownerId", ownerId);
		return getProjects(sizeQ, pageQ);
	}

	private Results<Project> getProjects(Query sizeQuery, Query pageQuery) throws MpDbException {
		final List<Project> l = (List<Project>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();

		return new Results<Project>(size, l);
	}
}
