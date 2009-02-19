package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Oxide;

public enum ChemicalAnalysisOxideProperty implements Property<ChemicalAnalysisOxide> {
	amount {
		public  Object get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide).getAmount();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object amount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setAmount(PropertyUtils.convertToDouble(amount));
		}
	},
	precision {
		public  Object get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getPrecision();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object precision) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setPrecision(PropertyUtils.convertToDouble(precision));
		}
	},
	precisionUnit {
		public  String get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getPrecisionUnit();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object precisionUnit) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setPrecisionUnit((String) precisionUnit);
		}
	},
	minAmount {
		public Object get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMinAmount();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object minAmount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMinAmount(PropertyUtils.convertToDouble(minAmount));
		}
	},
	maxAmount {
		public  Object get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMaxAmount();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object maxAmount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMaxAmount(PropertyUtils.convertToDouble(maxAmount));
		}
	},
	measurementUnit {
		public  String get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMeasurementUnit();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object measurementUnit) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMeasurementUnit((String) measurementUnit);
		}
	},
	oxide {
		public  Object get(final ChemicalAnalysisOxide chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide).getOxide();
		}

		public  void set(final ChemicalAnalysisOxide chemicalAnalysisOxide,
				final Object oxide) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setOxide((Oxide) oxide);
		}
	};

}
