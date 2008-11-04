package edu.rpi.metpetdb.server.dao.permissions;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class HibernateLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, ?> sharedState;
	private Map<String, ?> options;
	private Collection<Object> publicCredentials;
	private Collection<Principal> principals;
	private boolean success = true;

	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean commit() throws LoginException {

        if (success) {

            if (subject.isReadOnly()) {
                throw new LoginException ("Subject is Readonly");
            }

            try {
                subject.getPrincipals().addAll(principals);
                subject.getPublicCredentials().addAll(publicCredentials);

                principals.clear();
                publicCredentials.clear();

                if(callbackHandler instanceof GwtCallbackHandler)
                    ((GwtCallbackHandler)callbackHandler).clearInformation();

                return(true);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                throw new LoginException(ex.getMessage());
            }
        } else {
        	principals.clear();
            publicCredentials.clear();
            return(true);
        }
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	public boolean login() throws LoginException {
		if (callbackHandler == null)
			throw new LoginException("no handler");
		NameCallback nameCb = new NameCallback("user");
		PasswordCallback passCb = new PasswordCallback("password", true);
		Callback[] callbacks = new Callback[] {
				nameCb, passCb
		};
		try {
			callbackHandler.handle(callbacks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String username = nameCb.getName();
		String password = new String(passCb.getPassword());

		User u = new User();
		u.setEmailAddress(username);
		try {
			u = (new UserDAO(DataStore.open())).fill(u);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		principals = new ArrayList<Principal>();
		publicCredentials = new ArrayList<Object>();
		
		if (!PasswordEncrypter.verify(u.getEncryptedPassword(), password)) {
			success = false;
			throw new LoginException("invalid credentials");
		} else {
			publicCredentials.add(u);
			success = true;
		}

		return true; 
	}

	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

}
