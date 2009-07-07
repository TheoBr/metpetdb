package edu.rpi.metpetdb.client.model;

public class XrayImage extends Image {
	private static final long serialVersionUID = 1L;

	private Integer dwelltime;
	private Integer current;
	private Integer voltage;
	private String element;

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

	public String getElement() {
		return element;
	}

	public void setElement(final String e) {
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
		i.setChecksumMobile(this.getChecksumMobile());
		i.setFilename(this.getFilename());
		i.setSample(this.getSample());
		return i;
	}
}
