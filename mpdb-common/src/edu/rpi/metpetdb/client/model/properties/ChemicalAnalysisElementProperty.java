package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ChemicalAnalysisElementProperty implements Property {
	amount {
		public <T extends MObjectDTO> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.getAmount();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisElement, final K amount) {
			((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObjectDTO> Float get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.getPrecision();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisElement, final K precision) {
			((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObjectDTO> String get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.getPrecisionUnit();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisElement, final K precisionUnit) {
			((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.setPrecisionUnit((String) precisionUnit);
		}
	},
	element {
		public <T extends MObjectDTO> Object get(final T chemicalAnalysisElement) {
			return ((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.getElement();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisElement, final K element) {
			((ChemicalAnalysisElementDTO) chemicalAnalysisElement)
					.setElement((ElementDTO) element);
		}
	};
}
