package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;
import org.postgis.Point;

import edu.rpi.metpetdb.client.service.MpDbConstants;

public class SearchSampleDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private Set<String> possibleRockTypes = new HashSet<String>();

	private long id;
	private int version;

	private String sesarNumber;
	private Geometry location;

	private UserDTO owner;

	private String alias;

	private DateSpan collectionDateRange;
	private Short datePrecision;

	private Set<SubsampleDTO> subsamples;

	private Set<ProjectDTO> projects;

	private Set<SampleMineralDTO> minerals;

	private Set<ImageDTO> images;

	private String description;

	private String country;

	private String collector;

	private String locationText;

	private Float latitudeError;
	private Float longitudeError;

	private Set<RegionDTO> regions;

	private Set<MetamorphicGradeDTO> metamorphicGrades;

	private Set<ReferenceDTO> references;

	private Set<SampleCommentDTO> comments;

	private Set<ChemicalAnalysisElementDTO> elements;

	private Set<ChemicalAnalysisOxideDTO> oxides;

	private int subsampleCount;

	public Set<String> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addPossibleRockType(final String rt) {
		possibleRockTypes.add(rt);
	}

	public void setPossibleRockTypes(final Set<String> rt) {
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

	public int getSubsampleCount() {
		return subsampleCount;
	}

	public void setSubsampleCount(final int i) {
		subsampleCount = i;
	}

	public String getSesarNumber() {
		return sesarNumber;
	}

	public void setSesarNumber(final String s) {
		sesarNumber = s;
	}

	public Geometry getLocation() {
		return location;
	}

	public void setLocation(final Geometry g) {
		location = g;
	}

	public void setLatitude(final double d) {
		if (location == null) {
			location = new Point();
			((Point) location).dimension = 2;
			((Point) location).srid = MpDbConstants.WGS84;
		}
		((Point) location).x = d;
	}

	public void setLongitude(final double d) {
		if (location == null)
			location = new Point();
		((Point) location).y = d;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(final UserDTO u) {
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

	public Set<ProjectDTO> getProjects() {
		if (projects == null)
			projects = new HashSet<ProjectDTO>();
		return projects;
	}

	public void setProjects(final Set<ProjectDTO> s) {
		projects = s;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String c) {
		country = c;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String d) {
		description = d;
	}

	public String getCollector() {
		return collector;
	}

	public void setCollector(String c) {
		collector = c;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String loc) {
		locationText = loc;
	}

	public Float getLatitudeError() {
		return latitudeError;
	}

	public void setLatitudeError(Float le) {
		latitudeError = le;
	}

	public Float getLongitudeError() {
		return longitudeError;
	}

	public void setLongitudeError(Float le) {
		longitudeError = le;
	}

	public Set<RegionDTO> getRegions() {
		return regions;
	}

	public void setRegions(final Set<RegionDTO> r) {
		regions = r;
	}

	public void addRegion(final String name) {
		if (regions == null)
			regions = new HashSet<RegionDTO>();
		final RegionDTO r = new RegionDTO();
		r.setName(name);
		regions.add(r);
	}

	public Set<MetamorphicGradeDTO> getMetamorphicGrades() {
		return metamorphicGrades;
	}

	public void setMetamorphicGrades(final Set<MetamorphicGradeDTO> m) {
		metamorphicGrades = m;
	}

	public void addMetamorphicGrade(final String name) {
		if (metamorphicGrades == null)
			metamorphicGrades = new HashSet<MetamorphicGradeDTO>();
		final MetamorphicGradeDTO m = new MetamorphicGradeDTO();
		m.setName(name);
		metamorphicGrades.add(m);
	}

	public Set<ReferenceDTO> getReferences() {
		return references;
	}

	public void setReferences(final Set<ReferenceDTO> r) {
		references = r;
	}

	public void addReference(final String name) {
		if (references == null)
			references = new HashSet<ReferenceDTO>();
		final ReferenceDTO m = new ReferenceDTO();
		m.setName(name);
		references.add(m);
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

	public void setElements(final Set<ChemicalAnalysisElementDTO> e) {
		elements = e;
	}

	public Set<ChemicalAnalysisElementDTO> getElements() {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		return elements;
	}

	public void addElement(final ElementDTO e, final Float amount) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		ChemicalAnalysisElementDTO c = new ChemicalAnalysisElementDTO();
		c.setElement(e);
		c.setAmount(amount);
		elements.add(c);
	}

	public void addElement(final ElementDTO e, final Float amount,
			final Float precision) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		ChemicalAnalysisElementDTO c = new ChemicalAnalysisElementDTO();
		c.setElement(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		elements.add(c);
	}

	public void setOxides(final Set<ChemicalAnalysisOxideDTO> o) {
		oxides = o;
	}

	public Set<ChemicalAnalysisOxideDTO> getOxides() {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		return oxides;
	}

	public void addOxide(final OxideDTO e, final Float amount) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		ChemicalAnalysisOxideDTO c = new ChemicalAnalysisOxideDTO();
		c.setOxide(e);
		c.setAmount(amount);
		oxides.add(c);
	}

	public void addOxide(final OxideDTO e, final Float amount,
			final Float precision) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		ChemicalAnalysisOxideDTO c = new ChemicalAnalysisOxideDTO();
		c.setOxide(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		oxides.add(c);
	}

}
