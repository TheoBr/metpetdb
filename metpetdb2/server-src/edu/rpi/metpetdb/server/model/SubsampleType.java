package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "subsample_type")
public class SubsampleType {

	/**
	 *   subsample_type_id smallint NOT NULL,
  			subsample_type character varying(100) NOT NULL,

	 */
	
	private Long id;
	private String subsampleType;

	public SubsampleType()
	{
		super();
	}
	
	public SubsampleType(Long id, String subsampleType) {
		super();
		this.id = id;
		this.subsampleType = subsampleType;
	}
	
	@SequenceGenerator(sequenceName="subsample_type_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	
	public Long getSubsample_type_id() {
		return id;
	}
	public void setSubsample_type_id(Long id) {
		this.id = id;
	}
	
	@Column(name="subsample_type", length=100, nullable=false)
	public String getSubsampleType() {
		return subsampleType;
	}
	public void setSubsampleType(String subsampleType) {
		this.subsampleType = subsampleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subsampleType == null) ? 0 : subsampleType.hashCode());
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
		SubsampleType other = (SubsampleType) obj;
		if (subsampleType == null) {
			if (other.subsampleType != null)
				return false;
		} else if (!subsampleType.equals(other.subsampleType))
			return false;
		return true;
	}
	
	
	
}
