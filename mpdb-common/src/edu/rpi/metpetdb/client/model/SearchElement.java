package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchElement implements IsSerializable {
	private String elementSymbol;
	private float lowerBound;
	private float upperBound;

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

	public void setLowerBound(float aLowerBound) {
		lowerBound = aLowerBound;
	}

	public float getLowerBound() {
		return lowerBound;
	}

	public void setUpperBound(float aUpperBound) {
		upperBound = aUpperBound;
	}

	public float getUpperBound() {
		return upperBound;
	}
	
	public void setValues(final String elementSymbol, final float lowerBound,
			final float upperBound, final String measurementUnit){
		this.elementSymbol = elementSymbol;
		float unitMod = 1;
		if (measurementUnit.equalsIgnoreCase("% wt")){
			unitMod = 1;
		} else if (measurementUnit.equalsIgnoreCase("ppm")){
			unitMod = 10000;
		}
		this.lowerBound = lowerBound * unitMod;
		this.upperBound = upperBound * unitMod;
	}

}
