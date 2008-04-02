package edu.rpi.metpetdb.server.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class ChemicalAnalysis extends MObject {
	private static final long serialVersionUID = 1L;

	private int id;
	private String spotId;
	private int pointX;
	private int pointY;
	private Image image;
	private Subsample subsample;
	private String analysisMethod;
	private String location;
	private String analyst;
	private Timestamp analysisDate;
	private String description;
	private Mineral mineral;
	private Boolean largeRock;
	private Set<Element> elements;
	private Set<Oxide> oxides;

	public int getId() {
		return id;
	}

	public void setId(final int i) {
		id = i;
	}

	public String getSpotId() {
		return spotId;
	}

	public void setSpotId(final String i) {
		spotId = i;
	}

	public Subsample getSubsample() {
		return subsample;
	}

	public void setSubsample(final Subsample s) {
		subsample = s;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(final int i) {
		pointX = i;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(final int i) {
		pointY = i;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(final Image i) {
		image = i;
	}

	public String getAnalysisMethod() {
		return analysisMethod;
	}

	public void setAnalysisMethod(final String m) {
		analysisMethod = m;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(final String l) {
		location = l;
	}

	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(final String a) {
		analyst = a;
	}

	public Timestamp getAnalysisDate() {
		return analysisDate;
	}

	public void setAnalysisDate(Timestamp d) {
		analysisDate = d;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String d) {
		description = d;
	}

	public Mineral getMineral() {
		return mineral;
	}

	public void setMineral(final Mineral m) {
		mineral = m;
	}

	public Boolean getLargeRock() {
		return largeRock;
	}

	public void setLargeRock(Boolean largeRock) {
		this.largeRock = largeRock;
	}

	public void setElements(final Set<Element> e) {
		elements = e;
	}

	public Set<Element> getElements() {
		if (elements == null)
			elements = new HashSet<Element>();
		return elements;
	}

	public void setOxides(final Set<Oxide> o) {
		oxides = o;
	}

	public Set<Oxide> getOxides() {
		if (oxides == null)
			oxides = new HashSet<Oxide>();
		return oxides;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof ChemicalAnalysis && id == ((ChemicalAnalysis) o).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean mIsNew() {
		return id == 0;
	}
}
