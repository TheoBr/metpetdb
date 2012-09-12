package edu.rpi.metpetdb.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chemical_analysis_oxides")
public class ChemicalAnalysisOxide {
	@Embeddable
	public static class Pk implements Serializable {
		@Column(name = "chemical_analysis_id", nullable = false, updatable = false)
		private Long chemical_analysis_id;

		@Column(name = "oxide_id", nullable = false, updatable = false)
		private Long oxide_id;

		public Pk() {
			super();
		}

		public Pk(Long chemical_analysis_id, Long oxide_id) {
			this.chemical_analysis_id = chemical_analysis_id;
			this.oxide_id = oxide_id;
		}

		public Long getChemical_analysis_id() {
			return chemical_analysis_id;
		}

		public void setChemical_analysis_id(Long chemicalAnalysisId) {
			chemical_analysis_id = chemicalAnalysisId;
		}

		public Long getOxide_Id() {
			return oxide_id;
		}

		public void setOxide_id(Long oxideId) {
			oxide_id = oxideId;
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
					+ ((oxide_id == null) ? 0 : oxide_id.hashCode());
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
			if (oxide_id == null) {
				if (other.oxide_id != null)
					return false;
			} else if (!oxide_id.equals(other.oxide_id))
				return false;
			return true;
		}

	}

	@EmbeddedId
	private Pk pk;

	public ChemicalAnalysisOxide() {
		super();
	}

	public ChemicalAnalysisOxide(Double amount, Double precision,
			String precisionUnit, Double minAmount, Double maxAmount,
			String measurementUnit) {
		super();

		this.amount = amount;
		this.precision = precision;
		this.precisionUnit = precisionUnit;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.measurementUnit = measurementUnit;
	}

	/**
	 * ( chemical_analysis_id bigint NOT NULL, oxide_id smallint NOT NULL,
	 * amount double precision NOT NULL, "precision" double precision,
	 * precision_type character varying(3), measurement_unit character
	 * varying(4), min_amount double precision, max_amount double precision,
	 */

	@ManyToOne(targetEntity = edu.rpi.metpetdb.server.model.Oxide.class)
	@JoinColumn(name = "oxide_id", insertable = false, updatable = false)
	private Oxide oxide;

	@ManyToOne(targetEntity = edu.rpi.metpetdb.server.model.ChemicalAnalysis.class)
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

	public Oxide getOxide() {
		return oxide;
	}

	public void setOxide(Oxide oxide) {
		this.oxide = oxide;
	}

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

	public Double getMaxAmount() {
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = edu.rpi.metpetdb.server.model.ChemicalAnalysis.class)
	@JoinColumn(name = "chemical_analysis_id")
	public ChemicalAnalysis getChemicalAnalysis() {
		return chemicalAnalysis;
	}

	public void setChemicalAnalysis(ChemicalAnalysis chemicalAnalysis) {
		this.chemicalAnalysis = chemicalAnalysis;
	}

	public Pk getPk() {
		return pk;
	}

	public void setPk(Pk pk) {
		this.pk = pk;
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
		ChemicalAnalysisOxide other = (ChemicalAnalysisOxide) obj;
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
