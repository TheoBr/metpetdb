package edu.rpi.metpetdb.client.ui.object.details;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class SampleDetailsTest extends GWTTestCase {
	
	private User owner;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void setUp() {
		owner = new User();
		owner.setId(1);
	}

	public void testCreateSample() {
		final Sample s = new Sample();
		s.setSesarNumber("123456789");
		s.setLatitude(1);
		s.setLongitude(1);
		s.setRockType("rockie rock");
		s.setOwner(owner);
		new ServerOp() {
			public void begin() {
				MpDb.sample_svc.saveSample(s, this);
			}

			public void onSuccess(final Object result) {
				assertEquals(result, s);
				finishTest();
			}

			public void onFailure(final Throwable e) {
				e.printStackTrace();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test loading a sample by verifying the id's are the same
	 */
	public void testLoadSample() {
		new ServerOp() {
			public void begin() {
				//MpDb.sample_svc.details(sampleId, this);
			}

			public void onSuccess(final Object result) {
				//final Sample s = (Sample) result;
				//assertTrue(s.getId() == sampleId);
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test if a sample can be deleted by first deleting the sample and then
	 * attempting to load the sample again
	 */
	public void testDeleteSample() {
		new ServerOp() {
			public void begin() {
				//MpDb.sample_svc.delete(sampleId, this);
			}

			public void onSuccess(final Object result) {
				new ServerOp() {
					public void begin() {
						///MpDb.sample_svc.details(sampleId, this);
					}

					public void onSuccess(final Object result) {
						fail("Sample not deleted");
					}
					
					public void onFailure(final Throwable e) {
						assertTrue(true);
						finishTest();
					}
				}.begin();
				delayTestFinish(5000);
			}
		}.begin();
		delayTestFinish(5000);
	}

}
