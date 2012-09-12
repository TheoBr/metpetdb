package edu.rpi.metpetdb.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "metamorphic_grades")
public class MetamorphicGrade {

	private Long id;

	//@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	
	public MetamorphicGrade()
	{
		
	}
	
	public MetamorphicGrade(String name)
	{
		this.name = name;
	}

	@SequenceGenerator(sequenceName = "metamorphic_grade_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getMetamorphic_Grade_Id() {
		return id;
	}

	public void setMetamorphic_Grade_Id(Long id) {
		this.id = id;
	}
	@Column(unique=true, name = "name", nullable = false, length = 100)
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
		MetamorphicGrade other = (MetamorphicGrade) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
