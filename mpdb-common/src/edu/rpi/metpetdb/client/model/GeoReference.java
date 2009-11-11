package edu.rpi.metpetdb.client.model;

public class GeoReference  extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;
	
	private short id;
	private String title;
	private String first_author;
	private String second_authors;
	private String journal_name;
	private String full_text;
	
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
	
	public String getFirst_author(){
		return first_author;
	}
	
	public void setFirst_author(final String a){
		first_author = a;
	}
	
	public String getSecond_authors(){
		return second_authors;
	}
	
	public void setSecond_authors(final String a){
		second_authors = a;
	}
	
	public String getJournal_name(){
		return journal_name;
	}
	
	public void setJournal_name(final String j){
		journal_name = j;
	}
	
	public String getFull_text(){
		return full_text;
	}
	
	public void setFull_text(final String t){
		full_text = t;
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
