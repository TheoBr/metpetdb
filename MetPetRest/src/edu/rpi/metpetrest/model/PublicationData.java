package edu.rpi.metpetrest.model;

public class PublicationData implements Comparable<PublicationData>{

	
	private String referenceId;
	private String title;
	private String author;
	private String secondAuthors;
	private String journalName;
	private String abstractTxt;
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
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
	public String getSecondAuthors() {
		return secondAuthors;
	}
	public void setSecondAuthors(String secondAuthors) {
		this.secondAuthors = secondAuthors;
	}
	public String getJournalName() {
		return journalName;
	}
	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}
	public String getAbstractTxt() {
		return abstractTxt;
	}
	public void setAbstractTxt(String abstractTxt) {
		this.abstractTxt = abstractTxt;
	}
	@Override
	public int compareTo(PublicationData o) {
		if(o != null)
		{
			if (o.author != null && this.author != null) 
			{
				return this.author.compareTo(o.author);
			}
		}
		
		
		return 0;
		
	}
	
}
