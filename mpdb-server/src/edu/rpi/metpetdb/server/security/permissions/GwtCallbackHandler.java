package edu.rpi.metpetdb.server.security.permissions;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class GwtCallbackHandler implements CallbackHandler {

	private String username;
	private String password;

	public GwtCallbackHandler(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nameCb = (NameCallback) callbacks[i];
				nameCb.setName(username);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback passCb = (PasswordCallback) callbacks[i];
				passCb.setPassword(password.toCharArray());
			} else {
				throw (new UnsupportedCallbackException(callbacks[i],
						"Callback class not supported"));
			}
		}
	}

	public void clearInformation() {
		username = "";
		password = "";
	}
}
