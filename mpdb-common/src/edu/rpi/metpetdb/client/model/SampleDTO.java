package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;
import org.postgis.Point;

import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.service.MpDbConstants;

public class SampleDTO extends MObjectDTO implements IHasName {

	private static final long serialVersionUID = 1L;
	private long id;
	private int version;

	private String sesarNumber;
	private Geometry location;

	private UserDTO owner;

	private String alias;

	private Timestamp collectionDate;
	private Short datePrecision;

	private Boolean publicData;

	private RockTypeDTO rockType;

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

	private int subsampleCount;

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
		((Point) location).y = d;
	}

	public void setLongitude(final double d) {
		if (location == null)
			location = new Point();
		((Point) location).x = d;
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

	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(final Timestamp date) {
		collectionDate = date;
	}

	public RockTypeDTO getRockType() {
		return rockType;
	}

	public void setRockType(final RockTypeDTO rt) {
		rockType = rt;
	}
	
	public void addRockType(final String rockTypeName) {
		final RockTypeDTO rockType = new RockTypeDTO();
		rockType.setRockType(rockTypeName);
		setRockType(rockType);
	}

	public boolean isPublicData() {
		if (publicData != null)
			return publicData.booleanValue();
		else
			return false;
	}

	public void setPublicData(final Boolean p) {
		publicData = p;
	}

	public Set<SubsampleDTO> getSubsamples() {
		if (subsamples == null)
			subsamples = new HashSet<SubsampleDTO>();
		return subsamples;
	}

	public void setSubsamples(final Set<SubsampleDTO> c) {
		subsamples = c;
	}

	public void addSubsample(SubsampleDTO s) {
		s.setSample(this);
		if (subsamples == null)
			subsamples = new HashSet<SubsampleDTO>();
		subsamples.add(s);
	}

	public Set<ProjectDTO> getProjects() {
		if (projects == null)
			projects = new HashSet<ProjectDTO>();
		return projects;
	}

	public void setProjects(final Set<ProjectDTO> s) {
		projects = s;
	}

	public void setImages(final Set<ImageDTO> s) {
		if (images == null)
			images = new HashSet<ImageDTO>();
		images = s;
	}

	public Set<ImageDTO> getImages() {
		return images;
	}

	public void addImage(final ImageDTO i) {
		i.setSample(this);
		if (images == null)
			images = new HashSet<ImageDTO>();
		images.add(i);
	}

	public Set<SampleMineralDTO> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<SampleMineralDTO> c) {
		minerals = c;
	}

	public void addMineral(final MineralDTO min) {
		this.addMineral(min, new Float(0));
	}
	
	/**
	 * 
	 * @param minerals comma separated list of minerals
	 */
	public void addMineral(final String mineralName) {
		final MineralDTO mineral = new MineralDTO();
		mineral.setName(mineralName);
		addMineral(mineral);
	}

	public void addMineral(final MineralDTO min, final Float amount) {
		if (minerals == null)
			minerals = new HashSet<SampleMineralDTO>();
		final SampleMineralDTO m = new SampleMineralDTO();
		m.setAmount(amount);
		m.setMineral(min);
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

	public Set<SampleCommentDTO> getComments() {
		return comments;
	}

	public void setComments(Set<SampleCommentDTO> comments) {
		this.comments = comments;
	}

	public void addComment(final String comment) {
		if (comments == null)
			comments = new HashSet<SampleCommentDTO>();
		SampleCommentDTO sc = new SampleCommentDTO();
		sc.setSample(this);
		sc.setText(comment);
		this.comments.add(sc);
	}

	public String getName() {
		if (this.alias != null || !this.alias.equals(""))
			return this.alias;
		else
			return this.sesarNumber;
	}

	public boolean equals(final Object o) {
		return sesarNumber != null && o instanceof SampleDTO
				&& sesarNumber.equals(((SampleDTO) o).sesarNumber);
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
}
