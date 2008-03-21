package edu.rpi.metpetdb.client.model;


public class ChemicalAnalysisOxideDTO extends ElementDTO {

	private static final long serialVersionUID = 1L;
	private Float amount;
	private Float precision;
	private String precisionUnit;
	private OxideDTO oxide;

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

	public void setOxide(final OxideDTO m) {
		oxide = m;
		if (m != null) {
			this.setName(m.getSpecies());
			this.setId(m.getOxideId());
			// this.setName(m.getName());
			// this.setId(m.getId());
		}
	}

	public OxideDTO getOxide() {
		return oxide;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		if (amount != null)
			return oxide.getSpecies() + " (" + amount + ")";
		else
			return oxide.getSpecies();
	}

	public boolean equals(final Object o) {
		if (o instanceof ChemicalAnalysisElementDTO) {
			final boolean one = ((ChemicalAnalysisOxideDTO) o).getOxide()
					.equals(oxide);
			final boolean two = ((ChemicalAnalysisOxideDTO) o).getAmount() == null ? ((ChemicalAnalysisOxideDTO) o)
					.getAmount() == amount
					: ((ChemicalAnalysisOxideDTO) o).getAmount().equals(amount);
			return one && two;
		} else if (o instanceof OxideDTO) {
			return ((OxideDTO) o).equals(oxide);
		}
		return false;
	}

	public int hashCode() {
		return oxide != null && amount != null ? oxide.hashCode()
				+ amount.intValue() : oxide.hashCode();
		// return mineral.hashCode();
	}
}