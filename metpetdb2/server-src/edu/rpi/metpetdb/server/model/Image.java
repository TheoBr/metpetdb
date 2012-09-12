package edu.rpi.metpetdb.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;



@Entity
@Table(name = "images")
public class Image implements Serializable {

	private Long id;
	private int version;

	private ImageType imageType;
	private Long width;
	private Long height;
	private String checksum;
	private String checksum64x64;
	private String checksumHalf;
	private String checksumMobile;

	//private Subsample Subsample;

	private Sample sample;
	private String filename;
	//private XrayImage xrayImage;
	private String collector;
	//private Set<Reference> references;
	//private Set<ImageComment> comments;
	private Integer scale;
	private String description;
	private User owner;
	private Boolean publicData;
	

	public Image()
	{
		super();
	}
	
	
	public Image(Long id, int version, long width, long height, String checksum,
			String checksum64x64, String checksumHalf, String checksumMobile,
			Sample sample, String filename, String collector, Integer scale,
			String description, User owner, Boolean publicData) {
		super();
		this.id = id;
		this.version = version;
		this.width = width;
		this.height = height;
		this.checksum = checksum;
		this.checksum64x64 = checksum64x64;
		this.checksumHalf = checksumHalf;
		this.checksumMobile = checksumMobile;
		this.sample = sample;
		this.filename = filename;
		this.collector = collector;
		this.scale = scale;
		this.description = description;
		this.owner = owner;
		this.publicData = publicData;
	}
	
	@SequenceGenerator(sequenceName="image_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getImage_id() {
		return id;
	}
	public void setImage_id(Long id) {
		this.id = id;
	}
	
	@Version
	@Column(name="version", nullable=false)
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name="width", nullable=false)
	public Long getWidth() {
		return width;
	}
	public void setWidth(long width) {
		this.width = width;
	}
	
	@Column(name="height", nullable=false)
	public Long getHeight() {
		return height;
	}
	public void setHeight(long height) {
		this.height = height;
	}
	@Column(name="checksum", length=50, nullable=false)
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name="checksum_64x64", length=50, nullable=false)
	public String getChecksum64x64() {
		return checksum64x64;
	}
	public void setChecksum64x64(String checksum64x64) {
		this.checksum64x64 = checksum64x64;
	}
	
	@Column(name="checksum_half", length=50, nullable=false)
	public String getChecksumHalf() {
		return checksumHalf;
	}
	public void setChecksumHalf(String checksumHalf) {
		this.checksumHalf = checksumHalf;
	}
	
	@Column(name="checksum_mobile", length=50, nullable=true)
	public String getChecksumMobile() {
		return checksumMobile;
	}
	public void setChecksumMobile(String checksumMobile) {
		this.checksumMobile = checksumMobile;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Sample.class)
	@JoinColumn(name="sample_id")
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	@Column(name="filename", length=256, nullable=false)
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name="collector", length=50, nullable=true)
	public String getCollector() {
		return collector;
	}
	public void setCollector(String collector) {
		this.collector = collector;
	}
	@Column(name="scale", nullable=true)
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	
	@Column(name="description", length=1024, nullable=true)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.User.class)
	@JoinColumn(name="user_id")
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	@Column(name="public_data", nullable=true, length=1)
	@Type(type="yes_no")
	public Boolean getPublicData() {
		return publicData;
	}
	public void setPublicData(Boolean publicData) {
		this.publicData = publicData;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.ImageType.class)
	@JoinColumn(name="image_type_id")
	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checksum == null) ? 0 : checksum.hashCode());
		result = prime * result
				+ ((checksum64x64 == null) ? 0 : checksum64x64.hashCode());
		result = prime * result
				+ ((checksumHalf == null) ? 0 : checksumHalf.hashCode());
		result = prime * result
				+ ((checksumMobile == null) ? 0 : checksumMobile.hashCode());
		result = prime * result
				+ ((collector == null) ? 0 : collector.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result
				+ ((publicData == null) ? 0 : publicData.hashCode());
		result = prime * result + ((scale == null) ? 0 : scale.hashCode());
		result = prime * result + version;
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		Image other = (Image) obj;
		if (checksum == null) {
			if (other.checksum != null)
				return false;
		} else if (!checksum.equals(other.checksum))
			return false;
		if (checksum64x64 == null) {
			if (other.checksum64x64 != null)
				return false;
		} else if (!checksum64x64.equals(other.checksum64x64))
			return false;
		if (checksumHalf == null) {
			if (other.checksumHalf != null)
				return false;
		} else if (!checksumHalf.equals(other.checksumHalf))
			return false;
		if (checksumMobile == null) {
			if (other.checksumMobile != null)
				return false;
		} else if (!checksumMobile.equals(other.checksumMobile))
			return false;
		if (collector == null) {
			if (other.collector != null)
				return false;
		} else if (!collector.equals(other.collector))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (publicData == null) {
			if (other.publicData != null)
				return false;
		} else if (!publicData.equals(other.publicData))
			return false;
		if (scale == null) {
			if (other.scale != null)
				return false;
		} else if (!scale.equals(other.scale))
			return false;
		if (version != other.version)
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}
	
	
	/**
	 *   
  subsample_id bigint,
  image_format_id smallint,
	 */
	
}
