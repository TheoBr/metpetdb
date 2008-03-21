package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;

public interface Property {

	abstract <T extends MObjectDTO> Object get(final T object);

	abstract <T extends MObjectDTO, K> void set(final T object, final K value);

	abstract int ordinal();

	abstract String name();
}
