package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ChemicalAnalysisOxideProperty implements Property {
	amount {
		public <T extends MObjectDTO> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.getAmount();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisOxide, final K amount) {
			((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObjectDTO> Float get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.getPrecision();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisOxide, final K precision) {
			((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObjectDTO> String get(final T chemicalAnalysisOxide) {
			return ((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.getPrecisionUnit();
		}

		public <T extends MObjectDTO, K> void set(
				final T chemicalAnalysisOxide, final K precisionUnit) {
			((ChemicalAnalysisOxideDTO) chemicalAnalysisOxide)
					.setPrecisionUnit((String) precisionUnit);
		}
	};

}
