package edu.rpi.metpetdb.server.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class MineralAnalysisElement extends Element {
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

	public void setPrecision(final Float p){
		precision = p;
	}
	public Float getPrecision(){
		if(precision == null)
			return new Float(0);
		else
			return precision;
	}
	
	public void setPrecisionUnit(final String u){
		precisionUnit = u;
	}
	public String getPrecisionUnit(){
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
		if (o instanceof MineralAnalysisElement) {
			final boolean one = ((MineralAnalysisElement) o).getElement()
					.equals(element);
			final boolean two = ((MineralAnalysisElement) o).getAmount() == null
					? ((MineralAnalysisElement) o).getAmount() == amount
					: ((MineralAnalysisElement) o).getAmount().equals(amount);
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

	protected Object mSetGet(final int propertyId, final Object newValue) {

		switch (propertyId) {
			case P_amount :
				if (newValue != GET_ONLY)
					setAmount(setFloatValue(newValue));
				return getAmount();
			case P_precision :
				if(newValue != GET_ONLY)
					setPrecision(setFloatValue(newValue));
				return getPrecision();
			case P_precisionUnit :
				if(newValue != GET_ONLY)
					setPrecisionUnit((String) newValue);
				return getPrecisionUnit();
		}
		throw new InvalidPropertyException(propertyId);
	}
}