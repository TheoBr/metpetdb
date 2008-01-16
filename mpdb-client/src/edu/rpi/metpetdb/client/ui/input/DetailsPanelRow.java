package edu.rpi.metpetdb.client.ui.input;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import edu.rpi.metpetdb.client.model.MObject;

/**
 * Holds a row in the DetailsPanel
 * 
 */
public class DetailsPanelRow {
	private Element row;
	private Element tdLabel;
	private Element tdValue;
	private Element label;
	private MObject bean;
	private final String STYLENAME_DEFAULT = "pairedRow";

	/**
	 * Creates a new row with the table elements from it
	 * 
	 * @param row
	 *            <tr>
	 * @param tdLabel
	 *            <td>
	 * @param tdValue
	 *            <td>
	 * @param label
	 *            <label>
	 */
	public DetailsPanelRow(final Element row, final Element tdLabel,
			final Element tdValue, final Element label) {
		this.row = row;
		DOM.setElementAttribute(row, "class", STYLENAME_DEFAULT);
		this.tdLabel = tdLabel;
		DOM.setElementAttribute(tdLabel, "class", STYLENAME_DEFAULT + "-label");
		this.tdValue = tdValue;
		DOM.setElementAttribute(tdValue, "class", STYLENAME_DEFAULT + "-value");
		this.label = label;
	}
	
	public DetailsPanelRow(final Element row, final Element tdLabel,
			final Element tdValue, final Element label, final MObject bean) {
		this(row,tdLabel,tdValue,label);
		this.bean = bean;
	}
	
	/**
	 * Removes this row from the details panel;
	 */
	public void remove() {
		if (DOM.getParent(row) != null)
			DOM.removeChild(DOM.getParent(row), row);
	}

	public Element getLabel() {
		return label;
	}
	public void setLabel(Element label) {
		this.label = label;
	}
	public Element getRow() {
		return row;
	}
	public void setRow(Element row) {
		this.row = row;
	}
	public Element getTdLabel() {
		return tdLabel;
	}
	public void setTdLabel(Element tdLabel) {
		this.tdLabel = tdLabel;
	}
	public Element getTdValue() {
		return tdValue;
	}
	public void setTdValue(Element tdValue) {
		this.tdValue = tdValue;
	}
	public MObject getBean() {
		return bean;
	}
	public void setBean(MObject bean) {
		this.bean = bean;
	}
}