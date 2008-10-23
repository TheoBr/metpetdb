package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchOxide implements IsSerializable {
	private String species;
	private float lowerBound;
	private float upperBound;

	public void setSpecies(String aSpecies) {
		species = aSpecies;
	}

	//TODO: this is a hack... Don't know why it must be lower case
	public String getSpecies() {
		return species.toLowerCase();
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		return species + " (" + lowerBound + " - " + upperBound + ")";
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
