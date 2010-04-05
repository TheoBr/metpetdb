package edu.rpi.metpetdb.client.ui.sidebar;

import java.util.Iterator;
import java.util.Set;

import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class MyProjects extends Sidebar implements UsesSidebar {

	public MyProjects(final Set projects) {
		this.setStyleName("lcol-MyProjects");

		final MHtmlList pList = new MHtmlList();
		final MLink addProject = new MLink("Add Project", TokenSpace.newProject);
		pList.add(addProject);
		pList.add(addProjects(projects));
		pList.setStyleName("lcol-sectionList");

		this.add(pList);

	}

	public static MHtmlList addProjects(Set projects) {
		final MHtmlList list = new MHtmlList();

		Iterator it = projects.iterator();
		while (it.hasNext()) {
			final Project project = (Project) it.next();
			list.add(showProjectDetails(project));
		}

		return list;
	}

	public static MLink showProjectDetails(final Project project) {
		final MLink focusProject = new MLink(project.getName(), TokenSpace
				.samplesOf(project));
		return focusProject;
	}

	public void onPageChanged() {
		// TODO Auto-generated method stub

	}
	
	public void onBeforePageChanged() {
		
	}

}
