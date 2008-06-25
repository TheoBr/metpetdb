package edu.rpi.metpetdb.client.model;

public class ChemicalAnalysisElementDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private Float amount;
	private Float precision;
	private String precisionUnit;
	private ElementDTO element;

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
		if (precisionUnit == null)
			return "REL";
		return precisionUnit;
	}

	public void setElement(final ElementDTO m) {
		element = m;
	}

	public ElementDTO getElement() {
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
		if (o instanceof ChemicalAnalysisElementDTO) {
			final boolean one = ((ChemicalAnalysisElementDTO) o).getElement()
					.equals(element);
			final boolean two = ((ChemicalAnalysisElementDTO) o).getAmount() == null ? ((ChemicalAnalysisElementDTO) o)
					.getAmount() == amount
					: ((ChemicalAnalysisElementDTO) o).getAmount().equals(
							amount);
			return one && two;
		} else if (o instanceof ElementDTO) {
			return ((ElementDTO) o).equals(element);
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
