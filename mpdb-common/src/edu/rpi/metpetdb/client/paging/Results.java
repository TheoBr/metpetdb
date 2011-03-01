package edu.rpi.metpetdb.client.paging;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.interfaces.MObject;

/**
 * Useful container for holding a list of objects and the total number of
 * objects in the database. Used extensively by paging
 * 
 * @author anthony
 * 
 * @param <
 * 		T> the subtype of the list representing the results
 */
public class Results<T> implements IsSerializable {

	private List<T> list;
	private int count;

	public Results() {

	}

	/**
	 * Creates a new results container
	 * 
	 * @param count
	 * 		the total number of objects in the database
	 * @param list
	 * 		a subset of those objects
	 */
	public Results(final int count, final List<T> list) {
		this.list = list;
		this.count = count;
	}

	/**
	 * Gets the list of objects
	 * 
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Sets the list of object
	 * 
	 * @param data
	 * 		the data
	 */
	public void setList(List<T> data) {
		this.list = data;
	}

	/**
	 * Gets the total number of objects in the database
	 * 
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the total number of objects in the database
	 * 
	 * @param count
	 * 		the count
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
