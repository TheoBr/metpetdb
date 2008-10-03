package edu.rpi.metpetdb.client.ui;

public abstract class VoidServerOp extends ServerOp<Void> {

	public void onSuccess(final Void result) {
		onSuccess();
	}
	
	public abstract void onSuccess();

}
