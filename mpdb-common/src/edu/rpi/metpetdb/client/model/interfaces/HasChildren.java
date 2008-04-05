package edu.rpi.metpetdb.client.model.interfaces;

import java.util.Collection;

/**
 * 
 * @author anthony
 * 
 * @param <T>
 *            type of the children
 */
public interface HasChildren<T> {

	public Collection<T> getChildren();

}
