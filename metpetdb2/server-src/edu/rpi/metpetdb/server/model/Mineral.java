package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "minerals")

public class Mineral {


	/**
	 *   mineral_id smallint NOT NULL,
  real_mineral_id smallint NOT NULL,
  "name" character varying(100) NOT NULL,
	 */
	
	private Long mineralId = 0L;
	//TODO:  Find out what realMineralId is
	private Long realMineralId = 0L;
	private String name;
	
	public Mineral()
	{
		super();
	}
	
	public Mineral(Long mineralId, Long realMineralId, String name) {
		super();
		this.mineralId = mineralId;
		this.realMineralId = realMineralId;
		this.name = name;
	}
	@SequenceGenerator(sequenceName="mineral_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getMineral_id() {
		return mineralId;
	}
	public void setMineral_id(Long mineralId) {
		this.mineralId = mineralId;
	}
	
	@Column(name="real_mineral_id", nullable=true)
	public Long getRealMineralId() {
		return realMineralId;
	}
	public void setRealMineralId(Long realMineralId) {
		this.realMineralId = realMineralId;
	}
	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Mineral other = (Mineral) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
