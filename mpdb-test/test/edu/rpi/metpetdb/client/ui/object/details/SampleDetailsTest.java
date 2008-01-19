package edu.rpi.metpetdb.client.ui.object.details;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class SampleDetailsTest extends GWTTestCase {

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}
	
	public void testLoadSample() {
		new ServerOp() {
			public void begin() {
				MpDb.sample_svc.details(1,this);
			}
			public void onSuccess(final Object result) {
				final Sample s = (Sample) result;
				assertTrue(s.getId() == 1);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

}
