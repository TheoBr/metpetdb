package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.Element;

public enum ChemicalAnalysisElementProperty implements Property<ChemicalAnalysisElement> {
	amount {
		public  Float get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object amount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setAmount((Float) amount);
		}
	},
	precision {
		public  Float get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getPrecision();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object precision) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public  String get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getPrecisionUnit();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object precisionUnit) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setPrecisionUnit((String) precisionUnit);
		}
	},
	minAmount {
		public  Float get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMinAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object minAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMinAmount((Float) minAmount);
		}
	},
	maxAmount {
		public  Float get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMaxAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object maxAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMaxAmount((Float) maxAmount);
		}
	},
	measurementUnit {
		public  String get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMeasurementUnit();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object measurementUnit) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMeasurementUnit((String) measurementUnit);
		}
	},
	element {
		public  Object get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getElement();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object element) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setElement((Element) element);
		}
	};
}
