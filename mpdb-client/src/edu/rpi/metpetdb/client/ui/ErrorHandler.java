package edu.rpi.metpetdb.client.ui;

import com.google.gwt.core.client.GWT;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.ui.dialogs.NoObjectDialog;
import edu.rpi.metpetdb.client.ui.dialogs.UnknownErrorDialog;

public class ErrorHandler implements GWT.UncaughtExceptionHandler {
	static final ErrorHandler INSTANCE = new ErrorHandler();

	public static void dispatch(final Throwable caught) {
		INSTANCE.onUncaughtException(caught);
	}

	public void onUncaughtException(final Throwable caught) {
		GWT.log("Unexpected error: " + caught, caught);

		if (caught instanceof NoSuchObjectException)
			new NoObjectDialog((NoSuchObjectException) caught).show();
		else
			new UnknownErrorDialog(caught).show();
	}

	private ErrorHandler() {
	}
}
