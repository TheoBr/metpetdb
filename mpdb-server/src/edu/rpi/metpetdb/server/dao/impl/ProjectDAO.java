package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ProjectNotFoundException;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
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
			Project p = (Project) getResult(q);
			if (p != null)
				return p;
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

	public Results<User> getMembersForProject(final PaginationParameters p,
			int id) throws MpDbException {
		final Query sizeQ = sizeQuery("User.byProjectId");
		final Query pageQ = pageQuery("User.byProjectId", p);
		sizeQ.setLong("id", id);
		pageQ.setLong("id", id);
		return getMembers(sizeQ, pageQ);
	}
	
	private Results<User> getMembers(Query sizeQuery, Query pageQuery) throws MpDbException {
		final List<User> l = (List<User>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();
		
		return new Results<User>(size, l);
	}

	public Invite saveInvite(Invite i, User u) throws MpDbException {
		Project p = new Project();
		p.setId(i.getProject_id());
		p = fill(p);
		p.getInvites().add(u);
		save(p);
		return i;
	}
	
	public List<Project> getInvitesForUser(int id) throws MpDbException {
		final Query q = namedQuery("User.invites");
		q.setInteger("id", id);
		final List<Project> l = (List<Project>) getResults(q);
		return l;
	}
}
