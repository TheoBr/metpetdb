package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;

/**
 * Server operations to fetch and manipulate {@link User}s.
 */
public interface UserService extends RemoteService {
	/**
	 * Authenticate the user, beginning a new browser session.
	 * 
	 * @param ssr
	 * 		username and password to authenticate with.
	 * @return user record for the authenticated user, the record contains the
	 * 	basic attributes. Never null.
	 * @throws ValidationException
	 * 		input object is horribly misconfigured. It is likely one of the
	 * 		required fields was not populated.
	 * @throws LoginFailureException
	 * 		the username or password is invalid in some way. For security
	 * 		reasons the server does not actually respond with why.
	 */
	User startSession(StartSessionRequest ssr) throws LoginFailureException,
			ValidationException;

	/**
	 * Start a new (or restart a possibly existing) browser session.
	 * <p>
	 * Invoked by the client UI when a browser session is starting up. The
	 * response supplies the client with information about the current user (if
	 * any).
	 * </p>
	 * 
	 * @return user record for the currently logged in user. Never null.
	 */
	ResumeSessionResponse resumeSession();

	User beginEditMyProfile() throws MpDbException, LoginRequiredException;

	/**
	 * Register a new user account.
	 * 
	 * @param newbie
	 * 		the user to register. The user, new password and verify password
	 * 		fields must be populated. The old password field is ignored, as a
	 * 		new user cannot have an old password.
	 * @return the newly registered user instance, after corrections for default
	 * 	values have been applied on the server.
	 * @throws ValidationException
	 * 		one or more values within the new registration application are not
	 * 		valid.
	 * @throws MpDbException
	 * 		error saving user to the database
	 * @throws UnableToSendEmailException
	 * 		the welcome message could not be sent to the user's account.
	 */
	User registerNewUser(UserWithPassword newbie) throws ValidationException,
			MpDbException, UnableToSendEmailException;

	/**
	 * Change an existing user's password to a new (known) string.
	 * 
	 * @param uwp
	 * 		combination of the user to modify, that user's old password, the new
	 * 		password, and the new password again (to confirm it was entered
	 * 		correctly).
	 * @throws MpDbException
	 * 		user specified does not exist, or some other error retrieving the
	 * 		user from the db or saving the modified version to the db
	 * @throws LoginFailureException
	 * 		user's old password does not match.
	 * @throws LoginRequiredException
	 * 		user is not logged in. Logging in is required before the password
	 * 		can be changed.
	 * @throws ValidationException
	 * 		the old password was not supplied, the new password was not
	 * 		supplied, the new password does not meet our minimum password rules,
	 * 		or the verify password does not match the new password.
	 */
	void changePassword(UserWithPassword uwp) throws MpDbException,
			LoginFailureException, LoginRequiredException, ValidationException;

	/**
	 * Get the details about a particular user.
	 * 
	 * @param username
	 * 		unique username of the user to show the details of.
	 * @return the user. Never null.
	 * @throws MpDbException
	 * 		the user does not exist in the database. The caller has bad
	 * 		information and should reevaluate whatever source supplied it with
	 * 		this bad key.
	 */
	User details(String username) throws MpDbException;

	/**
	 * Generates a new password for the user and emails it.
	 * <p>
	 * The generated password is emailed to the address that is stored on file
	 * for the account. In an ideal world, only the true owner of this account
	 * would also have access to that email address.
	 * </p>
	 * 
	 * @param username
	 * 		username to change the password for.
	 * @throws MpDbException
	 * 		user does not exist in the database.
	 * @throws UnableToSendEmailException
	 * 		email system failed unexpectedly, and the server is unable to send a
	 * 		message to the user.
	 */
	void emailPassword(String username) throws MpDbException,
			UnableToSendEmailException;
	
	Set<String> allNames() throws MpDbException;
	
	User confirmUser(String confirmationCode) throws MpDbException,
			LoginRequiredException;
	
	User save(User user) throws MpDbException, ValidationException;
	
	void endSession();
	
	public void sendConfirmationCode(User u) throws ValidationException,
	MpDbException, UnableToSendEmailException;
}
