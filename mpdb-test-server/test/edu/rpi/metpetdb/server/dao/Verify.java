package edu.rpi.metpetdb.server.dao;

import static org.junit.Assert.assertEquals;

import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.Sample;

public class Verify {
	
	public static void verifyEqual(final Sample s1, final Sample s2) {
		assertEquals(s1.getNumber(), s2.getNumber());
		assertEquals(s1.getAnalysisCount(), s2.getAnalysisCount());
		assertEquals(s1.getCollectionDate(), s2.getCollectionDate());
		assertEquals(s1.getCollector(), s2.getCollector());
		assertEquals(s1.getCountry(), s2.getCountry());
		assertEquals(s1.getDescription(), s2.getDescription());
		assertEquals(s1.getId(), s2.getId());
		assertEquals(s1.getImageCount(), s2.getImageCount());
		assertEquals(s1.getLocation(), s2.getLocation());
		assertEquals(s1.getLocationError(), s2.getLocationError());
		assertEquals(s1.getLocationText(), s2.getLocationText());
		assertEquals(s1.getRockType(), s2.getRockType());
		assertEquals(s1.getSesarNumber(), s2.getSesarNumber());
		assertEquals(s1.getRegions(), s2.getRegions());
	}
	
	public static void verifyEqual(final Region r1, final Region r2) {
		assertEquals(r1.getName(), r2.getName());
	}

}
