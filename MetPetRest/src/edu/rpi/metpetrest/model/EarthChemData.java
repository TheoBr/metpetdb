package edu.rpi.metpetrest.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class EarthChemData implements Serializable{

	private MaterialData materialData = null;

	private Citation citation = null;

	public EarthChemData() {

	}

	public EarthChemData(PublicationData publication, MaterialData materialData) {
		this.materialData = materialData;

		this.citation = new Citation(publication, new SampleType(
				materialData.getRockClass(), materialData.getRockName()));
	}

	@XmlElement(name = "Citation")
	public Citation getCitation() {
		return this.citation;
	}

}
