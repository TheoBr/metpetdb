package edu.rpi.metpetdb.client.ui.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;

/**
 * An interface for a sponsor to review role changes that they have pending
 * 
 * NOTE THIS IS JUST A PROOF OF CONCEPT
 * 
 * @author anthony
 * 
 */
public class ReviewRoleChanges extends DataList<RoleChange> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ColumnDefinition<RoleChange> columns;
	static {
		columns = new ColumnDefinition<RoleChange>();

		columns.addColumn(new StringColumn<RoleChange>(enttxt.RoleChange_user(),
				RoleChangeProperty.user));
		columns.addColumn(new StringColumn<RoleChange>(enttxt.RoleChange_role(),
				RoleChangeProperty.role));
		columns.addColumn(new StringColumn<RoleChange>(enttxt
				.RoleChange_requestDate(), RoleChangeProperty.requestDate));
		columns.addColumn(new StringColumn<RoleChange>(enttxt
				.RoleChange_requestReason(), RoleChangeProperty.requestReason));
		columns
				.addColumn(new Column<RoleChange, FlowPanel>(enttxt
						.RoleChange_grant()) {
					@Override
					// TODO grant/deny reasons
					public FlowPanel getCellValue(final RoleChange rowValue) {
						//
						final FlowPanel actions = new FlowPanel();
						actions.add(new Button("Yes", new ClickListener() {
							public void onClick(final Widget sender) {
								new VoidServerOp() {
									public void begin() {
										MpDb.user_svc.approveRoleChange(
												rowValue, this);
									}
									@Override
									public void onSuccess() {
										actions.clear();
										actions.add(new Label("Processed"));
									}
								}.begin();
							}
						}));
						actions.add(new Button("No", new ClickListener() {
							public void onClick(final Widget sender) {
								new VoidServerOp() {
									public void begin() {
										MpDb.user_svc.denyRoleChange(rowValue,
												this);
									}
									@Override
									public void onSuccess() {
										actions.clear();
										actions.add(new Label("Processed"));
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
		// dataTable.setSelectionEnabled(false);
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
		MpDb.user_svc.getSponsorRoleChanges(MpDb.currentUser().getId(), p, ac);
	}

	@Override
	protected String getListName() {
		return "roleChange";
	}

	@Override
	protected ColumnDefinition<RoleChange> getDefaultColumns() {
		return columns;
	}
	
	protected Object getId(RoleChange rc){
		return rc.getId();
	}

	@Override
	public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
		MpDb.user_svc.getSponsorRoleChangeIds(MpDb.currentUser().getId(),ac);
	}
}
