package edu.rpi.metpetdb.client.model;

public class XrayImageDTO extends ImageDTO {

	private static final long serialVersionUID = 1L;
	private Boolean radiation;
	private String lines;
	private Integer dwelltime;
	private Integer current;
	private Integer voltage;
	private ElementDTO element;

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

	public ElementDTO getElement() {
		return element;
	}

	public void setElement(final ElementDTO e) {
		element = e;
	}

	public ImageDTO getImage() {
		final ImageDTO i = new ImageDTO();
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
		i.setReferences(this.getReferences());
		return i;
	}
}
