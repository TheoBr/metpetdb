package edu.rpi.metpetrest.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Citation {
	@XmlAttribute(name = "journal")
	private String journalName = null;

	@XmlTransient()
	private PublicationData publication = null;

	private SampleType sampleType = null;

	public Citation() {

	}

	public Citation(PublicationData publication, SampleType sampleType) {

		this.publication = publication;
		this.journalName = this.publication.getJournalName();

		this.sampleType = sampleType;
	}

	@XmlElement(name = "Title")
	public String getTitle() {
		return this.publication.getTitle();
	}

	@XmlElement(name = "Author")
	public String getAuthor() {
		return this.publication.getAuthor();
	}

	@XmlElement(name = "Author")
	public String getSecondAuthor() {
		return this.publication.getSecondAuthors();
	}

	@XmlElement(name = "SampleType")
	public SampleType getSampleType() {
		return this.sampleType;
	}

}
