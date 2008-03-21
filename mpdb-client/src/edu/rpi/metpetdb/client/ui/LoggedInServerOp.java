package edu.rpi.metpetdb.client.ui;

import com.google.gwt.user.client.History;

import edu.rpi.metpetdb.client.error.LoginRequiredException;

/**
 * Similar to a serverop but a user <b>must</b> be logged in for the call to
 * execute
 * 
 * @author anthony
 * 
 */
public abstract class LoggedInServerOp extends ServerOp<Object> {

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

	public void onSuccess(final Object result) {

	}

	public void cancel() {
		// Go back to revert the history token
		History.back();
	}

}
