package edu.rpi.metpetdb.client;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.ui.ServerOp;

public abstract class TestServerOp<T> extends ServerOp<T> {

	protected MpDbTestCase test;

	public TestServerOp(final MpDbTestCase test) {
		this.test = test;
	}

	public void onFailure(final Throwable e) {
		e.printStackTrace();
		if (e instanceof ValidationException) {
			MpDbTestCase.fail("Error executing test:" + test.getName() + " "
					+ ((ValidationException) e).format());
		} else {
			MpDbTestCase.fail();
		}
		test.finish();
	}
}
