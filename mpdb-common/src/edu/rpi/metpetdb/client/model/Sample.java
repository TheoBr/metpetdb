package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.postgis.Geometry;
import org.postgis.Point;

import edu.rpi.metpetdb.client.model.interfaces.HasDate;
import edu.rpi.metpetdb.client.model.interfaces.HasImages;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.client.service.MpDbConstants;

@Indexed
public class Sample extends MObject implements IHasName, HasDate, HasOwner, HasImages,
		PublicData {
	private static final long serialVersionUID = 1L;

	@DocumentId
	private long id;
	private int version;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String sesarNumber;
	private Geometry location;

	@IndexedEmbedded(depth = 1, prefix = "user_")
	private User owner;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String number;

	@Field(index = Index.UN_TOKENIZED)
	@DateBridge(resolution = Resolution.DAY)
	private Timestamp collectionDate;

	private Short datePrecision;

	@Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;

	@IndexedEmbedded(prefix = "rockType_")
	private RockType rockType;

	@ContainedIn
	private Set<Subsample> subsamples = new HashSet<Subsample>();

	private Set<Project> projects = new HashSet<Project>();

	@IndexedEmbedded(prefix = "sampleMineral_")
	private Set<SampleMineral> minerals = new HashSet<SampleMineral>();

	private Set<Image> images = new HashSet<Image>();

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String description;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String country;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String collector;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String locationText;

	private Float locationError;

	@IndexedEmbedded(prefix = "region_")
	private Set<Region> regions = new HashSet<Region>();

	@IndexedEmbedded(depth = 1, prefix = "metamorphicGrade_")
	private Set<MetamorphicGrade> metamorphicGrades = new HashSet<MetamorphicGrade>();

	@IndexedEmbedded(depth = 1, prefix = "reference_")
	private Set<Reference> references = new HashSet<Reference>();

	private Set<SampleComment> comments = new HashSet<SampleComment>();

	private int subsampleCount;

	private int imageCount;

	private int analysisCount;
	
	private static final String[] months = {
		"January", "February", "March", "April", "May", "June", "July",
		"August", "September", "October", "November", "December",
	};

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

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(final int i) {
		imageCount = i;
	}

	public int getAnalysisCount() {
		return analysisCount;
	}

	public void setAnalysisCount(final int i) {
		analysisCount = i;
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
		if (location == null) {
			location = new Point();
			((Point) location).dimension = 2;
			((Point) location).srid = MpDbConstants.WGS84;
		}
		((Point) location).x = d;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(final User u) {
		owner = u;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(final String s) {
		number = s;
	}

	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(final Timestamp date) {
		collectionDate = date;
	}

	public RockType getRockType() {
		return rockType;
	}

	public void setRockType(final RockType rt) {
		rockType = rt;
	}

	public void addRockType(final String s) {
		final RockType rt = new RockType();
		rt.setRockType(s);
		setRockType(rt);
	}

	public Boolean isPublicData() {
		return publicData;
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

	public void addMineral(final Mineral min) {
		this.addMineral(min, "");
	}

	/**
	 * 
	 * @param minerals
	 *            comma separated list of minerals
	 */
	public void addMineral(final String mineralName) {
		final Mineral mineral = new Mineral();
		mineral.setName(mineralName);
		addMineral(mineral);
	}

	public void addMineral(final Mineral min, final String amount) {
		if (minerals == null)
			minerals = new HashSet<SampleMineral>();
		final SampleMineral m = new SampleMineral();
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

	public Float getLocationError() {
		return locationError;
	}

	public void setLocationError(Float le) {
		locationError = le;
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
		final Region r = new Region(name);
		regions.add(r);
	}

	public Set<MetamorphicGrade> getMetamorphicGrades() {
		return metamorphicGrades;
	}

	public void setMetamorphicGrades(final Set<MetamorphicGrade> m) {
		metamorphicGrades = m;
	}

	public void addMetamorphicGrade(final String s) {
		final MetamorphicGrade mg = new MetamorphicGrade(s);
		if (metamorphicGrades == null)
			metamorphicGrades = new HashSet<MetamorphicGrade>();
		metamorphicGrades.add(mg);
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(final Set<Reference> r) {
		references = r;
	}

	public Set<SampleComment> getComments() {
		if (comments == null)
			comments = new HashSet<SampleComment>();
		return comments;
	}

	public void setComments(Set<SampleComment> comments) {
		this.comments = comments;
	}

	public void addComment(final String comment) {
		if (comments == null)
			comments = new HashSet<SampleComment>();
		SampleComment sc = new SampleComment(comment);
		sc.setSample(this);
		this.comments.add(sc);
	}

	public void addReference(final String name) {
		if (references == null)
			references = new HashSet<Reference>();
		final Reference m = new Reference(name);
		references.add(m);
	}

	public String getName() {
		if (this.number != null || !this.number.equals(""))
			return this.number;
		else
			return this.sesarNumber;
	}

	public boolean equals(final Object o) {
		return number != null && o instanceof Sample
				&& number.equalsIgnoreCase(((Sample) o).number);
	}

	public int hashCode() {
		return number == null ? 0 : number.toLowerCase().hashCode();
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public Short getDatePrecision() {
		return datePrecision;
	}

	public void setDatePrecision(Short datePrecision) {
		this.datePrecision = datePrecision;
	}

	public Timestamp getDate() {
		return getCollectionDate();
	}

	public void setDate(Timestamp date) {
		setCollectionDate(date);
	}
	
	public String toString() {
		if (number != null && number.length() > 0)
			return number;
		else
			return String.valueOf(id);
	}
	
	public final static String dateToString(final Date dt, final Short precision) {
		if (dt == null)
			return "";

		final int year = dt.getYear() + 1900;
		final int month = dt.getMonth();
		final int day = dt.getDate();

		final String m = months[month];
		final String d = day < 10 ? "0" + day : String.valueOf(day);

		if (precision == 0)
			return m + " " + d + ", " + String.valueOf(year);
		if (precision >= 365)
			return year + "";
		else
			return m + " " + year;
	}
	
}
