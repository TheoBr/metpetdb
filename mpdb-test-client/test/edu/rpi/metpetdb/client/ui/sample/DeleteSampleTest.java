package edu.rpi.metpetdb.client.ui.sample;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.VoidTestServerOp;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.ui.MpDb;

public class DeleteSampleTest extends MpDbTestCase {

	private static final long SAMPLE_ID = 1;
	private static final long SAMPLE_FAIL_ID = 0;

	/**
	 * Test if a sample can be deleted
	 */
	public void testDeleteSample() {
		new VoidTestServerOp(this) {
			public void begin() {
				MpDb.sample_svc.delete(SAMPLE_ID, this);
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Try deleting a sample that does not exist
	 */
	public void testDeleteSampleFail() {
		new VoidTestServerOp(this) {
			public void begin() {
				MpDb.sample_svc.delete(SAMPLE_FAIL_ID, this);
			}
			public void onSuccess(final Object result) {
				fail("testDeleteSampleFail expected exception");
			}
			public void onFailure(final Throwable e) {
				if (e instanceof NoSuchObjectException) {
					finishTest();
				} else {
					fail("testDeleteSampleFail wrong exception recieved");
				}
			}
		}.begin();
		delayTestFinish(5000);
	}

	public String getTestName() {
		return "DeleteSampleTest";
	}

}
