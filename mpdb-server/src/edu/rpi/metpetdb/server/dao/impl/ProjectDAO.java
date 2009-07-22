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
		_delete(inst);
		return null;
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

	public List<Project> getForUser(final long userId) throws MpDbException{
		Query q = namedQuery("Project.byMemberId");
		q.setLong("id", userId);
		return (List<Project>) getResults(q);
	}

	public Results<Project> getForUser(final PaginationParameters p,
			final long userId) throws MpDbException {
		final Query sizeQ = sizeQuery("Project.byMemberId");
		final Query pageQ = pageQuery("Project.byMemberId", p);
		sizeQ.setLong("id", userId);
		pageQ.setLong("id", userId);
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
		Query q = sess.createSQLQuery("INSERT INTO project_invites " +
				"VALUES (" + i.getProject_id() + "," + i.getUser_id() + ",now(),'" + i.getStatus() +"');");
		q.executeUpdate();
		return i;
	}
	
	public void rejectInvite(Invite i) throws MpDbException {
		Query q = sess.createSQLQuery("UPDATE project_invites " +
				"SET status='" + i.getStatus() + "',action_timestamp=now() " + 
				"WHERE project_id =" + i.getProject_id() + " AND user_id=" + i.getUser_id() + ";");
		q.executeUpdate();
		return;
	}
	
	public void acceptInvite(Invite i, User u) throws MpDbException {
		Project p = new Project();
		p.setId(i.getProject_id());
		p = fill(p);
		p.getMembers().add(u);
		save(p);
		Query q = sess.createSQLQuery("UPDATE project_invites " +
				"SET status='" + i.getStatus() + "',action_timestamp=now() " + 
				"WHERE project_id =" + i.getProject_id() + " AND user_id=" + i.getUser_id() + ";");
		q.executeUpdate();
		return;
	}
	
	public List<Project> getInvitesForUser(int id) throws MpDbException {
		final Query q = namedQuery("Project.invitesById");
		q.setInteger("id", id);
		return (List<Project>) getResults(q);
	}
}
