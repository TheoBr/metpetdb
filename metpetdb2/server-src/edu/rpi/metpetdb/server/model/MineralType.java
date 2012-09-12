package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "mineral_types")
public class MineralType {


	private String name;
	private Long id;
	
	public MineralType()
	{
		super();
	}

	public MineralType(String name, Long id) {
		super();
		this.name = name;
		this.id = id;
	}
	
	@Column(name="name", nullable=false, length=50)
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@SequenceGenerator(sequenceName="mineral_types_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getMineral_type_id() {
		return this.id;
	}
	public void setMineral_type_id(Long id) {
		this.id = id;
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
		MineralType other = (MineralType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
