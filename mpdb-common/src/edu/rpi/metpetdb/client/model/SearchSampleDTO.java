package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;

public class SearchSampleDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private Set<RockTypeDTO> possibleRockTypes = new HashSet<RockTypeDTO>();

	private long id;
	private int version;

	private String sesarNumber;
	private Geometry boundingBox;

	private String owner;

	private String alias;

	private DateSpan collectionDateRange;
	private Short datePrecision;

	private Set<SampleMineralDTO> minerals;
	
	private Set<SearchElementDTO> elements;

	private Set<SearchOxideDTO> oxides;
	
	private Set<RegionDTO> regions;

	public Set<RockTypeDTO> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addPossibleRockType(final RockTypeDTO rt) {
		possibleRockTypes.add(rt);
	}

	public void setPossibleRockTypes(final Set<RockTypeDTO> rt) {
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

	public Set<SampleMineralDTO> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<SampleMineralDTO> c) {
		minerals = c;
	}

	public void addMineral(final String name) {
		if (minerals == null)
			minerals = new HashSet<SampleMineralDTO>();
		final SampleMineralDTO m = new SampleMineralDTO();
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
		return sesarNumber != null && o instanceof SampleDTO
				&& sesarNumber.equals(((SearchSampleDTO) o).sesarNumber);
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

public void setElements(final Set<SearchElementDTO> e) {
		elements = e;
	}

	public Set<SearchElementDTO> getElements() {
		if (elements == null)
			elements = new HashSet<SearchElementDTO>();
		return elements;
	}

	public void addElement(final ElementDTO e, final Float lowerBound,
			final Float upperBound) {
		if (elements == null)
			elements = new HashSet<SearchElementDTO>();
		SearchElementDTO c = new SearchElementDTO();
		c.setElementSymbol(e.getSymbol());
		c.setLowerBound(lowerBound);
		c.setUpperBound(upperBound);
		elements.add(c);
	}

	public void setOxides(final Set<SearchOxideDTO> o) {
		oxides = o;
	}

	public Set<SearchOxideDTO> getOxides() {
		if (oxides == null)
			oxides = new HashSet<SearchOxideDTO>();
		return oxides;
	}
	
	public Set<RegionDTO> getRegions() {
		return regions;
	}

	public void setRegions(final Set<RegionDTO> r) {
		regions = r;
	}

	public void addOxide(final OxideDTO e, final Float lowerBound,
			final Float upperBound) {
		if (oxides == null)
			oxides = new HashSet<SearchOxideDTO>();
		SearchOxideDTO c = new SearchOxideDTO();
		c.setSpecies(e.getSpecies());
		c.setLowerBound(lowerBound);
		c.setUpperBound(upperBound);
		oxides.add(c);
	}
}
