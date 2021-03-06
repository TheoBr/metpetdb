package edu.rpi.metpetdb.client.ui.commands;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.ui.ErrorHandler;
import edu.rpi.metpetdb.client.ui.dialogs.LoginDialog;
import edu.rpi.metpetdb.client.ui.dialogs.MpDbExceptionDialog;

/**
 * An AsyncCallback implementation with some error handling and recovery.
 * <p>
 * This class provides some assistance for AsyncCallbacks by making the callback
 * restartable, and able to automatically detect and recover from some classes
 * of caught exceptions (such as {@link LoginRequiredException}).
 * </p>
 */
public abstract class ServerOp<T> implements AsyncCallback<T>, Command {
	/** Automatically invoke {@link #begin()}. */
	public void execute() {
		begin();
	}

	public void onFailure(final Throwable e) {
		if (e instanceof LoginRequiredException)
			new LoginDialog(this, ((LoginRequiredException) e).format()).show();
		else if (e instanceof MpDbException)
			new MpDbExceptionDialog((MpDbException) e, this).show();
		else
			ErrorHandler.dispatch(e);
	}

	/**
	 * Invoked when the user has decided to cancel this operation.
	 * <p>
	 * Not all ServerOps can be cancelled. Typically a cancel can occur if the
	 * server threw back {@link LoginRequiredException} and the UI shows the
	 * user {@link LoginDialog}. When this happens the user has an option to
	 * cancel (not login), at which point there is no way for this operation to
	 * continue.
	 * </p>
	 */
	public void cancel() {
	}

	/**
	 * Invoke the proper async service method with the necessary arguments.
	 * <p>
	 * Subclasses must implement this method to allow callers (such as {@link
	 * LoginDialog} to restart a previously failed server operation.
	 * </p>
	 */
	public abstract void begin();
}
