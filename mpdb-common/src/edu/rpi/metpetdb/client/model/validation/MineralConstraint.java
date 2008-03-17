package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;

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
		//fixChildren(m);
	}

	/**
	 * fixes persistent set/bag problems by forcing the children to be an
	 * arraylist
	 * 
	 * @param m
	 */
	@Deprecated
	public <T extends IHasChildren> void fixChildren(final Collection<T> m) {
		final Iterator<T> itr = m.iterator();
		while (itr.hasNext()) {
			final T parent = itr.next();
			if (parent.getChildren() != null && parent.getChildren().size() > 0)
				fixChildren(parent.getChildren());
			parent.setChildren(new HashSet<IHasChildren>(parent.getChildren()));
		}
	}
}
