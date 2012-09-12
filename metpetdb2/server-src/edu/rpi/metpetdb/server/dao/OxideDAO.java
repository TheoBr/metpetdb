package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.model.Oxide;

public interface OxideDAO {

	public void saveOxide(Oxide oxide);
	
	public Oxide loadOxide(Long id);
}
