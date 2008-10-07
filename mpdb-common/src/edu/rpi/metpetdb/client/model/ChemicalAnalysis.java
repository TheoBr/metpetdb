package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.HasDate;

public class ChemicalAnalysis extends MObject implements HasDate {
	private static final long serialVersionUID = 1L;

	private int id;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String spotId;
	private int pointX;
	private int pointY;
	private Image image;
	@ContainedIn
	private Subsample subsample;
	private String analysisMethod;
	private String location;
	private String analyst;
	private Timestamp analysisDate;
	private Short datePrecision;
	private String description;
	private Reference reference;
	private Mineral mineral;
	private Boolean largeRock;
	private Float total;
	@IndexedEmbedded(prefix = "elements_")
	private Set<ChemicalAnalysisElement> elements;
	@IndexedEmbedded(prefix = "oxides_")
	private Set<ChemicalAnalysisOxide> oxides;

	private String SubsampleName;
	private String sampleName;

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

	public String getSubsampleName() {
		return SubsampleName;
	}

	public void setSubsampleName(final String SubsampleName) {
		this.SubsampleName = SubsampleName;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(final String sampleName) {
		this.sampleName = sampleName;
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

	public Short getDatePrecision() {
		return datePrecision;
	}

	public void setDatePrecision(Short datePrecision) {
		this.datePrecision = datePrecision;
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

	public void setElements(final Set<ChemicalAnalysisElement> e) {
		elements = e;
	}

	public void addElement(final Element e, final Float amount) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElement>();
		ChemicalAnalysisElement c = new ChemicalAnalysisElement();
		c.setElement(e);
		c.setAmount(amount);
		elements.add(c);
	}

	public void addElement(final Element e, final Float amount,
			final Float precision) {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElement>();
		ChemicalAnalysisElement c = new ChemicalAnalysisElement();
		c.setElement(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		elements.add(c);
	}

	public Set<ChemicalAnalysisElement> getElements() {
		if (elements == null)
			elements = new HashSet<ChemicalAnalysisElement>();
		return elements;
	}

	public void setOxides(final Set<ChemicalAnalysisOxide> o) {
		oxides = o;
	}

	public Set<ChemicalAnalysisOxide> getOxides() {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxide>();
		return oxides;
	}

	public void addOxide(final Oxide e, final Float amount) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxide>();
		ChemicalAnalysisOxide c = new ChemicalAnalysisOxide();
		c.setOxide(e);
		c.setAmount(amount);
		oxides.add(c);
	}

	public void addOxide(final Oxide e, final Float amount,
			final Float precision) {
		if (oxides == null)
			oxides = new HashSet<ChemicalAnalysisOxide>();
		ChemicalAnalysisOxide c = new ChemicalAnalysisOxide();
		c.setOxide(e);
		c.setAmount(amount);
		c.setPrecision(precision);
		oxides.add(c);
	}

	public Widget getActualImage() {
		return actualImage;
	}

	public void setActualImage(Widget actualImage) {
		this.actualImage = actualImage;
	}

	public float getPercentX() {
		return percentX;
	}

	public void setPercentX(float percentX) {
		this.percentX = percentX;
	}

	public float getPercentY() {
		return percentY;
	}

	public void setPercentY(float percentY) {
		this.percentY = percentY;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
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

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Float getTotal() {
		return total;
	}

	public void setTotal(Float total) {
		this.total = total;
	}

	public Timestamp getDate() {
		return getAnalysisDate();
	}

	public void setDate(Timestamp date) {
		setAnalysisDate(date);
	}
}
