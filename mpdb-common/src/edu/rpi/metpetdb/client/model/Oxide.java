package edu.rpi.metpetdb.client.model;

import java.util.Set;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

public class Oxide extends MObject {
	private static final long serialVersionUID = 1L;

	private Element element;
	private short oxideId;
	private short oxidationState;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String species;
	private Float weight;
	private short cationsPerOxide;
	private Float conversionFactor;
	private Set<MineralType> mineralTypes;

	public Oxide() {

	}

	public Oxide(final String species) {
		this.species = species;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
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

	public Set<MineralType> getMineralTypes() {
		return mineralTypes;
	}

	public void setMineralTypes(Set<MineralType> mineralTypes) {
		this.mineralTypes = mineralTypes;
	}

	public boolean equals(final Object o) {
		return o instanceof Oxide
				&& (((Oxide) o).getOxideId() == oxideId || (((Oxide) o)
						.getOxideId() == 0 && ((Oxide) o).getSpecies()
						.equalsIgnoreCase(species)));
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

	public native String getDisplayName() /*-{ 
	var s =
	this.@edu.rpi.metpetdb.client.model.Oxide::species; return
	s.replace(/(\d+)/g, "<sub>$1</sub>");
	}- */;
}
