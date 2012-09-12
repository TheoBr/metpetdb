package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "rock_type")

public class RockType {

	private Long id;

	private String typeName;
	
	public RockType()
	{
		super();
	}

	public RockType(Long id, String typeName) {
		super();
		this.id = id;
		this.typeName = typeName;
	}

	@SequenceGenerator(sequenceName = "rock_type_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getRock_type_id() {
		return id;
	}

	public void setRock_type_id(Long id) {
		this.id = id;
	}

	@Column(name = "rock_type", nullable = false, length = 100)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
