package edu.rpi.metpetdb.client.ui.left_side;

import java.util.Iterator;
import java.util.Set;

import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.LeftColWidget;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class MyProjects extends LeftColWidget {
	
	public MyProjects(final Set projects) {
		super("My Projects");
		this.setStyleName("lcol-MyProjects");

		final MUnorderedList pList = addProjects(projects);
		pList.setStyleName("lcol-sectionList");
		
		this.add(pList);

	}
	
	public static MUnorderedList addProjects(Set projects) {
		final MUnorderedList list = new MUnorderedList();

		Iterator it = projects.iterator();
		while (it.hasNext()) {
			final Project project = (Project) it.next();
			list.add(showProjectDetails(project));
		}

		return list;
	}
	
	public static MLink showProjectDetails(final Project project) {
		final MLink focusProject = new MLink(project.getName(),TokenSpace.listOf(project)); // End of ClickListener

		// myProjects.add(focusProject);
		return focusProject;
	}

}
