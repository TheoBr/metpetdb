package edu.rpi.metpetdb.server.model;

import java.util.HashSet;
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
@Table(name = "subsamples")
public class Subsample {

	
//	@DocumentId
	private Long id;

	//@ContainedIn
//	@IndexedEmbedded(prefix = "sample_")
	private Sample sample;

	private int version;

//	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;

	private SubsampleType type;

	private Set<Image> images = new HashSet<Image>();

//	private Grid grid;
	
//	@Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;
	
//	@IndexedEmbedded(depth = 1, prefix = "user_")
	private User owner;

	//@IndexedEmbedded(prefix = "chemicalAnalysis_")
	private Set<ChemicalAnalysis> chemicalAnalysis = new HashSet<ChemicalAnalysis>(); 
//	private int imageCount;
//	private int analysisCount;
//	private Long sampleId;

//	private String sampleName;

	
	public Subsample()
	{
		super();
	}
	
	public Subsample(Long id, Sample sample, int version, String name, Boolean publicData,
			User owner) {
		super();
		this.id = id;
		this.sample = sample;
		this.version = version;
		this.name = name;
		this.type = type;
		this.images = images;
		this.publicData = publicData;
		this.owner = owner;
	}
	
	@SequenceGenerator(sequenceName="subsample_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getSubsample_Id() {
		return id;
	}

	public void setSubsample_Id(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Sample.class)
	@JoinColumn(name="sample_id")	
	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	@Version
	@Column(name="version", nullable=false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.SubsampleType.class)
	@JoinColumn(name="subsample_type_id")
	public SubsampleType getType() {
		return type;
	}

	public void setType(SubsampleType type) {
		this.type = type;
	}

	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Image.class)
	@JoinColumn(name="subsample_id")
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
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

	public void addImage(Image image)
	{
		this.images.add(image);
	}
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.ChemicalAnalysis.class)
	@JoinColumn(name="subsample_id")

	public Set<ChemicalAnalysis> getChemicalAnalysis() {
		return chemicalAnalysis;
	}

	public void setChemicalAnalysis(Set<ChemicalAnalysis> chemicalAnalysis) {
		this.chemicalAnalysis = chemicalAnalysis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
	//	result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((publicData == null) ? 0 : publicData.hashCode());
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
		Subsample other = (Subsample) obj;
//		if (id != other.id)
//			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publicData == null) {
			if (other.publicData != null)
				return false;
		} else if (!publicData.equals(other.publicData))
			return false;
		if (version != other.version)
			return false;
		return true;
	}


	
	
	
}
