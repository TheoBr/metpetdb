package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.server.search.bridges.FloatBridge;

public class ChemicalAnalysisElement extends MObject {
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
	
	public void setValues(final Element e, final float amount, float precision, 
			final String measurementUnit, final String precisionUnit){
		element = e;
		this.measurementUnit = measurementUnit;
		this.precisionUnit = precisionUnit;
		this.amount = amount;
		this.precision = precision;
		float unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		if (precisionUnit.equalsIgnoreCase("abs")){
			this.maxAmount = (amount + precision) * unitMod;
			this.minAmount = (amount - precision) * unitMod;
		} else if (precisionUnit.equalsIgnoreCase("rel")){
			if (precision == 0)
				precision = .2F;
			this.maxAmount = (amount * (1+precision)) * unitMod;
			this.minAmount = (amount * (1-precision)) * unitMod;
		}
	}

	@Override
	public boolean mIsNew() {
		return false;
	}
}
