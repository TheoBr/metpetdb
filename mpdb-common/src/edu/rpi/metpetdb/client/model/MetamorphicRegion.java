package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.postgis.Geometry;

public class MetamorphicRegion extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;

	private short id;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	private String description;
	private Geometry shape;
	private Geometry labelLocation;
	
	
	public MetamorphicRegion() {
		
	}
	
	public MetamorphicRegion(final String name) {
		this.name = name;
	}

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(final String s) {
		name = s;
	}
	public void setDescription(final String s){
		description=s;
	}
	public String getDescription(){
		return description;
	}
	public Geometry getShape() {
		return shape;
	}

	public void setShape(final Geometry g) {
		shape = g;
	}
	
	public Geometry getLabelLocation() {
		return labelLocation;
	}

	public void setLabelLocation(final Geometry g) {
		labelLocation = g;
	}
	//the following function takes a sample as an argument and determines whether 
	//or not it is contained in the polygon
	public boolean contains(final Sample s)
	{
		return true;
	}

	public boolean equals(final Object o) {
		return name != null && o instanceof MetamorphicRegion
				&& name.equals(((MetamorphicRegion) o).name) && ((MetamorphicRegion) o).getId() == id;
	}

	public int hashCode() {
		return name != null ? name.hashCode() + id : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	
	public int compareTo(Object r) {
		if(!(r instanceof MetamorphicRegion))
			throw new ClassCastException("MetamorphicRegion object expected");
		return this.getName().compareTo(((MetamorphicRegion) r).getName());
	}
}
