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

	public String getElementSymbol() {
		return elementSymbol;
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

}
