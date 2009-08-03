package edu.rpi.metpetdb.client.ui.plot;

import edu.rpi.metpetdb.client.model.Element;

/**
 * An element and a coefficient used for the AxisFormula
 * @author Dennis
 *
 */
public class AxisFormulaElement {
	private Element e;
	private double coefficient;
	
	public AxisFormulaElement(){
		
	}
	
	public AxisFormulaElement(final Element e, double coefficient){
		this.e = e;
		this.coefficient = coefficient;
	}
	
	public void setElement(final Element e){
		this.e = e;
	}
	
	public Element getElement(){
		return e;
	}
	
	public void setCoefficient(double coefficient){
		this.coefficient = coefficient;
	}
	
	public double getCoefficient(){
		return coefficient;
	}
}
