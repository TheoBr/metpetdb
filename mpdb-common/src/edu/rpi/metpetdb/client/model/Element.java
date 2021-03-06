package edu.rpi.metpetdb.client.model;

import java.util.Set;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class Element extends MObject implements IHasName {
	private static final long serialVersionUID = 1L;

	private short id;
	private String name;
	private String alternateName;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String symbol;
	private int atomicNumber;
	private Float weight;
	private Set<MineralType> mineralTypes;
	private int orderId;
	
	public Element() {
		
	}
	
	public Element(final String name) {
		this.name = name;
	}

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

	public Set<MineralType> getMineralTypes() {
		return mineralTypes;
	}

	public void setMineralTypes(Set<MineralType> mineralTypes) {
		this.mineralTypes = mineralTypes;
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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
}
