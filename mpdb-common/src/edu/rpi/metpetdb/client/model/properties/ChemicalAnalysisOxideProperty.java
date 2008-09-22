package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Oxide;

public enum ChemicalAnalysisOxideProperty implements Property {
	amount {
		public <T extends MObject> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide).getAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K amount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObject> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getPrecision();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K precision) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObject> String get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getPrecisionUnit();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K precisionUnit) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setPrecisionUnit((String) precisionUnit);
		}
	},
	minAmount {
		public <T extends MObject> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMinAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K minAmount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMinAmount((Float) minAmount);
		}
	},
	maxAmount {
		public <T extends MObject> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMaxAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K maxAmount) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMaxAmount((Float) maxAmount);
		}
	},
	measurementUnit {
		public <T extends MObject> String get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.getMeasurementUnit();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K measurementUnit) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setMeasurementUnit((String) measurementUnit);
		}
	},
	oxide {
		public <T extends MObject> Object get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxide) chemicalAnalysisOxide).getOxide();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisOxide,
				final K oxide) {
			((ChemicalAnalysisOxide) chemicalAnalysisOxide)
					.setOxide((Oxide) oxide);
		}
	};

}
