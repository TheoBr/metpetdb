package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;

import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;

public class Image extends MObject implements HasSubsample, HasSample, PublicData, HasOwner {
	private static final long serialVersionUID = 1L;

	private long id;
	private int version;

	private ImageType imageType;
	private int width;
	private int height;
	private String checksum;
	private String checksum64x64;
	private String checksumHalf;
	private String checksumMobile;

	private Subsample Subsample;

	private Sample sample;
	private String filename;
	private XrayImage xrayImage;
	private String collector;
	private Set<Reference> references;
	private Set<ImageComment> comments;
	private Integer scale;
	private String description;
	private User owner;
	private Boolean publicData;
	
	

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
	
	

	public User getOwner() {
		return owner;
	}

	public Boolean getPublicData() {
		//by default be private
		if (publicData == null)
			publicData = false;
		return publicData;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setPublicData(Boolean publicData) {
		this.publicData = publicData;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(final ImageType it) {
		imageType = it;
	}

	public void addImageType(final String imageTypeName) {
		final ImageType imageType = new ImageType();
		imageType.setImageType(imageTypeName);
		setImageType(imageType);
	}

	public Set<ImageComment> getComments() {
		if (comments == null)
			comments = new HashSet<ImageComment>();
		return comments;
	}

	public void setComments(Set<ImageComment> comments) {
		this.comments = comments;
	}

	public void addComment(final String comment) {
		if (comments == null)
			comments = new HashSet<ImageComment>();
		ImageComment sc = new ImageComment();
		sc.setImage(this);
		sc.setText(comment);
		this.comments.add(sc);
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Subsample getSubsample() {
		return Subsample;
	}

	public void setSubsample(final Subsample s) {
		Subsample = s;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public String getChecksumMobile(){
		return checksumMobile;
	}
	public void setChecksumMobile(final String s){
		checksumMobile= s;
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

	public String get64x64ServerPath() {
		return "image/?checksum="
				+ this.getChecksum64x64();
	}

	public String getHalfServerPath() {
		return "image/?checksum="
				+ this.getChecksumHalf();
	}

	public String getServerPath() {
		return "image/?checksum="
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

	public Boolean isPublicData() {
		return publicData;
	}
	
	@Override
	public String toString() {
		return filename;
	}
}
