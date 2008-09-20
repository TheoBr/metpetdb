package edu.rpi.metpetdb.client.ui.sample;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.validation.InvalidSESARNumberException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;

/**
 * Contains all of the test for saving samples. Performed test include
 * <ul>
 * <li>Saving</li>
 * <li>Constrains are verified</li>
 * </ul>
 * 
 * @author anthony
 * 
 */
public class SaveSampleTest extends MpDbTestCase {

	private static final String SESAR_NUMBER = "000000000";
	private static final double LATITUDE = 1;
	private static final double LONGITUDE = 1;
	private static RockType ROCK_TYPE;
	private static final String ALIAS = String.valueOf(System
			.currentTimeMillis());
	private Sample sample;
	private static int ROTATING_SAMPLE_ID = 200;

	@Override
	public void gwtSetUp() {
		super.gwtSetUp();
		ROCK_TYPE = new RockType();
		ROCK_TYPE.setRockType("gentoo");

		sample = new Sample();
		sample.setSesarNumber(SESAR_NUMBER);
		sample.setLatitude(LATITUDE);
		sample.setLongitude(LONGITUDE);
		sample.setRockType(ROCK_TYPE);
		sample.setAlias(ALIAS);
		sample.setId(ROTATING_SAMPLE_ID++);
		sample.setOwner(this.getUser());
	}

	/**
	 * Test saving a sample that satisfies all of the constraints
	 */
	public void testSaveSample() {
		new TestServerOp<Sample>(this) {
			public void begin() {
				MpDb.sample_svc.save(sample, this);
			}

			public void onSuccess(final Sample s) {
				assertEquals(s, sample);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

	public void testSaveSampleFailSesarNumber() {
		sample.setSesarNumber("error1234");
		new TestServerOp<Sample>(this) {
			public void begin() {
				MpDb.sample_svc.save(sample, this);
			}

			public void onSuccess(final Sample s) {
				fail("testSaveSampleFailSesarNumber failed, expected an exception");
			}

			public void onFailure(final Throwable e) {
				if (e instanceof InvalidSESARNumberException) {
					finishTest();
				} else {
					fail("testSaveSampleFailSesarNumber failed, wrong exception");
				}
			}
		}.begin();
		sample.setSesarNumber(SESAR_NUMBER);
		delayTestFinish(10000);
	}

	/**
	 * Currently location is only required to be required, so right now we don't
	 * test lat/long bounds
	 */
	public void testSaveSampleFailLocation() {
		sample.setLocation(null);
		new TestServerOp<Sample>(this) {
			public void begin() {
				MpDb.sample_svc.save(sample, this);
			}

			public void onSuccess(final Sample s) {
				fail("testSaveSampleFailLatitude failed, expected an exception");
			}

			public void onFailure(final Throwable e) {
				if (e instanceof PropertyRequiredException) {
					finishTest();
				} else {
					e.printStackTrace();
					fail("testSaveSampleFailLatitude failed, wrong exception");
				}
			}
		}.begin();
		sample.setLatitude(LATITUDE);
		sample.setLongitude(LONGITUDE);
		delayTestFinish(10000);
	}

	public void testSaveSampleFailAlias() {
		sample.setAlias(null);
		new TestServerOp<Sample>(this) {
			public void begin() {
				MpDb.sample_svc.save(sample, this);
			}

			public void onSuccess(final Sample s) {
				fail("testSaveSampleFailAlias failed, expected an exception");
			}

			public void onFailure(final Throwable e) {
				if (e instanceof PropertyRequiredException) {
					finishTest();
				} else {
					e.printStackTrace();
					fail("testSaveSampleFailAlias failed, wrong exception");
				}
			}
		}.begin();
		sample.setAlias(ALIAS);
		delayTestFinish(10000);
	}

	public void testSaveSampleFailOwner() {
		sample.setOwner(null);
		new TestServerOp<Sample>(this) {
			public void begin() {
				MpDb.sample_svc.save(sample, this);
			}

			public void onSuccess(final Sample s) {
				fail("testSaveSampleFailOwner failed, expected an exception");
			}

			public void onFailure(final Throwable e) {
				if (e instanceof LoginRequiredException) {
					finishTest();
				} else {
					e.printStackTrace();
					fail("testSaveSampleFailOwner failed, wrong exception");
				}
			}
		}.begin();
		sample.setOwner(getUser());
		delayTestFinish(10000);
	}

	public String getTestName() {
		return "SaveSampleTest";
	}

}
