package edu.rpi.metpetdb.client.model;

public class GeoReference  extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String referenceNumber;
	private String title;
	private String firstAuthor;
	private String secondAuthors;
	private String journalName;
	private String fullText;
	private Reference reference;
	private Long referenceId;
	private String doi;
	private String journalName2;
	
	public GeoReference()
	{
		
	}
	
	public GeoReference(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}
	public Long getId(){
		return id;
	}
	
	public void setId(final Long i){
		id = i;
	}
	
	public String getReferenceNumber(){
		return referenceNumber;
	}
	
	public void setReferenceNumber(String r){
		referenceNumber = r;
	}
	
	public String getDOI()
	{
		return doi;
	}
	
	public void setDOI(String r)
	{
		doi = r;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(final String t){
		title = t;
	}
	
	public String getFirstAuthor(){
		return firstAuthor;
	}
	
	public void setFirstAuthor(final String a){
		firstAuthor = a;
	}
	
	public String getSecondAuthors(){
		return secondAuthors;
	}
	
	public void setSecondAuthors(final String a){
		secondAuthors = a;
	}
	
	public String getJournalName(){
		return journalName;
	}
	
	public void setJournalName(final String j){
		journalName = j;
	}
	
	public String getJournalName2(){
		return journalName2;
	}
	
	public void setJournalName2(final String j){
		journalName2 = j;
	}
	
	public String getFullText(){
		return fullText;
	}
	
	public void setFullText(final String t){
		fullText = t;
	}
	
	@Override
	public boolean mIsNew() {
		return id == 0;
	}
	
	public int hashCode() {
		return title != null ? title.hashCode() + (int)id.intValue() : 0;
	}

	public int compareTo(Object g) {
		if(!(g instanceof GeoReference))
			throw new ClassCastException("GeoReference object expected");
		return this.getTitle().compareTo(((GeoReference) g).getTitle());
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

}
