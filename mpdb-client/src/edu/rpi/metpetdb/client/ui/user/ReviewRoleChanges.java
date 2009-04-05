package edu.rpi.metpetdb.client.ui.user;

import java.util.ArrayList;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.properties.RoleChangeProperty;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.objects.list.List;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.StringColumn;

/**
 * An interface for a sponsor to review role changes that they have pending
 * 
 * NOTE THIS IS JUST A PROOF OF CONCEPT
 * 
 * @author anthony
 * 
 */
public class ReviewRoleChanges extends List<RoleChange> {
	
	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ArrayList<Column<RoleChange, ?>> columns;
	static {
		columns = new ArrayList<Column<RoleChange, ?>>();
		
		columns.add(new StringColumn<RoleChange>(enttxt.RoleChange_user(),
				RoleChangeProperty.user));
		columns.add(new StringColumn<RoleChange>(enttxt.RoleChange_role(),
				RoleChangeProperty.role));
		columns.add(new StringColumn<RoleChange>(enttxt.RoleChange_requestDate(),
				RoleChangeProperty.requestDate));
		columns.add(new StringColumn<RoleChange>(enttxt.RoleChange_requestReason(),
				RoleChangeProperty.requestReason));
		columns.add(new Column<RoleChange, FlowPanel>(enttxt.RoleChange_grant()) {

			@Override
			public FlowPanel getCellValue(final RoleChange rowValue) {
				//
				final FlowPanel actions = new FlowPanel();
				actions.add(new Button("Yes", new ClickListener() {
					public void onClick(final Widget sender) {
						new VoidServerOp() {
							public void begin() {
								MpDb.user_svc.approveRoleChange(rowValue, this);
							}
							@Override
							public void onSuccess() {
							}
						}.begin();
					}
				}));
				actions.add(new Button("No", new ClickListener() {
					public void onClick(final Widget sender) {
						new VoidServerOp() {
							public void begin() {
								MpDb.user_svc.denyRoleChange(rowValue, this);
							}
							@Override
							public void onSuccess() {
							}
						}.begin();
					}
				}));
				return actions;
			}
			
		});
			
	}

	public ReviewRoleChanges() {
		super(columns);
		//dataTable.setSelectionEnabled(false);
	}

	@Override
	public String getDefaultSortParameter() {
		return "requestDate";
	}

	@Override
	protected Widget getNoResultsWidget() {
		return new Label("No pending role changes");
	}

	@Override
	public void update(PaginationParameters p,
			AsyncCallback<Results<RoleChange>> ac) {
		MpDb.user_svc.getSponsorRoleChanges(MpDb.currentUser().getId(),
				p, ac);
	}
}
