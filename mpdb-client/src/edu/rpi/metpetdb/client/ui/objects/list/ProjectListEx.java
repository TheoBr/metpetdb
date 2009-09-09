package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.ProjectProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class ProjectListEx extends ListEx<Project> {

	public ProjectListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public ProjectListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column(true, "Check", true, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(true,enttxt.Project_name(), true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink((String) data.mGet(ProjectProperty.name), 
						new ClickListener() {
						public void onClick(Widget sender) {
							History.newItem(TokenSpace.descriptionOf((Project) data));
						}
					});
				}
			},
			new Column(true,enttxt.Project_Owner(), ProjectProperty.owner, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					User owner = ((User) data.mGet(ProjectProperty.owner));
					return new MLink(owner.getName(), TokenSpace.detailsOf(owner));
				}
			},
			new Column(true,enttxt.Project_MemberCount(),
					ProjectProperty.memberCount),
			new Column(true,enttxt.Project_LastSampleAddded(), true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					//return new Label(data.mGet(ProjectProperty.memberCount).toString());
					return new Label("Coming soon");
				}
			}, new Column(true,enttxt.Project_ViewDetails(), true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink("View Samples", new ClickListener() {
						public void onClick(Widget sender) {
							History.newItem(TokenSpace.samplesOf((Project) data));
						}
					});
				}
			}
	};

	public String getDefaultSortParameter() {
		return ProjectProperty.name.name();
	}

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<Project>> ac);
}
