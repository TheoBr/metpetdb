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

import com.google.gwt.core.client.JsArrayString;

import edu.rpi.metpetdb.client.model.interfaces.HasDate;
import edu.rpi.metpetdb.client.model.interfaces.HasImages;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.client.service.MpDbConstants;

@Indexed
public class Sample extends MObject implements IHasName, HasDate, HasOwner,
		HasImages, PublicData {
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

	private Set<SampleAlias> aliases = new HashSet<SampleAlias>();

	@Field(index = Index.UN_TOKENIZED)
	@DateBridge(resolution = Resolution.DAY)
	private Timestamp collectionDate;

	private Short datePrecision;

	@Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;

	@IndexedEmbedded(prefix = "rockType_")
	private RockType rockType;

	private String rockTypeName;

	@ContainedIn
	private Set<Subsample> subsamples = new HashSet<Subsample>();

	private Set<Project> projects = new HashSet<Project>();

	@IndexedEmbedded(prefix = "sampleMineral_")
	private Set<SampleMineral> minerals = new HashSet<SampleMineral>();

	private String firstMineral;

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

	private String firstRegion;

	@IndexedEmbedded(depth = 1, prefix = "metamorphicRegion_")
	private Set<MetamorphicRegion> metamorphicRegions = new HashSet<MetamorphicRegion>();
	private String firstMetamorphicRegion;

	@IndexedEmbedded(depth = 1, prefix = "metamorphicGrade_")
	private Set<MetamorphicGrade> metamorphicGrades = new HashSet<MetamorphicGrade>();

	private String firstMetamorphicGrade;

	@IndexedEmbedded(depth = 1, prefix = "reference_")
	private Set<Reference> references = new HashSet<Reference>();

	private String firstReference;

	private Set<GeoReference> geoReferences = new HashSet<GeoReference>();

	private Set<SampleComment> comments = new HashSet<SampleComment>();

	private int subsampleCount;

	private int imageCount;

	private int analysisCount;

	private static final String[] months = {
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December",
	};

	public Sample() {
		super();
	}



	public Sample(SampleJSON json) {

		if (json.getId() != null)
			this.id = Long.valueOf(json.getId());

		if (json.getImageCount() != null)
			this.imageCount = Integer.valueOf(json.getImageCount());

		if (json.getSubsampleCount() != null)
			this.subsampleCount = Integer.valueOf(json.getSubsampleCount());

		if (json.getAnalysisCount() != null)
			this.analysisCount = Integer.valueOf(json.getAnalysisCount());

		if (json.getCollectionDate() != null)
			this.collectionDate = Timestamp.valueOf(json.getCollectionDate());

		this.location = new Point();
		location.dimension = 2;
		location.srid = MpDbConstants.WGS84;

		if (json.getLongitude() != null)
			((Point) location).x = Double.valueOf(json.getLongitude());

		if (json.getLatitude() != null)
			((Point) location).y = Double.valueOf(json.getLatitude());

		this.locationText = json.getLocationText();

		this.country = json.getCountry();

		this.collector = json.getCollector();

		if (json.getMinerals() != null) {
			JsArrayString minerals = (JsArrayString) json.getMinerals();

			for (int i = 0; i < minerals.length(); i++) {
				this.minerals.add(new SampleMineral(
						new Mineral(minerals.get(i))));
			}
		}

		if (json.getRegions() != null) {
			JsArrayString regions = (JsArrayString) json.getRegions();

			for (int i = 0; i < regions.length(); i++) {
				this.regions.add(new Region(regions.get(i)));
			}
		}

		if (json.getMetamorphicRegions() != null) {
			JsArrayString metamorphicRegions = (JsArrayString) json
					.getMetamorphicRegions();

			for (int i = 0; i < metamorphicRegions.length(); i++) {
				this.metamorphicRegions.add(new MetamorphicRegion(
						metamorphicRegions.get(i)));
			}
		}

		if (json.getMetamorphicGrades() != null) {
			JsArrayString metamorphicGrades = (JsArrayString) json
					.getMetamorphicGrades();

			for (int i = 0; i < metamorphicGrades.length(); i++) {
				this.metamorphicGrades.add(new MetamorphicGrade(
						metamorphicGrades.get(i)));
			}
		}

		if (json.getReferences() != null) {
			JsArrayString references = (JsArrayString) json.getReferences();

			for (int i = 0; i < references.length(); i++) {
				this.references.add(new Reference(references.get(i)));

			}
		}

		if (json.getGradeName() != null)
			this.metamorphicGrades
					.add(new MetamorphicGrade(json.getGradeName()));

		this.owner = new User(json.getOwner());

		if (json.getRockType() != null)
			this.rockType = new RockType(json.getRockType());

		this.number = json.getSampleNumber();

		if (json.getPublicData() != null) {
			if (json.getPublicData().equals("Y"))
				this.publicData = Boolean.TRUE;

			if (json.getPublicData().equals("N"))
				this.publicData = Boolean.FALSE;

		}
		
		
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

	public String getRockTypeName() {
		return rockTypeName;
	}

	public void setRockTypeName(final String s) {
		rockTypeName = s;
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

	public void setAliases(final Set<SampleAlias> sa) {
		aliases = sa;
		for (SampleAlias alias : aliases) {
			alias.setSample(this);
		}
	}

	public void addAlias(final SampleAlias sa) {
		sa.setSample(this);
		if (aliases == null)
			aliases = new HashSet<SampleAlias>();
		aliases.add(sa);
	}

	public void addAlias(final String alias) {
		SampleAlias sa = new SampleAlias(alias);
		addAlias(sa);
	}

	public Set<SampleAlias> getAliases() {
		if (aliases == null)
			aliases = new HashSet<SampleAlias>();
		return aliases;
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

	public void setFirstMineral(final String s) {
		this.firstMineral = s;
	}

	public String getFirstMineral() {
		return firstMineral;
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

	public String getFirstRegion() {
		return firstRegion;
	}

	public void setFirstRegion(final String region) {
		firstRegion = region;
	}
	public Set<MetamorphicRegion> getMetamorphicRegions() {
		return metamorphicRegions;
	}
	public void setMetamorphicRegions(final Set<MetamorphicRegion> mr) {
		metamorphicRegions = mr;
	}
	public void addMetamorphicRegion(final MetamorphicRegion newRegion) {
		if (metamorphicRegions == null)
			metamorphicRegions = new HashSet<MetamorphicRegion>();
		metamorphicRegions.add(newRegion);
	}
	public String getFirstMetamorphicRegion() {
		return firstMetamorphicRegion;
	}
	public void setFirstMetamorphicRegion(final String s) {
		firstMetamorphicRegion = s;
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

	public String getFirstMetamorphicGrade() {
		return firstMetamorphicGrade;
	}

	public void setFirstMetamorphicGrade(final String s) {
		firstMetamorphicGrade = s;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(final Set<Reference> r) {
		references = r;
	}

	public String getFirstReference() {
		return firstReference;
	}

	public void setFirstReference(final String s) {
		firstReference = s;
	}

	public Set<GeoReference> getGeoReferences() {
		return geoReferences;
	}

	public void setGeoReferences(final Set<GeoReference> g) {
		geoReferences = g;
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

		if (precision == null || precision == 0)
			return m + " " + d + ", " + String.valueOf(year);
		if (precision != null && precision >= 365)
			return year + "";
		else
			return m + " " + year;
	}

}
