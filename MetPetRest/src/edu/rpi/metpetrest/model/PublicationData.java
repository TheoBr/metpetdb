package edu.rpi.metpetrest.model;

public class PublicationData implements Comparable<PublicationData>{

	
	private String referenceId;
	private String title;
	private String author;
	private String secondAuthors;
	private String journalName;
	private String journalName2;
	private String doi;
	private String publicationYear;
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
	public String getJournalName2() {
		return journalName2;
	}
	public void setJournalName2(String journalName2) {
		this.journalName2 = journalName2;
	}
	public String getDOI()
	{
		return this.doi;
	}
	public void setDOI(String doi)
	{
		this.doi = doi;
	}
	
	public String getPublicationYear()
	{
		return this.publicationYear;
	}
	public void setPublicationYear(String publicationYear)
	{
		this.publicationYear = publicationYear;
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
				if (this.author.equals(o.author))
				{
					if (this.publicationYear.equals(o.publicationYear))
						return this.secondAuthors.compareTo(o.secondAuthors);
					else
						return this.publicationYear.compareTo(o.publicationYear);
				}
				else
				{
					return this.author.compareTo(o.author);
				}
			}
		}
		
		
		return 0;
		
	}
	
}
