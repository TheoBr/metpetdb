package edu.rpi.metpetdb.client.ui.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.postgis.Point;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.event.PolygonMouseOverHandler.PolygonMouseOverEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.ui.MpDb;

public class MetamorphicRegionsView extends FlowPanel{
	private MapWidget map;

	public MetamorphicRegionsView() {
		map = new MapWidget(LatLng.newInstance(43.8, -5.6),1);
		map.addControl(new LargeMapControl());
		map.addControl(new MapTypeControl());
		map.addControl(new ScaleControl()); 
		map.setSize("100%", "100%");

		Collection<MetamorphicRegion> regions = MpDb.doc.Sample_metamorphicRegions.getValues();

		for (final MetamorphicRegion mr : regions){ 
			final List<LatLng> points = new ArrayList<LatLng>();
			org.postgis.Polygon pg = (org.postgis.Polygon) mr.getShape();

			for (int i = 0; i < pg.getRing(0).numPoints(); i++){
				Point p = pg.getRing(0).getPoint(i);

				points.add(LatLng.newInstance(p.y, p.x));
			}
			final LatLng[] newPoints = points.toArray(new LatLng[]{});
			Polygon overlay = new Polygon(newPoints, "#0f2e3c", 1, .60, "#000000", .33);
			overlay.addPolygonClickHandler(new PolygonClickHandler(){

				public void onClick(PolygonClickEvent event) {
					map.getInfoWindow().open(event.getLatLng(),
							new InfoWindowContent(mr.getName()));
				}


			});
			map.addOverlay(overlay);
		}
		this.add(map);
		this.setSize("1200px", "500px");
		map.checkResize();
	}
}