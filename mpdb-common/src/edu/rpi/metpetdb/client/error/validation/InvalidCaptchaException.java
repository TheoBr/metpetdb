package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;


public class InvalidCaptchaException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String taken;

	public InvalidCaptchaException() {
	}
	
	public InvalidCaptchaException(final String v)
	{
		taken = v;
	}
	public InvalidCaptchaException(final PropertyConstraint p, final String v) {
		super(p);
		taken = v;
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidCaptcha();
	}
}
