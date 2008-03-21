package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;

public enum OxideProperty implements Property {
	oxidationState {
		public <T extends MObjectDTO> Short get(final T oxide) {
			return ((OxideDTO) oxide).getOxidationState();
		}

		public <T extends MObjectDTO, K> void set(final T oxide,
				final K oxidationState) {
			((OxideDTO) oxide).setOxidationState((Short) oxidationState);
		}
	},
	species {
		public <T extends MObjectDTO> String get(final T oxide) {
			return ((OxideDTO) oxide).getSpecies();
		}

		public <T extends MObjectDTO, K> void set(final T oxide, final K species) {
			((OxideDTO) oxide).setSpecies((String) species);
		}
	},
	weight {
		public <T extends MObjectDTO> Float get(final T oxide) {
			return ((OxideDTO) oxide).getWeight();
		}

		public <T extends MObjectDTO, K> void set(final T oxide, final K weight) {
			((OxideDTO) oxide).setWeight((Float) weight);
		}
	},
	cationsPerOxide {
		public <T extends MObjectDTO> Short get(final T oxide) {
			return ((OxideDTO) oxide).getCationsPerOxide();
		}

		public <T extends MObjectDTO, K> void set(final T oxide,
				final K cationsPerOxide) {
			((OxideDTO) oxide).setCationsPerOxide((Short) cationsPerOxide);
		}
	},
	conversionFactor {
		public <T extends MObjectDTO> Float get(final T oxide) {
			return ((OxideDTO) oxide).getConversionFactor();
		}

		public <T extends MObjectDTO, K> void set(final T oxide,
				final K conversionFactor) {
			((OxideDTO) oxide).setConversionFactor((Float) conversionFactor);
		}
	},
	mineralType {
		public <T extends MObjectDTO> String get(final T oxide) {
			return ((OxideDTO) oxide).getMineralType();
		}

		public <T extends MObjectDTO, K> void set(final T oxide, final K mineralType) {
			((OxideDTO) oxide).setMineralType((String) mineralType);
		}
	},
}
