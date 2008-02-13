package edu.rpi.metpetdb.client.model.interfaces;

import java.util.Set;

public interface IHasChildren {

	public Set<IHasChildren> getChildren();

	public String getName();

	public void setChildren(final Set<IHasChildren> c);

}
