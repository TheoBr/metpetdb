package edu.rpi.metpetdb.server.model;

public class Oxide extends MObject {
	private static final long serialVersionUID = 1L;

	private short elementId;
	private short oxideId;
	private short oxidationState;
	private String species;
	private Float weight;
	private short cationsPerOxide;
	private Float conversionFactor;
	private String mineralType;

	public void setElementId(final short s) {
		elementId = s;
	}

	public short getElementId() {
		return elementId;
	}

	public void setOxideId(final short s) {
		oxideId = s;
	}

	public short getOxideId() {
		return oxideId;
	}

	public void setOxidationState(final short o) {
		oxidationState = o;
	}

	public short getOxidationState() {
		return oxidationState;
	}

	public void setSpecies(final String s) {
		species = s;
	}

	public String getSpecies() {
		return species;
	}

	public void setWeight(final Float w) {
		weight = w;
	}

	public Float getWeight() {
		return weight;
	}

	public void setCationsPerOxide(final short c) {
		cationsPerOxide = c;
	}

	public short getCationsPerOxide() {
		return cationsPerOxide;
	}

	public void setConversionFactor(final Float c) {
		conversionFactor = c;
	}

	public Float getConversionFactor() {
		return conversionFactor;
	}

	public void setMineralType(final String t) {
		mineralType = t;
	}

	public String getMineralType() {
		return mineralType;
	}

	public boolean equals(final Object o) {
		return o instanceof Oxide && ((Oxide) o).getOxideId() == oxideId;
	}

	public int hashCode() {
		return oxideId;
	}

	public boolean mIsNew() {
		return oxideId == 0;
	}

	public String toString() {
		return species;
	}
}
