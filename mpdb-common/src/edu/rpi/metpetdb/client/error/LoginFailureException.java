package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

public class LoginFailureException extends ValidationException
		implements
			IsSerializable {
	private static final long serialVersionUID = 1L;

	public LoginFailureException() {
	}
	public LoginFailureException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidLogin();
	}
}
