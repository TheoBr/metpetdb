package edu.rpi.metpetdb.client.ui.user;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.UserDTO;

/**
 * Marker interface for widgets that care about who the current user is.
 * <p>
 * Widgets should implement this interface if their display is affected by
 * {@link LocaleHandler#currentUser()}, or {@link LocaleHandler#isCurrentUser(UserDTO)}, or
 * {@link LocaleHandler#isLoggedIn()}.
 * </p>
 */
public interface UsesCurrentUser {
	/**
	 * Invoked when the current user is modified.
	 * 
	 * @param whoIsIt
	 *            the new current user; null if there is no current user (they
	 *            have logged out).
	 * @throws LoginRequiredException
	 *             the receiver demands that there be a current user, but the
	 *             input argument was null. Notification events will stop and
	 *             the application will redirect to the introduction page, or
	 *             something otherwise suitable for a not-logged-in user.
	 */
	public void onCurrentUserChanged(UserDTO whoIsIt)
			throws LoginRequiredException;
}
