package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "regions")
public class Region {

	private Long id;

	//@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	
	
	public Region()
	{
		
	}
	
	public Region(String name)
	{
		this.name = name;
	}

	@SequenceGenerator(sequenceName = "region_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getRegion_Id() {
		return id;
	}

	public void setRegion_Id(Long id) {
		this.id = id;
	}
	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
