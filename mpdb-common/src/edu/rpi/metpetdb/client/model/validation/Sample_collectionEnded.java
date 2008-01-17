package edu.rpi.metpetdb.client.model.validation;

import java.sql.Timestamp;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.ValueNotAfterException;
import edu.rpi.metpetdb.client.model.MObject;

public class Sample_collectionEnded extends TimestampConstraint {
	TimestampConstraint collectionBegan;

	public void validateEntity(final MObject obj) throws ValidationException {
		super.validateEntity(obj);
		final Timestamp b = (Timestamp) obj.mGet(collectionBegan.propertyId);
		final Timestamp e = (Timestamp) obj.mGet(propertyId);
		if (b == e)
			return;
		if (b == null || e == null || e.getTime() < b.getTime())
			throw new ValueNotAfterException(this, collectionBegan);
	}
}
