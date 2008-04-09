package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.OxideDTO;

//TODO make this extend CollectionConstraint
public class OxideConstraint extends PropertyConstraint {
	private Collection<OxideDTO> oxides;

	public OxideConstraint() {

	}

	/**
	 * TODO make this validate that the value is in the collection Oxides
	 */
	public void validateValue(final OxideDTO value) throws ValidationException {
		super.validateValue(value);
	}

	public Collection<OxideDTO> getOxides() {
		return oxides;
	}

	public void setOxides(final List<OxideDTO> o) {
		oxides = o;
		// fixChildren(m);
	}
}
