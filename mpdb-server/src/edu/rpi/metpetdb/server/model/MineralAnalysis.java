package edu.rpi.metpetdb.server.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

public class MineralAnalysis extends MObject {
	private static final long serialVersionUID = 1L;
	public static final int P_spotId = 0;
	public static final int P_pointX = 1;
	public static final int P_pointY = 2;
	public static final int P_analysisMethod = 3;
	public static final int P_location = 4;
	public static final int P_analyst = 5;
	public static final int P_analysisDate = 6;
	public static final int P_referenceId = 7;
	public static final int P_description = 8;
	public static final int P_mineral = 9;
	public static final int P_sampleName = 10;
	public static final int P_subsampleName = 11;
	public static final int P_image = 12;
	public static final int P_largeRock = 13;
	public static final int P_elements = 14;
	public static final int P_oxides = 15;

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
	private Integer referenceId;
	private String description;
	private Mineral mineral;
	private Boolean largeRock;
	private Set<Element> elements;
	private Set<Oxide> oxides;

	private transient Widget actualImage;
	private transient float percentX;
	private transient float percentY;
	private transient boolean isLocked;

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

	public Widget getActualImage() {
		return actualImage;
	}

	public void setActualImage(final Widget w) {
		actualImage = w;
	}

	public float getPercentX() {
		return percentX;
	}

	public void setPercentX(final float f) {
		percentX = f;
	}

	public float getPercentY() {
		return percentY;
	}

	public void setPercentY(final float f) {
		percentY = f;
	}

	public boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(final boolean b) {
		isLocked = b;
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

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(final Integer r) {
		referenceId = r;
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
		return o instanceof MineralAnalysis && id == ((MineralAnalysis) o).id;
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