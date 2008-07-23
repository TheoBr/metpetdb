package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.objects.list.ListEx;

public class CustomTableView extends MDialogBox implements ClickListener,
		KeyboardListener {
	private ArrayList<CheckBox> cb;
	private final Button submit;
	private final Button cancel;
	private final ListEx list;
	private final String table;
	private final static long millisecondsIn30Days = 2592000000L;

	public CustomTableView(final ListEx myList, final String myTable) {
		list = myList;
		table = myTable;
		setText("Please choose your desired fields");

		cb = new ArrayList<CheckBox>();

		submit = new Submit(LocaleHandler.lc_text.buttonSubmit(), this);

		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		final FlexTable ft = new FlexTable();

		final ArrayList<Column> displayColumns = list.getDisplayColumns();
		for (int i = 1; i < list.getOriginalColumns().size(); i++) {
			Column original = (Column) list.getOriginalColumns().get(i);
			CheckBox tempCB = new CheckBox();
			tempCB.setName(original.getTitle());
			if (displayColumns.contains(original))
				tempCB.setChecked(true);
			ft.setWidget(i, 0, tempCB);
			ft.setWidget(i, 1, new Label(tempCB.getName()));
			cb.add(tempCB);
		}
		ft.getColumnFormatter().setWidth(0, "20px");

		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(submit);
		hp.add(cancel);
		hp.setSpacing(5);
		ft.setWidget(ft.getRowCount(), 0, hp);
		ft.getFlexCellFormatter().setColSpan(ft.getRowCount() - 1, 0, 3);
		ft.setCellSpacing(5);
		this.setWidget(ft);
		ft.setStyleName("mpdb-dataTable");
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
