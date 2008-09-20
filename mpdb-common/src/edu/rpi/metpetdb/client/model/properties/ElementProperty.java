package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.MineralType;

public enum ElementProperty implements Property {
	name {
		public <T extends MObject> String get(final T element) {
			return ((Element) element).getName();
		}

		public <T extends MObject, K> void set(final T element, final K name) {
			((Element) element).setName((String) name);
		}
	},
	alternateName {
		public <T extends MObject> String get(final T element) {
			return ((Element) element).getAlternateName();
		}

		public <T extends MObject, K> void set(final T element,
				final K alternateName) {
			((Element) element).setAlternateName((String) alternateName);
		}
	},
	symbol {
		public <T extends MObject> String get(final T element) {
			return ((Element) element).getSymbol();
		}

		public <T extends MObject, K> void set(final T element, final K symbol) {
			((Element) element).setSymbol((String) symbol);
		}
	},
	atomicNumber {
		public <T extends MObject> Integer get(final T element) {
			return ((Element) element).getAtomicNumber();
		}

		public <T extends MObject, K> void set(final T element,
				final K atomicNumber) {
			((Element) element).setAtomicNumber((Integer) atomicNumber);
		}
	},
	weight {
		public <T extends MObject> Float get(final T element) {
			return ((Element) element).getWeight();
		}

		public <T extends MObject, K> void set(final T element, final K weight) {
			((Element) element).setWeight((Float) weight);
		}
	},
	mineralTypes {
		public <T extends MObject> Object get(final T element) {
			return ((Element) element).getMineralTypes();
		}

		public <T extends MObject, K> void set(final T element,
				final K mineralTypes) {
			((Element) element)
					.setMineralTypes((Set<MineralType>) mineralTypes);
		}
	},
}
