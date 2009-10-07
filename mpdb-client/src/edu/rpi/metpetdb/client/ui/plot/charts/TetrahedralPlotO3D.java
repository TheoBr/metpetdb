package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;

public class TetrahedralPlotO3D extends MPlot {
	private HTML container = new HTML("<div id=\"o3d\" style=\"width: 300px; height: 300px;\"></div>");
	public TetrahedralPlotO3D(){
		
	} 
	
	public int getAxisCount(){
		return 4;
	}
	
	public String getSVG(){
		return "";
	}

	public Widget createWidget(List<ChemicalAnalysis> data,
			List<AxisFormula> formulas, Map<Integer, Set<Integer>> groups) {
		myInit(container.getElement());
		return container;
	}
	
	public native void myInit(Element element) /*-{
		$wnd.o3djs.util.createClient(element);
	}-*/;

}
