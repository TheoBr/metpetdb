package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.model.Element;

public interface ElementDAO {

	public void saveElement(Element element);
	
	public Element loadElement(Long id);
}
