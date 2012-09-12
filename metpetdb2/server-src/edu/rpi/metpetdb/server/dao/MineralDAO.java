package edu.rpi.metpetdb.server.dao;

import java.util.List;

import edu.rpi.metpetdb.server.model.Mineral;

public interface MineralDAO {
	
	public List<Mineral> loadMinerals();

}
