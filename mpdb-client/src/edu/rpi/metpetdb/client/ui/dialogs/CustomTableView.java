package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.objects.list.List;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;

public class CustomTableView<RowType extends MObject> extends MDialogBox implements ClickListener,
		KeyboardListener {
	private ArrayList<MCheckBox> cb;
	private final Button submit;
	private final Button cancel;
	private final List<RowType> list;
	private final String table;
	private final static long millisecondsIn30Days = 2592000000L;
	private DefaultTableDefinition<RowType> tableDef;
	private ArrayList<Column<RowType, ?>> columns;

	public CustomTableView(final List<RowType> myList, final String myTable) {
		list = myList;
		table = myTable;
		columns = getOptionalColumns(list.getDisplayColumns());
		setText("Custom View");

		final Label infoPara = new Label(
				"Choose which columns you want displayed.");

		cb = new ArrayList<MCheckBox>();
		submit = new Submit(LocaleHandler.lc_text.buttonSubmit(), this);
		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		final SimplePanel container = new SimplePanel();
		final FlexTable ft = new FlexTable();
		final FlowPanel leftPanel = new FlowPanel();
		final FlowPanel rightPanel = new FlowPanel();
		final FlowPanel bottomPanel = new FlowPanel();

		for (int i = 0; i < columns.size(); i++) {
			Column<RowType, ?> original = (Column<RowType, ?>) columns.get(i);
			MCheckBox tempCB = new MCheckBox(original.getHeader());
			tempCB.setName(original.getHeader().toString());
			if (columns.contains(original))
				tempCB.setChecked(true);
			if (i > columns.size() / 2)
				rightPanel.add(tempCB);
			else
				leftPanel.add(tempCB);
			cb.add(tempCB);
		}

		bottomPanel.add(cancel);
		bottomPanel.add(submit);
		bottomPanel.setStyleName(CSS.POPUP_CUSTOM_COLS_BOTTOM);

		ft.setWidget(0, 0, infoPara);
		ft.setWidget(1, 0, leftPanel);
		ft.setWidget(1, 1, rightPanel);
		ft.setWidget(2, 0, bottomPanel);
		ft.getFlexCellFormatter().setColSpan(0, 0, 2);
		ft.getFlexCellFormatter().setColSpan(2, 0, 2);
		ft.setCellPadding(10);
		ft.setStyleName(CSS.POPUP_CUSTOM_COLS);
		container.add(ft);

		this.setWidget(container);
		container.setStyleName(CSS.POPUP_CUSTOM_COLS_CONTAINER);
		this.show();
	}

	public void onClick(final Widget sender) {
		if (cancel == sender)
			cancel();
		else if (submit == sender) {
			ArrayList<Column<RowType, ?>> displayColumns = list.getOriginalColumns();
			for (int i=0; i<displayColumns.size(); i++) {
				Column<RowType, ?> col = displayColumns.get(i);
				for (MCheckBox cbox : cb) {
					if (col.getHeader() == cbox.getValue()) {
						list.getTableDefinition().setColumnVisible(
								list.getTableDefinition().getColumnDefinition(i), 
								cbox.isChecked());
					}
				}
			}
			createCookie(table);
			hide();
		}
	}

	public void createCookie(final String table) {
		String value = "";
		for (int i = 0; i < list.getTableDefinition().getColumnDefinitionCount(); i++) {
			if (list.getTableDefinition().isColumnVisible(list.getTableDefinition().getColumnDefinition(i)))
				value += 1 + ",";
			else
				value += 0 + ",";
		}
		final Date expires = new Date();
		expires.setTime(expires.getTime() + millisecondsIn30Days);
		Cookies.setCookie(table, value, expires);
	}

	public void getCookie(final String table) {

	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ENTER) {
		}
	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	private void cancel() {
		hide();
	}
	
	private ArrayList<Column<RowType, ?>> getOptionalColumns(ArrayList<Column<RowType, ?>> columns) {
		final ArrayList<Column<RowType, ?>> cols = new ArrayList<Column<RowType, ?>>();
		for (Column<RowType, ?> c : columns) {
			if (c.isOptional()) {
				cols.add(c);
			}
		}
		return cols;
	}

}
