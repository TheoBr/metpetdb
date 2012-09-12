package edu.rpi.metpetdb.server.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "chemical_analyses")

public class ChemicalAnalysis {



	private Long id;
//	@Field(index = Index.TOKENIZED, store = Store.NO)
	private int spotId;
	private int version;
	private double referenceX;
	private double referenceY;
	private Image image;
	private Subsample subsample;
	private String analysisMethod;
	private String location;
	private String analyst;
	private Timestamp analysisDate;
	private Short datePrecision;
	private String description;
	private Reference reference;
//	@IndexedEmbedded(prefix = "mineral_")
	private Mineral mineral;
//	@Field(index = Index.UN_TOKENIZED)
	private Boolean largeRock;
	private Double total;
//	@IndexedEmbedded(prefix = "elements_")
	private Set<ChemicalAnalysisElement> elements = new HashSet<ChemicalAnalysisElement>();
//	@IndexedEmbedded(prefix = "oxides_")
	private Set<ChemicalAnalysisOxide> oxides = new HashSet<ChemicalAnalysisOxide>();

//	@Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;

	@ManyToOne
//	@IndexedEmbedded(depth = 1, prefix = "user_")
	private User owner;

//	private String subsampleName;
//	private String sampleName;
//	private String referenceName;
//	private String analysisMaterial;
	
//	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private Long subsampleId;
	
	private Double stageX;
	private Double stageY;


	private static Map<String, Double> measurementUnits = new HashMap<String, Double>() {
		{
			put("wt%", 1d);
			put("ppm", .00001d);
		}
	};

	public static double defaultPrecision = .02d;
	
	public ChemicalAnalysis()
	{
		super();
	}

	public ChemicalAnalysis(Long id, int spotId, int version, double referenceX,
			double referenceY, Image image, Subsample subsample,
			String analysisMethod, String location, String analyst,
			Timestamp analysisDate, Short datePrecision, String description,
			Reference reference, Mineral mineral, Boolean largeRock,
			Double total,  Boolean publicData, User owner,
			Long subsampleId, Double stageX, Double stageY) {
		super();
		this.id = id;
		this.spotId = spotId;
		this.version = version;
		this.referenceX = referenceX;
		this.referenceY = referenceY;
		this.image = image;
		this.subsample = subsample;
		this.analysisMethod = analysisMethod;
		this.location = location;
		this.analyst = analyst;
		this.analysisDate = analysisDate;
		this.datePrecision = datePrecision;
		this.description = description;
		this.reference = reference;
		this.mineral = mineral;
		this.largeRock = largeRock;
		this.total = total;
		this.publicData = publicData;
		this.owner = owner;
		this.subsampleId = subsampleId;
		this.stageX = stageX;
		this.stageY = stageY;
	}
	
