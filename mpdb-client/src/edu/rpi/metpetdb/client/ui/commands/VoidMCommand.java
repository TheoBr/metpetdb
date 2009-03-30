package edu.rpi.metpetdb.client.ui.commands;

public abstract class VoidMCommand extends MCommand<Void> {

	public void execute(Void obj) {
		
	}
	
	public abstract void execute();
}
