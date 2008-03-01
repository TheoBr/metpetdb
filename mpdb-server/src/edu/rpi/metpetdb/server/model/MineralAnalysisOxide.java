package edu.rpi.metpetdb.server.model;


public class MineralAnalysisOxide extends Element {
	private static final long serialVersionUID = 1L;
	public static final int P_amount = 0;
	public static final int P_precision = 1;
	public static final int P_precisionUnit = 2;

	private Float amount;
	private Float precision;
	private String precisionUnit;
	private Oxide oxide;

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

	public void setOxide(final Oxide m) {
		oxide = m;
		if (m != null) {
			this.setName(m.getSpecies());
			this.setId(m.getOxideId());
			// this.setName(m.getName());
			// this.setId(m.getId());
		}
	}

	public Oxide getOxide() {
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
		if (o instanceof MineralAnalysisElement) {
			final boolean one = ((MineralAnalysisOxide) o).getOxide().equals(
					oxide);
			final boolean two = ((MineralAnalysisOxide) o).getAmount() == null ? ((MineralAnalysisOxide) o)
					.getAmount() == amount
					: ((MineralAnalysisOxide) o).getAmount().equals(amount);
			return one && two;
		} else if (o instanceof Oxide) {
			return ((Oxide) o).equals(oxide);
		}
		return false;
	}

	public int hashCode() {
		return oxide != null && amount != null ? oxide.hashCode()
				+ amount.intValue() : oxide.hashCode();
		// return mineral.hashCode();
	}
}