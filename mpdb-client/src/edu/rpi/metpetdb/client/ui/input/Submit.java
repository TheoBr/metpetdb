package edu.rpi.metpetdb.client.ui.input;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;

import edu.rpi.metpetdb.client.ui.Styles;

public class Submit extends Button {
	public Submit() {
		setStyleName(Styles.SUBMIT);
	}
	public Submit(final String html) {
		super(html);
		setStyleName(Styles.SUBMIT);
	}
	public Submit(final String html, final ClickListener listener) {
		super(html, listener);
		setStyleName(Styles.SUBMIT);
	}
}
