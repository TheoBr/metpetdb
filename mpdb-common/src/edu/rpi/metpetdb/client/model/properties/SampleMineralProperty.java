package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.SampleMineral;

public enum SampleMineralProperty implements Property<SampleMineral> {
	amount {
		public String get(final SampleMineral sampleMineral) {
			return ((SampleMineral) sampleMineral).getAmount();
		}

		public void set(final SampleMineral sampleMineral, final Object amount) {
			((SampleMineral) sampleMineral).setAmount((String) amount);
		}
	},
	mineral {
		public Object get(final SampleMineral sampleMineral) {
			return ((SampleMineral) sampleMineral).getMineral();
		}

		public void set(final SampleMineral sampleMineral, final Object mineral) {
			((SampleMineral) sampleMineral).setMineral((Mineral) mineral);
		}
	};
}
