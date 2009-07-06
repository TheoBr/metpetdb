package edu.rpi.metpetdb.server.util;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;

public class ImageUtil {
	
	public static String stripFilename(String path){
		String[] splitPath = path.split("/");
		path = splitPath[splitPath.length-1];
		splitPath = path.split("\\\\");
		return splitPath[splitPath.length-1];
	}
	
	public static Set<Image> stripFilename(Set<Image> images){
		for (Image i: images){
			i.setFilename(stripFilename(i.getFilename()));
		}
		return images;
	}

}
