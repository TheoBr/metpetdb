package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.server.search.bridges.FloatBridge;

public class ChemicalAnalysisOxide extends MObject {
	private static final long serialVersionUID = 1L;

	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = FloatBridge.class)
	private Float amount;
	private Float precision;
	private String precisionUnit;
	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = FloatBridge.class)
	private Float minAmount;
	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = FloatBridge.class)
	private Float maxAmount;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String measurementUnit;

	@IndexedEmbedded(prefix = "oxide_")
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
	}

	public Oxide getOxide() {
		return oxide;
	}
	
	public void setMeasurementUnit(final String s) {
		measurementUnit = s;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}
	
	public void setMinAmount(final Float ma) {
		minAmount = ma;
	}

	public Float getMinAmount() {
		if (minAmount == null)
			return new Float(0);
		else
			return minAmount;
	}
	
	public void setMaxAmount(final Float ma) {
		maxAmount = ma;
	}

	public Float getMaxAmount() {
		if (maxAmount == null)
			return new Float(0);
		else
			return maxAmount;
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
	
	public String getDisplayName() {
		if (amount != null)
			return oxide.getDisplayName() + " (" + amount + ")";
		else
			return oxide.getDisplayName();
	}

	public boolean equals(final Object o) {
		if (o instanceof ChemicalAnalysisOxide) {
			final boolean one = ((ChemicalAnalysisOxide) o).getOxide().equals(
					oxide);
			final boolean two = ((ChemicalAnalysisOxide) o).getAmount() == null ? ((ChemicalAnalysisOxide) o)
					.getAmount() == amount
					: ((ChemicalAnalysisOxide) o).getAmount().equals(amount);
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

	@Override
	public boolean mIsNew() {
		return false;
	}
}
