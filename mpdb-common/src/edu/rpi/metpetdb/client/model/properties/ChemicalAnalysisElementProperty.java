package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum ChemicalAnalysisElementProperty implements Property {
	amount {
		public <T extends MObject> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K amount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObject> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getPrecision();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K precision) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObject> String get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getPrecisionUnit();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K precisionUnit) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setPrecisionUnit((String) precisionUnit);
		}
	},
	minAmount {
		public <T extends MObject> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMinAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K minAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMinAmount((Float) minAmount);
		}
	},
	maxAmount {
		public <T extends MObject> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMaxAmount();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K maxAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMaxAmount((Float) maxAmount);
		}
	},
	measurementUnit {
		public <T extends MObject> String get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMeasurementUnit();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K measurementUnit) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMeasurementUnit((String) measurementUnit);
		}
	},
	element {
		public <T extends MObject> Object get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getElement();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysisElement,
				final K element) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setElement((Element) element);
		}
	};
}
