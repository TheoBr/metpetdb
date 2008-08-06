package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

public class ChemicalAnalysisDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
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
	private Short datePrecision;
	private ReferenceDTO reference;
	private String description;
	private MineralDTO mineral;
	private Boolean largeRock;
	private Float total;
	private Set<ChemicalAnalysisElementDTO> elements;
	private Set<ChemicalAnalysisOxideDTO> oxides;

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

	public Short getDatePrecision() {
		if (datePrecision == null)
			datePrecision = Short.parseShort("0");
		return datePrecision;
	}

	public void setDatePrecision(Short datePrecision) {
		this.datePrecision = datePrecision;
	}

	public ReferenceDTO getReference() {
		return reference;
	}

	public void setReference(final ReferenceDTO r) {
		reference = r;
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

	public void setElements(final Set<ChemicalAnalysisElementDTO> e) {
		elements = e;
	}

	public Set<ChemicalAnalysisElementDTO> getElements() {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		return elements;
	}

	public void addElement(final ElementDTO e, final Float amount) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		ChemicalAnalysisElementDTO c = new ChemicalAnalysisElementDTO();
		c.setElement(e);
		c.setAmount(amount);
		elements.add(c);
	}

	public void addElement(final ElementDTO e, final Float amount,
			final Float precision) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElementDTO>();
		ChemicalAnalysisElementDTO c = new ChemicalAnalysisElementDTO();
		c.setElement(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		elements.add(c);
	}

	public void setOxides(final Set<ChemicalAnalysisOxideDTO> o) {
		oxides = o;
	}

	public Set<ChemicalAnalysisOxideDTO> getOxides() {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		return oxides;
	}

	public void addOxide(final OxideDTO e, final Float amount) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		ChemicalAnalysisOxideDTO c = new ChemicalAnalysisOxideDTO();
		c.setOxide(e);
		c.setAmount(amount);
		oxides.add(c);
	}

	public void addOxide(final OxideDTO e, final Float amount,
			final Float precision) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		ChemicalAnalysisOxideDTO c = new ChemicalAnalysisOxideDTO();
		c.setOxide(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		oxides.add(c);
	}

	public boolean equals(final Object o) {
		return o instanceof ChemicalAnalysisDTO
				&& id == ((ChemicalAnalysisDTO) o).id;
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}
}