	@SequenceGenerator(sequenceName="chemical_analyses_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )	
	public Long getChemical_analysis_id() {
		return id;
	}

	public void setChemical_analysis_id(Long id) {
		this.id = id;
	}
	
	@Column(name="spot_id", nullable=false)
	public int getSpotId() {
		return spotId;
	}

	public void setSpotId(int spotId) {
		this.spotId = spotId;
	}
	
	@Version
	@Column(name="version", nullable=false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	@Column(name="reference_x", nullable=true)
	public double getReferenceX() {
		return referenceX;
	}

	public void setReferenceX(double referenceX) {
		this.referenceX = referenceX;
	}
	@Column(name="reference_y", nullable=true)
	public double getReferenceY() {
		return referenceY;
	}

	public void setReferenceY(double referenceY) {
		this.referenceY = referenceY;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Image.class)
	@JoinColumn(name="image_id")
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Subsample.class)
	@JoinColumn(name="subsample_id")

	public Subsample getSubsample() {
		return subsample;
	}

	public void setSubsample(Subsample subsample) {
		this.subsample = subsample;
	}

	@Column(name="analysis_method", nullable=true, length=50)
	public String getAnalysisMethod() {
		return analysisMethod;
	}

	public void setAnalysisMethod(String analysisMethod) {
		this.analysisMethod = analysisMethod;
	}
	
	@Column(name="where_done", nullable=true, length=50)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name="analyst", nullable=true, length=50)
	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	@Column(name="analysis_date", nullable=true)
	public Timestamp getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Timestamp analysisDate) {
		this.analysisDate = analysisDate;
	}
	@Column(name="date_precision", nullable=true)
	public Short getDatePrecision() {
		return datePrecision;
	}

	public void setDatePrecision(Short datePrecision) {
		this.datePrecision = datePrecision;
	}
	
	@Column(name="description", nullable=true, length=1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Reference.class)
	@JoinColumn(name="reference_id")	
	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Mineral.class)
	@JoinColumn(name="mineral_id")	
	public Mineral getMineral() {
		return mineral;
	}

	public void setMineral(Mineral mineral) {
		this.mineral = mineral;
	}

	@Column(name="large_rock", nullable=true, length=1)
	@Type(type="yes_no")
	public Boolean getLargeRock() {
		return largeRock;
	}

	public void setLargeRock(Boolean largeRock) {
		this.largeRock = largeRock;
	}
	@Column(name="total", nullable=true)
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	
	//According to Hibernate documentation, having a OneToMany relationship that is
	//bidirectional is not optimal and requires extra update statements by the ORM in 
	//order to implement.  It would be better to have a Join table 
	//http://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/#setup-requirements
	// See section 2.2.5.3.1.1
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.ChemicalAnalysisElement.class)
	@JoinColumn(name="chemical_analysis_id")
	
	
	public Set<ChemicalAnalysisElement> getChemicalAnalysisElements() {
		return elements;
	}

	public void setChemicalAnalysisElements(Set<ChemicalAnalysisElement> elements) {
		this.elements = elements;
	}

	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.ChemicalAnalysisOxide.class)
	@JoinColumn(name="chemical_analysis_id")	
	public Set<ChemicalAnalysisOxide> getChemicalAnalysisOxides() {
		return oxides;
	}

	public void setChemicalAnalysisOxides(Set<ChemicalAnalysisOxide> oxides) {
		this.oxides = oxides;
	}
	
	@Column(name="public_data", nullable=true, length=1)
	@Type(type="yes_no")
	public Boolean getPublicData() {
		return publicData;
	}

	public void setPublicData(Boolean publicData) {
		this.publicData = publicData;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.User.class)
	@JoinColumn(name="user_id")
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Column(name="stage_x", nullable=true)
	public Double getStageX() {
		return stageX;
	}

	public void setStageX(Double stageX) {
		this.stageX = stageX;
	}
	
	@Column(name="stage_y", nullable=true)
	public Double getStageY() {
		return stageY;
	}

	public void setStageY(Double stageY) {
		this.stageY = stageY;
	}

	public void addChemicalAnalysisElement(ChemicalAnalysisElement element) {
		this.elements.add(element);
		
	}
	
	public void addChemicalAnalysisOxide(ChemicalAnalysisOxide oxide) {
		this.oxides.add(oxide);
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((analysisDate == null) ? 0 : analysisDate.hashCode());
		result = prime * result
				+ ((analysisMethod == null) ? 0 : analysisMethod.hashCode());
		result = prime * result + ((analyst == null) ? 0 : analyst.hashCode());
		result = prime * result
				+ ((datePrecision == null) ? 0 : datePrecision.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((largeRock == null) ? 0 : largeRock.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((publicData == null) ? 0 : publicData.hashCode());
		long temp;
		temp = Double.doubleToLongBits(referenceX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(referenceY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + spotId;
		result = prime * result + ((stageX == null) ? 0 : stageX.hashCode());
		result = prime * result + ((stageY == null) ? 0 : stageY.hashCode());
		result = prime * result
				+ ((subsampleId == null) ? 0 : subsampleId.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		ChemicalAnalysis other = (ChemicalAnalysis) obj;
		if (analysisDate == null) {
			if (other.analysisDate != null)
				return false;
		} else if (!analysisDate.equals(other.analysisDate))
			return false;
		if (analysisMethod == null) {
			if (other.analysisMethod != null)
				return false;
		} else if (!analysisMethod.equals(other.analysisMethod))
			return false;
		if (analyst == null) {
			if (other.analyst != null)
				return false;
		} else if (!analyst.equals(other.analyst))
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (largeRock == null) {
			if (other.largeRock != null)
				return false;
		} else if (!largeRock.equals(other.largeRock))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (publicData == null) {
			if (other.publicData != null)
				return false;
		} else if (!publicData.equals(other.publicData))
			return false;
		if (Double.doubleToLongBits(referenceX) != Double
				.doubleToLongBits(other.referenceX))
			return false;
		if (Double.doubleToLongBits(referenceY) != Double
				.doubleToLongBits(other.referenceY))
			return false;
		if (spotId != other.spotId)
			return false;
		if (stageX == null) {
			if (other.stageX != null)
				return false;
		} else if (!stageX.equals(other.stageX))
			return false;
		if (stageY == null) {
			if (other.stageY != null)
				return false;
		} else if (!stageY.equals(other.stageY))
			return false;
		if (subsampleId == null) {
			if (other.subsampleId != null)
				return false;
		} else if (!subsampleId.equals(other.subsampleId))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		if (version != other.version)
			return false;
		return true;
	}


	/**
	 *  chemical_analysis_id bigint NOT NULL,
  "version" integer NOT NULL,
  spot_id bigint NOT NULL,
  subsample_id bigint NOT NULL,
  public_data character(1) NOT NULL,
  reference_x double precision,
  reference_y double precision,
  stage_x double precision,
  stage_y double precision,
  image_id bigint,
  analysis_method character varying(50),
  where_done character varying(50),
  analyst character varying(50),
  analysis_date timestamp without time zone,
  date_precision smallint DEFAULT 0,
  reference_id bigint,
  description character varying(1024),
  mineral_id smallint,
  user_id integer NOT NULL,
  large_rock character(1) NOT NULL,
  total double precision,
	 */
	
	
}
