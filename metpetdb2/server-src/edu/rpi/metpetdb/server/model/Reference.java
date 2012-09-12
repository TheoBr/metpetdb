package edu.rpi.metpetdb.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "reference")
public class Reference {

/**
 *   reference_id bigint NOT NULL,
  "name" character varying(100) NOT NULL,
 */
	
	private Long referenceId = 0L;
	private String name;
	
	private Georeference georeference;
	
	public Reference()
	{
		super();
	}
	
	public Reference(Long referenceId, String name) {
		super();
		this.referenceId = referenceId;
		this.name = name;
	}
	
	@SequenceGenerator(sequenceName="reference_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )	
	public Long getReference_id() {
		return referenceId;
	}
	public void setReference_id(Long referenceId) {
		this.referenceId = referenceId;
	}
	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//@OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.Georeference.class)
	//@JoinColumn(name="reference_id", referencedColumnName="reference_id")	
	
	@OneToOne(optional=true, mappedBy="reference", cascade={CascadeType.ALL})
	public Georeference getGeoreference()
	{
		return this.georeference;
	}
	
	public void setGeoreference(Georeference georeference)
	{
		this.georeference = georeference;
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
		Reference other = (Reference) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
