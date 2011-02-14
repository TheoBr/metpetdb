package edu.rpi.metpetrest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name = "EarthChemModel")
public class EarthChemModel {

	private List<EarthChemSample> samples = new ArrayList<EarthChemSample>();

	@XmlAttribute(name = "firstResultPosition")
	int firstResultPosition = 0;
	@XmlAttribute(name = "majordateupdated")
	int majordateupdated = 0;
	@XmlAttribute(name = "totalResultsAvailable")
	int totalResultsAvailable = 0;
	@XmlAttribute(name = "totalResultsReturned")
	int totalResultsReturned = 0;

	private Map<QName, Object> any;

	public EarthChemModel() {
		any = new HashMap<QName, Object>();

		any.put(new QName("xmlns"),
				"http://www.earthchemportal.org/schema");
		any.put(new QName("xmlns:xsi"),
				"http://www.w3.org/2001/XMLSchema-instance");
		any.put(new QName("xsi:schemaLocation"),
				"http://www.earthchemportal.org/schema http://www.earthchemportal.org/schema/earthchem_schema.xsd");
	}

	@XmlAnyAttribute()
	public Map<QName, Object> getAny() {

		return any;
	}

	public void addEarthChemSample(EarthChemSample sample) {
		this.samples.add(sample);
	}

	@XmlElement(name = "EarthChemSample")
	public List<EarthChemSample> getSamples() {
		return samples;
	}

	public void setSamples(List<EarthChemSample> samples) {
		this.samples = samples;
	}

}
