package edu.rpi.metpetdb.client.ui.commands;


public abstract class VoidServerOp extends ServerOp<Void> {

	public void onSuccess(final Void result) {
		onSuccess();
	}
	
	public abstract void onSuccess();

}
