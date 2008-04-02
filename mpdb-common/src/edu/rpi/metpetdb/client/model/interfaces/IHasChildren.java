package edu.rpi.metpetdb.client.model.interfaces;

import java.util.Collection;

public interface IHasChildren<T> {

	public Collection<T> getChildren();

}
