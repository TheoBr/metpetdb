package edu.rpi.metpetdb.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class SampleJSON extends JavaScriptObject {

	protected SampleJSON() {}

	public final native String getId() /*-{
		return this.sampleId;
	}-*/;

	public final native String getSampleNumber() /*-{
		return this.sampleNumber;
	}-*/;

	public final native String getPublicData() /*-{
		return this.publicData;
	}-*/;

	public final native String getOwner() /*-{
		return this.owner;
	}-*/;

	public final native String getRockType() /*-{
		return this.rockType;
	}-*/;

	public final native String getCollectionDate() /*-{
		return this.collectionDate;
	}-*/;

	public final native String getImageCount() /*-{
		return this.imageCount;
	}-*/;

	public final native String getAnalysisCount() /*-{
		return this.analysisCount;
	}-*/;

	public final native String getSubsampleCount() /*-{
		return this.subsampleCount;
	}-*/;

	public final native JsArrayString getMinerals() /*-{
		return this.minerals;
	}-*/;

	public final native String getLatitude() /*-{
		return this.latitude;
	}-*/;

	public final native String getLongitude() /*-{
		return this.longitude;
	}-*/;

	public final native String getLocationText() /*-{
		return this.locationText;
	}-*/;
	
	
	public final native String getCountry() /*-{
		return this.country;
	}-*/;

	public final native String getCollector() /*-{
	return this.collector;
}-*/;

	public final native JsArrayString getRegions() /*-{
		return this.regions;
	}-*/;

	public final native JsArrayString getMetamorphicRegions() /*-{
		return this.metamorphicRegions;
	}-*/;

	public final native JsArrayString getMetamorphicGrades() /*-{
	return this.metamorphicGrades;
}-*/;
	
	public final native String getGradeName() /*-{
	return this.gradeName;
}-*/;
	
	public final native String getCount() /*-{
		return this.count;
	}-*/;

}
