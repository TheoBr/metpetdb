package edu.rpi.metpetdb.client.ui.widgets;

import java.util.List;

import org.postgis.Point;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.EarthInstanceHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class MGoogleEarthPopUp extends MDialogBox{
	public MGoogleEarthPopUp(){
		super(false,false);
	}
	
	public void createUI(final List<Sample> list){
		final FlowPanel earthContainer = new FlowPanel();
		final Button close = new Button("Close");
		close.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				hide();
			}
		});
		final MapWidget map = new MapWidget();
		new ServerOp<String>(){
			public void begin(){
				final String baseURL = "#" + 
				LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater();
				MpDb.search_svc.createKML(list, baseURL, this);
			}
			public void onSuccess(final String kml){
				map.getEarthInstance(new EarthInstanceHandler(){
					public void onEarthInstance(final EarthInstanceEvent e){
						MGoogleEarth.parseKML(e,kml);
				      }
				});
			}
		}.begin();
		
		map.setSize("500px", "500px");
	    map.addControl(new SmallMapControl());
	    map.addMapType(MapType.getEarthMap());
	    map.setCurrentMapType(MapType.getEarthMap());

	    // Automatically zoom to the first sample in the list
	    if (list.size() > 0) {
	    	map.getEarthInstance(new EarthInstanceHandler(){
	    		public void onEarthInstance(final EarthInstanceEvent e){
	    	    	Point zoom_point = ((Point)list.get(list.size() - 1).getLocation());
					MGoogleEarth.setView(e, zoom_point.x, zoom_point.y);
					// Hack to fix zoom; the call to setView calls a native function
					// after a 1000ms timer and we need to wait for that to have been called
					new Timer() {
			            @Override
			            public void run() {
			            	map.setZoomLevel(3);
			            }
			        }.schedule(1010);
					
	    		}
	    	});
		}
	    
    	earthContainer.add(map);
    	final ListBox locationLinks = new ListBox();
	    for (final Sample s : list){
			locationLinks.addItem(s.getNumber(), String.valueOf(((Point) s.getLocation()).x) + "," + String.valueOf(((Point) s.getLocation()).y));
	    }
	    final Button goTo = new Button(LocaleHandler.lc_text.googleEarth_GoToSample());
	    goTo.addClickListener(new ClickListener(){
	    	public void onClick(final Widget sender){
	    		map.getEarthInstance(new EarthInstanceHandler(){
	    			public void onEarthInstance(final EarthInstanceEvent e){
	    				final double x = Double.parseDouble(locationLinks.getValue(locationLinks.getSelectedIndex()).split(",")[1]);
	    				final double y = Double.parseDouble(locationLinks.getValue(locationLinks.getSelectedIndex()).split(",")[0]);
	    				MGoogleEarth.setView(e,x,y);
	    		      }
	    		});
	    	}
	    });
	    earthContainer.add(locationLinks);
	    earthContainer.add(goTo);
    	earthContainer.add(close);
		setWidget(earthContainer);
	}
	
	public void createUIMetamorphicRegions(){
		final FlowPanel earthContainer = new FlowPanel();
		final Button close = new Button("Close");
		close.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				hide();
			}
		});
		final MapWidget map = new MapWidget();
		new ServerOp<String>(){
			public void begin(){
				final String baseURL = GWT.getModuleBaseURL() + "#" + 
				LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater();
				MpDb.search_svc.createKMLMetamorphicRegions(this);
			}
			public void onSuccess(final String kml){
				map.getEarthInstance(new EarthInstanceHandler(){
					public void onEarthInstance(final EarthInstanceEvent e){
						MGoogleEarth.parseKML(e,kml);
				      }
				});
			}
		}.begin();
		
		map.setSize("500px", "500px");
	    map.addControl(new SmallMapControl());
	    map.addMapType(MapType.getEarthMap());
	    map.setCurrentMapType(MapType.getEarthMap());
	    
    	earthContainer.add(map);
    	earthContainer.add(close);
		setWidget(earthContainer);
	}
}
