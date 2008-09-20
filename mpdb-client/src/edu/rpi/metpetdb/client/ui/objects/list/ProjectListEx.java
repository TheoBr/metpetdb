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
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
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
			new Column("Check", true, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(enttxt.Project_name(), ProjectProperty.name),
			new Column(enttxt.Project_Owner(), ProjectProperty.owner, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(((User) data.mGet(ProjectProperty.owner))
							.getEmailAddress(), TokenSpace
							.detailsOf((User) data.mGet(ProjectProperty.owner)));
				}
			},
			new Column(enttxt.Project_MemberCount(),
					ProjectProperty.memberCount),
			new Column(enttxt.Project_LastSampleAddded(), true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new Label("Coming Soon");
				}
			}, new Column(enttxt.Project_Actions(), true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink("Go to project", new ClickListener() {
						public void onClick(Widget sender) {
							History.newItem(TokenSpace
									.samplesOf((Project) data));
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
