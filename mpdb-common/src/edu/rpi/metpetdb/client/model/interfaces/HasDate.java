package edu.rpi.metpetdb.client.model.interfaces;

import java.sql.Timestamp;

public interface HasDate {
	public Short getDatePrecision();
	/**
	 * 0 means exact day, 31/30 means only the month/year are exact and 365
	 * means only the year is exact
	 * 
	 * @return
	 */
	public void setDatePrecision(Short datePrecision);
	public Timestamp getDate();
	public void setDate(final Timestamp date);
}
