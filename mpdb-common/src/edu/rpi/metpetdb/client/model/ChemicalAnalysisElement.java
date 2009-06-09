package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.server.search.bridges.DoubleBridge;

public class ChemicalAnalysisElement extends MObject {
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
	
	@IndexedEmbedded(prefix = "element_")
	private Element element;

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

	public void setElement(final Element m) {
		element = m;
	}

	public Element getElement() {
		return element;
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
	
	public String getDisplayName() {
		return element.getName();
	}
	
	// FIXME this is bad, a function called 
	// getName should only return the name
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
			final boolean two = ((ChemicalAnalysisElement) o).getAmount().equals(amount);
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
	
	public void setValues(final Element e, final Double amount, Double precision, 
			final String measurementUnit, final String precisionUnit){
		element = e;
		this.measurementUnit = measurementUnit;
		this.precisionUnit = precisionUnit;
		this.amount = amount;
		this.precision = precision;
	}
	
	public void setMinMax(){
		if (amount == null || measurementUnit == null)
			return;
		if (precision == null){
			precision = ChemicalAnalysis.defaultPrecision;
			precisionUnit = "REL";
		}
		Double unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		if (precisionUnit.equalsIgnoreCase("ABS")){
			maxAmount = (amount + precision) * unitMod;
			minAmount = (amount - precision) * unitMod;
		} else if (precisionUnit.equalsIgnoreCase("REL")){
			maxAmount = (amount + precision) * unitMod;
			minAmount = (amount - precision) * unitMod;
		}
	}

	@Override
	public boolean mIsNew() {
		return false;
	}
}
