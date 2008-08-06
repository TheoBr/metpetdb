package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.ValueWrongTypeException;

/**
 * Exact same as a rock type constraint except that it allows many rock types to
 * be chose as opposed to one
 * 
 * @author anthony
 * 
 */
public class MultipleRockTypeConstraint extends RockTypeConstraint {

	@Override
	public void validateValue(final Object value) throws ValidationException {
		/* Value should be a collection */
		if (value instanceof Collection) {
			final Iterator<?> itr = ((Collection<?>) value).iterator();
			while (itr.hasNext())
				super.validateValue(itr.next());
		} else {
			throw new ValueWrongTypeException(this);
		}
	}

}
