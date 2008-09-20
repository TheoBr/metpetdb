package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.client.service.UserService;

/**
 * Response sent by {@link UserService#resumeSession()}.
 * <p>
 * The browser typically always runs a {@link UserService#resumeSession()} call
 * when the application loads. Such a call is designed to fetch a number of
 * application constants from the server, such as who the currently logged-in
 * user is (based on our {@link MpDbConstants#USERID_COOKIE} value). To minimize
 * the number of round-trips during startup we try to use a single call to the
 * server and pack a bunch of the data into an instance of this class.
 * </p>
 */
public class ResumeSessionResponse implements IsSerializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Logged in user; null if the user is not logged in.
	 * <p>
	 * If null the client should behave as though the user is not logged in.
	 * There are a number of reasons why this could occur, such as missing user
	 * record in the database, no cookie, invalid cookie, expired cookie, etc.
	 * The client is not informed as to why the user could not be obtained.
	 * </p>
	 */
	public User user;

	/**
	 * System-wide entity constraint information.
	 * <p>
	 * Never null. This attribute provides the system-wide singleton instance of
	 * {@link DatabaseObjectConstraints} and can be used by the client and the
	 * server to validate properties of objects.
	 * </p>
	 */
	public DatabaseObjectConstraints databaseObjectConstraints;
	public ObjectConstraints objectConstraints;
}
