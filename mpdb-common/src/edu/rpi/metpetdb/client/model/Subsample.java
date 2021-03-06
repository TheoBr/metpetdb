package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.client.model.interfaces.HasImages;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;

@Indexed
public class Subsample extends MObject implements HasOwner, PublicData, HasSample, HasImages {
	private static final long serialVersionUID = 1L;

	@DocumentId
	private long id;

	//@ContainedIn
	@IndexedEmbedded(prefix = "sample_")
	private Sample sample;

	private int version;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;

	private SubsampleType type;

	private Set<Image> images;

	private Grid grid;
	
	@Field(index = Index.UN_TOKENIZED)
	private Boolean publicData;
	
	@IndexedEmbedded(depth = 1, prefix = "user_")
	private User owner;

	//@IndexedEmbedded(prefix = "chemicalAnalysis_")
	private Set<ChemicalAnalysis> chemicalAnalysis;
	private int imageCount;
	private int analysisCount;
	private Long sampleId;

	private String sampleName;

	public long getId() {
		return id;
	}

	public void setId(final long i) {
		id = i;
	}

	/**
	 * Lazy loaded
	 */
	public Sample getSample() {
		return sample;
	}

	public void setSample(final Sample s) {
		sample = s;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(final String sampleName) {
		this.sampleName = sampleName;
	}

	public Long getSampleId() {
		return sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
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

	public SubsampleType getSubsampleType() {
		return type;
	}

	public void setSubsampleType(SubsampleType t) {
		type = t;
	}

	public void addSubsampleType(String type) {
		final SubsampleType st = new SubsampleType();
		st.setSubsampleType(type);
		setSubsampleType(st);
	}

	public void setImages(final Set<Image> s) {
		images = s;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void addImage(Image i) {
		i.setSubsample(this);
		if (images == null)
			images = new HashSet<Image>();
		images.add(i);
	}

	public Set<ChemicalAnalysis> getChemicalAnalyses() {
		return chemicalAnalysis;
	}

	public void setChemicalAnalyses(final Set<ChemicalAnalysis> s) {
		chemicalAnalysis = s;
	}

	public void addChemicalAnalysis(ChemicalAnalysis ma) {
		ma.setSubsample(this);
		if (chemicalAnalysis == null)
			chemicalAnalysis = new HashSet<ChemicalAnalysis>();
		chemicalAnalysis.add(ma);
	}

	public void setGrid(final Grid g) {
		grid = g;
	}

	public Grid getGrid() {
		return grid;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(final int i) {
		imageCount = i;
	}

	public int getAnalysisCount() {
		return analysisCount;
	}

	public void setAnalysisCount(final int i) {
		analysisCount = i;
	}

	public boolean equals(final Object o) {
		return o instanceof Subsample && id == ((Subsample) o).id;
	}

	public int hashCode() {
		return name != null ? name.toLowerCase().hashCode() + (int) id : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	public Boolean isPublicData() {
		if (publicData != null)
			return publicData.booleanValue();
		else
			return false;
	}

	public void setPublicData(final Boolean p) {
		publicData = p;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(final User u) {
		owner = u;
	}
}
