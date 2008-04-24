package edu.rpi.metpetdb.client.model;

import java.util.Set;

public class OxideDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private ElementDTO element;
	private short oxideId;
	private short oxidationState;
	private String species;
	private Float weight;
	private short cationsPerOxide;
	private Float conversionFactor;
	private Set<MineralTypeDTO> mineralTypes;

	public void setElement(final ElementDTO s) {
		element = s;
	}

	public ElementDTO getElement() {
		return element;
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

	public Set<MineralTypeDTO> getMineralTypes() {
		return mineralTypes;
	}

	public void setMineralTypes(Set<MineralTypeDTO> mineralTypes) {
		this.mineralTypes = mineralTypes;
	}

	public boolean equals(final Object o) {
		return o instanceof OxideDTO && ((OxideDTO) o).getOxideId() == oxideId;
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
