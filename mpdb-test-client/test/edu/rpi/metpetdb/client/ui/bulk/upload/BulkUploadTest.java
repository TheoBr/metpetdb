package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.Map;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.ui.MpDb;

public class BulkUploadTest extends MpDbTestCase {

	// public void setUp() {
	// super.setUp();
	// }

	public void testBulkUpload() {

		new TestServerOp<Map<Integer, ValidationException>>(this) {
			public void begin() {
				MpDb.bulkUpload_svc
						.saveSamplesFromSpreadsheet(
								"/home/arnold/workspace/mpdb-common/sample-data/easy_samples.xls",
								this);
			}

			public void onSuccess(final Map<Integer, ValidationException> s) {

				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

	public String getTestName() {
		return "BulkUploadTest";
	}
}
