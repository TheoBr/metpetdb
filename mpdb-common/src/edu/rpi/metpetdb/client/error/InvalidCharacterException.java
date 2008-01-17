package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.RestrictedCharacterStringConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

/** Indicates the value for a property is malformed. */
public class InvalidCharacterException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private char bad;

	public InvalidCharacterException() {
	}
	public InvalidCharacterException(
			final RestrictedCharacterStringConstraint c, final char badSubstring) {
		super(c);
		bad = badSubstring;
	}

	protected String formatPattern() {
		final String pattern = ((RestrictedCharacterStringConstraint) constraint).pattern;
		final StringBuffer r = new StringBuffer();
		for (int k = 0; k < pattern.length(); k++) {
			switch (pattern.charAt(k)) {
				case '\\' :
					if (r.length() > 0)
						r.append(", ");
					r.append(formatCharacter(pattern.charAt(++k)));
					break;
				case '-' :
					r.append('-');
					r.append(formatCharacter(pattern.charAt(++k)));
					break;
				default :
					if (r.length() > 0)
						r.append(", ");
					r.append(formatCharacter(pattern.charAt(k)));
					break;
			}
		}
		return r.toString();
	}
	protected static String formatCharacter(final char in) {
		switch (in) {
			case ' ' :
				return LocaleHandler.lc_text.character_space();
			case ',' :
				return LocaleHandler.lc_text.character_comma();
			case '.' :
				return LocaleHandler.lc_text.character_period();
			case '\'' :
				return LocaleHandler.lc_text.character_singleQuote();
			case '"' :
				return LocaleHandler.lc_text.character_doubleQuote();
			case '_' :
				return LocaleHandler.lc_text.character_underscore();
			case '`' :
				return LocaleHandler.lc_text.character_backtick();
		}
		return String.valueOf(in);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidCharacter(formatPropertyName(),
				formatPattern(), formatCharacter(bad));
	}
}
