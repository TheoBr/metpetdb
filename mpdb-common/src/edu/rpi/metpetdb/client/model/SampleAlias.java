package edu.rpi.metpetdb.client.model;

public class SampleAlias extends MObject{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String alias;
	private Sample sample;
	
	public SampleAlias() {
		
	}
	
	public SampleAlias(final String alias) {
		this.alias = alias;
	}
	
	public void setSample(final Sample s){
		sample = s;
	}
	
	public Sample getSample(){
		return sample;
	}
	
	public void setId(final long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void setAlias(final String alias){
		this.alias = alias;
	}
	
	public String getAlias(){
		return alias;
	}
	
	@Override
	public String toString(){
		return alias;
	}
	
	@Override
	public boolean mIsNew() {
		return false;
	}
}
