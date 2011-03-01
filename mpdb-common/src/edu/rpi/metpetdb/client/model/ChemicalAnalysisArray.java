package edu.rpi.metpetdb.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class ChemicalAnalysisArray extends JavaScriptObject {
	
		protected ChemicalAnalysisArray() {}
	
		public final native JsArray<ChemicalAnalysisJSON> getChemicalAnalyses() /*-{
	    	return this.chemicalAnalyses;
	  	}-*/;
		
	}