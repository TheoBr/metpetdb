package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class MineralAnalysisDTO extends MObjectDTO {
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
	private ImageDTO image;
	private SubsampleDTO subsample;
	private String analysisMethod;
	private String location;
	private String analyst;
	private Timestamp analysisDate;
	private Integer referenceId;
	private String description;
	private MineralDTO mineral;
	private Boolean largeRock;
	private Set<ElementDTO> elements;
	private Set<OxideDTO> oxides;

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
	
	public SubsampleDTO getSubsample() {
		return subsample;
	}
	public void setSubsample(final SubsampleDTO s) {
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

	public ImageDTO getImage() {
		return image;
	}
	public void setImage(final ImageDTO i) {
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

	public MineralDTO getMineral() {
		return mineral;
	}

	public void setMineral(final MineralDTO m) {
		mineral = m;
	}

	public Boolean getLargeRock() {
		return largeRock;
	}
	public void setLargeRock(Boolean largeRock) {
		this.largeRock = largeRock;
	}
	
	public void setElements(final Set<ElementDTO> e) {
		elements = e;
	}
	public Set<ElementDTO> getElements() {
		if (elements == null)
			elements = new HashSet<ElementDTO>();
		return elements;
	}

	public void setOxides(final Set o) {
		oxides = o;
	}
	public Set getOxides() {
		if (oxides == null)
			oxides = new HashSet();
		return oxides;
	}

	public boolean equals(final Object o) {
		return o instanceof MineralAnalysisDTO && id == ((MineralAnalysisDTO) o).id;
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_spotId :
				if (newValue != GET_ONLY)
					setSpotId((String) newValue);
				return getSpotId();
			case P_pointX :
				if (newValue != GET_ONLY)
					setPointX(Integer.parseInt((String) newValue));
				return String.valueOf(getPointX());
			case P_pointY :
				if (newValue != GET_ONLY)
					setPointY(Integer.parseInt((String) newValue));
				return String.valueOf(getPointY());
			case P_analysisMethod :
				if (newValue != GET_ONLY)
					setAnalysisMethod((String) newValue);
				return getAnalysisMethod();
			case P_location :
				if (newValue != GET_ONLY)
					setLocation((String) newValue);
				return getLocation();
			case P_analyst :
				if (newValue != GET_ONLY)
					setAnalyst((String) newValue);
				return getAnalyst();
			case P_analysisDate :
				if (newValue != GET_ONLY)
					setAnalysisDate((Timestamp) newValue);
				return getAnalysisDate();
			case P_referenceId :
				if (newValue != GET_ONLY)
					setReferenceId(setIntegerValue(newValue));
				return getReferenceId();
			case P_description :
				if (newValue != GET_ONLY)
					setDescription((String) newValue);
				return getDescription();
			case P_mineral :
				if (newValue != GET_ONLY)
					setMineral((MineralDTO) newValue);
				return getMineral();
			case P_subsampleName :
				return getSubsample().getName();
			case P_sampleName :
				return getSubsample().getSample().getAlias();
			case P_image :
				if (newValue != GET_ONLY)
					setImage((ImageDTO) newValue);
				return getImage();
			case P_largeRock :
				if (newValue != GET_ONLY)
					setLargeRock((Boolean) newValue);
				return getLargeRock();
			case P_elements :
				if(newValue != GET_ONLY)
					setElements((Set)newValue);
				return getElements();
			case P_oxides :
				if(newValue != GET_ONLY)
					setOxides((Set)newValue);
				return getOxides();
		}
		throw new InvalidPropertyException(propertyId);
	}
}