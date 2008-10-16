package edu.rpi.metpetdb.client.ui;

import com.google.gwt.dom.client.Element;

public class JS {
	
	private JS() {}
	
	public static native void blur(Element elem) /*-{
		elem.blur();
	}-*/; 
}
