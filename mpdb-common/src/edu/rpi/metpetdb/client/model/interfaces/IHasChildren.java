package edu.rpi.metpetdb.client.model.interfaces;

import java.util.List;

import edu.rpi.metpetdb.client.model.MObjectDTO;

public interface IHasChildren {

	public <T extends MObjectDTO> List<T> getChildren();

}
