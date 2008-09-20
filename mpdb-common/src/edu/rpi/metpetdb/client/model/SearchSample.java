package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

public class SearchSample implements IsSerializable, MObject {

	private static final long serialVersionUID = 1L;
	private Set<RockType> possibleRockTypes = new HashSet<RockType>();

	private long id;
	private int version;

	private String sesarNumber;
	private Geometry boundingBox;

	private String owner;

	private String alias;

	private DateSpan collectionDateRange;
	private Short datePrecision;

	private Set<SampleMineral> minerals;

	private Set<SearchElement> elements;

	private Set<SearchOxide> oxides;

	private Set<Region> regions;
	
	public SearchSample() {
		
	}

	public Set<RockType> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addPossibleRockType(final RockType rt) {
		possibleRockTypes.add(rt);
	}

	public void setPossibleRockTypes(final Set<RockType> rt) {
		possibleRockTypes = rt;
	}

	public long getId() {
		return id;
	}

	public void setId(final long i) {
		id = i;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public String getSesarNumber() {
		return sesarNumber;
	}

	public void setSesarNumber(final String s) {
		sesarNumber = s;
	}

	public Geometry getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(final Geometry g) {
		boundingBox = g;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(final String u) {
		owner = u;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(final String s) {
		alias = s;
	}

	public DateSpan getCollectionDateRange() {
		return collectionDateRange;
	}

	public void setCollectionDateRange(final DateSpan dateRange) {
		collectionDateRange = dateRange;
	}

	public Set<SampleMineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<SampleMineral> c) {
		minerals = c;
	}

	public void addMineral(final String name) {
		if (minerals == null)
			minerals = new HashSet<SampleMineral>();
		final SampleMineral m = new SampleMineral();
		// m.setName(name);
		minerals.add(m);
	}

	public String getName() {
		if (this.alias != null || !this.alias.equals(""))
			return this.alias;
		else
			return this.sesarNumber;
	}

	public boolean equals(final Object o) {
		return sesarNumber != null && o instanceof Sample
				&& sesarNumber.equals(((SearchSample) o).sesarNumber);
	}

	public int hashCode() {
		return sesarNumber != null ? sesarNumber.hashCode() : 0;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public short getDatePrecision() {
		if (datePrecision == null)
			datePrecision = Short.parseShort("0");
		return datePrecision;
	}

	public void setDatePrecision(short datePrecision) {
		this.datePrecision = datePrecision;
	}

	public void setElements(final Set<SearchElement> e) {
		elements = e;
	}

	public Set<SearchElement> getElements() {
		if (elements == null)
			elements = new HashSet<SearchElement>();
		return elements;
	}

	public void addElement(final Element e, final Float lowerBound,
			final Float upperBound) {
		if (elements == null)
			elements = new HashSet<SearchElement>();
		SearchElement c = new SearchElement();
		c.setElementSymbol(e.getSymbol());
		c.setLowerBound(lowerBound);
		c.setUpperBound(upperBound);
		elements.add(c);
	}

	public void setOxides(final Set<SearchOxide> o) {
		oxides = o;
	}

	public Set<SearchOxide> getOxides() {
		if (oxides == null)
			oxides = new HashSet<SearchOxide>();
		return oxides;
	}

	public Set<Region> getRegions() {
		return regions;
	}

	public void setRegions(final Set<Region> r) {
		regions = r;
	}

	public void addOxide(final Oxide e, final Float lowerBound,
			final Float upperBound) {
		if (oxides == null)
			oxides = new HashSet<SearchOxide>();
		SearchOxide c = new SearchOxide();
		c.setSpecies(e.getSpecies());
		c.setLowerBound(lowerBound);
		c.setUpperBound(upperBound);
		oxides.add(c);
	}

	public Object mGet(Property property) {
		return property.get(this);
	}

	public void mSet(Property property, Object newVal) {
		property.set(this, newVal);
	}
}
