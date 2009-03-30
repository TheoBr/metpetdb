package edu.rpi.metpetdb.client.model.interfaces;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;

public interface HasImages extends MObject {

	public Set<Image> getImages();

}
