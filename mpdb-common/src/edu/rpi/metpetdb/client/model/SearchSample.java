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
	
	private Set<String> collectors  = new HashSet<String>();
	
	private Set<String> countries  = new HashSet<String>();

	private Set<String> owners  = new HashSet<String>();

	private String number;

	private DateSpan collectionDateRange;
	private Short datePrecision;

	private Set<Mineral> minerals  = new HashSet<Mineral>();

	private Set<SearchElement> elements  = new HashSet<SearchElement>();

	private Set<SearchOxide> oxides  = new HashSet<SearchOxide>();

	private Set<Region> regions  = new HashSet<Region>();
	
	private Set<Reference> references  = new HashSet<Reference>();
	
	private Set<MetamorphicGrade> metamorphicGrades  = new HashSet<MetamorphicGrade>();
	
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

	public Set<String> getOwners() {
		return owners;
	}

	public void setOwners(final Set<String> o) {
		owners = o;
	}

	public void addOwner(final String name) {
		if (owners == null)
			owners = new HashSet<String>();
		owners.add(name);
	}
	
	public Set<String> getCollectors() {
		return collectors;
	}

	public void setCollectors(final Set<String> c) {
		collectors = c;
	}

	public void addCollector(final String name) {
		if (collectors == null)
			collectors = new HashSet<String>();
		collectors.add(name);
	}
	
	public Set<String> getCountries() {
		return countries;
	}

	public void setCountries(final Set<String> c) {
		countries = c;
	}

	public void addCountry(final String name) {
		if (countries == null)
			countries = new HashSet<String>();
		countries.add(name);
	}


	public String getNumber() {
		return number;
	}

	public void setNumber(final String s) {
		number = s;
	}

	public DateSpan getCollectionDateRange() {
		return collectionDateRange;
	}

	public void setCollectionDateRange(final DateSpan dateRange) {
		collectionDateRange = dateRange;
	}

	public Set<Mineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<Mineral> c) {
		minerals = c;
	}

	public void addMineral(final String name) {
		if (minerals == null)
			minerals = new HashSet<Mineral>();
		final Mineral m = new Mineral();
		// m.setName(name);
		minerals.add(m);
	}

	public String getName() {
		if (this.number != null || !this.number.equals(""))
			return this.number;
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

	public void addElement(final Element e, final Double lowerBound,
			final Double upperBound) {
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

	public void addOxide(final Oxide e, final Double lowerBound,
			final Double upperBound) {
		if (oxides == null)
			oxides = new HashSet<SearchOxide>();
		SearchOxide c = new SearchOxide();
		c.setSpecies(e.getSpecies());
		c.setLowerBound(lowerBound);
		c.setUpperBound(upperBound);
		oxides.add(c);
	}
	
	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(final Set<Reference> r) {
		references = r;
	}

	public void addReferences(final String name) {
		if (references == null)
			references = new HashSet<Reference>();
		final Reference r = new Reference(name);
		references.add(r);
	}
	
	public Set<MetamorphicGrade> getMetamorphicGrades() {
		return metamorphicGrades;
	}

	public void setMetamorphicGrades(final Set<MetamorphicGrade> mg) {
		metamorphicGrades = mg;
	}

	public void addMetamorphicGrade(final String name) {
		if (metamorphicGrades == null)
			metamorphicGrades = new HashSet<MetamorphicGrade>();
		final MetamorphicGrade mg = new MetamorphicGrade(name);
		metamorphicGrades.add(mg);
	}

	public Object mGet(Property property) {
		return property.get(this);
	}

	public void mSet(Property property, Object newVal) {
		property.set(this, newVal);
	}
}
