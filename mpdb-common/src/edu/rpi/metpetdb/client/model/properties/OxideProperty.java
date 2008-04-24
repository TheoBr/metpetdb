package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralTypeDTO;
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
	mineralTypes {
		public <T extends MObjectDTO> Object get(final T oxide) {
			return ((OxideDTO) oxide).getMineralTypes();
		}

		public <T extends MObjectDTO, K> void set(final T oxide,
				final K mineralTypes) {
			((OxideDTO) oxide)
					.setMineralTypes((Set<MineralTypeDTO>) mineralTypes);
		}
	},
}
