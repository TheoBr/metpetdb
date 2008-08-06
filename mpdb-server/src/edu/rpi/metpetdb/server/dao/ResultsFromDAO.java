package edu.rpi.metpetdb.server.dao;

import java.util.List;

import edu.rpi.metpetdb.server.model.MObject;

public class ResultsFromDAO<T extends MObject> {

	private List<T> list;
	private int count;

	public ResultsFromDAO() {

	}

	/**
	 * Creates a new results container
	 * 
	 * @param count
	 *            the total number of objects in the database
	 * @param list
	 *            a subset of those objects
	 */
	public ResultsFromDAO(final int count, final List<T> list) {
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
	 *            the data
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
	 *            the count
	 */
	public void setCount(int count) {
		this.count = count;
	}

}
