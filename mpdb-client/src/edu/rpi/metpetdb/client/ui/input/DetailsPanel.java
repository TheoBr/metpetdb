package edu.rpi.metpetdb.client.ui.input;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class DetailsPanel<T extends MObject> extends ComplexPanel {
	private String panelId;
	// ArrayList<GenericAttribute>
	protected ArrayList<GenericAttribute> attributes;
	// HashMap<GenericAttribute, DetailsPanelEntry >
	protected HashMap<GenericAttribute, DetailsPanelEntry> dpEntries;
	private T bean;
	protected int actionCount;
	protected Element tbody;
	protected boolean isEditMode = false;
	protected final String STYLENAME_DEFAULT = "detailsPanel";

	public DetailsPanel(final GenericAttribute[] atts) {
		this(atts, null);
	}

	public DetailsPanel(final GenericAttribute[] atts, final Widget[] actions) {
		this(atts, actions, true);
	}

	public DetailsPanel(final GenericAttribute[] atts, final Widget[] actions,
			final boolean showHeaders) {
		init(atts, actions, showHeaders, true);
	}

	protected DetailsPanel() {
	}

	protected void init(final GenericAttribute[] atts, final Widget[] actions) {
		init(atts, actions, false, true);
	}

	/**
	 * 
	 * @param atts
	 * @param actions
	 * @param showHeaders
	 * 		whether we show the view/edit headers
	 * @param showInitial
	 * 		whether we initially show the attributes (for
	 * 		MultipleObjectDetailsPanel)
	 */
	protected void init(final GenericAttribute[] atts, final Widget[] actions,
			final boolean showHeaders, final boolean showInitial) {
		panelId = HTMLPanel.createUniqueId();
		setElement(DOM.createDiv());
		setStylePrimaryName(STYLENAME_DEFAULT);

		final Element table = DOM.createTable();
		CSS.setStyleName(table, STYLENAME_DEFAULT + "-table");
		tbody = DOM.createTBody();
		DOM.appendChild(getElement(), table);
		DOM.appendChild(table, tbody);

		attributes = new ArrayList<GenericAttribute>();
		dpEntries = new HashMap<GenericAttribute, DetailsPanelEntry>();
		for (int row = 0; row < atts.length; ++row) {
			attributes.add(atts[row]);
			final DetailsPanelRow newRow = createRow(atts[row], row);
			if (!showInitial) {
				// TODO: stupid ie does not support
				// visibility:collapse..................
				// DOM.setStyleAttribute(newRow.getRow(), "visibility",
				// "collapse");
				DOM.setStyleAttribute(newRow.getRow(), "display", "none");
			}
			DOM.insertChild(tbody, newRow.getRow(), row);
			dpEntries.put(atts[row], new DetailsPanelEntry(atts[row], newRow));
		}

		if (actions != null && actions.length > 0) {
			final Element tr = DOM.createTR();
			CSS.setStyleName(tr, CSS.LAST_ROW);
			DOM.appendChild(tbody, tr);
			DOM.appendChild(tr, DOM.createTD());
			final Element td = DOM.createTD();
			CSS.setStyleName(td, CSS.ACTIONS);
			DOM.appendChild(tr, td);
			for (int k = 0; k < actions.length; k++)
				add(actions[k], td);
			actionCount = actions.length;
		} else {
			actionCount = 0;
		}
	}

	protected DetailsPanelRow createRow(final GenericAttribute attr,
			final int row) {
		return createRow(attr, row, attr.getLabel());
	}

	protected DetailsPanelRow createRow(final GenericAttribute attr,
			final int row, final String labelText) {
		final Element tr = DOM.createTR();

		final Element labelTD = DOM.createTD();
		DOM.appendChild(tr, labelTD);
		final Element label = DOM.createLabel();
		DOM.appendChild(labelTD, label);
		DOM.setInnerText(label, labelText);

		if (attr.getConstraint().required) {
			final Element em = DOM.createElement("em");
			DOM.setInnerText(em, "(required)");
			DOM.appendChild(labelTD, em);
		}

		final Element valueTD = DOM.createTD();
		DOM.appendChild(tr, valueTD);

		attr.setMyPanel(this);
		return new DetailsPanelRow(tr, labelTD, valueTD, label, attr.getConstraint().required);
	}

	public T getBean() {
		return bean;
	}

	public void setBean(final T o) {
		bean = o;
	}

	public void show(final T toShow) {
		removeStyleDependentName(CSS.EDITMODE);
		addStyleDependentName(CSS.SHOWMODE);
		bean = toShow;
		clearNonActions();
		for (int row = 0; row < attributes.size(); row++) {
			final GenericAttribute attr = (GenericAttribute) attributes
					.get(row);
			final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
					.get(attr);
			final Widget[] widgets = attr.createDisplayWidget(bean);
			fixRows(dpEntry, widgets.length);
			for (int i = 0; i < widgets.length; ++i) {
				add(widgets[i], dpEntry.getRow(i).getTdValue());
			}
			dpEntry.setCurrentDisplayWidgets(widgets);
		}
		// Set the header/description text
		// setPanelHeader(viewHeader);
		// setPanelDescription(viewDescription);
		isEditMode = false;
	}

	public void fixRows(final DetailsPanelEntry dpEntry, final int rowsNeeded) {
		while (dpEntry.getRowCount() < rowsNeeded) {
			// We need to add rows to the tbody
			final Element lastRow = dpEntry.getLastRow().getRow();
			final int lastRowIndex = DOM.getChildIndex(tbody, lastRow);
			// TODO row number is going to be wrong for the label, but w/e
			final DetailsPanelRow newRow = createRow(dpEntry.getAttr(), dpEntry
					.getRowCount() + 1, dpEntry.getAttr().getLabel(
					dpEntry.getRowCount()));
			DOM.insertChild(tbody, newRow.getRow(), lastRowIndex + 1);
			dpEntry.addRow(newRow);
		}
		while (dpEntry.getRowCount() > rowsNeeded) {
			// We need to remove rows from the tbody
			final Element lastRow = dpEntry.getLastRow().getRow();
			DOM.removeChild(tbody, lastRow);
			dpEntry.getEntryRows().remove(dpEntry.getLastRow());
		}
	}

	public void updateEditWidget(final GenericAttribute attr) {
		final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
				.get(attr);
		for (int i = 0; i < dpEntry.getCurrentEditWidgets().length; ++i)
			this.remove(dpEntry.getCurrentDisplayWidgets()[i]);
		showEditWidget(dpEntry, attr);
	}

	private void showEditWidget(final DetailsPanelEntry dpEntry,
			final GenericAttribute attr) {
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

	public void edit(final T toEdit) {
		removeStyleDependentName(CSS.SHOWMODE);
		addStyleDependentName(CSS.EDITMODE);
		bean = toEdit;
		clearNonActions();
		for (int row = 0; row < attributes.size(); row++) {
			final GenericAttribute attr = (GenericAttribute) attributes
					.get(row);
			final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
					.get(attr);
			showEditWidget(dpEntry, attr);
			final CurrentError err = new CurrentError();
			dpEntry.setCurrentError(err);
			add(err, dpEntry.getLastRow().getTdValue());
		}
		// Set the header/description text
		// setPanelHeader(editHeader);
		// setPanelDescription(editDescription);
		isEditMode = true;
	}

	public void startValidation(final ServerOp<?> r) {
		new ServerOp<Object>() {
			int succeeded;

			public void begin() {
				validateEdit(this);
			}

			public void onSuccess(final Object result) {
				succeeded++;
				if (succeeded == attributes.size()) {
					r.onSuccess(null);
				}
			}

			public void onFailure(final Throwable e) {
				r.onFailure(e);
				// super.onFailure(e);
			}
		}.begin();
	}

	public boolean validateEdit() {
		return validateEdit(null);
	}

	public boolean validateEdit(final ServerOp<?> r) {
		if (!isEditMode())
			return true;
		int failed = 0;
		for (int row = 0; row < attributes.size(); row++) {
			final GenericAttribute attr = (GenericAttribute) attributes
					.get(row);
			// Window.alert("validing attribute " + attr.getLabel());
			final CurrentError err = getCurrentError(attr);
			// Window.alert("attr readonly?" + attr.getReadOnly());
			// Window.alert("attr immutable?" + attr.getImmutable());
			// Window.alert("bean new?" + bean.mIsNew());
			if (!attr.getReadOnly()) {
				// Window.alert("we not read only");
				if ((attr.getImmutable() && bean.mIsNew())
						|| !attr.getImmutable()) {
					// Window.alert("committing edit");
					attr.commitEdit(bean, getEditWidgets(attr), err, r);
				}
			} else if (r != null) {
				r.onSuccess(null);
			}
			if (attr.getReadOnly())
				err.setText("");
			if (err.isVisible())
				failed++;
		}
		return failed == 0;
	}

	public void forceFailure(final GenericAttribute attr,
			final ValidationException whybad) {
		if (!isEditMode())
			return;
		final CurrentError err = getCurrentError(attr);
		attr.showError(getEditWidgets(attr), err, whybad);
	}

	public boolean contains(final PropertyConstraint c) {
		for (int row = 0; row < attributes.size(); row++)
			if (((GenericAttribute) attributes.get(row)).getConstraint()
					.equals(c))
				return true;
		return false;
	}

	public boolean handlesValidationException(final ValidationException whybad) {
		return isEditMode() ? contains(whybad.getPropertyConstraint()) : false;
	}

	public boolean showValidationException(final ValidationException whybad) {
		if (!isEditMode())
			return false;
		final PropertyConstraint c = whybad.getPropertyConstraint();
		for (int row = 0; row < attributes.size(); row++) {
			final GenericAttribute attr = (GenericAttribute) attributes
					.get(row);
			if (attr.getConstraint().equals(c)) {
				final CurrentError err = getCurrentError(attr);
				final Widget[] edr = getEditWidgets(attr);
				((GenericAttribute) attributes.get(row)).showError(edr, err,
						whybad);
				((GenericAttribute) attributes.get(row)).setFocus(edr[0], true);
				return true;
			}
		}
		return false;
	}

	public void setEnabled(final boolean enable) {
		for (int k = 0; k < actionCount; k++)
			FocusSupport.setEnabled(getChildren().get(k), enable);
		if (isEditMode())
			for (int row = 0; row < attributes.size(); row++) {
				final GenericAttribute attr = (GenericAttribute) attributes
						.get(row);
				attr.setEnabled(getEditWidgets(attr), enable);
			}
	}

	protected String idForRow(final int row) {
		return panelId + "_" + row;
	}

	private Widget[] getEditWidgets(final GenericAttribute attr) {
		if (!isEditMode())
			throw new IllegalStateException();
		final DetailsPanelEntry dpEntry = (DetailsPanelEntry) dpEntries
				.get(attr);
		return dpEntry.getCurrentEditWidgets();
	}

	private CurrentError getCurrentError(final GenericAttribute attr) {
		if (!isEditMode())
			throw new IllegalStateException();;
		return ((DetailsPanelEntry) dpEntries.get(attr)).getCurrentError();
	}

	protected boolean isEditMode() {
		return isEditMode;
	}

	protected void clearNonActions() {
		while (getChildren().size() > actionCount) {
			final int idx = getChildren().size() - 1;
			final Widget c = getChildren().get(idx);
			remove(c);
		}
	}
}
