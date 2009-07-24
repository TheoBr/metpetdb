package edu.rpi.metpetdb.server.impl;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.InviteDAO;
import edu.rpi.metpetdb.server.dao.impl.ProjectDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public Results<Project> all(final PaginationParameters p, final long userId) throws MpDbException {
		return (new ProjectDAO(this.currentSession())).getForUser(p, userId);
	}

	public List<Project> all(final long userId) throws MpDbException {
		List<Project> lDAO = (new ProjectDAO(this.currentSession()))
				.getForUser(userId);
		return (lDAO);
	}
	
	/**
	 * used for pagination tables to select all
	 * the boolean is null because projects are not public or private
	 */
	public Map<Object,Boolean> allIdsForUser(final long userId) throws MpDbException {
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Integer i : new ProjectDAO(this.currentSession()).getAllIdsForUser(userId)){
			ids.put(i,null);
		}
		return ids;
	}

	public Project details(final int projectId) throws MpDbException {
		Project p = new Project();
		p.setId(projectId);
		p = (new ProjectDAO(this.currentSession())).fill(p);
		return (p);
	}
	
	public List<Project> details(final List<Integer> ids) throws MpDbException {
		List<Project> projects = new ArrayList<Project>();
		for (Integer projectId : ids){
			Project p = new Project();
			p.setId(projectId);
			p = (new ProjectDAO(this.currentSession())).fill(p);
			projects.add(p);
		}
		return projects;
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
	
	public Map<Object,Boolean> allProjectMemberIds(int id) throws MpDbException {
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Long l : new ProjectDAO(this.currentSession()).getProjectMemberIds(id)){
			ids.put(l,null);
		}
		return ids;
	}
	
	public Invite saveInvite(Invite i) throws MpDbException {
		i.setAction_timestamp(new Timestamp(new Date().getTime()));
		new InviteDAO(this.currentSession()).save(i);
		commit();
		return i;
	}
	
	public List<Invite> getInvitesForUser(int id) throws MpDbException {
		return new InviteDAO(currentSession()).getInvitesForUser(id);
	}

	public void acceptInvite(Invite i) throws MpDbException {
		User u = new User();
		u.setId(i.getUser_id());
		u = (new UserDAO(this.currentSession())).fill(u);
		i.setStatus("Accepted");
		i.setAction_timestamp(new Timestamp(new Date().getTime()));
		new InviteDAO(this.currentSession()).acceptInvite(i, u);
		commit();
		return;
	}

	public void rejectInvite(Invite i) throws MpDbException {
		i.setStatus("Rejected");
		i.setAction_timestamp(new Timestamp(new Date().getTime()));
		new InviteDAO(this.currentSession()).save(i);
		commit();
		return;
	}

	public void deleteAll(Collection<Integer> ids) throws MpDbException,
			LoginRequiredException {
		final Iterator<Integer> itr = ids.iterator();
		while(itr.hasNext()) {
			deleteImpl(itr.next());
		}
		commit();
	}
	
	private void deleteImpl(int id) throws MpDbException, LoginRequiredException {
		ProjectDAO dao = new ProjectDAO(this.currentSession());
		Project p = new Project();
		p.setId(id);
		p = dao.fill(p);
		dao.delete(p);
	}

	public Map<Invite, Project> getProjectsForInvites(List<Invite> invites)
			throws MpDbException {
		Map<Invite,Project> inviteMap = new HashMap<Invite,Project>();
		final Iterator<Invite> itr = invites.iterator();
		while(itr.hasNext()){
			Invite current = itr.next();
			Project p = new Project();
			p.setId(current.getProject_id());
			p = (new ProjectDAO(this.currentSession())).fill(p);
			inviteMap.put(current, p);
		}
		return inviteMap;
	}
	
	public Invite inviteDetails(int id) throws MpDbException {
		return (new InviteDAO(this.currentSession())).inviteDetails(id);
	}
}
