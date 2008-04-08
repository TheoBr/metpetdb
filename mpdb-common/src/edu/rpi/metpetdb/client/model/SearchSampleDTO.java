package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

public class SearchSampleDTO extends SampleDTO {

	private static final long serialVersionUID = 1L;
	private Set<String> possibleRockTypes = new HashSet<String>();

	public Set<String> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addPossibleRockType(final String rt) {
		possibleRockTypes.add(rt);
	}

	public void setPossibleRockTypes(final Set<String> rt) {
		possibleRockTypes = rt;
	}

}
