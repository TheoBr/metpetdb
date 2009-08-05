package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.charting.Axis;
import com.objetdirect.tatami.client.charting.Chart2D;
import com.objetdirect.tatami.client.charting.Plot2D;
import com.objetdirect.tatami.client.charting.Point;
import com.objetdirect.tatami.client.charting.Serie;
import com.objetdirect.tatami.client.charting.Themes;
import com.objetdirect.tatami.client.charting.effects.EffectHighlight;
import com.objetdirect.tatami.client.charting.effects.EffectMagnify;
import com.objetdirect.tatami.client.charting.effects.EffectTooltip;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaElement;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaOxide;

public class ScatterPlot extends MPlot{
	
	public ScatterPlot(){
		
	}
	
	public Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas){
		Chart2D c = new Chart2D("400px", "400px");
		Plot2D p = new Plot2D(Plot2D.PLOT_TYPE_SCATTER);
		
		FlowPanel chartPanel = new FlowPanel();
		double xMax = 0;
		double yMax = 0;
		
		if (formulas.size() == 2){
			AxisFormula formulaLeft = formulas.get(0);
			AxisFormula formulaBottom = formulas.get(1);
			
			List<Point> points = new ArrayList<Point>();
			for (ChemicalAnalysis ca : data){
				float bottomAxisTotal = new Double(formulaBottom.getConstant()).floatValue();
				float leftAxisTotal = new Double(formulaLeft.getConstant()).floatValue();
				Iterator<ChemicalAnalysisOxide> itr = ca.getOxides().iterator();
				while (itr.hasNext()){
					ChemicalAnalysisOxide o = itr.next();
					for (AxisFormulaOxide i : formulaBottom.getOxides()){
						if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
							bottomAxisTotal += i.getCoefficient()*o.getAmount();
						}
					}
					for (AxisFormulaOxide i : formulaLeft.getOxides()){
						if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
							leftAxisTotal += i.getCoefficient()*o.getAmount();
						}
					}
				}
				Iterator<ChemicalAnalysisElement> itr2 = ca.getElements().iterator();
				while (itr2.hasNext()){
					ChemicalAnalysisElement o = itr2.next();
					for (AxisFormulaElement i : formulaBottom.getElements()){
						if (i.getElement().getId() == o.getElement().getId()){
							bottomAxisTotal += i.getCoefficient()*o.getAmount();
						}
					}
					for (AxisFormulaElement i : formulaLeft.getElements()){
						if (i.getElement().getId() == o.getElement().getId()){
							leftAxisTotal += i.getCoefficient()*o.getAmount();
						}
					}
				}
				if (leftAxisTotal != 0 && bottomAxisTotal != 0) {
					yMax = (leftAxisTotal > yMax+5) ? leftAxisTotal+5 : yMax;
					xMax = (bottomAxisTotal > xMax+5) ? bottomAxisTotal+5 : xMax;
					points.add(new Point(bottomAxisTotal, leftAxisTotal, ca.getSpotId()));
				}
			}
			final Serie s = new Serie<Point>(points);
			p.addSerie(s);
		} else {
			xMax = 100;
			yMax = 100;
		}
			
		c.addPlot(p);		
		p.setShadow(2, 2, 2);
		p.addEffect(new EffectTooltip());
		p.addEffect(new EffectHighlight("gold"));
		p.addEffect(new EffectMagnify(2));
		
		c.setDefaultXAxis(new Axis());
		c.getDefaultXAxis().setIncludeZero(true);
		c.getDefaultXAxis().setMajorTicks(true);
		c.getDefaultXAxis().setMinorTicks(true);
		c.getDefaultXAxis().setPosition(Axis.BOTTOM);
		c.getDefaultXAxis().setPosition(Axis.HORIZONTAL);
		
		c.setDefaultYAxis(new Axis());
		c.getDefaultYAxis().setPosition(Axis.LEFT);
		c.getDefaultYAxis().setPosition(Axis.VERTICAL);
		c.getDefaultYAxis().setIncludeZero(true);
		c.getDefaultYAxis().setMajorTicks(true);
		c.getDefaultYAxis().setMinorTicks(true);
		
		if (formulas.size() == 2){
			c.setTitle("Y Axis: " + formulas.get(0).getAxisLabel() +
							"\nX Axis: " + formulas.get(1).getAxisLabel());
		}
		c.getDefaultYAxis().setMax(yMax);
		c.getDefaultXAxis().setMax(xMax);
		
        c.setTheme(Themes.MiamiNice);
        chartPanel.add(c);
        return chartPanel;
	}
	
	public int getAxisCount(){
		return 2;
	}
}