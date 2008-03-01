package edu.rpi.metpetdb.server.model;

import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class Element extends MObject implements IHasName {
	private static final long serialVersionUID = 1L;
	public static final int P_name = 0;
	public static final int P_alternateName = 1;
	public static final int P_symbol = 2;
	public static final int P_atomicNumber = 3;
	public static final int P_weight = 4;
	public static final int P_mineralType = 5;

	private short id;
	private String name;
	private String alternateName;
	private String symbol;
	private int atomicNumber;
	private Float weight;
	private String mineralType;

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

	public void setMineralType(final String t) {
		mineralType = t;
	}

	public String getMineralType() {
		return mineralType;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof Element && ((Element) o).getId() == id;
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