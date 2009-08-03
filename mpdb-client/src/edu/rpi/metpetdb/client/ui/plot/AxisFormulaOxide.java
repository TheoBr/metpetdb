package edu.rpi.metpetdb.client.ui.plot;

import edu.rpi.metpetdb.client.model.Oxide;

/**
 * An Oxide and a coefficient used for the AxisFormula
 * @author Dennis
 *
 */
public class AxisFormulaOxide {


	private Oxide o;
	private double coefficient;

	public AxisFormulaOxide(){

	}

	public AxisFormulaOxide(final Oxide o, double coefficient){
		this.o = o;
		this.coefficient = coefficient;
	}

	public void setOxide(final Oxide o){
		this.o = o;
	}

	public Oxide getOxide(){
		return o;
	}

	public void setCoefficient(double coefficient){
		this.coefficient = coefficient;
	}

	public double getCoefficient(){
		return coefficient;

	}
}
