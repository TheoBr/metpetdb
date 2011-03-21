package edu.rpi.metpetdb.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class SampleArray extends JavaScriptObject {

	
	protected SampleArray() {}
	
	public final native JsArray<SampleJSON> getSamples() /*-{
    	return this.samples;
  	}-*/;
	
}
