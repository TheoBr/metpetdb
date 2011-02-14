package edu.rpi.metpetrest.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.postgis.Point;



@XmlRootElement(name="EarthChemSample")
@XmlType(propOrder={"geography", "earthChemData"})

public class EarthChemSample implements Serializable {

	private String igsn;
	
	private String sampleNumber;

	private String sampleId;
	
	private StringBuffer url = new StringBuffer("http://metpetdb.rpi.edu/metpetweb/#sample/");

	private String source = "MetPetDB";
	
	private Point location;

	
	private Double locationPrecision = null;
	
	private EarthChemData earthChemData = null;
	

	@XmlAttribute(name="igsn")
	public String getIgsn() {
		if (igsn != null)
			return igsn;
		else
			return "";
	}

	public void setIgsn(String igsn) {
		this.igsn = igsn;
	}
//No, this is not a mistake
	@XmlAttribute(name="sample_id")
	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
//No, this is not a mistake.  
	@XmlAttribute(name="samplenumber")	
	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;

	}

	@XmlAttribute(name="url")
	public String getUrl() {
		return url.toString() + sampleId.toString();
	}

	public void setUrl(String url) {
		this.url = new StringBuffer(url);
	}
	
	@XmlAttribute(name="source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@XmlElement(name="Geography")
	public Geography getGeography()
	{
		return new Geography(this.location.getX(), this.location.getY(), this.locationPrecision);

	}

	@XmlTransient()
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	@XmlElement(name="EarthChemData")
	public EarthChemData getEarthChemData() {
		return earthChemData;
	}

	public void setEarthChemData(EarthChemData earthChemData) {
		this.earthChemData = earthChemData;
	}

	@XmlTransient()
	public Double getLocationPrecision() {
		if (locationPrecision == null)
			return new Double(1000);
		else
			return locationPrecision;
	}

	public void setLocationPrecision(Double locationPrecision) {
		this.locationPrecision = locationPrecision;
	}
	
	
}
