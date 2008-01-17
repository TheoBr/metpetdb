package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class XrayImage extends Image {
	public static final int P_imageType = 0;
	public static final int P_subsample = 1;
	public static final int P_pixelsize = 2;
	public static final int P_contrast = 3;
	public static final int P_brightness = 4;
	public static final int P_lut = 5;
	public static final int P_radiation = 6;
	public static final int P_lines = 8;
	public static final int P_dwelltime = 9;
	public static final int P_current = 10;
	public static final int P_voltage = 11;
	public static final int P_element = 12;

	private Boolean radiation;
	private String lines;
	private Integer dwelltime;
	private Integer current;
	private Integer voltage;
	private Element element;

	public Boolean getRadiation() {
		return radiation;
	}
	public void setRadiation(final Boolean b) {
		radiation = b;
	}

	public String getLines() {
		return lines;
	}
	public void setLines(final String s) {
		lines = s;
	}

	public Integer getDwelltime() {
		return dwelltime;
	}
	public void setDwelltime(final Integer i) {
		dwelltime = i;
	}

	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(final Integer i) {
		current = i;
	}

	public Integer getVoltage() {
		return voltage;
	}
	public void setVoltage(final Integer i) {
		voltage = i;
	}

	public Element getElement() {
		return element;
	}
	public void setElement(final Element e) {
		element = e;
	}

	public Image getImage() {
		final Image i = new Image();
		i.setId(this.getId());
		i.setVersion(this.getVersion());
		i.setImageType(this.getImageType());
		i.setSubsample(this.getSubsample());
		i.setWidth(this.getWidth());
		i.setHeight(this.getHeight());
		i.setChecksum(this.getChecksum());
		i.setChecksum64x64(this.getChecksum64x64());
		i.setChecksumHalf(this.getChecksumHalf());
		i.setFilename(this.getFilename());
		i.setSample(this.getSample());
		i.setBrightness(this.getBrightness());
		i.setContrast(this.getContrast());
		i.setLut(this.getLut());
		i.setPixelsize(this.getPixelsize());
		return i;
	}

	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_imageType :
				if (newValue != GET_ONLY)
					setImageType((String) newValue);
				return getImageType();
			case P_contrast :
				if (newValue != GET_ONLY)
					setContrast(setIntegerValue(newValue));
				return getContrast();
			case P_lut :
				if (newValue != GET_ONLY)
					setLut(setIntegerValue(newValue));
				return getLut();
			case P_brightness :
				if (newValue != GET_ONLY)
					setBrightness(setIntegerValue(newValue));
				return getBrightness();
			case P_pixelsize :
				if (newValue != GET_ONLY)
					setPixelsize(setIntegerValue(newValue));
				return getPixelsize();
			case P_radiation :
				if (newValue != GET_ONLY)
					setRadiation((Boolean) newValue);
				return getRadiation();
			case P_dwelltime :
				if (newValue != GET_ONLY)
					setDwelltime(setIntegerValue(newValue));
				return getDwelltime();
			case P_current :
				if (newValue != GET_ONLY)
					setCurrent(setIntegerValue(newValue));
				return getCurrent();
			case P_voltage :
				if (newValue != GET_ONLY)
					setVoltage(setIntegerValue(newValue));
				return getVoltage();
			case P_lines :
				if (newValue != GET_ONLY)
					setLines((String) newValue);
				return getLines();
			case P_element :
				if (newValue != GET_ONLY)
					setElement((Element) newValue);
				return getElement();
		}
		throw new InvalidPropertyException(propertyId);
	}
}
