package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.SampleMineral;

public enum SampleMineralProperty implements Property {
	amount {
		public <T extends MObject> Float get(final T sampleMineral) {
			return ((SampleMineral) sampleMineral).getAmount();
		}

		public <T extends MObject, K> void set(final T sampleMineral,
				final K amount) {
			((SampleMineral) sampleMineral).setAmount(Float
					.valueOf((String) amount));
		}
	},
	mineral {
		public <T extends MObject> Object get(final T sampleMineral) {
			return ((SampleMineral) sampleMineral).getMineral();
		}

		public <T extends MObject, K> void set(final T sampleMineral,
				final K mineral) {
			((SampleMineral) sampleMineral).setMineral((Mineral) mineral);
		}
	};
}
