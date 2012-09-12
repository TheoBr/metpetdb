package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.model.Georeference;
import edu.rpi.metpetdb.server.model.Reference;

public interface ReferenceDAO {

	public void saveGeoreference(Reference georeference);
	
	public Reference lookupReference(Georeference geoRef);
}
