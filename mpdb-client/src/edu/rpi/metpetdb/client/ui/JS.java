package edu.rpi.metpetdb.client.ui;

import com.google.gwt.dom.client.Element;

/** CSS and HTML constants */
public class JS {
	
	private JS() {}
	
	public static native void blur(Element elem) /*-{
		elem.blur();
	}-*/; 
}
