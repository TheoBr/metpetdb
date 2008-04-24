package edu.rpi.metpetdb.client.model;

import java.util.Set;

import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class ElementDTO extends MObjectDTO implements IHasName {

	private static final long serialVersionUID = 1L;
	private short id;
	private String name;
	private String alternateName;
	private String symbol;
	private int atomicNumber;
	private Float weight;
	private Set<MineralTypeDTO> mineralTypes;

	public void setId(final short s) {
		id = s;
	}

	public short getId() {
		return id;
	}

	public void setName(final String s) {
		name = s;
	}

	public String getName() {
		return name;
	}

	public void setAlternateName(final String s) {
		alternateName = s;
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setSymbol(final String s) {
		symbol = s;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setAtomicNumber(final int n) {
		atomicNumber = n;
	}

	public int getAtomicNumber() {
		return atomicNumber;
	}

	public void setWeight(final Float w) {
		weight = w;
	}

	public Float getWeight() {
		return weight;
	}

	public Set<MineralTypeDTO> getMineralTypes() {
		return mineralTypes;
	}

	public void setMineralTypes(Set<MineralTypeDTO> mineralTypes) {
		this.mineralTypes = mineralTypes;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof ElementDTO && ((ElementDTO) o).getId() == id;
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public String toString() {
		return name;
	}
}
