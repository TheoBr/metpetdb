package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.bridge.builtin.DoubleBridge;

public class ChemicalAnalysisOxide extends MObject {
	private static final long serialVersionUID = 1L;

	private Double amount;
	private Double precision;
	private String precisionUnit;
	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = DoubleBridge.class)
	private Double minAmount;
	@Field(index = Index.UN_TOKENIZED)
	@FieldBridge(impl = DoubleBridge.class)
	private Double maxAmount;
	private String measurementUnit;

	@IndexedEmbedded(prefix = "oxide_")
	private Oxide oxide;

	public void setAmount(final Double f) {
		amount = f;
	}

	public Double getAmount() {
		if (amount == null)
			return new Double(0);
		else
			return amount;
	}

	public void setPrecision(final Double p) {
		precision = p;
	}

	public Double getPrecision() {
		if (precision == null)
			return new Double(0);
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
	
	public void setMinAmount(final Double ma) {
		minAmount = ma;
	}

	public Double getMinAmount() {
		if (minAmount == null)
			return new Double(0);
		else
			return minAmount;
	}
	
	public void setMaxAmount(final Double ma) {
		maxAmount = ma;
	}

	public Double getMaxAmount() {
		if (maxAmount == null)
			return new Double(0);
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
		return oxide.getDisplayName();
	}

	public boolean equals(final Object o) {
		if (o instanceof ChemicalAnalysisOxide) {
			final boolean one = ((ChemicalAnalysisOxide) o).getOxide().equals(
					oxide);
			final boolean two = ((ChemicalAnalysisOxide) o).getAmount().equals(amount);
			return one;
		} else if (o instanceof Oxide) {
			return ((Oxide) o).equals(oxide);
		}
		return false;
	}

	public int hashCode() {
		return oxide != null ? oxide.hashCode() : 0;
	}
	
	public String getDisplayAmount(){
		if (amount >= 0){
			return amount.toString();
		} else {
			return "<" + String.valueOf((-amount));
		}
	}
	
	public void setValues(final Oxide o, final Double amount, final Double precision, 
			final String measurementUnit, final String precisionUnit){
		oxide = o;
		this.measurementUnit = measurementUnit;
		this.precisionUnit = precisionUnit;
		this.amount = amount;
		this.precision = precision;
		setMinMax();
	}
	
	public void setMinMax(){
		if (amount == null || measurementUnit == null)
			return;
		if (precision == null){
			precision = ChemicalAnalysis.defaultPrecision;
			precisionUnit = "REL";
		}
		Double unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		if (amount < 0){
			minAmount = 0D;
			if (precisionUnit.equalsIgnoreCase("ABS")){
				maxAmount = (-amount + precision) * unitMod;
			} else if (precisionUnit.equalsIgnoreCase("REL")){
				maxAmount = (-amount * (1+precision)) * unitMod;
			}
		} else {
			if (precisionUnit.equalsIgnoreCase("ABS")){
				maxAmount = (amount + precision) * unitMod;
				minAmount = (amount - precision) * unitMod;
			} else if (precisionUnit.equalsIgnoreCase("REL")){
				maxAmount = (amount * (1+precision)) * unitMod;
				minAmount = (amount * (1-precision)) * unitMod;
			}
		}
	}

	@Override
	public boolean mIsNew() {
		return false;
	}
}
