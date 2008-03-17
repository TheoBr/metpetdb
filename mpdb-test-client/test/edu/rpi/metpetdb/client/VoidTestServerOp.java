package edu.rpi.metpetdb.client;


public abstract class VoidTestServerOp extends TestServerOp<Object> {
	
	public VoidTestServerOp(final MpDbTestCase test) {
		super(test);
	}
	
	public void onSuccess(final Object result) {
		test.finish();
	}

}
