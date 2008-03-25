package edu.rpi.metpetdb.client.model;

import java.util.Set;

import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class SearchSampleDTO extends SampleDTO implements IHasName {

	private Set<String> possibleRockTypes;

	public Set<String> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addRockType(final String rt) {
		possibleRockTypes.add(rt);
	}
}