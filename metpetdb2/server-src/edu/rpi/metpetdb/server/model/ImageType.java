package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "image_type")

public class ImageType {
/**
	  image_type_id smallint NOT NULL,
	  image_type character varying(100) NOT NULL,
	  abbreviation character varying(10),
	  comments character varying(250),*/
	
	private Long id;
	private String imageType;
	private String abbreviation;

	public ImageType()
	{
		
	}
	
	
	public ImageType(Long id, String imageType, String abbreviation) {
		super();
		this.id = id;
		this.imageType = imageType;
		this.abbreviation = abbreviation;
	}
	
	@SequenceGenerator(sequenceName="image_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getImage_type_id() {
		return id;
	}
	public void setImage_type_id(Long id) {
		this.id = id;
	}
	
	@Column(name="image_type", length=10, nullable=false)
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	@Column(name="abbreviation", length=10, nullable=true)
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	
	
	
}
