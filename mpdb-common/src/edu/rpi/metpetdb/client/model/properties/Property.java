package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.SampleDTO;

public interface Property {

	abstract Object get(final SampleDTO sample);

	abstract <T> void set(final SampleDTO sample, final T value);

	abstract int ordinal();

	abstract String name();
}
