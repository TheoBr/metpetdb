package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;

import edu.rpi.metpetdb.server.search.bridges.FloatBridge;

public class ChemicalAnalysisElement extends MObject {
	private static final long serialVersionUID = 1L;

	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = FloatBridge.class)
	private Float amount;
	private Float precision;
	private String precisionUnit;
	@IndexedEmbedded(prefix = "element_")
	private Element element;

	public void setAmount(final Float f) {
		amount = f;
	}

	public Float getAmount() {
		if (amount == null)
			return new Float(0);
		else
			return amount;
	}

	public void setPrecision(final Float p) {
		precision = p;
	}

	public Float getPrecision() {
		if (precision == null)
			return new Float(0);
		else
			return precision;
	}

	public void setPrecisionUnit(final String u) {
		precisionUnit = u;
	}

	public String getPrecisionUnit() {
		return precisionUnit;
	}

	public void setElement(final Element m) {
		element = m;
	}

	public Element getElement() {
		return element;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		if (amount != null)
			return element.getName() + " (" + amount + ")";
		else
			return element.getName();
	}

	public boolean equals(final Object o) {
		if (o instanceof ChemicalAnalysisElement) {
			final boolean one = ((ChemicalAnalysisElement) o).getElement()
					.equals(element);
			final boolean two = ((ChemicalAnalysisElement) o).getAmount() == null ? ((ChemicalAnalysisElement) o)
					.getAmount() == amount
					: ((ChemicalAnalysisElement) o).getAmount().equals(amount);
			return one && two;
		} else if (o instanceof Element) {
			return ((Element) o).equals(element);
		}
		return false;
	}

	public int hashCode() {
		return element != null && amount != null ? element.hashCode()
				+ amount.intValue() : element.hashCode();
		// return mineral.hashCode();
	}

	@Override
	public boolean mIsNew() {
		return false;
	}
}
