package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.interfaces.IHasImages;

public class Subsample extends MObject implements IHasImages {
	public static final int P_name = 0;
	public static final int P_type = 1;
	public static final int P_images = 2;
	public static final int P_imageCount = 3;
	public static final int P_analysisCount = 4;
	public static final int P_sampleName = 5;
	
	private long id;
	private Sample sample;
	private int version;
	private String name;
	private String type;
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.Image>
	 */
	private Set images;
	private Grid grid;
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.MineralAnalysis>
	 */
	private Set mineralAnalyses;
	private int imageCount;
	private int analysisCount;

	public long getId() {
		return id;
	}
	public void setId(final long i) {
		id = i;
	}

	public Sample getSample() {
		return sample;
	}
	public void setSample(final Sample s) {
		sample = s;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public String getName() {
		return name;
	}

	public void setName(final String s) {
		name = s;
	}
	
	public String getType(){
		return type;
	}

	public void setType(String t){
		type = t;
	}
	
	public void setImages(final Set s) {
		images = s;
	}

	public Set getImages() {
		return images;
	}

	public void addImage(Image i) {
		i.setSubsample(this);
		this.getSample().addImage(i);
		if (images == null)
			images = new HashSet();
		images.add(i);
	}

	public Set getMineralAnalyses() {
		return mineralAnalyses;
	}
	public void setMineralAnalyses(final Set s) {
		mineralAnalyses = s;
	}
	public void addMineralAnalysis(MineralAnalysis ma) {
		ma.setSubsample(this);
		if (mineralAnalyses == null)
			mineralAnalyses = new HashSet();
		mineralAnalyses.add(ma);
	}

	public void setGrid(final Grid g) {
		grid = g;
	}

	public Grid getGrid() {
		return grid;
	}
	
	public int getImageCount(){
		return imageCount;
	}
	public void setImageCount(final int i){
		imageCount = i;
	}
	
	public int getAnalysisCount(){
		return analysisCount;
	}
	public void setAnalysisCount(final int i){
		analysisCount = i;
	}
	
	public boolean equals(final Object o) {
		return o instanceof Subsample && id == ((Subsample) o).id;
	}

	public int hashCode() {
		return name != null ? name.hashCode() + (int) id : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_name :
				if (newValue != GET_ONLY)
					setName((String) newValue);
				return getName();
			case P_type :
				if (newValue != GET_ONLY)
					setType((String) newValue);
				return getType();
			case P_images :
				if (newValue != GET_ONLY)
					setImages((Set) newValue);
				return getImages();
			case P_imageCount : 
				if (newValue != GET_ONLY)
					setImageCount(Integer.parseInt(newValue.toString()));
				return new Integer(getImageCount());
			case P_analysisCount : 
				if (newValue != GET_ONLY)
					setAnalysisCount(Integer.parseInt(newValue.toString()));
				return new Integer(getAnalysisCount());
			case P_sampleName :
				return this.getSample().getName();
		}
		throw new InvalidPropertyException(propertyId);
	}
}