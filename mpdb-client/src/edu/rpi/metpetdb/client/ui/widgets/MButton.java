package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.JS;

public class MButton extends Button {
	
	private final String STYLENAME = "button";
	
	public MButton() {
		super();
		setStylePrimaryName(STYLENAME);
		addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				JS.blur(getElement());
			}
		});
	}

	public MButton(String html) {
		this();
		setHTML(html);
	}

	public MButton(String html, ClickListener listener) {
		this(html);
		addClickListener(listener);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) removeStyleDependentName(CSS.DISABLED);
		else addStyleDependentName(CSS.DISABLED);
	}
	
}
