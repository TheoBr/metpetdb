package edu.rpi.metpetdb.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;

import edu.rpi.metpetdb.client.locale.LocaleHandler;

public abstract class TokenHandler {
	static final char sep = LocaleHandler.lc_text.tokenSeparater().charAt(0);
	protected final String name;

	protected TokenHandler(final String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

	public abstract String makeToken(Object obj);
	public abstract void executeToken(String args);

	public static class NoOp extends TokenHandler {
		public NoOp(final String n) {
			super(n);
		}
		public String makeToken(final Object obj) {
			return null;
		}
		public void executeToken(final String args) {
		}
	}

	public abstract static class Screen extends TokenHandler implements Command {
		public Screen(final String n) {
			super(n);
		}
		public String makeToken(final Object obj) {
			return getName();
		}
		public void execute() {
			final String token = makeToken(null);
			if (token.equals(History.getToken()))
				TokenSpace.dispatch(token);
			else
				History.newItem(token);
		}
	}

	public static abstract class SKey extends TokenHandler {
		protected SKey(final String n) {
			super(n);
		}
		public String makeToken(final Object obj) {
			return name + sep + get(obj);
		}
		public void executeToken(final String token) {
			execute(token);
		}
		public abstract String get(Object obj);
		public abstract void execute(String value);
	}

	public static abstract class LKey extends TokenHandler {
		protected LKey(final String n) {
			super(n);
		}
		public String makeToken(final Object obj) {
			return name + sep + String.valueOf(get(obj));
		}
		public void executeToken(final String token) {
			execute(Long.parseLong(token));
		}
		public abstract long get(Object obj);
		public abstract void execute(long value);
	}

	public static abstract class IKey extends TokenHandler {
		protected IKey(final String n) {
			super(n);
		}
		public String makeToken(final Object obj) {
			return name + sep + String.valueOf(get(obj));
		}
		public void executeToken(final String token) {
			execute(Integer.parseInt(token));
		}
		public abstract int get(Object obj);
		public abstract void execute(int value);
	}
	
	public static char getTokenSeparator() {
		return sep;
	}
}
