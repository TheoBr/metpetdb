package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.properties.ProjectProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class ProjectListEx extends ListEx<ProjectDTO> {

	public ProjectListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column("Check", true, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(enttxt.Project_name(), ProjectProperty.name),
			new Column(enttxt.Project_Owner(), ProjectProperty.owner, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink(((UserDTO) data
							.mGet(ProjectProperty.owner)).getUsername(),
							TokenSpace.detailsOf((UserDTO) data
									.mGet(ProjectProperty.owner)));
				}
			},
			new Column(enttxt.Project_MemberCount(),
					ProjectProperty.memberCount),
			new Column(enttxt.Project_LastSampleAddded(), true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new Label("Coming Soon");
				}
			}, new Column(enttxt.Project_Actions(), true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					final FlexTable ft = new FlexTable();
					ft.setWidget(0, 0, new MLink("Go to project",
							new ClickListener() {
								public void onClick(Widget sender) {

								}
							}));
					ft.setWidget(0, 1, new MLink("Leave Project",
							new ClickListener() {
								public void onClick(Widget sender) {

								}
							}));
					ft.getFlexCellFormatter().setWordWrap(0, 0, false);
					ft.getFlexCellFormatter().setWordWrap(0, 1, false);
					return ft;
				}
			}

	};

	public String getDefaultSortParameter() {
		return ProjectProperty.name.name();
	}

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<ProjectDTO>> ac);
}
