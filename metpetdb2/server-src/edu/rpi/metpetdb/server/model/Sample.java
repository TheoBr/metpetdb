package edu.rpi.metpetdb.server.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.annotations.Type;

import com.google.gwt.validation.client.interfaces.IValidatable;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import edu.rpi.metpetdb.server.Foo;

@Entity
@Table(name = "samples")
public class Sample implements IValidatable {

	/**
	 * sample_id bigint NOT NULL, "version" integer NOT NULL, sesar_number
	 * character(9), public_data character(1) NOT NULL, collection_date
	 * timestamp without time zone, date_precision smallint, "number" character
	 * varying(35) NOT NULL, rock_type_id smallint NOT NULL, user_id integer NOT
	 * NULL, location_error real, country character varying(100), description
	 * text, collector character varying(50), metamorphic_region_id bigint,
	 * collector_id integer, location_text character varying(50), "location"
	 * geometry NOT NULL,
	 */

	/**
	 * 
	 *] sampleHeaderPatterns.put("Latitude",
	 * Pattern.compile("(\\QLatitude\\E)|(\\QLat\\E)",
	 * Pattern.CASE_INSENSITIVE)); sampleHeaderPatterns.put("Longitude",
	 * Pattern.compile("(\\QLongitude\\E)|(\\QLong\\E)",
	 * Pattern.CASE_INSENSITIVE)); 
	 * sampleHeaderPatterns.put("Present sample location",
	 * Pattern.compile("\\QPresent sample location\\E",
	 * Pattern.CASE_INSENSITIVE)); 
	 */

	private Long sample_id;

	private int version;

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String sesarNumber;

	private Geometry location;

	// @IndexedEmbedded(depth = 1, prefix = "user_")
	@com.google.gwt.validation.client.NotNull
	private User owner;


	private Long user_id;

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String number;

	// private Set<SampleAlias> aliases = new HashSet<SampleAlias>();

	// @Field(index = Index.UN_TOKENIZED)
	// @DateBridge(resolution = Resolution.DAY)
	private Timestamp collectionDate;

	private Short datePrecision;

	// @Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;

	// @IndexedEmbedded(prefix = "rockType_")
	private RockType rockType;

	// private String rockTypeName;

	// @ContainedIn
	private Set<Subsample> subsamples = new HashSet<Subsample>();

	// private Set<Project> projects = new HashSet<Project>();

	// @IndexedEmbedded(prefix = "sampleMineral_")
	 private Set<Mineral> minerals = new HashSet<Mineral>();

	// private String firstMineral;

	private Set<Image> images = new HashSet<Image>();

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String description;

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String country;

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String collector;

	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String locationText;

	private Float locationError;
	
	// @IndexedEmbedded(prefix = "region_")
	 private Set<Region> regions = new HashSet<Region>();


	// @IndexedEmbedded(depth = 1, prefix = "metamorphicRegion_")
	// private Set<MetamorphicRegion> metamorphicRegions = new
	// HashSet<MetamorphicRegion>();
	// private String firstMetamorphicRegion;

	// @IndexedEmbedded(depth = 1, prefix = "metamorphicGrade_")
	 private Set<MetamorphicGrade> metamorphicGrades = new HashSet<MetamorphicGrade>();
	 
	 private List<Mineral> validMinerals = null;
	 
	 private transient Double longitude = null;
	 
	 private transient Double latitude = null;


	public Sample() {
		super();
	}

