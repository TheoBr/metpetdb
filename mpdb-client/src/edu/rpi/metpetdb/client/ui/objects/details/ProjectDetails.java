package edu.rpi.metpetdb.client.ui.objects.details;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.left.side.MyProjects;

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
				final ProjectDTO p = (ProjectDTO) getBean();
				MpDb.project_svc.details(p != null && !p.mIsNew() ? p.getId()
						: projectId, ac);
			}
			protected void saveBean(final AsyncCallback ac) {
				// true when saved the first time or saved after editing
				savedNew = true;
				final ProjectDTO p = (ProjectDTO) getBean();
				// MpDb.currentUser().getProjects().add(p);
				// p.setOwner(MpDb.currentUser());
				MpDb.project_svc.saveProject(p, ac);
			}
			protected void deleteBean(final AsyncCallback ac) {
				// TODO: implement delete for project
			}
			protected boolean canEdit() {
				return MpDb.isCurrentUser(((ProjectDTO) getBean()).getOwner());
			}
			protected void onSaveCompletion(final MObjectDTO result) {
				// if (savedNew)
				// MpDb.currentUser().getProjects().add((ProjectDTO) result);
				// MpDb.createUserHistory(MpDb.currentUser());
				addProjectsToLeft();
				this.show(result);
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_project));
		addProjectsToLeft();
	}

	public ProjectDetails showById(final int id) {
		projectId = id;
		p_project.load();
		return this;
	}

	public void addProjectsToLeft() {
		MetPetDBApplication.clearLeftSide();
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				MetPetDBApplication.clearLeftSide();
				final Set<ProjectDTO> leftprojects = new HashSet<ProjectDTO>(
						(List<ProjectDTO>) result);
				MetPetDBApplication.appendToLeft(new MyProjects(leftprojects));
			}
		}.begin();

	};

	public ProjectDetails createNew() {
		final ProjectDTO p = new ProjectDTO();
		p.setOwner(MpDb.currentUser());
		p.getMembers().add(MpDb.currentUser());
		p_project.edit(p);
		return this;
	}
}
