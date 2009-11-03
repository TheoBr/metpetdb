package edu.rpi.metpetdb.client.ui.bulk.upload;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.ui.MpDb;

public class BulkUploadTest extends MpDbTestCase {

	// public void setUp() {
	// super.setUp();
	// }

	public void testBulkUpload() {

		new TestServerOp<BulkUploadResult>(this) {
			public void begin() {
				MpDb.bulkUploadSamples_svc
						.parser(
								"/home/arnold/workspace/mpdb-common/sample-data/easy_samples.xls",
								false, this);
			}

			public void onSuccess(final BulkUploadResult result) {

				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

	public String getTestName() {
		return "BulkUploadTest";
	}
}
