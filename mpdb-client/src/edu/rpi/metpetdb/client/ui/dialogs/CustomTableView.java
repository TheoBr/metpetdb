package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;

public abstract class CustomTableView<RowType> extends
		MDialogBox implements ClickListener, KeyboardListener {
	private final Button submit;
	private final Button cancel;
	/** maps a column number to a checkbox */
	private final Map<Integer, MCheckBox> checkBoxes;
	private final ColumnDefinition<RowType> allColumns;

	public CustomTableView(final ColumnDefinition<RowType> allColumns,
			final ColumnDefinition<RowType> displayColumns) {
		this.allColumns = allColumns;
		setText("Custom View");

		final Label infoPara = new Label(
				"Choose which columns you want displayed.");

		submit = new Submit(LocaleHandler.lc_text.buttonSubmit(), this);
		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		final SimplePanel container = new SimplePanel();
		final FlexTable ft = new FlexTable();
		final FlowPanel leftPanel = new FlowPanel();
		final FlowPanel rightPanel = new FlowPanel();
		final FlowPanel bottomPanel = new FlowPanel();

		checkBoxes = new TreeMap<Integer, MCheckBox>();

		for (int i = 0; i < allColumns.size(); i++) {
			Column<RowType, ?> original = (Column<RowType, ?>) allColumns
					.getColumn(i);
			MCheckBox tempCB = new MCheckBox(original.getHeader().toString(), true);
			tempCB.setName(original.getHeader().toString());
			if (displayColumns.contains(allColumns.getColumnName(i)))
				tempCB.setChecked(true);
			if (i > allColumns.size() / 2)
				rightPanel.add(tempCB);
			else
				leftPanel.add(tempCB);
			if (!original.isOptional() && tempCB.isChecked())
				tempCB.setEnabled(false);
			checkBoxes.put(i, tempCB);
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
			ok();
		}
	}

	private void ok() {
		final Iterator<Entry<Integer, MCheckBox>> itr = checkBoxes.entrySet()
				.iterator();
		final ColumnDefinition<RowType> newCols = new ColumnDefinition<RowType>();
		while (itr.hasNext()) {
			final Entry<Integer, MCheckBox> e = itr.next();
			if (e.getValue().isChecked()) {
				newCols.addColumn(allColumns.getColumn(e.getKey()));
			}
		}
		onSetNewColumns(newCols);
		hide();
	}

	public abstract void onSetNewColumns(final ColumnDefinition<RowType> newCols);

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ENTER) {
			ok();
		} else if (kc == KEY_ESCAPE) {
			cancel();
		}
	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {}

	private void cancel() {
		hide();
	}
}
