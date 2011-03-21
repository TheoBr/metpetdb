package edu.rpi.metpetdb.client.model;

import java.util.List;

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
		
		public final native String getCount() /*-{
		return this.count;
	}-*/;
		
		

}
