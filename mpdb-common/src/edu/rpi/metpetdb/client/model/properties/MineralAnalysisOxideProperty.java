package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralAnalysisOxideDTO;

public enum MineralAnalysisOxideProperty implements Property {
	amount {
		public <T extends MObjectDTO> Float get(final T mineralAnalysisOxide) {
			return ((MineralAnalysisOxideDTO) mineralAnalysisOxide).getAmount();
		}

		public <T extends MObjectDTO, K> void set(final T mineralAnalysisOxide,
				final K amount) {
			((MineralAnalysisOxideDTO) mineralAnalysisOxide)
					.setAmount((Float) amount);
		}
	},
	precision {
		public <T extends MObjectDTO> Float get(final T mineralAnalysisOxide) {
			return ((MineralAnalysisOxideDTO) mineralAnalysisOxide)
					.getPrecision();
		}

		public <T extends MObjectDTO, K> void set(final T mineralAnalysisOxide,
				final K precision) {
			((MineralAnalysisOxideDTO) mineralAnalysisOxide)
					.setPrecision((Float) precision);
		}
	},
	precisionUnit {
		public <T extends MObjectDTO> String get(final T mineralAnalysisOxide) {
			return ((MineralAnalysisOxideDTO) mineralAnalysisOxide)
					.getPrecisionUnit();
		}

		public <T extends MObjectDTO, K> void set(final T mineralAnalysisOxide,
				final K precisionUnit) {
			((MineralAnalysisOxideDTO) mineralAnalysisOxide)
					.setPrecisionUnit((String) precisionUnit);
		}
	};

}
