package edu.rpi.metpetdb.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class ChemicalAnalysisJSON extends JavaScriptObject {

	protected ChemicalAnalysisJSON() {}
	
	public final native String getId() /*-{
	return this.id;
}-*/;
	
	public final native String getSpotId() /*-{
	return this.spot_id;
}-*/;
	
	public final native String getY() /*-{
	return this.y;
}-*/;
	
	public final native String getX() /*-{
	return this.x;
}-*/;

	public final native String getCount() /*-{
	return this.count;
}-*/;

	public final native String getAnalysisMethod() /*-{
	return this.analysis_method;
}-*/;
	
	public final native String getAnalysisMaterial() /*-{
	return this.analysis_material;
}-*/;
	
	public final native String getAnalysisLocation() /*-{
	return this.analysis_location;
}-*/;

	public final native String getAnalyst() /*-{
	return this.analyst;
}-*/;
	
	public final native String getAnalysisDate() /*-{
	return this.analysis_date;
}-*/;

	public final native String getTotal() /*-{
	return this.total;
}-*/;

}