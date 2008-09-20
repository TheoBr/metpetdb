package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.MineralType;
import edu.rpi.metpetdb.client.model.Oxide;

public enum OxideProperty implements Property {
	oxidationState {
		public <T extends MObject> Short get(final T oxide) {
			return ((Oxide) oxide).getOxidationState();
		}

		public <T extends MObject, K> void set(final T oxide,
				final K oxidationState) {
			((Oxide) oxide).setOxidationState((Short) oxidationState);
		}
	},
	species {
		public <T extends MObject> String get(final T oxide) {
			return ((Oxide) oxide).getSpecies();
		}

		public <T extends MObject, K> void set(final T oxide, final K species) {
			((Oxide) oxide).setSpecies((String) species);
		}
	},
	weight {
		public <T extends MObject> Float get(final T oxide) {
			return ((Oxide) oxide).getWeight();
		}

		public <T extends MObject, K> void set(final T oxide, final K weight) {
			((Oxide) oxide).setWeight((Float) weight);
		}
	},
	cationsPerOxide {
		public <T extends MObject> Short get(final T oxide) {
			return ((Oxide) oxide).getCationsPerOxide();
		}

		public <T extends MObject, K> void set(final T oxide,
				final K cationsPerOxide) {
			((Oxide) oxide).setCationsPerOxide((Short) cationsPerOxide);
		}
	},
	conversionFactor {
		public <T extends MObject> Float get(final T oxide) {
			return ((Oxide) oxide).getConversionFactor();
		}

		public <T extends MObject, K> void set(final T oxide,
				final K conversionFactor) {
			((Oxide) oxide).setConversionFactor((Float) conversionFactor);
		}
	},
	mineralTypes {
		public <T extends MObject> Object get(final T oxide) {
			return ((Oxide) oxide).getMineralTypes();
		}

		public <T extends MObject, K> void set(final T oxide,
				final K mineralTypes) {
			((Oxide) oxide).setMineralTypes((Set<MineralType>) mineralTypes);
		}
	},
}
