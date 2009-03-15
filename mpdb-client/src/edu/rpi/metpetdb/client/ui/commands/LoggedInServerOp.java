package edu.rpi.metpetdb.client.ui.commands;

import com.google.gwt.user.client.History;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.ui.MpDb;

/**
 * Similar to a serverop but a user <b>must</b> be logged in for the command to
 * execute
 * 
 * @author anthony
 * 
 */
public abstract class LoggedInServerOp<T> extends ServerOp<T> {

	public void begin() {
		if (MpDb.isLoggedIn()) {
			command();
		} else
			onFailure(new LoginRequiredException());
	}

	/**
	 * command to execute if the user is logged in
	 */
	public abstract void command();

	public void onSuccess(final T result) {

	}

	public void cancel() {
		// Go back to revert the history token
		History.back();
	}

}
