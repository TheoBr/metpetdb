package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;

public enum SampleMineralProperty implements Property {
	amount {
		public <T extends MObjectDTO> Float get(final T sampleMineral) {
			return ((SampleMineralDTO) sampleMineral).getAmount();
		}

		public <T extends MObjectDTO, K> void set(final T sampleMineral,
				final K amount) {
			((SampleMineralDTO) sampleMineral).setAmount((Float) amount);
		}
	},
}
