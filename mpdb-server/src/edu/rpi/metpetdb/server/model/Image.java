package edu.rpi.metpetdb.server.model;

import com.google.gwt.core.client.GWT;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class Image extends MObject {
	public static final int P_imageType = 0;
	public static final int P_subsample = 1;
	public static final int P_pixelsize = 2;
	public static final int P_contrast = 3;
	public static final int P_brightness = 4;
	public static final int P_lut = 5;
	public static final int P_checksum = 6;

	private long id;
	private int version;
	
	private String imageType;
	private int width;
	private int height;
	private String checksum;
	private String checksum64x64;
	private String checksumHalf;
	
	
	private Subsample subsample;
	
	
	private Sample sample;
	private String filename;
	private XrayImage xrayImage;
	private Integer pixelsize;
	private Integer contrast;
	private Integer brightness;
	private Integer lut;

	public long getId() {
		return id;
	}
	public void setId(final long l) {
		id = l;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(final int v) {
		version = v;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(final String it) {
		imageType = it;
	}

	public Subsample getSubsample() {
		return subsample;
	}

	public void setSubsample(final Subsample s) {
		subsample = s;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int l) {
		width = l;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int l) {
		height = l;
	}

	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(final String i) {
		checksum = i;
	}

	public String getChecksum64x64() {
		return checksum64x64;
	}

	public void setChecksum64x64(final String s) {
		checksum64x64 = s;
	}

	public String getChecksumHalf() {
		return checksumHalf;
	}

	public void setChecksumHalf(final String s) {
		checksumHalf = s;
	}

	public Sample getSample() {
		return sample;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(final String s) {
		filename = s;
	}

	public void setSample(final Sample s) {
		sample = s;
	}

	public XrayImage getXrayImage() {
		return xrayImage;
	}
	public void setXrayImage(final XrayImage xray) {
		xrayImage = xray;
	}

	public Integer getBrightness() {
		return brightness;
	}
	public void setBrightness(Integer brightness) {
		this.brightness = brightness;
	}
	public Integer getContrast() {
		return contrast;
	}
	public void setContrast(Integer contrast) {
		this.contrast = contrast;
	}
	public Integer getLut() {
		return lut;
	}
	public void setLut(Integer lut) {
		this.lut = lut;
	}
	public Integer getPixelsize() {
		return pixelsize;
	}
	public void setPixelsize(Integer pixelsize) {
		this.pixelsize = pixelsize;
	}
	
	public String get64x64ServerPath() {
		return GWT.getModuleBaseURL() + "/image/?checksum="
				+ this.getChecksum64x64();
	}

	public String getHalfServerPath() {
		return GWT.getModuleBaseURL() + "/image/?checksum="
				+ this.getChecksumHalf();
	}

	public String getServerPath() {
		return GWT.getModuleBaseURL() + "/image/?checksum="
				+ this.getChecksum();
	}

	public static String getServerPath(final String checksum) {
		final String folder = checksum.substring(0, 2);
		final String subfolder = checksum.substring(2, 4);
		final String filename = checksum.substring(4, checksum.length());

		return folder + "/" + subfolder + "/" + filename;
	}

	public boolean equals(final Object o) {
		return checksum != null && o instanceof Image
				&& checksum.equals(((Image) o).checksum);
	}

	public int hashCode() {
		return checksum != null ? checksum.hashCode() + (int)id: 0;
	}

	public boolean mIsNew() {
		return id == 0;
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
			case P_checksum :
				if (newValue != GET_ONLY)
					setChecksum((String) newValue);
				return getChecksum();
		}
		throw new InvalidPropertyException(propertyId);
	}
}
