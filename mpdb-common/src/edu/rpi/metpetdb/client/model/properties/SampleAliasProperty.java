package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.SampleAlias;

public enum SampleAliasProperty implements Property<SampleAlias> {
	alias {
		public  Object get(final SampleAlias sa) {
			return sa.getAlias();
		}

		public  void set(final SampleAlias sa,
				final Object alias) {
			((SampleAlias) sa).setAlias((String)alias);
		}
	};
}
