package edu.rpi.metpetdb.client.ui.widgets;

import java.util.ArrayList;

import org.postgis.Point;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.EarthInstanceHandler;
import com.google.gwt.maps.client.event.EarthInstanceHandler.EarthInstanceEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class MGoogleEarthPopUp extends MDialogBox{
	public MGoogleEarthPopUp(){
		
	}
	
	public void createUI(final ArrayList<Sample> list){
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
}
