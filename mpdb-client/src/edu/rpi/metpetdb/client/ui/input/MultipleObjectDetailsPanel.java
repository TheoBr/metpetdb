package edu.rpi.metpetdb.client.ui.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class MultipleObjectDetailsPanel<T extends MObjectDTO> extends
		DetailsPanel<T> {
	// HashMap<MObject, HashMap<GenericAttribute, DetailsPanelEntry >
	protected HashMap<T, HashMap<GenericAttribute, DetailsPanelEntry>> dpBeans;

	public MultipleObjectDetailsPanel(final GenericAttribute[] atts) {
		this.init(atts, null, false, false);
	}

	protected MultipleObjectDetailsPanel() {
	}

	public T getBean() {
		return null;
	}

	public void show(final ArrayList<T> beans) {
		addStyleName(STYLENAME_DEFAULT + "-" + CSS.SHOWMODE);
		removeStyleName(STYLENAME_DEFAULT + "-" + CSS.EDITMODE);
		clearNonActions();
		if (dpBeans == null)
			dpBeans = new HashMap<T, HashMap<GenericAttribute, DetailsPanelEntry>>();
		final Iterator<T> itr = beans.iterator();
		while (itr.hasNext()) {
			final T bean = itr.next();
			final HashMap<GenericAttribute, DetailsPanelEntry> dpEntries = new HashMap<GenericAttribute, DetailsPanelEntry>();
			for (int row = 0; row < attributes.size(); row++) {
				final GenericAttribute attr = (GenericAttribute) attributes
						.get(row);
				final DetailsPanelEntry dpEntry = copyDpEntry(
						(DetailsPanelEntry) dpEntries.get(attr), bean);
				final Widget[] widgets = attr.createDisplayWidget(bean);
				fixRows(dpEntry, widgets.length);
				for (int i = 0; i < widgets.length; ++i) {
					add(widgets[i], dpEntry.getRow(i).getTdValue());
				}
				dpEntry.setCurrentDisplayWidgets(widgets);
				dpEntries.put(attr, dpEntry);
			}
			dpBeans.put(bean, dpEntries);
		}
		// Set the header/description text
		setPanelHeader(viewHeader);
		setPanelDescription(viewDescription);
		isEditMode = false;
	}

	private DetailsPanelEntry copyDpEntry(final DetailsPanelEntry original,
			final MObjectDTO bean) {
		final DetailsPanelEntry copy = new DetailsPanelEntry();
		copy.setAttr(original.getAttr());
		final ArrayList<DetailsPanelRow> rows = new ArrayList<DetailsPanelRow>();
		final DetailsPanelRow dpRow = createRow(copy.getAttr(), 1, bean
				.toString());
		rows.add(dpRow);
		copy.setEntryRows(rows);
		DOM.insertChild(tbody, dpRow.getRow(), 1);
		return copy;
	}

	public void updateEditWidget(final GenericAttribute attr, final T bean) {
		final HashMap<GenericAttribute, DetailsPanelEntry> dpEntries = dpBeans
				.get(bean);
		final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
				.get(attr);
		for (int i = 0; i < dpEntry.getCurrentEditWidgets().length; ++i)
			this.remove(dpEntry.getCurrentDisplayWidgets()[i]);
		showEditWidget(dpEntry, attr, bean);
	}

	private void showEditWidget(final DetailsPanelEntry dpEntry,
			final GenericAttribute attr, final MObjectDTO bean) {
		if (attr.getReadOnly() || (attr.getImmutable() && !bean.mIsNew())) {
			// If it is immutable and we are not creating it show the
			// display widget or if it is readonly
			final Widget[] widgets = attr.createDisplayWidget(bean);
			fixRows(dpEntry, widgets.length);
			for (int i = 0; i < widgets.length; ++i) {
				add(widgets[i], dpEntry.getRow(i).getTdValue());
			}
			dpEntry.setCurrentEditWidgets(widgets);
		} else {
			final Widget[] widgets = attr.createEditWidget(bean, "TEMP", attr);
			fixRows(dpEntry, widgets.length);
			for (int i = 0; i < widgets.length; ++i) {
				add(widgets[i], dpEntry.getRow(i).getTdValue());
			}
			dpEntry.setCurrentEditWidgets(widgets);
		}
	}

	public void edit(final ArrayList<T> beans) {
		addStyleName(STYLENAME_DEFAULT + "-" + CSS.EDITMODE);
		removeStyleName(STYLENAME_DEFAULT + "-" + CSS.SHOWMODE);
		clearNonActions();
		if (dpBeans == null)
			dpBeans = new HashMap<T, HashMap<GenericAttribute, DetailsPanelEntry>>();
		final Iterator<HashMap<GenericAttribute, DetailsPanelEntry>> entriesItr = dpBeans
				.values().iterator();
		while (entriesItr.hasNext()) {
			final HashMap<GenericAttribute, DetailsPanelEntry> dpEntries = entriesItr
					.next();
			if (dpEntries != null) {
				final Iterator<GenericAttribute> dpItr = dpEntries.keySet()
						.iterator();
				while (dpItr.hasNext()) {
					final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
							.get(dpItr.next());
					dpEntry.remove();
				}
			}
		}
		dpBeans.clear();
		final Iterator<T> itr = beans.iterator();
		while (itr.hasNext()) {
			final T bean = itr.next();
			HashMap<GenericAttribute, DetailsPanelEntry> dpEntries;
			if (dpBeans.containsKey(bean))
				dpEntries = dpBeans.get(bean);
			else {
				dpEntries = new HashMap<GenericAttribute, DetailsPanelEntry>();
				dpBeans.put(bean, dpEntries);
			}
			for (int row = 0; row < attributes.size(); row++) {
				final GenericAttribute attr = (GenericAttribute) attributes
						.get(row);
				DetailsPanelEntry dpEntry;
				if (dpEntries.containsKey(attr))
					dpEntry = (DetailsPanelEntry) dpEntries.get(attr);
				else {
					dpEntry = copyDpEntry((DetailsPanelEntry) this.dpEntries
							.get(attr), bean);
					dpEntries.put(attr, dpEntry);
				}
				showEditWidget(dpEntry, attr, bean);
				final CurrentError err = new CurrentError();
				dpEntry.setCurrentError(err);
				add(err, dpEntry.getLastRow().getTdValue());
			}
		}
		// Set the header/description text
		setPanelHeader(editHeader);
		setPanelDescription(editDescription);
		isEditMode = true;
	}
	public boolean validateEdit(final ServerOp r) {
		if (!isEditMode())
			return true;
		int failed = 0;
		final Iterator<T> itr = dpBeans.keySet().iterator();
		while (itr.hasNext()) {
			final T bean = itr.next();
			for (int row = 0; row < attributes.size(); row++) {
				final GenericAttribute attr = (GenericAttribute) attributes
						.get(row);
				final CurrentError err = getCurrentError(attr, bean);
				attr.commitEdit(bean, getEditWidgets(attr, bean), err, null);
				if (err.isVisible())
					failed++;
			}
		}
		if (failed == 0)
			r.onSuccess(null);
		else
			r.onFailure(null);
		return failed == 0;
	}
	private Widget[] getEditWidgets(final GenericAttribute attr,
			final MObjectDTO bean) {
		if (!isEditMode())
			throw new IllegalStateException();
		final DetailsPanelEntry dpEntry = dpBeans.get(bean).get(attr);
		return dpEntry.getCurrentEditWidgets();
	}
	private CurrentError getCurrentError(final GenericAttribute attr,
			final MObjectDTO bean) {
		if (!isEditMode())
			throw new IllegalStateException();;
		return dpBeans.get(bean).get(attr).getCurrentError();
	}
}
