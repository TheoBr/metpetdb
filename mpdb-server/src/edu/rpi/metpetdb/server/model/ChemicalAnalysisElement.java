package edu.rpi.metpetdb.server.model;


public class ChemicalAnalysisElement extends Element {
	private static final long serialVersionUID = 1L;
	public static final int P_amount = 0;
	public static final int P_precision = 1;
	public static final int P_precisionUnit = 2;

	private Float amount;
	private Float precision;
	private String precisionUnit;
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
		if (m != null) {
			this.setName(m.getName());
			this.setId(m.getId());
		}
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
}