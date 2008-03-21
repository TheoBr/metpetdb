package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralAnalysisElementDTO;

public enum MineralAnalysisElementProperty implements Property {
	amount {
		public <T extends MObjectDTO> Float get(final T mineralAnalysisElement) {
			return ((MineralAnalysisElementDTO) mineralAnalysisElement)
					.getAmount();
		}

		public <T extends MObjectDTO, K> void set(
				final T mineralAnalysisElement, final K amount) {
			((MineralAnalysisElementDTO) mineralAnalysisElement)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObjectDTO> Float get(final T mineralAnalysisElement) {
			return ((MineralAnalysisElementDTO) mineralAnalysisElement)
					.getPrecision();
		}

		public <T extends MObjectDTO, K> void set(
				final T mineralAnalysisElement, final K precision) {
			((MineralAnalysisElementDTO) mineralAnalysisElement)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObjectDTO> String get(final T mineralAnalysisElement) {
			return ((MineralAnalysisElementDTO) mineralAnalysisElement)
					.getPrecisionUnit();
		}

		public <T extends MObjectDTO, K> void set(
				final T mineralAnalysisElement, final K precisionUnit) {
			((MineralAnalysisElementDTO) mineralAnalysisElement)
					.setPrecisionUnit((String) precisionUnit);
		}
	};
}
