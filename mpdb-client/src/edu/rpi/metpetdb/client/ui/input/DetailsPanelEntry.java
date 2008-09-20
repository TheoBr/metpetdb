package edu.rpi.metpetdb.client.ui.input;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

/**
 * Comprised of all of the DetailsPanelRow for this GenericAttribute
 * 
 */
public class DetailsPanelEntry {

	private GenericAttribute attr;
	// ArrayList<DetailsPanelRow>
	private ArrayList<DetailsPanelRow> entryRows;
	private CurrentError currentError;
	private Widget[] currentEditWidgets;
	private Widget[] currentDisplayWidgets;

	/**
	 * Creates a new DetailsPanelEntry, all of them need to have atleast 1 row
	 * or they are worthless
	 * 
	 * @param ga
	 * 		The GenericAttribute for this set of rows
	 * @param row
	 * 		The first row in the table for this GenericAttribute
	 */
	public DetailsPanelEntry(final GenericAttribute ga,
			final DetailsPanelRow row) {
		this.entryRows = new ArrayList<DetailsPanelRow>();
		this.entryRows.add(row);
		this.attr = ga;
	}

	public DetailsPanelEntry() {

	}

	/**
	 * Removes all of the rows from the table
	 */
	public void remove() {
		final Iterator<DetailsPanelRow> itr = entryRows.iterator();
		while (itr.hasNext()) {
			final DetailsPanelRow dpRow = (DetailsPanelRow) itr.next();
			dpRow.remove();
		}
	}

	public int getRowCount() {
		if (entryRows != null)
			return entryRows.size();
		else
			return 0;
	}
	public DetailsPanelRow getLastRow() {
		if (entryRows != null) {
			return (DetailsPanelRow) entryRows.get(entryRows.size() - 1);
		}
		return null;
	}
	public DetailsPanelRow getRow(final int index) {
		if (entryRows != null && index < entryRows.size())
			return (DetailsPanelRow) entryRows.get(index);
		else
			return null;
	}

	public void addRow(final DetailsPanelRow row) {
		if (this.entryRows == null)
			this.entryRows = new ArrayList<DetailsPanelRow>();
		this.entryRows.add(row);
	}

	public GenericAttribute getAttr() {
		return attr;
	}
	public void setAttr(GenericAttribute ga) {
		this.attr = ga;
	}
	public ArrayList<DetailsPanelRow> getEntryRows() {
		return entryRows;
	}
	public void setEntryRows(ArrayList<DetailsPanelRow> entryRows) {
		this.entryRows = entryRows;
	}

	public Widget[] getCurrentDisplayWidgets() {
		return currentDisplayWidgets;
	}

	public void setCurrentDisplayWidgets(Widget[] currentDisplayWidgets) {
		this.currentDisplayWidgets = currentDisplayWidgets;
	}

	public Widget[] getCurrentEditWidgets() {
		return currentEditWidgets;
	}

	public void setCurrentEditWidgets(Widget[] currentEditWidgets) {
		this.currentEditWidgets = currentEditWidgets;
	}

	public CurrentError getCurrentError() {
		return currentError;
	}

	public void setCurrentError(CurrentError currentError) {
		this.currentError = currentError;
	}
}
