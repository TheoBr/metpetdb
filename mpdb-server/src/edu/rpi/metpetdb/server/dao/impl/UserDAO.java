package edu.rpi.metpetdb.server.dao.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.UserNotFoundException;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class UserDAO extends MpDbDAO<User> {

	public UserDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public User delete(User inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public User fill(User inst) throws MpDbException {
		// Use Id
		if (inst.getId() > 0) {
			Query q = namedQuery("User.byId");
			q.setParameter("id", inst.getId());
			if (getResult(q) != null)
				return (User) getResult(q);
		}

		// Use Name
		if (inst.getEmailAddress() != null) {
			Query q = namedQuery("User.byEmailAddress");
			q.setParameter("emailAddress", inst.getEmailAddress());
			if (getResult(q) != null)
				return (User) getResult(q);
		}

		throw new UserNotFoundException();
	}
	
	public Collection<User> getEligableSponsors(Role role) throws MpDbException{
		final Query q = namedQuery("User.eligableSponsors");
		q.setParameter("rank", role.getRank());
		return getResults(q);
	}
	
	public Collection<Role> getEligableRoles(int currentRank) throws MpDbException {
		final Query q = namedQuery("Role.eligableRoles");
		q.setParameter("rank", currentRank);
		return getResults(q);
	}

	public User getUserByContributorCode(String contributorCode) throws MpDbException {
		final Query q = namedQuery("User.byContributorCode");
		q.setParameter("contributorCode", contributorCode);
		return (User) getResult(q);
	}
	
	
	public Results<RoleChange> getSponsorRoleChanges(int sponsorId, PaginationParameters p) throws MpDbException {
		final Query q = namedQuery("RoleChange.bySponsorId");
		final Query sizeQuery = sizeQuery("RoleChange.bySponsorId", sponsorId);
		final Query pageQuery = pageQuery("RoleChange.bySponsorId", p, sponsorId);
		final List<RoleChange> l = (List<RoleChange>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();
		return new Results<RoleChange>(size, l);
	}
	
	public List<Long> getSponsorRoleChangeIds(int sponsorId) throws MpDbException {
		final Query q = namedQuery("RoleChange.bySponsorId.Ids");
		q.setLong("id", sponsorId);
		return ((List<Long>) getResults(q));
	}

	public Object[] allNames() throws MpDbException{
		final Query q = namedQuery("User.all/name");
		return	((List<String>)getResults(q)).toArray();
	}
	@Override
	public User save(User inst) throws MpDbException {
		inst = _save(inst);
		return inst;
	}

	public User getNameById(int user_id) throws MpDbException {
		User u = new User();
		u.setId(user_id);
		u = fill(u);
		return u;
	}
	
	

}
