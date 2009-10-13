package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.charting.Point;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;

public abstract class MPlot {
	
	public MPlot(){
		
	}
	
	protected double toMoleNumber(final ChemicalAnalysisElement e){
		Double wt = e.getAmount()*ChemicalAnalysis.getUnitOffset(e.getMeasurementUnit());
		Float molecularWeight = e.getElement().getWeight();
		return wt.floatValue()/molecularWeight;
	}
	
	protected double toMoleNumber(final ChemicalAnalysisOxide o){
		Double wt = o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
		Float molecularWeight = o.getOxide().getWeight()*o.getOxide().getCationsPerOxide();
		return wt.floatValue()/molecularWeight;
	}
	
	protected Map<Object, Double> toMoles(final ChemicalAnalysis ca){
		Map<Object, Double> moles = new HashMap();
		Double moleSum = 0.;
		
		// TODO: Change based on the mineral
		Double normalizationFactor = 24.;
		
		for (ChemicalAnalysisElement e : ca.getElements()){
			moles.put(e.getElement(), toMoleNumber(e));
		}
		for (ChemicalAnalysisOxide o : ca.getOxides()){
			moles.put(o.getOxide(), toMoleNumber(o));
		}
		for (Double d : moles.values()){
			moleSum += d;
		}		
		normalizationFactor /= moleSum;
		for (Double d : moles.values()){
			d *= normalizationFactor;
		}		
		
		return moles;
	}
	
	protected List<Point> analysesToPoints(final List<ChemicalAnalysis> analyses,
			final Map<Integer, Double> elementsXaxis, final Map<Integer, Double> oxidesXaxis,
			final Map<Integer, Double> elementsYaxis, final Map<Integer, Double> oxidesYaxis){
		
		ArrayList<Point> data = new ArrayList<Point>();
		
		for (ChemicalAnalysis ca : analyses){
//			if (ca.getMineral().getName().equalsIgnoreCase("Garnet")){
				
			float xAxisTotal = 0;
			float yAxisTotal = 0;
			Map<Object, Double> Moles = toMoles(ca);
			Iterator<Object> itr = Moles.keySet().iterator();
			while (itr.hasNext()){
				Object obj = itr.next();
				if (obj instanceof Element){
					Element e = (Element) obj;
					for (Integer i : elementsXaxis.keySet()){
						if (i.intValue() == e.getId()){
							xAxisTotal += elementsXaxis.get(i)*Moles.get(obj);
						}
					}
					for (Integer i : elementsYaxis.keySet()){
						if (i.intValue() == e.getId()){
							yAxisTotal += elementsYaxis.get(i)*Moles.get(obj);
						}
					}
				} else if (obj instanceof Oxide){
					Oxide o = (Oxide) obj;
					for (Integer i : oxidesXaxis.keySet()){
						if (i.intValue() == o.getOxideId()){
							xAxisTotal += oxidesXaxis.get(i)*Moles.get(obj);
						}
					}
					for (Integer i : oxidesYaxis.keySet()){
						if (i.intValue() == o.getOxideId()){
							yAxisTotal += oxidesYaxis.get(i)*Moles.get(obj);
						}
					}
				}
			}
			
			if (xAxisTotal != 0 || yAxisTotal !=0)
				data.add(new Point(xAxisTotal, yAxisTotal, ca.getSpotId()));
		}
		
//		}
		
		
		return data;
	}
	
	public abstract Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas, Map<Integer,Set<Integer>> groups, boolean moles);
	
	public abstract int getAxisCount();
	
	public abstract String getSVG();

}
