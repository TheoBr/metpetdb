package edu.rpi.metpetdb.client.model.validation.interfaces;

public interface NumberConstraint<T extends Number> {

	public void setMaxValue(final T v);

	public void setMinValue(final T v);

	public T getMinValue();

	public T getMaxValue();

}
