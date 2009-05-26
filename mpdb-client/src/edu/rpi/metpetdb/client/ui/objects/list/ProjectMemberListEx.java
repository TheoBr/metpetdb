package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.UserProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class ProjectMemberListEx extends ListEx<User> {
	
	public ProjectMemberListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}
	
	public ProjectMemberListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	
	private static Column[] columns = {
		new Column(true, "Check", true, true) {
			protected Object getWidget(final MObject data,
					final int currentRow) {
				return new MCheckBox(data);
			}
		},
		new Column(true, enttxt.User_name(), true) {
			protected Object getWidget(final MObject data,
					final int currentRow){
				return new MLink((String) data.mGet(UserProperty.name), 
						TokenSpace.detailsOf((User) data));
			}
		},
		new Column(true, enttxt.User_emailAddress(), true) {
			protected Object getWidget(final MObject data,
					final int currentRow){
				return new MText((String) data.mGet(UserProperty.emailAddress));
			}
		},
		new Column(true, "Member Since", true) {
			protected Object getWidget(final MObject data,
					final int currentRow){
				return new MText("Coming soon");
			}
		},
		new Column(true, "Samples Added", true) {
			protected Object getWidget(final MObject data,
					final int currentRow){
				return new MText("Coming soon");
			}
		}
	};

	public String getDefaultSortParameter() {
		return UserProperty.name.name();
	}

	public abstract void update(PaginationParameters p, AsyncCallback<Results<User>> ac);
}
