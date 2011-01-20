package edu.rpi.metpetrest.model;

public class SampleData {

	private Long sampleId;
	private String sampleNumber;
	private String title;
	private String author;
	private String description;
	private String owner;

	public SampleData() {
		this.title = "";
		this.author = "";
		this.description = "";
		this.owner = "";
	}

	public SampleData(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public Long getSampleId() {
		return sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

}
