package edu.rpi.metpetdb.client.ui.sample;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.error.dao.SampleNotFoundException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;

public class LoadSampleTest extends MpDbTestCase {

	private static final long SAMPLE_ID = 1;
	private static final long SAMPLE_FAIL_ID = 0;

	/**
	 * Test loading a sample by verifying the id's are the same
	 */
	public void testLoadSample() {
		new TestServerOp<SampleDTO>(this) {
			public void begin() {
				MpDb.sample_svc.details(SAMPLE_ID, this);
			}

			public void onSuccess(final SampleDTO sample) {
				assertEquals(SAMPLE_ID, sample.getId());
				finishTest();
			}
		}.begin();
		delayTestFinish(50000);
	}

	/**
	 * Test how the server responds when we give it a wrong id
	 */
	public void testLoadSampleFail() {
		new TestServerOp<SampleDTO>(this) {
			public void begin() {
				MpDb.sample_svc.details(SAMPLE_FAIL_ID, this);
			}

			public void onSuccess(final SampleDTO s) {
				fail("testLoadSampleFail failed, expected an exception");
			}

			public void onFailure(final Throwable e) {
				if (e instanceof SampleNotFoundException) {
					finishTest();
				} else {
					e.printStackTrace();
					fail("testLoadSampleFail failed, wrong exception");
				}
			}
		}.begin();
		delayTestFinish(5000);
	}

	public String getTestName() {
		return "LoadSampleTest";
	}

}
