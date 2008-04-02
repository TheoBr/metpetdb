package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class MethodAssociation<T> {
	private Pattern pattern;
	private Method method;

	public MethodAssociation(final String regex, final String method,
			final Class param) throws NoSuchMethodException {
		this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		this.method = this.getClass().getMethod(method, param);
	}

	public boolean matches(final String input) {
		return pattern.matcher(input).find();
	}

	public Method getMethod() {
		return method;
	}
}
