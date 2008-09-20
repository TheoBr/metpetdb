package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class ProjectDetails extends FlowPanel {
	private static GenericAttribute[] projectAtts = {
		new TextAttribute(MpDb.doc.Project_name)
	};

	private final ObjectEditorPanel p_project;
	private int projectId;

	public ProjectDetails() {
		p_project = new ObjectEditorPanel(projectAtts, LocaleHandler.lc_text
				.addProject(), LocaleHandler.lc_text.addProjectDescription()) {
			private boolean savedNew;

			protected void loadBean(final AsyncCallback ac) {
				final Project p = (Project) getBean();
				MpDb.project_svc.details(p != null && !p.mIsNew() ? p.getId()
						: projectId, ac);
			}
			protected void saveBean(final AsyncCallback ac) {
				// true when saved the first time or saved after editing
				savedNew = true;
				final Project p = (Project) getBean();
				// MpDb.currentUser().getProjects().add(p);
				// p.setOwner(MpDb.currentUser());
				MpDb.project_svc.saveProject(p, ac);
			}
			protected void deleteBean(final AsyncCallback ac) {
				// TODO: implement delete for project
			}
			protected boolean canEdit() {
				return MpDb.isCurrentUser(((Project) getBean()).getOwner());
			}
			protected void onSaveCompletion(final MObject result) {
				if (savedNew)
					MpDb.currentUser().getProjects().add((Project) result);
				this.show(result);
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_project));
	}

	public ProjectDetails showById(final int id) {
		projectId = id;
		p_project.load();
		return this;
	}

	public ProjectDetails createNew() {
		final Project p = new Project();
		p.setOwner(MpDb.currentUser());
		p.getMembers().add(MpDb.currentUser());
		p_project.edit(p);
		return this;
	}
}
