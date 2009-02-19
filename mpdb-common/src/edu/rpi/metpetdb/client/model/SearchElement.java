package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchElement implements IsSerializable {
	private String elementSymbol;
	private Double lowerBound;
	private Double upperBound;

	public void setElementSymbol(String aSymbol) {
		elementSymbol = aSymbol;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		return elementSymbol + " (" + lowerBound + " - " + upperBound + ")";
	}

	// TODO: This is really a hack... not sure why this must be this way.
	public String getElementSymbol() {
		return elementSymbol.toLowerCase();
	}

	public void setLowerBound(Double aLowerBound) {
		lowerBound = aLowerBound;
	}

	public Double getLowerBound() {
		return lowerBound;
	}

	public void setUpperBound(Double aUpperBound) {
		upperBound = aUpperBound;
	}

	public Double getUpperBound() {
		return upperBound;
	}
	
	public void setValues(final String elementSymbol, final float lowerBound,
			final float upperBound, final String measurementUnit){
		this.elementSymbol = elementSymbol;
		Double unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		this.lowerBound = lowerBound * unitMod;
		this.upperBound = upperBound * unitMod;
	}

}
