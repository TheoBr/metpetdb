package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;

/**
 * Constraint for minerals, since minerals are not added by users they are
 * fetched from the server when the ObjectConstraints are initialized. They are
 * stored in a collection.
 * 
 */

//TODO make this extend CollectionConstraint
public class MineralConstraint extends PropertyConstraint {
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.Mineral>
	 */
	private Collection minerals;

	public MineralConstraint() {

	}

	/**
	 * TODO make this validate that the value is in the collection minerals
	 */
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
	}

	public Collection getMinerals() {
		return minerals;
	}

	public void setMinerals(final Collection m) {
		minerals = m;
		fixChildren(m);
	}

	/**
	 * fixes persistent set/bag problems by forcing the children to be an
	 * arraylist
	 * 
	 * @param m
	 */
	public void fixChildren(final Collection m) {
		final Iterator itr = m.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			if (obj instanceof IHasChildren) {
				final IHasChildren parent = (IHasChildren) obj;
				if (parent.getChildren().size() > 0)
					fixChildren(parent.getChildren());
				parent.setChildren(new HashSet(parent.getChildren()));
			}
		}
	}
}
