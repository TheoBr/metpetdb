package edu.rpi.metpetdb.client.model.properties;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.interfaces.MObject;

public interface Property<T extends MObject> extends IsSerializable {

	abstract Object get(final T object);

	abstract void set(final T object, final Object value);

	abstract int ordinal();

	abstract String name();
}
