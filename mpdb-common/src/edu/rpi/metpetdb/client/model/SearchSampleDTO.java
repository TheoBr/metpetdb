package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;
import org.postgis.Point;
import org.postgis.Polygon;

import edu.rpi.metpetdb.client.service.MpDbConstants;

public class SearchSampleDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private Set<String> possibleRockTypes = new HashSet<String>();

	private long id;
	private int version;

	private String sesarNumber;
	private Geometry boundingBox;

	private String owner;

	private String alias;

	private DateSpan collectionDateRange;
	private Short datePrecision;

	private Set<SampleMineralDTO> minerals;
	
	private Set<ChemicalAnalysisElementDTO> elements;

	private Set<ChemicalAnalysisOxideDTO> oxides;

	public Set<String> getPossibleRockTypes() {
		return possibleRockTypes;
	}

	public void addPossibleRockType(final String rt) {
		possibleRockTypes.add(rt);
	}

	public void setPossibleRockTypes(final Set<String> rt) {
		possibleRockTypes = rt;
	}

	public long getId() {
		return id;
	}

	public void setId(final long i) {
		id = i;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public String getSesarNumber() {
		return sesarNumber;
	}

	public void setSesarNumber(final String s) {
		sesarNumber = s;
	}

	public Geometry getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(final Geometry g) {
		boundingBox = g;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(final String u) {
		owner = u;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(final String s) {
		alias = s;
	}

	public DateSpan getCollectionDateRange() {
		return collectionDateRange;
	}

	public void setCollectionDateRange(final DateSpan dateRange) {
		collectionDateRange = dateRange;
	}

	public Set<SampleMineralDTO> getMinerals() {
		return minerals;
	}

	public void setMinerals(final Set<SampleMineralDTO> c) {
		minerals = c;
	}

	public void addMineral(final String name) {
		if (minerals == null)
			minerals = new HashSet<SampleMineralDTO>();
		final SampleMineralDTO m = new SampleMineralDTO();
		// m.setName(name);
		minerals.add(m);
	}

	public String getName() {
		if (this.alias != null || !this.alias.equals(""))
			return this.alias;
		else
			return this.sesarNumber;
	}

	public boolean equals(final Object o) {
		return sesarNumber != null && o instanceof SampleDTO
				&& sesarNumber.equals(((SearchSampleDTO) o).sesarNumber);
	}

	public int hashCode() {
		return sesarNumber != null ? sesarNumber.hashCode() : 0;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public short getDatePrecision() {
		if (datePrecision == null)
			datePrecision = Short.parseShort("0");
		return datePrecision;
	}

	public void setDatePrecision(short datePrecision) {
		this.datePrecision = datePrecision;
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
}
