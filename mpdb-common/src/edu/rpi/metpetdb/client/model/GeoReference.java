package edu.rpi.metpetdb.client.model;

public class GeoReference  extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;
	
	private short id;
	private String title;
	private String firstAuthor;
	private String secondAuthors;
	private String journalName;
	private String fullText;
	private String filename;
	
	public short getId(){
		return id;
	}
	
	public void setId(final short i){
		id = i;
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
	
	public String getFullText(){
		return fullText;
	}
	
	public void setFullText(final String t){
		fullText = t;
	}
	
	public String getFilename(){
		return filename;
	}
	
	public void setFilename(final String f){
		filename = f;
	}

	@Override
	public boolean mIsNew() {
		return id == 0;
	}
	
	public int hashCode() {
		return title != null ? title.hashCode() + id : 0;
	}

	public int compareTo(Object g) {
		if(!(g instanceof GeoReference))
			throw new ClassCastException("GeoReference object expected");
		return this.getTitle().compareTo(((GeoReference) g).getTitle());
	}

}
