package edu.rpi.metpetdb.client.model.interfaces;

import edu.rpi.metpetdb.client.model.Image;


public interface HasImage extends MObject {

	public Image getImage();
	
	public void setImage(final Image i);
}
