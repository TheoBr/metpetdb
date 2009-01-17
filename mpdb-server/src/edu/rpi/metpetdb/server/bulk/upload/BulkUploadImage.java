package edu.rpi.metpetdb.server.bulk.upload;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.properties.ImageOnGridProperty;
import edu.rpi.metpetdb.client.model.properties.Property;

public class BulkUploadImage extends MObject implements HasSubsample, HasSample {
	
	private static final long serialVersionUID = 1L;
	private Image image;
	private ImageOnGrid imageOnGrid;
	
	public BulkUploadImage() {
		image = new XrayImage();
		imageOnGrid = new ImageOnGrid();
	}
	
	public Image getImage() {
		return image;
	}
	
	public ImageOnGrid getImageOnGrid() {
		return imageOnGrid;
	}
	
	public void setImage(final Image i) {
		image = i;
	}
	
	@Override
	public void mSet(final Property p, final Object value) {
		if (ImageOnGridProperty.class.equals(p.getClass())) {
			p.set(imageOnGrid, value);
		} else {
			p.set(image, value);
		}
	}
	
	@Override
	public Object mGet(final Property p) {
		if (ImageOnGridProperty.class.equals(p.getClass())) {
			return p.get(imageOnGrid);
		} else {
			return p.get(image);
		}
	}

	@Override
	public boolean mIsNew() {
		return false;
	}

	public Subsample getSubsample() {
		return image.getSubsample();
	}

	public void setSubsample(Subsample subsample) {
		image.setSubsample(subsample);
	}

	public Sample getSample() {
		return image.getSample();
	}

	public void setSample(Sample sample) {
		image.setSample(sample);
	}
}
