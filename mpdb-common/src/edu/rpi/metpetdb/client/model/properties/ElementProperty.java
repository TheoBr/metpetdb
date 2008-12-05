package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.MineralType;

public enum ElementProperty implements Property<Element> {
	name {
		public String get(final Element element) {
			return ((Element) element).getName();
		}

		public void set(final Element element, final Object name) {
			((Element) element).setName((String) name);
		}
	},
	alternateName {
		public String get(final Element element) {
			return ((Element) element).getAlternateName();
		}

		public void set(final Element element, final Object alternateName) {
			((Element) element).setAlternateName((String) alternateName);
		}
	},
	symbol {
		public String get(final Element element) {
			return ((Element) element).getSymbol();
		}

		public void set(final Element element, final Object symbol) {
			((Element) element).setSymbol((String) symbol);
		}
	},
	atomicNumber {
		public Integer get(final Element element) {
			return ((Element) element).getAtomicNumber();
		}

		public void set(final Element element, final Object atomicNumber) {
			((Element) element).setAtomicNumber((Integer) atomicNumber);
		}
	},
	weight {
		public Float get(final Element element) {
			return ((Element) element).getWeight();
		}

		public void set(final Element element, final Object weight) {
			((Element) element).setWeight((Float) weight);
		}
	},
	mineralTypes {
		public Object get(final Element element) {
			return ((Element) element).getMineralTypes();
		}

		public void set(final Element element, final Object mineralTypes) {
			((Element) element)
					.setMineralTypes((Set<MineralType>) mineralTypes);
		}
	},
}
