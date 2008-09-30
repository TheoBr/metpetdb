package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.objects.list.ListEx;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;

public class CustomTableView extends MDialogBox implements ClickListener,
		KeyboardListener {
	private ArrayList<MCheckBox> cb;
	private final Button submit;
	private final Button cancel;
	private final ListEx list;
	private final String table;
	private final static long millisecondsIn30Days = 2592000000L;

	public CustomTableView(final ListEx myList, final String myTable) {
		list = myList;
		table = myTable;
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

		final ArrayList<Column> displayColumns = list.getDisplayColumns();
		for (int i = 1; i < list.getOriginalColumns().size(); i++) {
			Column original = (Column) list.getOriginalColumns().get(i);
			MCheckBox tempCB = new MCheckBox(original.getTitle());
			tempCB.setName(original.getTitle());
			if (displayColumns.contains(original))
				tempCB.setChecked(true);
			if (i > list.getOriginalColumns().size() / 2)
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
			ArrayList<Column> originalColumns = new ArrayList<Column>(list
					.getOriginalColumns());
			ArrayList<Column> displayColumns = new ArrayList<Column>();
			displayColumns.add(originalColumns.get(0));
			for (int i = 0; i < cb.size(); i++) {
				if (cb.get(i).isChecked()) {
					displayColumns.add(originalColumns.get(i + 1));
				}
			}
			createCookie(table, displayColumns);
			list.newView(displayColumns);
			hide();
		}
	}

	public void createCookie(final String table,
			final ArrayList<Column> displayColumns) {
		String value = "";
		for (int i = 0; i < displayColumns.size(); i++) {
			value += displayColumns.get(i).getTitle() + ",";
		}
		final Date expires = new Date();
		expires.setTime(expires.getTime() + millisecondsIn30Days);
		Cookies.setCookie(table, value, expires);
	}

	public void getCookie(final String table,
			final ArrayList<Column> displayColumns) {

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

}
