package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;


public class MultiValuedStringConstraint extends StringConstraint {
	
	public void validateValue(final Object value) throws ValidationException {
		if (value instanceof Collection) {
			final Iterator itr = ((Collection) value).iterator();
			while(itr.hasNext()) {
				super.validateValue(itr.next().toString());
			}
		}
	}
	
}