package edu.rpi.metpetdb.server.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;

public class ConstantsDAO  {
	
	//final protected Session sess;

	public ConstantsDAO()
	{
		//this.sess = sess;
	}
	
	public List<String> getOrderedOxidesAndElements(Session sess)
	{
		List<String> orderedOxidesAndElements = new ArrayList<String>();

		final Query q = sess.getNamedQuery("Oxide.ordered");
		final Query q2 = sess.getNamedQuery("Element.ordered");
		
		List<Oxide> oxides = (List<Oxide>) q.list();
		
		for (Oxide currOxide : oxides)
		{
			orderedOxidesAndElements.add(currOxide.getSpecies());
		}
		
		List<Element> elements = (List<Element>) q2.list();
		
		for (Element currElement : elements)
		{
			orderedOxidesAndElements.add(currElement.getSymbol());
		}

		
		
		return orderedOxidesAndElements;
	}

}
