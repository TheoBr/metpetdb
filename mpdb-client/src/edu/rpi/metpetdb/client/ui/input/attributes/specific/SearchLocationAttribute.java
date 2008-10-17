package edu.rpi.metpetdb.client.ui.input.attributes.specific;

//import java.math.BigDecimal;

import java.util.ArrayList;

import org.postgis.LinearRing;
import org.postgis.Point;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;

public class SearchLocationAttribute extends SearchGenericAttribute implements
		ClickListener {
	private final MTwoColPanel panel = new MTwoColPanel();
	private Button viewBounds = new Button("View Bounds", this);
	private final MLink clearMarkers = new MLink("Clear Markers", this);
	private MapWidget map;
	private Marker markerPoint1;
	private Marker markerPoint2;
	private Polygon box;
	private final TextBox northInput = new TextBox();
	private final TextBox southInput = new TextBox();
	private final TextBox eastInput = new TextBox();
	private final TextBox westInput = new TextBox();
	private org.postgis.Polygon boundingBox;
	private static final String STYLENAME = "search-map";

	public SearchLocationAttribute(final PropertyConstraint sc) {
		super(sc);
		panel.setStyleName(STYLENAME);
		northInput.setStyleName(STYLENAME + "-nb");
		southInput.setStyleName(STYLENAME + "-sb");
		eastInput.setStyleName(STYLENAME + "-eb");
		westInput.setStyleName(STYLENAME + "-wb");
	}

	public Widget[] createDisplayWidget(final MObject obj) {

		return new Widget[] {
			panel
		};
	}
	
	public MapWidget getMap(){
		return map;
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		panel.clear();

		markerPoint1 = null;
		markerPoint2 = null;

		map = new MapWidget(LatLng.newInstance(38, -38),2);
		map.addControl(new LargeMapControl());
		map.addControl(new MapTypeControl());
		map.addControl(new ScaleControl());
		map.setSize("100%", "300px");
		
		panel.getLeftCol().add(map);
		panel.getRightCol().add(viewBounds);
		panel.getRightCol().add(northInput);
		panel.getRightCol().add(southInput);
		panel.getRightCol().add(eastInput);
		panel.getRightCol().add(westInput);
		panel.getRightCol().add(clearMarkers);
		
		setClickHandler();

		return new Widget[] {
			panel
		};
	}
	
	private void setClickHandler() {
		map.addMapClickHandler(new MapClickHandler() {
			public void onClick(MapClickHandler.MapClickEvent sender) {
				if (sender.getOverlay() != null) {
					if (sender.getOverlay() == box) {
						clearSpecificOverlays(new Overlay[] {
								box, markerPoint2
						});
					} else if (sender.getOverlay() == markerPoint1) {
						clearSpecificOverlays(new Overlay[] {
								box, markerPoint1
						});
					} else if (sender.getOverlay() == markerPoint2)
						clearSpecificOverlays(new Overlay[] {
								box, markerPoint2
						});
					{
					}
					clearBounds();
				} else {
					if (markerPoint1 == null) {
						markerPoint1 = new Marker(sender.getLatLng());
						map.addOverlay(markerPoint1);
						if (markerPoint2 != null) {
							final Marker temp = markerPoint1;
							markerPoint1 = markerPoint2;
							markerPoint2 = temp;
						}
						createBox();
					} else {
						if (markerPoint2 != null) {
							map.removeOverlay(markerPoint2);
						}
						markerPoint2 = new Marker(sender.getLatLng());
						map.addOverlay(markerPoint2);
						createBox();
					}
				}
			}
		});
	}

	private void createBox() {
		if (markerPoint1 != null && markerPoint2 != null) {
			final double N;
			final double S;
			final double E;
			final double W;

			if (markerPoint1.getPoint().getLatitude() > markerPoint2.getPoint()
					.getLatitude()) {
				N = markerPoint1.getPoint().getLatitude();
				S = markerPoint2.getPoint().getLatitude();
			} else {
				N = markerPoint2.getPoint().getLatitude();
				S = markerPoint1.getPoint().getLatitude();
			}

			if (markerPoint1.getPoint().getLongitude() > markerPoint2
					.getPoint().getLongitude() + 180) {
				E = markerPoint2.getPoint().getLongitude();
				W = markerPoint1.getPoint().getLongitude();
			} else if (markerPoint1.getPoint().getLongitude() > markerPoint2
					.getPoint().getLongitude()) {
				E = markerPoint1.getPoint().getLongitude();
				W = markerPoint2.getPoint().getLongitude();
			} else {
				E = markerPoint2.getPoint().getLongitude();
				W = markerPoint1.getPoint().getLongitude();
			}

			final LatLng NW = LatLng.newInstance(N, W);
			final LatLng NE = LatLng.newInstance(N, E);
			final LatLng SE = LatLng.newInstance(S, E);
			final LatLng SW = LatLng.newInstance(S, W);

			final LatLng[] points = {
					NW, NE, SE, SW, NW
			};
			if (box != null)
				map.removeOverlay(box);
			box = new Polygon(points, "#0f2e3c", 1, .60, "#000000", .33);
			map.addOverlay(box);

			fillBounds(N, S, E, W);
		}
	}

	public void fillBounds(double N, double S, double E, double W) {
		northInput.setText(String.valueOf(N));
		southInput.setText(String.valueOf(S));
		eastInput.setText(String.valueOf(E));
		westInput.setText(String.valueOf(W));
	}

	public void clearBounds() {
		northInput.setText("");
		southInput.setText("");
		eastInput.setText("");
		westInput.setText("");
	}

	public void clearSpecificOverlays(Overlay[] overlays) {
		for (int i = 0; i < overlays.length; i++) {
			if (overlays[i] != null) {
				map.removeOverlay(overlays[i]);
				if (overlays[i] == box)
					box = null;
				else if (overlays[i] == markerPoint1)
					markerPoint1 = null;
				else if (overlays[i] == markerPoint2)
					markerPoint2 = null;
			}
		}
	}

	public void clearMap() {
		map.clearOverlays();
		markerPoint1 = null;
		markerPoint2 = null;
		boundingBox = null;
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {
		if (sender == viewBounds) {
			try {
				final double N = Double.parseDouble(northInput.getText());
				final double S = Double.parseDouble(southInput.getText());
				final double E = Double.parseDouble(eastInput.getText());
				final double W = Double.parseDouble(westInput.getText());
				clearMap();
				markerPoint1 = new Marker(LatLng.newInstance(N, W));
				markerPoint2 = new Marker(LatLng.newInstance(S, E));
				map.addOverlay(markerPoint1);
				map.addOverlay(markerPoint2);
				createBox();
			} catch (Exception e) {

			}
		} else if (sender == clearMarkers) {
			clearMap();
			clearBounds();
		}
	}

	protected Object get(Widget editWidget) throws ValidationException {
		try {
			final double n = Double.parseDouble(northInput.getText()); // y
			final double s = Double.parseDouble(southInput.getText()); // y
			final double e = Double.parseDouble(eastInput.getText()); // x
			final double w = Double.parseDouble(westInput.getText()); // x

			final Point p1 = new Point();
			p1.x = w;
			p1.y = s;

			final Point p2 = new Point();
			p2.x = w;
			p2.y = n;

			final Point p3 = new Point();
			p3.x = e;
			p3.y = n;

			final Point p4 = new Point();
			p4.x = e;
			p4.y = s;

			final LinearRing[] ringArray = new LinearRing[1];
			final Point[] points = new Point[5];
			points[0] = p1;
			points[1] = p2;
			points[2] = p3;
			points[3] = p4;
			points[4] = p1;
			final LinearRing ring = new LinearRing(points);
			ringArray[0] = ring;
			boundingBox = new org.postgis.Polygon(
					ringArray);
			boundingBox.srid = MpDbConstants.WGS84;
			boundingBox.dimension = 2;
			return boundingBox;
		} catch (Exception e) {
			// TODO
		}
		return null;
	}
	
	public void onRemoveCriteria(final Object obj){
		if (obj == boundingBox){
			clearMap();
			clearBounds();
		}
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
		try{
	//		if (boundingBox != null)
			if(northInput.getText().length() > 0 && southInput.getText().length() > 0 && eastInput.getText().length() > 0 && westInput.getText().length() > 0)
			{
				criteria.add(new Pair(createCritRow("Location:", " N: " + String.valueOf(northInput.getText()) + " S: "
						+ String.valueOf(southInput.getText()) +  " E: " + String.valueOf(eastInput.getText())  
						+ " W: " + String.valueOf(westInput.getText())), get(null)));
			}
		}
		catch (Exception e){
			// TODO
		}
		return criteria;
	}

}
