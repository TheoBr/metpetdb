package edu.rpi.metpetdb.server.impl;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DuplicateValueException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.service.ProjectService;
import edu.rpi.metpetdb.server.MpDbServlet;

public class ProjectServiceImpl extends MpDbServlet implements ProjectService {
	private static final long serialVersionUID = 1L;

	public Project details(final int projectId) throws NoSuchObjectException {
		final Project p = (Project) byId("Project", projectId);
		p.setMembers(load(p.getMembers()));
		p.setSamples(load(p.getSamples()));
		forgetChanges();
		return p;
	}

	public Project saveProject(Project p) throws ValidationException,
			LoginRequiredException {
		doc.validate(p);
		if (p.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify projects you don't own.");

		try {
			if (p.mIsNew()) {
				p.getMembers().add(p.getOwner());
				insert(p);
			} else
				p = (Project) update(merge(p));
			commit();
			p.setMembers(load(p.getMembers()));
			p.setSamples(load(p.getSamples()));
			forgetChanges();
			return p;
		} catch (ConstraintViolationException cve) {
			if ("projects_nk".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.Project_name, p.getName());
			throw cve;
		}
	}
	
	public Results samplesFromProject(PaginationParameters parameters, long id) {
		final String name = "Sample.forProject";
		return toResults(sizeQuery(name, id), pageQuery(name, parameters, id));
	}
}