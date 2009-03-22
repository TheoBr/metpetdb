package edu.rpi.metpetdb.client.ui.commands;

import com.google.gwt.user.client.Command;

public abstract class MCommand<DataType> implements Command {

	public abstract void execute(DataType object);
	
	public void execute() {
		
	}
	
}
