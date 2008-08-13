package edu.rpi.metpetdb.client.ui.left.side;

import java.util.Iterator;
import java.util.Set;

import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class MyProjects extends LeftColWidget implements UsesLeftColumn {

	public MyProjects(final Set projects) {
		super("My Projects");
		this.setStyleName("lcol-MyProjects");

		final MUnorderedList pList = new MUnorderedList();
		final MLink addProject = new MLink("Add Project", TokenSpace.newProject);
		pList.add(addProject);
		pList.add(addProjects(projects));
		pList.setStyleName("lcol-sectionList");

		this.add(pList);

	}

	public static MUnorderedList addProjects(Set projects) {
		final MUnorderedList list = new MUnorderedList();

		Iterator it = projects.iterator();
		while (it.hasNext()) {
			final ProjectDTO project = (ProjectDTO) it.next();
			list.add(showProjectDetails(project));
		}

		return list;
	}

	public static MLink showProjectDetails(final ProjectDTO project) {
		final MLink focusProject = new MLink(project.getName(), TokenSpace
				.samplesOf(project));
		return focusProject;
	}

	public void onPageChanged() {
		// TODO Auto-generated method stub

	}

}
