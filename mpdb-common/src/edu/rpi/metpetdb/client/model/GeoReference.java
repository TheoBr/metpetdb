package edu.rpi.metpetdb.client.model;

public class GeoReference  extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;
	
	private short id;
	private String title;
	private String authors;
	private String journal_name;
	private String misc_info;
	
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
	
	public String getAuthors(){
		return authors;
	}
	
	public void setAuthors(final String a){
		authors = a;
	}
	
	public String getJournal_name(){
		return journal_name;
	}
	
	public void setJournal_name(final String j){
		journal_name = j;
	}
	
	public String getMisc_info(){
		return misc_info;
	}
	
	public void setMisc_info(final String m){
		misc_info = m;
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
