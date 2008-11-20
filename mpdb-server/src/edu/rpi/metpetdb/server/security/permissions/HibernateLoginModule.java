package edu.rpi.metpetdb.server.security.permissions;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

@Deprecated
public class HibernateLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
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
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			throw new LoginException(e.getMessage());
		}
		String username = nameCb.getName();
		String password = new String(passCb.getPassword());

	
		
		
		publicCredentials = new ArrayList<Object>();
		
		
		//TODO add credentials and principals for the subject
		
		

		return true; 
	}

	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

}