	public Sample(Row headerRow, Row currentRow, List<Mineral> validMinerals) {
		super();

		this.validMinerals = validMinerals;
		
		for (Cell headerCell : headerRow) {
			if (headerCell.getCellType() == Cell.CELL_TYPE_STRING) {
				String headerName = headerCell.getStringCellValue();

				Method matchedMethod = this.findMethodMatch(headerName);
				
				Cell valueCell = null;

				try {
					if (matchedMethod != null) {

						System.out.println(matchedMethod.getName()
								+ "=>"
								+ currentRow.getCell(headerCell
										.getColumnIndex()));

						valueCell = currentRow.getCell(headerCell
								.getColumnIndex());

						if (valueCell != null
								&& valueCell.getCellType() == Cell.CELL_TYPE_STRING)						
						{
							matchedMethod.invoke(this, valueCell
									.getStringCellValue());
					
						}
						else if (valueCell != null
								&& valueCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

							float floatValue = 0.0f;
							try {
								floatValue = Double.valueOf(
										valueCell.getNumericCellValue())
										.floatValue();
								matchedMethod.invoke(this, floatValue);

							} catch (Exception nfe) {
								// nfe.printStackTrace();

								Date dateValue = null;
								try {
									dateValue = valueCell.getDateCellValue();
									matchedMethod.invoke(this, dateValue);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						} else if (valueCell != null) {
							throw new RuntimeException("undefined type");
						}
					}
					else
					{
						valueCell = currentRow.getCell(headerCell
								.getColumnIndex());

						if (valueCell != null
								&& valueCell.getCellType() == Cell.CELL_TYPE_STRING)						
						{
						this.determineValidMineral(headerName, valueCell.getStringCellValue());
						}
						
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				


				
			}
		
		}

		if (this.latitude != null && this.longitude != null)
		{
			Point point = new Point(new Coordinate(this.latitude.doubleValue(), this.longitude.doubleValue()), new PrecisionModel(PrecisionModel.FLOATING), 4326 );
			
			this.setLocation(point);
		}
		
		this.setPublicData(Boolean.FALSE);
	}
	
	public void determineValidMineral(String headerName, String mineralValue)
	{
		for (Mineral validMineral : validMinerals)
		{
			if (validMineral.getName().toUpperCase().equals(headerName.toUpperCase()) && mineralValue.toUpperCase().equals("X"))			
				this.addMineral(validMineral);			
			else
				System.out.println("Invalid Mineral:" + headerName + " " + mineralValue);

		}
	
	}

	@SequenceGenerator(sequenceName = "sample_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getSample_Id() {
		return sample_id;
	}

	public void setSample_Id(Long id) {
		this.sample_id = id;
	}

	@Version
	@Column(name = "version", nullable = false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "sesar_number", nullable = true, length = 9)
	public String getSesarNumber() {
		return sesarNumber;
	}

	@Foo(expression = "(\\QIGSN\\E)|(\\QSesar\\E)")
	public void setSesarNumber(String sesarNumber) {
		this.sesarNumber = sesarNumber;
	}

	@Column(name = "location", nullable = false)
	@Type(type = "org.hibernatespatial.GeometryUserType")
	public Geometry getLocation() {
		return location;
	}

	public void setLocation(Geometry location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = edu.rpi.metpetdb.server.model.User.class)
	@JoinColumn(name = "user_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Column(name = "number", nullable = false, length = 35)
	public String getNumber() {
		return number;
	}

	@Foo(expression = "\\QSample Number\\E")
	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "collection_date", nullable = true)
	public Timestamp getCollectionDate() {
		return collectionDate;
	}

	@Foo(expression = "\\QDate of Collection\\E")
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = new Timestamp(collectionDate.getYear(),
				collectionDate.getMonth(), collectionDate.getDate(), 0, 0, 0, 0);
	}

	public void setCollectionDate(Timestamp collectionDate) {
		this.collectionDate = collectionDate;
	}

	@Column(name = "date_precision", nullable = true)
	public Short getDatePrecision() {
		return datePrecision;
	}

	public void setDatePrecision(Short datePrecision) {
		this.datePrecision = datePrecision;
	}

	@Column(name = "public_data", nullable = true, length = 1)
	@Type(type = "yes_no")
	public Boolean getPublicData() {
		return publicData;
	}

	public void setPublicData(Boolean publicData) {
		this.publicData = publicData;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = edu.rpi.metpetdb.server.model.RockType.class)
	@JoinColumn(name = "rock_type_id")
	public RockType getRockType() {
		return rockType;
	}

	@Foo(expression = "\\QRock Type\\E")
	public void setRockType(String rockTypeStr) {
		RockType rockType = new RockType(null, rockTypeStr);
		this.rockType = rockType;
	}

	public void setRockType(RockType rockType) {
		this.rockType = rockType;
	}

	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	@Foo(expression = "\\QComment\\E")
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "country", nullable = true, length = 100)
	public String getCountry() {
		return country;
	}

	@Foo(expression = "\\QCountry\\E")
	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "collector", nullable = true, length = 100)
	public String getCollector() {
		return collector;
	}

	@Foo(expression = "\\QCollector\\E")
	public void setCollector(String collector) {
		this.collector = collector;
	}

	@Column(name = "location_text", nullable = true, length = 50)
	public String getLocationText() {
		return locationText;
	}

	@Foo(expression = "\\QPresent sample location\\E")
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	@Column(name = "location_error", nullable = true)
	public Float getLocationError() {
		return locationError;
	}

	@Foo(expression = "\\QLocation Error\\E")
	public void setLocationError(Float locationError) {
		this.locationError = locationError;
	}

	public Sample(Long id, int version, String sesarNumber, Geometry location,
			String number, Timestamp collectionDate, Short datePrecision,
			Boolean publicData, String description, String country,
			String locationText, Float locationError) {
		super();
		this.sample_id = id;
		this.version = version;
		this.sesarNumber = sesarNumber;
		this.location = location;
		this.number = number;
		this.collectionDate = collectionDate;
		this.datePrecision = datePrecision;
		this.publicData = publicData;
		this.description = description;
		this.country = country;
		this.locationText = locationText;
		this.locationError = locationError;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = edu.rpi.metpetdb.server.model.Image.class)
	@JoinColumn(name = "sample_id")
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public void addImage(Image image) {
		this.images.add(image);
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = edu.rpi.metpetdb.server.model.Subsample.class)
	@JoinColumn(name = "sample_id")
	public Set<Subsample> getSubsamples() {
		return subsamples;
	}

	public void setSubsamples(Set<Subsample> subsamples) {
		this.subsamples = subsamples;
	}

	public void addSubsample(Subsample subsample) {
		this.subsamples.add(subsample);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((collectionDate == null) ? 0 : collectionDate.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((datePrecision == null) ? 0 : datePrecision.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((locationError == null) ? 0 : locationError.hashCode());
		result = prime * result
				+ ((locationText == null) ? 0 : locationText.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((publicData == null) ? 0 : publicData.hashCode());
		// = prime * result
		// + ((sample_id == null) ? 0 : sample_id.hashCode());
		result = prime * result
				+ ((sesarNumber == null) ? 0 : sesarNumber.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample other = (Sample) obj;
		if (collectionDate == null) {
			if (other.collectionDate != null)
				return false;
		} else if (!collectionDate.equals(other.collectionDate))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (datePrecision == null) {
			if (other.datePrecision != null)
				return false;
		} else if (!datePrecision.equals(other.datePrecision))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (locationError == null) {
			if (other.locationError != null)
				return false;
		} else if (!locationError.equals(other.locationError))
			return false;
		if (locationText == null) {
			if (other.locationText != null)
				return false;
		} else if (!locationText.equals(other.locationText))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (publicData == null) {
			if (other.publicData != null)
				return false;
		} else if (!publicData.equals(other.publicData))
			return false;
		// if (sample_id == null) {
		// if (other.sample_id != null)
		// return false;
		// } else if (!sample_id.equals(other.sample_id))
		// return false;
		if (sesarNumber == null) {
			if (other.sesarNumber != null)
				return false;
		} else if (!sesarNumber.equals(other.sesarNumber))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	public Method findMethodMatch(String headerName) {

		System.out.println("looking for a match: " + headerName);

		for (Method currentMethod : Sample.class.getMethods()) {

			if (currentMethod.getName().startsWith("set") || currentMethod.getName().startsWith("add")) {
				System.out.println("method " + currentMethod.getName());

				Annotation[] annotations = currentMethod
						.getDeclaredAnnotations();

				for (Annotation annotation : annotations) {

					if (annotation instanceof Foo) {
						Foo matchAnnotation = (Foo) annotation;
						System.out.println("found annotation "
								+ currentMethod.getName());

						if (Pattern.compile(matchAnnotation.expression(), Pattern.CASE_INSENSITIVE)
								.matcher(headerName).matches())

						{
							System.out.println("matched " + headerName
									+ "using" + matchAnnotation.expression());
							return currentMethod;
						} else {
							System.out.println("missed " + headerName + "using"
									+ matchAnnotation.expression());

						}
					}
				}

			}
		}

		return null;

		/*
		 * if (annotation instanceof javax.persistence.Column) { Column
		 * myAnnotation = (Column) annotation; System.out.println("name: " +
		 * myAnnotation.name()); System.out.println("insertable: " +
		 * myAnnotation.insertable()); System.out.println("length: " +
		 * myAnnotation.length()); System.out.println("precision: " +
		 * myAnnotation.precision()); System.out.println("null allowed: " +
		 * myAnnotation.nullable()); System.out.println("return type" +
		 * currentMethod.getReturnType());
		 * 
		 * }
		 */
	}

	/**
	 * Validation for upload
	 */
	public void validate() {
		// if (this.getLatitude() && this.getLongitude()) then
		// this.setLocation(location);

	}

	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Region.class)
	@JoinTable(
	        name="sample_regions",
	        joinColumns=@JoinColumn(name="sample_id", referencedColumnName="sample_id"),
	        inverseJoinColumns=@JoinColumn(name="region_id", referencedColumnName="region_id")
	        )
	public Set<Region> getRegions() {
		return regions;
	}


	public void setRegions(Set<Region> regions) {
		this.regions = regions;
	}

	@Foo(expression="\\QRegion\\E")
	public void addRegion(String regionStr)
	{
		this.regions.add(new Region(regionStr));
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.MetamorphicGrade.class)
	@JoinTable(
	        name="sample_metamorphic_grades",
	        joinColumns=@JoinColumn(name="sample_id", referencedColumnName="sample_id"),
	        inverseJoinColumns=@JoinColumn(name="metamorphic_grade_id", referencedColumnName="metamorphic_grade_id")
	        )
	public Set<MetamorphicGrade> getMetamorphicGrades() {
		return metamorphicGrades;
	}

	public void setMetamorphicGrades(Set<MetamorphicGrade> metamorphicGrades) {
		this.metamorphicGrades = metamorphicGrades;
	}
	
	@Foo(expression="\\QMetamorphic Grade\\E")
	public void addMetamorphicGrade(String metamorphicGradeStr)
	{
		this.metamorphicGrades.add(new MetamorphicGrade(metamorphicGradeStr));
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Mineral.class)
	@JoinTable(
	        name="sample_minerals",
	        joinColumns=@JoinColumn(name="sample_id", referencedColumnName="sample_id"),
	        inverseJoinColumns=@JoinColumn(name="mineral_id", referencedColumnName="mineral_id")
	        )
	public Set<Mineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(Set<Mineral> minerals) {
		this.minerals = minerals;
	}

	public void addMineral(Mineral mineral)
	{
		this.minerals.add(mineral);		

	}
		

	@Transient
	public Double getLongitude() {
		return longitude;
	}

	@Foo(expression="\\QLongitude\\E")
	public void setLongitude(Float longitude) {
		this.longitude = longitude.doubleValue();
	}

	@Transient
	@Column(insertable=false, updatable=false)
	public Double getLatitude() {
		return latitude;
	}
	
	@Foo(expression="\\QLatitude\\E")
	public void setLatitude(Float latitude) {
		this.latitude = latitude.doubleValue();
	}





	// private String firstMetamorphicGrade;

	// @IndexedEmbedded(depth = 1, prefix = "reference_")
	// private Set<Reference> references = new HashSet<Reference>();

	// private String firstReference;

	/*
	 * private Set<GeoReference> geoReferences = new HashSet<GeoReference>();
	 * 
	 * private Set<SampleComment> comments = new HashSet<SampleComment>();
	 */
	// private int subsampleCount;

	// private int imageCount;

	// private int analysisCount;

}
