package edu.rpi.metpetdb.server.bulk.upload;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class MethodAssociation<T> {
	private Pattern pattern;
	private Method method;
	private String name;

	/**
	 * creates a MethodAssociation
	 * 
	 * @param regex
	 * 		the regular expression to match
	 * @param method
	 * 		the method name to execute
	 * @param param
	 * 		the class of the parameter to the method
	 * @param t
	 * 		an example of the class, upon which to reflect
	 * @param name
	 * 		human readable name of what this represents
	 */
	public MethodAssociation(final String regex, final String method,
			final Class<?> param, final T t, final String name)
			throws NoSuchMethodException {
		this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		this.method = t.getClass().getMethod(method, param);
		this.name = name;
	}

	public boolean matches(final String input) {
		return pattern.matcher(input).find();
	}

	public Method getMethod() {
		return method;
	}

	public String getName() {
		return name;
	}
}
