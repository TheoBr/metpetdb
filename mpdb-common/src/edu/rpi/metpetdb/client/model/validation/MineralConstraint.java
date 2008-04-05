package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralDTO;

/**
 * Constraint for minerals, since minerals are not added by users they are
 * fetched from the server when the ObjectConstraints are initialized. They are
 * stored in a collection.
 * 
 */

// TODO make this extend CollectionConstraint
public class MineralConstraint extends PropertyConstraint {
	private Collection<MineralDTO> minerals;

	public MineralConstraint() {

	}

	/**
	 * TODO make this validate that the value is in the collection minerals
	 */
	public void validateValue(final MineralDTO value)
			throws ValidationException {
		super.validateValue(value);
	}

	public Collection<MineralDTO> getMinerals() {
		return minerals;
	}

	public void setMinerals(final List<MineralDTO> m) {
		minerals = m;
	}

	public Collection<MineralDTO> getParents() {
		return minerals;
	}
}
