package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.interfaces.IHasImages;

public class SubsampleDTO extends MObjectDTO implements IHasImages {
	public static final int P_name = 0;
	public static final int P_type = 1;
	public static final int P_images = 2;
	public static final int P_imageCount = 3;
	public static final int P_analysisCount = 4;
	public static final int P_sampleName = 5;

	private long id;

	private SampleDTO sample;

	private int version;

	private String name;

	private String type;

	private Set<ImageDTO> images;

	private GridDTO grid;

	private Set<ChemicalAnalysisDTO> chemicalAnalyses;
	private int imageCount;
	private int analysisCount;

	public long getId() {
		return this.id;
	}

	public void setId(final long i) {
		this.id = i;
	}

	public SampleDTO getSample() {
		return this.sample;
	}

	public void setSample(final SampleDTO s) {
		this.sample = s;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int v) {
		this.version = v;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String s) {
		this.name = s;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String t) {
		this.type = t;
	}

	public void setImages(final Set<ImageDTO> s) {
		this.images = s;
	}

	public Set<ImageDTO> getImages() {
		return this.images;
	}

	public void addImage(final ImageDTO i) {
		i.setSubsample(this);
		this.getSample().addImage(i);
		if (this.images == null)
			this.images = new HashSet();
		this.images.add(i);
	}

	public Set<ChemicalAnalysisDTO> getChemicalAnalyses() {
		return this.chemicalAnalyses;
	}

	public void setChemicalAnalyses(final Set<ChemicalAnalysisDTO> s) {
		this.chemicalAnalyses = s;
	}

	public void addChemicalAnalysis(final ChemicalAnalysisDTO ma) {
		ma.setSubsample(this);
		if (this.chemicalAnalyses == null)
			this.chemicalAnalyses = new HashSet();
		this.chemicalAnalyses.add(ma);
	}

	public void setGrid(final GridDTO g) {
		this.grid = g;
	}

	public GridDTO getGrid() {
		return this.grid;
	}

	public int getImageCount() {
		return this.imageCount;
	}

	public void setImageCount(final int i) {
		this.imageCount = i;
	}

	public int getAnalysisCount() {
		return this.analysisCount;
	}

	public void setAnalysisCount(final int i) {
		this.analysisCount = i;
	}

	@Override
	public boolean equals(final Object o) {
		return (o instanceof SubsampleDTO)
				&& (this.id == ((SubsampleDTO) o).id);
	}

	@Override
	public int hashCode() {
		return this.name != null ? this.name.hashCode() + (int) this.id : 0;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean mIsNew() {
		return this.id == 0;
	}

	@Override
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
		case P_name:
			if (newValue != MObjectDTO.GET_ONLY)
				this.setName((String) newValue);
			return this.getName();
		case P_type:
			if (newValue != MObjectDTO.GET_ONLY)
				this.setType((String) newValue);
			return this.getType();
		case P_images:
			if (newValue != MObjectDTO.GET_ONLY)
				this.setImages((Set<ImageDTO>) newValue);
			return this.getImages();
		case P_imageCount:
			if (newValue != MObjectDTO.GET_ONLY)
				this.setImageCount(Integer.parseInt(newValue.toString()));
			return new Integer(this.getImageCount());
		case P_analysisCount:
			if (newValue != MObjectDTO.GET_ONLY)
				this.setAnalysisCount(Integer.parseInt(newValue.toString()));
			return new Integer(this.getAnalysisCount());
		case P_sampleName:
			return this.getSample().getName();
		}
		throw new InvalidPropertyException(propertyId);
	}
}