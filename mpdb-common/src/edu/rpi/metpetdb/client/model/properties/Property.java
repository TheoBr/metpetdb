package edu.rpi.metpetdb.client.model.properties;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.interfaces.MObject;


public interface Property extends IsSerializable {

	abstract <T extends MObject> Object get(final T object);

	abstract <T extends MObject, K> void set(final T object, final K value);

	abstract int ordinal();

	abstract String name();
}
