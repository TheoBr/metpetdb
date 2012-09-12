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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "elements")
public class Element {

	
	/**
	 *   element_id smallint NOT NULL,
  "name" character varying(100) NOT NULL,
  alternate_name character varying(100),
  symbol character varying(4) NOT NULL,
  atomic_number integer NOT NULL,
  weight real,
  
	 * @param elementId
	 * @param name
	 * @param alternateName
	 * @param symbol
	 * @param atomicNumber
	 * @param weight
	 * 
	 * 
	 */
	
	public Element()
	{
		super();
	}
	
	public Element(Long elementId, String name, String alternateName,
			String symbol, int atomicNumber, Float weight) {
		super();
		element_id = elementId;
		this.name = name;
		this.alternateName = alternateName;
		this.symbol = symbol;
		this.atomicNumber = atomicNumber;
		this.weight = weight;
	}
	private Long element_id;
	private String name;
	private String alternateName;
//	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String symbol;
	private int atomicNumber;
	private Float weight;
	private Set<MineralType> mineralTypes =  new HashSet<MineralType>();

	
	@SequenceGenerator(sequenceName="element_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getElement_Id() {
		return element_id;
	}
	public void setElement_Id(Long id) {
		this.element_id = id;
	}
	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="alternate_name", nullable=true, length=100)
	public String getAlternateName() {
		return alternateName;
	}
	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}
	
	@Column(name="symbol", nullable=false, length=4)
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Column(name="atomic_number", nullable=false)
	public int getAtomicNumber() {
		return atomicNumber;
	}
	public void setAtomicNumber(int atomicNumber) {
		this.atomicNumber = atomicNumber;
	}
	
	@Column(name="weight", nullable=true)
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alternateName == null) ? 0 : alternateName.hashCode());
		result = prime * result + atomicNumber;
		//result = prime * result
	//			+ ((element_id == null) ? 0 : element_id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		Element other = (Element) obj;
		if (alternateName == null) {
			if (other.alternateName != null)
				return false;
		} else if (!alternateName.equals(other.alternateName))
			return false;
		if (atomicNumber != other.atomicNumber)
			return false;
	//	if (element_id == null) {
	//		if (other.element_id != null)
//				return false;
	//	} else if (!element_id.equals(other.element_id))
	//		return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}
	

	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.MineralType.class)
	@JoinTable(
	        name="element_mineral_types",
	        joinColumns=@JoinColumn(name="element_id", referencedColumnName="element_id"),
	        inverseJoinColumns=@JoinColumn(name="mineral_type_id", referencedColumnName="mineral_type_id")
	        )
	        
	  public Set<MineralType> getMineralTypes() {
		return mineralTypes;
	}
	public void setMineralTypes(Set<MineralType> mineralTypes) {
		this.mineralTypes = mineralTypes;
	}
	
	
	
}
