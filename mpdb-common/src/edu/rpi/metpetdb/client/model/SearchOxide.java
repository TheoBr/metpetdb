package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchOxide implements IsSerializable {
	private String species;
	private Double lowerBound;
	private Double upperBound;

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
	
	public void setValues(final String species, final float lowerBound,
			final float upperBound, final String measurementUnit){
		this.species = species;
		Double unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		this.lowerBound = lowerBound * unitMod;
		this.upperBound = upperBound * unitMod;
	}
}
