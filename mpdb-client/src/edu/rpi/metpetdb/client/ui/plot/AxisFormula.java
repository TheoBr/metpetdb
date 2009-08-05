package edu.rpi.metpetdb.client.ui.plot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;

/**
 * This class represents a linear combination of elements and oxides
 * used to define an axis.
 * @author Dennis
 *
 */
public class AxisFormula {
	private double constant;
	private Set<AxisFormulaElement> elements;
	private Set<AxisFormulaOxide> oxides;
	
	public AxisFormula(){
		elements = new HashSet<AxisFormulaElement>();
		oxides = new HashSet<AxisFormulaOxide>();
	}
	
	public AxisFormula(double constant, Set<AxisFormulaElement> elements, Set<AxisFormulaOxide> oxides){
		this.constant = constant;
		this.elements = elements;
		this.oxides = oxides;
	}
	
	public double getConstant(){
		return constant;
	}
	
	public void setConstant(double constant){
		this.constant = constant;
	}
	
	public Set<AxisFormulaElement> getElements(){
		return elements;
	}
	
	public void setElements(final Set<AxisFormulaElement> elements){
		this.elements = elements;
	}
	
	public Set<AxisFormulaOxide> getOxides(){
		return oxides;
	}
	
	public void setOxides(final Set<AxisFormulaOxide> oxides){
		this.oxides = oxides;
	}
	
	public void addElement(final AxisFormulaElement e){
		elements.add(e);
	}
	
	public void addAllElements(final Collection<AxisFormulaElement> elements){
		this.elements.addAll(elements);
	}
	
	public void addElement(final Element e, double coefficient){
		elements.add(new AxisFormulaElement(e,coefficient));
	}
	
	public void addOxide(final AxisFormulaOxide o){
		oxides.add(o);
	}
	
	public void addAllOxides(final Collection<AxisFormulaOxide> oxides){
		this.oxides.addAll(oxides);
	}
	
	public void addOxide(final Oxide o, double coefficient){
		oxides.add(new AxisFormulaOxide(o,coefficient));
	}
	
	public void removeElement(final Element e){
		for (AxisFormulaElement afe : elements){
			if (afe.getElement() == e){
				elements.remove(e);
				return;
			}
		}
	}
	
	public void removeOxide(final Oxide o){
		for (AxisFormulaOxide afo : oxides){
			if (afo.getOxide() == o){
				oxides.remove(o);
				return;
			}
		}
	}
	
	public void updateCoefficent(final Element e, double coefficient){
		for (AxisFormulaElement afe : elements){
			if (afe.getElement() == e){
				afe.setCoefficient(coefficient);
				return;
			}
		}
	}
	
	public void updateCoefficent(final Oxide o, double coefficient){
		for (AxisFormulaOxide afo : oxides){
			if (afo.getOxide() == o){
				afo.setCoefficient(coefficient);
				return;
			}
		}
	}
	
	public String getAxisLabel(){
		String label = (constant != 0) ? String.valueOf(constant) : "";
		for (AxisFormulaElement afe : elements){
			if (afe.getCoefficient()!=0){
				label += ((label.length() > 0) ? " + " : "") + afe.getCoefficient() + 
							"*" + afe.getElement().getSymbol();
			}
		}
		for (AxisFormulaOxide afo : oxides){
			if (afo.getCoefficient() != 0){
				label += ((label.length() > 0) ? " + " : "") + afo.getCoefficient() + 
							"*" + afo.getOxide().getSpecies();
			}
		}
		return label;
	}
}
