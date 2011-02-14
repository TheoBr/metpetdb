package edu.rpi.metpetrest.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"title", "author", "sampleType"})


public class Citation {
	@XmlAttribute(name = "journal")
	private String journalName = null;
	
	@XmlAttribute(name="year")
	private int year = 0;
	
	@XmlAttribute(name="pages")
	private int pages = 0;

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
		return this.publication.getAuthor() + this.publication.getSecondAuthors();
	}

	//@XmlElement(name = "Author")
	@XmlTransient()
	public String getSecondAuthor() {
		return this.publication.getSecondAuthors();
	}

	@XmlElement(name = "Sampletype")
	public SampleType getSampleType() {
		return this.sampleType;
	}

}
