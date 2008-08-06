package edu.rpi.metpetdb.client.error;

public abstract class MpDbException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Format this error for display to the end-user.
	 * <p>
	 * <b><i>Only available on the client side.</i></b>
	 * </p>
	 * <p>
	 * This method requires the localication support supplied by GWT, and that
	 * is only available on the client side of the system. Invoking this method
	 * on the server will throw an internal GWT error.
	 * </p>
	 * 
	 * @return the formatted string describing this error.
	 */
	abstract public String format();
}
