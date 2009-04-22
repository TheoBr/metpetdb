package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchElement implements IsSerializable {
	private String elementSymbol;
	private Double lowerBound;
	private Double upperBound;
	private Boolean wholeRock;
	private Set<Mineral> minerals;

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
	
	public void setWholeRock(Boolean aWholeRock) {
		wholeRock = aWholeRock;
	}

	public Boolean getWholeRock() {
		return wholeRock;
	}
	
	public Set<Mineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<Mineral> c) {
		minerals = c;
	}

	public void addMineral(final String mineralName) {
		final Mineral mineral = new Mineral();
		mineral.setName(mineralName);
		addMineral(mineral);
	}

	public void addMineral(final Mineral min) {		
		if (minerals == null)
			minerals = new HashSet<Mineral>();
		minerals.add(min);
	}
	
	
	public void setValues(final String elementSymbol, final float lowerBound,
			final float upperBound, final String measurementUnit){
		this.elementSymbol = elementSymbol;
		Double unitMod = ChemicalAnalysis.getUnitOffset(measurementUnit);
		this.lowerBound = lowerBound * unitMod;
		this.upperBound = upperBound * unitMod;
	}

}
