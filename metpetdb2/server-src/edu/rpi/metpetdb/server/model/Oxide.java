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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "oxides")
public class Oxide {

	public Oxide(Long oxideId, Long oxidationState, String species,
			Float weight, Long cationsPerOxide, Float conversionFactor) {
		super();
		this.oxideId = oxideId;
		this.oxidationState = oxidationState;
		this.species = species;
		this.weight = weight;
		this.cationsPerOxide = cationsPerOxide;
		this.conversionFactor = conversionFactor;
	}

	/*
	 * oxide_id smallint NOT NULL, element_id smallint NOT NULL, oxidation_state
	 * smallint, species character varying(20), weight double precision,
	 * cations_per_oxide smallint, conversion_factor double precision NOT NULL,
	 */

	private Element element;
	private Long oxideId;
	private Long oxidationState;
	// @Field(index = Index.TOKENIZED, store = Store.NO)
	private String species;
	private Float weight;
	private Long cationsPerOxide;
	private Float conversionFactor;
	private Set<MineralType> mineralTypes = new HashSet<MineralType>();

	public Oxide() {
		super();
	}
	
	

	@ManyToOne(targetEntity=edu.rpi.metpetdb.server.model.Element.class)
	@JoinColumn(name="element_id", columnDefinition="element_id")
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@SequenceGenerator(sequenceName="oxide_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	public Long getOxide_Id() {
		return oxideId;
	}

	public void setOxide_Id(Long oxideId) {
		this.oxideId = oxideId;
	}
	
	@Column(name="oxidation_state", nullable=true)
	public Long getOxidationState() {
		return oxidationState;
	}

	public void setOxidationState(Long oxidationState) {
		this.oxidationState = oxidationState;
	}

	@Column(name="species", nullable=true, length=20)
	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
	
	@Column(name="weight", nullable=true)
	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	@Column(name="cations_per_oxide", nullable=true)
	public Long getCationsPerOxide() {
		return cationsPerOxide;
	}

	public void setCationsPerOxide(Long cationsPerOxide) {
		this.cationsPerOxide = cationsPerOxide;
	}

	@Column(name="conversion_factor", nullable=false)
	public Float getConversionFactor() {
		return conversionFactor;
	}

	public void setConversionFactor(Float conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL}, targetEntity=edu.rpi.metpetdb.server.model.MineralType.class)
	@JoinTable(
	        name="oxide_mineral_types",
	        joinColumns=@JoinColumn(name="oxide_id", referencedColumnName="oxide_id"),
	        inverseJoinColumns=@JoinColumn(name="mineral_type_id", referencedColumnName="mineral_type_id")
	        )

	public Set<MineralType> getMineralTypes() {
		return mineralTypes;
	}

	public void setMineralTypes(Set<MineralType> mineralTypes) {
		this.mineralTypes = mineralTypes;
	}

}
