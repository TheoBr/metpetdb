package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.Element;

public enum ChemicalAnalysisElementProperty implements Property<ChemicalAnalysisElement> {
	amount {
		public  Object get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object amount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setAmount(PropertyUtils.convertToDouble(amount));
		}
	},
	precision {
		public  Object get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getPrecision();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object precision) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setPrecision(PropertyUtils.convertToDouble(precision));
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
		public  Object get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMinAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object minAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMinAmount(PropertyUtils.convertToDouble(minAmount));
		}
	},
	maxAmount {
		public  Object get(final ChemicalAnalysisElement chemicalAnalysisElement) {
			return ((ChemicalAnalysisElement) chemicalAnalysisElement)
					.getMaxAmount();
		}

		public  void set(final ChemicalAnalysisElement chemicalAnalysisElement,
				final Object maxAmount) {
			((ChemicalAnalysisElement) chemicalAnalysisElement)
					.setMaxAmount(PropertyUtils.convertToDouble(maxAmount));
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
