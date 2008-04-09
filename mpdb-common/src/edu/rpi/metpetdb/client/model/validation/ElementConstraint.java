package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ElementDTO;

//TODO make this extend CollectionConstraint
public class ElementConstraint extends PropertyConstraint {
	private Collection<ElementDTO> elements;

	public ElementConstraint() {

	}

	/**
	 * TODO make this validate that the value is in the collection elements
	 */
	public void validateValue(final ElementDTO value)
			throws ValidationException {
		super.validateValue(value);
	}

	public Collection<ElementDTO> getElement() {
		return elements;
	}

	public void setElements(final List<ElementDTO> e) {
		elements = e;
		// fixChildren(m);
	}
}
