package edu.rpi.metpetdb.client.model.interfaces;

import edu.rpi.metpetdb.client.model.User;

public interface HasOwner {
	
	public User getOwner();

	public void setOwner(final User user);

}
