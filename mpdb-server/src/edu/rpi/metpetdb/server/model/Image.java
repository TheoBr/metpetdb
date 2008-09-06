package edu.rpi.metpetdb.server.model;

import java.util.Set;

import com.google.gwt.core.client.GWT;

public class Image extends MObject {
	private static final long serialVersionUID = 1L;

	private long id;
	private int version;

	private ImageType imageType;
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
	private String collector;
	private Set<Reference> references;
	private Set<ImageComment> comments;
	private int scale;

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

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(final ImageType it) {
		imageType = it;
	}

	public Set<ImageComment> getComments() {
		return comments;
	}

	public void setComments(Set<ImageComment> comments) {
		this.comments = comments;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
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
		return checksum != null ? checksum.hashCode() + (int) id : 0;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}
}
