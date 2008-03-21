package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ElementProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T element) {
			return ((ElementDTO) element).getName();
		}

		public <T extends MObjectDTO, K> void set(final T element, final K name) {
			((ElementDTO) element).setName((String) name);
		}
	},
	alternateName {
		public <T extends MObjectDTO> String get(final T element) {
			return ((ElementDTO) element).getAlternateName();
		}

		public <T extends MObjectDTO, K> void set(final T element,
				final K alternateName) {
			((ElementDTO) element).setAlternateName((String) alternateName);
		}
	},
	symbol {
		public <T extends MObjectDTO> String get(final T element) {
			return ((ElementDTO) element).getSymbol();
		}

		public <T extends MObjectDTO, K> void set(final T element,
				final K symbol) {
			((ElementDTO) element).setSymbol((String) symbol);
		}
	},
	atomicNumber {
		public <T extends MObjectDTO> Integer get(final T element) {
			return ((ElementDTO) element).getAtomicNumber();
		}

		public <T extends MObjectDTO, K> void set(final T element,
				final K atomicNumber) {
			((ElementDTO) element).setAtomicNumber((Integer) atomicNumber);
		}
	},
	weight {
		public <T extends MObjectDTO> Float get(final T element) {
			return ((ElementDTO) element).getWeight();
		}

		public <T extends MObjectDTO, K> void set(final T element,
				final K weight) {
			((ElementDTO) element).setWeight((Float) weight);
		}
	},
	mineralType {
		public <T extends MObjectDTO> String get(final T element) {
			return ((ElementDTO) element).getMineralType();
		}

		public <T extends MObjectDTO, K> void set(final T element,
				final K mineralType) {
			((ElementDTO) element).setMineralType((String) mineralType);
		}
	},
}
