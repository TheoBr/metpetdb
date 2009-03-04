package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.maps.client.event.EarthInstanceHandler.EarthInstanceEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class MGoogleEarth {
	private static final String geNotInit = "Failed to init earth plugin";
	
	public static void addControls(final EarthInstanceEvent e){
		final JavaScriptObject earth = e.getEarthInstance();
		if (earth == null) {
          Window.alert(geNotInit);
        } else {
          new Timer() {
            @Override
            public void run() {
              addControls(earth);
            }
          }.schedule(1000);
        }
		
	}
	
	public static native void addControls(JavaScriptObject ge) /*-{
		var screenXY = ge.getNavigationControl().getScreenXY();
  		screenXY.setYUnits(ge.UNITS_PIXELS); 
  		screenXY.setXUnits(ge.UNITS_PIXELS); 
	 	ge.getOptions().setOverviewMapVisibility(true);
	}-*/;
	
	public static void createPlacemark(final EarthInstanceEvent e, final double x, final double y){
		final JavaScriptObject earth = e.getEarthInstance();
        if (earth == null) {
          Window.alert(geNotInit);
        } else {
          new Timer() {
            @Override
            public void run() {
              createPlacemark(earth, x, y);
            }
          }.schedule(1000);
        }
	}
		
	public static native void createPlacemark(JavaScriptObject ge, double x, double y) /*-{
		
	    var placemark = ge.createPlacemark('');
	    ge.getFeatures().appendChild(placemark);
	
	    // Create style map for placemark
	    var normal = ge.createIcon('');
	    normal.setHref('http://maps.google.com/mapfiles/kml/paddle/blue-circle.png');
	    var iconNormal = ge.createStyle('');
	    iconNormal.getIconStyle().setIcon(normal);
	    var highlight = ge.createIcon('');
	    highlight.setHref('http://maps.google.com/mapfiles/kml/paddle/red-circle.png');
	    var iconHighlight = ge.createStyle('');
	    iconHighlight.getIconStyle().setIcon(highlight);
	    var styleMap = ge.createStyleMap('');
	    styleMap.setNormalStyle(iconNormal);
	    styleMap.setHighlightStyle(iconHighlight);
	    placemark.setStyleSelector(styleMap);
	
	    // Create point     
	    var point = ge.createPoint('');
	    point.setLatitude(y);
	    point.setLongitude(x);
	    placemark.setGeometry(point);
	    
	}-*/;
	
//	public static void createPolygon(final EarthInstanceEvent e,
//			final double x1, final double y1, final double x2, final double y2){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//            	createPolygon(earth,x1,y1,x2,y2);
//            }
//          }.schedule(1000);
//        }
//	}
//	
//	public static native void createPolygon(JavaScriptObject ge, 
//			double x1, double y1, double x2, double y2) /*-{
//	
//		// outer boundary is a square
//		var outerBoundary = ge.createLinearRing('');
//		var coords = outerBoundary.getCoordinates();
//		coords.pushLatLngAlt(y1, x1, 0); 
//		coords.pushLatLngAlt(y1, x2, 0); 
//		coords.pushLatLngAlt(y2, x2, 0); 
//		coords.pushLatLngAlt(y2, x1, 0); 
//			
//		// create the polygon and set its boundaries
//		var polygon = ge.createPolygon('');
//		polygon.setOuterBoundary(outerBoundary);
//		
//		if (!window.polygonPlacemark) {
//			
//			
//			// create the polygon placemark and add it to Earth
//			var polygonPlacemark = ge.createPlacemark('');
//			polygonPlacemark.setGeometry(polygon);
//			
//			polygonPlacemark.setStyleSelector(ge.createStyle(''));
//			polygonPlacemark.getStyleSelector().getPolyStyle().getColor().set('70000000');
//			polygonPlacemark.getStyleSelector().getLineStyle().getColor().set('C0f2e3c');
//			
//			ge.getFeatures().appendChild(polygonPlacemark);
//			
//			// persist the placemark for other interactive samples
//			window.polygonPlacemark = polygonPlacemark;
//		} else {
//			window.polygonPlacemark.setGeometry(polygon);
//		}
//	    
//	}-*/;
//	
//	public static void createPlacemark1(final EarthInstanceEvent e, final double x, final double y){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//              createPlacemark(earth, x, y);
//            }
//          }.schedule(1000);
//        }
//	}
//		
//	public static native void createPlacemark1(JavaScriptObject ge, double x, double y) /*-{
//		// Create point     
//	    var point = ge.createPoint('');
//	    point.setLatitude(y);
//	    point.setLongitude(x);
//		    
//		if (!window.placemark1) {
//		    var placemark = ge.createPlacemark('');
//		    ge.getFeatures().appendChild(placemark);
//		
//		    // Create style map for placemark
//		    var normal = ge.createIcon('');
//		    normal.setHref('http://maps.google.com/mapfiles/kml/paddle/blue-circle.png');
//		    var iconNormal = ge.createStyle('');
//		    iconNormal.getIconStyle().setIcon(normal);
//		    var highlight = ge.createIcon('');
//		    highlight.setHref('http://maps.google.com/mapfiles/kml/paddle/red-circle.png');
//		    var iconHighlight = ge.createStyle('');
//		    iconHighlight.getIconStyle().setIcon(highlight);
//		    var styleMap = ge.createStyleMap('');
//		    styleMap.setNormalStyle(iconNormal);
//		    styleMap.setHighlightStyle(iconHighlight);
//		    placemark.setStyleSelector(styleMap);
//		
//		    
//		    placemark.setGeometry(point);	    
//		    window.placemark1 = placemark;
//	    } else {
//	    	window.placemark1.setGeometry(point);
//	    }
//	    
//	}-*/;
//	
//	public static void createPlacemark2(final EarthInstanceEvent e, final double x, final double y){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//              createPlacemark(earth, x, y);
//            }
//          }.schedule(1000);
//        }
//	}
//		
//	public static native void createPlacemark2(JavaScriptObject ge, double x, double y) /*-{
//		
//	   // Create point     
//	    var point = ge.createPoint('');
//	    point.setLatitude(y);
//	    point.setLongitude(x);
//		    
//		if (!window.placemark2) {
//		    var placemark = ge.createPlacemark('');
//		    ge.getFeatures().appendChild(placemark);
//		
//		    // Create style map for placemark
//		    var normal = ge.createIcon('');
//		    normal.setHref('http://maps.google.com/mapfiles/kml/paddle/blue-circle.png');
//		    var iconNormal = ge.createStyle('');
//		    iconNormal.getIconStyle().setIcon(normal);
//		    var highlight = ge.createIcon('');
//		    highlight.setHref('http://maps.google.com/mapfiles/kml/paddle/red-circle.png');
//		    var iconHighlight = ge.createStyle('');
//		    iconHighlight.getIconStyle().setIcon(highlight);
//		    var styleMap = ge.createStyleMap('');
//		    styleMap.setNormalStyle(iconNormal);
//		    styleMap.setHighlightStyle(iconHighlight);
//		    placemark.setStyleSelector(styleMap);
//		
//		    
//		    placemark.setGeometry(point);	    
//		    window.placemark2 = placemark;
//	    } else {
//	    	window.placemark2.setGeometry(point);
//	    }
//	    
//	}-*/;
//	
//	public static void clearPlacemark2(final EarthInstanceEvent e){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//            	clearPlacemark2(earth);
//            }
//          }.schedule(1000);
//        }
//	}
//	
//	public static native void clearPlacemark2(JavaScriptObject ge) /*-{
//		    
//		if (window.placemark2) {	
//		    window.alert("true2");
//		    window.placemark2 = null;
//		}
//		window.alert("false2");
//	    
//	}-*/;
//	
//	public static void clearPlacemark1(final EarthInstanceEvent e){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//            	clearPlacemark1(earth);
//            }
//          }.schedule(1000);
//        }
//	}
//	
//	public static native void clearPlacemark1(JavaScriptObject ge) /*-{
//		
//		if (window.placemark1) {	   
//		 	window.alert("true1");
//		    window.placemark1 = null;
//		}
//		window.alert("false1");
//	    
//	}-*/;
//	
//	public static void clearPolygon(final EarthInstanceEvent e){
//		final JavaScriptObject earth = e.getEarthInstance();
//        if (earth == null) {
//          Window.alert(geNotInit);
//        } else {
//          new Timer() {
//            @Override
//            public void run() {
//            	clearPolygon(earth);
//            }
//          }.schedule(1000);
//        }
//	}
//	
//	public static native void clearPolygon(JavaScriptObject ge) /*-{
//    
//		if (window.polygonPlacemark) {	
//	    	window.alert("true");
//		    window.polygonPlacemark.setGeometry(null);
//		}
//		window.alert("false");
//	    
//	}-*/;
	
	public static void setView(final EarthInstanceEvent e,final double x, final double y){
		final JavaScriptObject earth = e.getEarthInstance();
        if (earth == null) {
          Window.alert(geNotInit);
        } else {
          new Timer() {
            @Override
            public void run() {
            	setView(earth,x,y);
            }
          }.schedule(1000);
        }
	}
	
	public static native void setView(JavaScriptObject ge, final double x, final double y) /*-{
		var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
		lookAt.setLatitude(x);
		lookAt.setLongitude(y);
		ge.getView().setAbstractView(lookAt);
	    
	}-*/;
	
	
	public static void parseKML(final EarthInstanceEvent e, final String KML){
		final JavaScriptObject earth = e.getEarthInstance();
        if (earth == null) {
          Window.alert(geNotInit);
        } else {
          new Timer() {
            @Override
            public void run() {
            	parseKML(earth, KML);
            }
          }.schedule(1000);
        }
	}
	
	public static native void parseKML(JavaScriptObject ge, final String KML) /*-{  
		var output = ge.parseKml(KML);
		ge.getFeatures().appendChild(output);
	}-*/;
	
}
