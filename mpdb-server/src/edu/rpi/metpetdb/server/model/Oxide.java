package edu.rpi.metpetdb.server.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class Oxide extends MObject {
	public static final int P_oxidationState = 0;
	public static final int P_species = 1;
	public static final int P_weight = 2;
	public static final int P_cationsPerOxide = 3;
	public static final int P_conversionFactor = 4;
	public static final int P_mineralType = 5;
	
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

	public void setOxidationState(final short o){
		oxidationState = o;
	}
	public short getOxidationState(){
		return oxidationState;
	}
	
	public void setSpecies(final String s){
		species = s;
	}
	public String getSpecies(){
		return species;
	}

	public void setWeight(final Float w){
		weight = w;
	}
	public Float getWeight(){
		return weight;
	}

	public void setCationsPerOxide(final short c){
		cationsPerOxide = c;
	}
	public short getCationsPerOxide(){
		return cationsPerOxide;
	}
	
	public void setConversionFactor(final Float c){
		conversionFactor = c;
	}
	public Float getConversionFactor(){
		return conversionFactor;
	}
	
	public void setMineralType(final String t){
		mineralType = t;
	}
	public String getMineralType(){
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

	protected Object mSetGet(final int propertyId, final Object newValue) {

		switch (propertyId) {

			case P_oxidationState :
				if (newValue != GET_ONLY)
					setOxidationState(Short.parseShort(newValue.toString()));
				return new Short(getOxidationState());
			case P_species :
				if(newValue != GET_ONLY)
					setSpecies((String) newValue);
				return getSpecies();
			case P_weight :
				if (newValue != GET_ONLY)
					setWeight(setFloatValue(newValue));
			 	return getWeight();
			case P_cationsPerOxide :
				if (newValue != GET_ONLY)
					setCationsPerOxide(Short.parseShort(newValue.toString()));
				return new Short(getCationsPerOxide());
			case P_conversionFactor :
				if (newValue != GET_ONLY)
					setConversionFactor(setFloatValue(newValue));
				return getConversionFactor();
			case P_mineralType :
				if(newValue != GET_ONLY)
					setMineralType((String) newValue);
				return getMineralType();
		}
		throw new InvalidPropertyException(propertyId);
	}
}