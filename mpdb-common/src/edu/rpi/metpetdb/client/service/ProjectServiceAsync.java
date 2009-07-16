package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see ProjectService */
public interface ProjectServiceAsync {

	void all(PaginationParameters p, long ownerId,
			AsyncCallback<Results<Project>> ac);

	void details(int projectId, AsyncCallback<Project> ac);

	void saveProject(Project proj, AsyncCallback<Project> ac);
	
	void deleteAll(Collection<Integer> ids, AsyncCallback<Void> ac);

	void samplesFromProject(PaginationParameters parameters, long id,
			AsyncCallback<Results<Sample>> ac);

	void all(long userId, AsyncCallback<List<Project>> ac);

	void allProjectMembers(PaginationParameters p, int id,
			AsyncCallback<Results<User>> ac);

	void saveInvite(Invite i, AsyncCallback<Invite> ac);
	
	void acceptInvite(Invite i, AsyncCallback<Void> ac);
	
	void rejectInvite(Invite i, AsyncCallback<Void> ac);
	
	void getInvitesForUser(int id, AsyncCallback<List<Project>> ac);
}
