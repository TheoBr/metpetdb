package edu.rpi.metpetdb.server.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.postgis.Geometry;
import org.postgis.Point;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.service.MpDbConstants;

@Indexed
public class Sample extends MObject implements IHasName {
	public static final int P_sesarNumber = 0;
	public static final int P_location = 1;
	public static final int P_owner = 2;
	public static final int P_alias = 3;
	public static final int P_collectionDate = 4;
	public static final int P_publicData = 5;
	public static final int P_rockType = 6;
	public static final int P_images = 7;
	public static final int P_minerals = 8;
	public static final int P_country = 9;
	public static final int P_description = 10;
	public static final int P_collector = 11;
	public static final int P_locationText = 12;
	public static final int P_latitudeError = 13;
	public static final int P_longitudeError = 14;
	public static final int P_regions = 15;
	public static final int P_metamorphicGrades = 16;
	public static final int P_references = 17;
	public static final int P_subsampleCount = 18;

	@DocumentId
	private long id;
	private int version;

	@Field(index=Index.TOKENIZED, store=Store.NO)
	private String sesarNumber;
	private Geometry location;

	private User owner;

	private String alias;
	private Timestamp collectionDate;

	private Boolean publicData;

	@Field(index=Index.TOKENIZED, store=Store.NO)
	private String rockType;

	private Set<Subsample> subsamples;

	private Set<Project> projects;

	private Set<SampleMineral> minerals;

	private Set<Image> images;

	private String description;

	private String country;

	private String collector;

	private String locationText;

	private Float latitudeError;
	private Float longitudeError;

	private Set<Region> regions;

	private Set<MetamorphicGrade> metamorphicGrades;

	private Set<Reference> references;

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
		((Point) location).x = d;
	}

	public void setLongitude(final double d) {
		if (location == null)
			location = new Point();
		((Point) location).y = d;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(final User u) {
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

	public String getRockType() {
		return rockType;
	}

	public void setRockType(final String rt) {
		rockType = rt;
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

	public Set<Subsample> getSubsamples() {
		if (subsamples == null)
			subsamples = new HashSet<Subsample>();
		return subsamples;
	}

	public void setSubsamples(final Set<Subsample> c) {
		subsamples = c;
	}

	public void addSubsample(Subsample s) {
		s.setSample(this);
		if (subsamples == null)
			subsamples = new HashSet<Subsample>();
		subsamples.add(s);
	}

	public Set<Project> getProjects() {
		if (projects == null)
			projects = new HashSet<Project>();
		return projects;
	}

	public void setProjects(final Set<Project> s) {
		projects = s;
	}

	public void setImages(final Set<Image> s) {
		if (images == null)
			images = new HashSet<Image>();
		images = s;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void addImage(final Image i) {
		i.setSample(this);
		if (images == null)
			images = new HashSet<Image>();
		images.add(i);
	}

	public Set<SampleMineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<SampleMineral> c) {
		minerals = c;
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

	public Set<Region> getRegions() {
		return regions;
	}

	public void setRegions(final Set<Region> r) {
		regions = r;
	}

	public void addRegion(final String name) {
		if (regions == null)
			regions = new HashSet<Region>();
		final Region r = new Region();
		r.setName(name);
		regions.add(r);
	}

	public Set<MetamorphicGrade> getMetamorphicGrades() {
		return metamorphicGrades;
	}

	public void setMetamorphicGrades(final Set<MetamorphicGrade> m) {
		metamorphicGrades = m;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(final Set<Reference> r) {
		references = r;
	}

	public String getName() {
		if (this.alias != null || !this.alias.equals(""))
			return this.alias;
		else
			return this.sesarNumber;
	}

	public boolean equals(final Object o) {
		return sesarNumber != null && o instanceof Sample
				&& sesarNumber.equals(((Sample) o).sesarNumber);
	}

	public int hashCode() {
		return sesarNumber != null ? sesarNumber.hashCode() : 0;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
		case P_sesarNumber:
			if (newValue != GET_ONLY)
				setSesarNumber((String) newValue);
			return getSesarNumber();

		case P_location:
			if (newValue != GET_ONLY)
				setLocation((Geometry) newValue);
			return getLocation();

		case P_owner:
			if (newValue != GET_ONLY)
				setOwner((User) newValue);
			return getOwner();

		case P_alias:
			if (newValue != GET_ONLY)
				setAlias((String) newValue);
			return getAlias();

		case P_collectionDate:
			if (newValue != GET_ONLY)
				setCollectionDate((Timestamp) newValue);
			return getCollectionDate();

		case P_publicData:
			if (newValue != GET_ONLY)
				setPublicData(((Boolean) newValue));
			return isPublicData() ? Boolean.TRUE : Boolean.FALSE;

		case P_rockType:
			if (newValue != GET_ONLY)
				setRockType((String) newValue);
			return getRockType();

		case P_images:
			if (newValue != GET_ONLY)
				setImages((Set<Image>) newValue);
			return getImages();

		case P_minerals:
			if (newValue != GET_ONLY)
				setMinerals((Set<SampleMineral>) newValue);
			return getMinerals();

		case P_country:
			if (newValue != GET_ONLY)
				setCountry((String) newValue);
			return getCountry();

		case P_description:
			if (newValue != GET_ONLY)
				setDescription((String) newValue);
			return getDescription();

		case P_collector:
			if (newValue != GET_ONLY)
				setCollector((String) newValue);
			return getCollector();

		case P_locationText:
			if (newValue != GET_ONLY)
				setLocationText((String) newValue);
			return getLocationText();

		case P_latitudeError:
			if (newValue != GET_ONLY)
				setLatitudeError(setFloatValue(newValue));
			return getLatitudeError();

		case P_longitudeError:
			if (newValue != GET_ONLY)
				setLongitudeError(setFloatValue(newValue));
			return getLongitudeError();

		case P_regions:
			if (newValue != GET_ONLY)
				setRegions((Set<Region>) newValue);
			return getRegions();

		case P_metamorphicGrades:
			if (newValue != GET_ONLY)
				setMetamorphicGrades((Set<MetamorphicGrade>) newValue);
			return getMetamorphicGrades();

		case P_references:
			if (newValue != GET_ONLY)
				setReferences((Set<Reference>) newValue);
			return getReferences();

		case P_subsampleCount:
			if (newValue != GET_ONLY)
				setSubsampleCount(Integer.parseInt(newValue.toString()));
			return new Integer(getSubsampleCount());
		}
		throw new InvalidPropertyException(propertyId);
	}
}