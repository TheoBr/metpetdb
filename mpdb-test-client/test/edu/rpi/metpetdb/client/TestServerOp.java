package edu.rpi.metpetdb.client;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.ui.ServerOp;

public abstract class TestServerOp<T> extends ServerOp<T> {
	
	protected MpDbTestCase test;
	
	public TestServerOp(final MpDbTestCase test) {
		this.test = test;
	}
	
	public void onFailure(final Throwable e) {
		e.printStackTrace();
		GWTTestCase.fail("Error executing test:" + test.toString());
	}

}
