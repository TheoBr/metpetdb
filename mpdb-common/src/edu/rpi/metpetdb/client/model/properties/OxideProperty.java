package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.MineralType;
import edu.rpi.metpetdb.client.model.Oxide;

public enum OxideProperty implements Property<Oxide> {
	oxidationState {
		public  Short get(final Oxide oxide) {
			return ((Oxide) oxide).getOxidationState();
		}

		public  void set(final Oxide oxide,
				final Object oxidationState) {
			((Oxide) oxide).setOxidationState((Short) oxidationState);
		}
	},
	species {
		public  String get(final Oxide oxide) {
			return ((Oxide) oxide).getSpecies();
		}

		public  void set(final Oxide oxide, final Object species) {
			((Oxide) oxide).setSpecies((String) species);
		}
	},
	weight {
		public  Float get(final Oxide oxide) {
			return ((Oxide) oxide).getWeight();
		}

		public  void set(final Oxide oxide, final Object weight) {
			((Oxide) oxide).setWeight((Float) weight);
		}
	},
	cationsPerOxide {
		public  Short get(final Oxide oxide) {
			return ((Oxide) oxide).getCationsPerOxide();
		}

		public  void set(final Oxide oxide,
				final Object cationsPerOxide) {
			((Oxide) oxide).setCationsPerOxide((Short) cationsPerOxide);
		}
	},
	conversionFactor {
		public  Float get(final Oxide oxide) {
			return ((Oxide) oxide).getConversionFactor();
		}

		public  void set(final Oxide oxide,
				final Object conversionFactor) {
			((Oxide) oxide).setConversionFactor((Float) conversionFactor);
		}
	},
	mineralTypes {
		public  Object get(final Oxide oxide) {
			return ((Oxide) oxide).getMineralTypes();
		}

		public  void set(final Oxide oxide,
				final Object mineralTypes) {
			((Oxide) oxide).setMineralTypes((Set<MineralType>) mineralTypes);
		}
	},
}
