package edu.rpi.metpetdb.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chemical_analysis_elements")
public class ChemicalAnalysisElement implements Serializable {
	@Embeddable
	public static class Pk implements Serializable
	{
		@Column(name="chemical_analysis_id", nullable=false, updatable=false)
		private Long chemical_analysis_id;
		
		@Column(name="element_id", nullable=false, updatable=false)
		private Long element_id;
		
		public Pk()
		{
			super();
		}
		
		public Pk(Long chemical_analysis_id, Long element_id)
		{
			this.chemical_analysis_id = chemical_analysis_id;
			this.element_id = element_id;
		}

		public Long getChemical_analysis_id() {
			return chemical_analysis_id;
		}

		public void setChemical_analysis_id(Long chemicalAnalysisId) {
			chemical_analysis_id = chemicalAnalysisId;
		}

		public Long getElement_id() {
			return element_id;
		}

		public void setElement_id(Long elementId) {
			element_id = elementId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((chemical_analysis_id == null) ? 0
							: chemical_analysis_id.hashCode());
			result = prime * result
					+ ((element_id == null) ? 0 : element_id.hashCode());
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
			Pk other = (Pk) obj;
			if (chemical_analysis_id == null) {
				if (other.chemical_analysis_id != null)
					return false;
			} else if (!chemical_analysis_id.equals(other.chemical_analysis_id))
				return false;
			if (element_id == null) {
				if (other.element_id != null)
					return false;
			} else if (!element_id.equals(other.element_id))
				return false;
			return true;
		}
		
		
	}
	
	public ChemicalAnalysisElement( Double amount,
			Double precision, String precisionUnit, Double minAmount,
			Double maxAmount, String measurementUnit) {
		super();
//		element_id = elementId;
		this.amount = amount;
		this.precision = precision;
		this.precisionUnit = precisionUnit;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.measurementUnit = measurementUnit;
	}
	/**
	 *   chemical_analysis_id bigint NOT NULL,
  element_id smallint NOT NULL,
  amount double precision NOT NULL,
  "precision" double precision,
  precision_type character varying(3),
  measurement_unit character varying(4),
  min_amount double precision,
  max_amount double precision,
	 */

	@ManyToOne(targetEntity=edu.rpi.metpetdb.server.model.ChemicalAnalysis.class)
	@JoinColumn(name = "chemical_analysis_id", insertable = false, updatable = false)
	private ChemicalAnalysis chemicalAnalysis;
	@Column(name="amount", nullable=false)
	private Double amount;

	@Column(name="precision", nullable=true)
	private Double precision;
	
	@Column(name="precision_type", nullable=true, length=3)
	private String precisionUnit;
//	@Field(index = Index.UN_TOKENIZED)
//	@FieldBridge(impl = DoubleBridge.class)
	
	@Column(name="min_amount", nullable=true)
	private Double minAmount;
//	@Field(index = Index.UN_TOKENIZED)
//	@FieldBridge(impl = DoubleBridge.class)
	@Column(name="max_amount", nullable=true)
	private Double maxAmount;
	
	@Column(name="measurement_unit", nullable=true, length=4)
	private String measurementUnit;
//	@IndexedEmbedded(prefix = "element_")

	@ManyToOne(targetEntity=edu.rpi.metpetdb.server.model.Element.class)
	@JoinColumn(name = "element_id", insertable = false, updatable = false)
	private Element element;
	
//	private Long elementId;
//	private Long chemicalAnalysisId;
	
	@EmbeddedId
	private Pk pk;
	
//	@SequenceGenerator(sequenceName="chemical_analysis_elements_seq", name = "generator")
//	@Id 
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )	
	
	public ChemicalAnalysisElement()
	{
		super();
	}
	

	public Pk getPk()
	{
		return this.pk;
	}
	

	public void setPk(Pk pk) {
		this.pk = pk;
	}

/*	public Long getElement_id() {
		return element_id;
	}
	public void setElement_id(Long elementId) {
		element_id = elementId;
	}*/

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getPrecision() {
		return precision;
	}
	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public String getPrecisionUnit() {
		return precisionUnit;
	}
	public void setPrecisionUnit(String precisionUnit) {
		this.precisionUnit = precisionUnit;
	}
	public Double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}
	public Double getMax_Amount() {
		return maxAmount;
	}
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public ChemicalAnalysis getChemicalAnalysis() {
		return chemicalAnalysis;
	}

	public void setChemicalAnalysis(ChemicalAnalysis chemicalAnalysis) {
		this.chemicalAnalysis = chemicalAnalysis;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((maxAmount == null) ? 0 : maxAmount.hashCode());
		result = prime * result
				+ ((measurementUnit == null) ? 0 : measurementUnit.hashCode());
		result = prime * result
				+ ((minAmount == null) ? 0 : minAmount.hashCode());
		result = prime * result
				+ ((precision == null) ? 0 : precision.hashCode());
		result = prime * result
				+ ((precisionUnit == null) ? 0 : precisionUnit.hashCode());
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
		ChemicalAnalysisElement other = (ChemicalAnalysisElement) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (maxAmount == null) {
			if (other.maxAmount != null)
				return false;
		} else if (!maxAmount.equals(other.maxAmount))
			return false;
		if (measurementUnit == null) {
			if (other.measurementUnit != null)
				return false;
		} else if (!measurementUnit.equals(other.measurementUnit))
			return false;
		if (minAmount == null) {
			if (other.minAmount != null)
				return false;
		} else if (!minAmount.equals(other.minAmount))
			return false;
		if (precision == null) {
			if (other.precision != null)
				return false;
		} else if (!precision.equals(other.precision))
			return false;
		if (precisionUnit == null) {
			if (other.precisionUnit != null)
				return false;
		} else if (!precisionUnit.equals(other.precisionUnit))
			return false;
		return true;
	}


	

}
