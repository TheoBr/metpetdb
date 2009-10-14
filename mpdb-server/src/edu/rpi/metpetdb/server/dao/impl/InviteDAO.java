package edu.rpi.metpetdb.server.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class InviteDAO extends MpDbDAO<Invite> {

	public InviteDAO(Session session) {
		super(session);
	}

	@Override
	public Invite delete(Invite inst) throws MpDbException{
		throw new FunctionNotImplementedException();
	}

	@Override
	public Invite fill(Invite inst) throws MpDbException{
		Query q = namedQuery("Invite.byProjectIdMemberId");
		q.setInteger("project_id", inst.getProject_id());
		q.setInteger("user_id", inst.getUser_id());
		return (Invite) getResult(q);
	}

	@Override
	public Invite save(Invite inst) throws MpDbException{
		inst = _save(inst);
		return inst;
	}

	public List<Invite> getInvitesForUser(int id) throws MpDbException{
		Query q = namedQuery("Invite.byUserId");
		q.setInteger("user_id", id);
		return (List<Invite>) getResults(q);
	}
	
	public List<Invite> getInvitesForProject(long id) throws MpDbException {
		Query q = namedQuery("Invite.byProjectId");
		q.setLong("project_id", id);
		return (List<Invite>) getResults(q);
	}

	public void acceptInvite(Invite i, User u) throws MpDbException {
		Project p = new Project();
		ProjectDAO dao = new ProjectDAO(this.sess);
		p.setId(i.getProject_id());
		p = dao.fill(p);
		u.getProjects().add(p);
		UserDAO uDAO = new UserDAO(this.sess);
		uDAO.save(u);
		save(i);
	}
	
	public Invite inviteDetails(int id) throws MpDbException {
		Query q = namedQuery("Invite.byId");
		q.setInteger("id", id);
		return (Invite) getResult(q);
	}
}