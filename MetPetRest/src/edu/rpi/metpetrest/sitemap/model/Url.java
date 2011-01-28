package edu.rpi.metpetrest.sitemap.model;

import javax.xml.bind.annotation.XmlElement;


public class Url {

	private String loc;
	private String lastmod;
	private String changefreq;
	private String priority;

	public Url()
	{
		
	}
	
	public Url (String loc, String lastmod, String changefreq, String priority)
	{
		this.loc = loc;
		this.lastmod = lastmod;
		this.changefreq = changefreq;
		this.priority = priority;
	}
	@XmlElement(name = "loc")
	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	@XmlElement(name = "lastmod")
	public String getLastmod() {
		return lastmod;
	}

	public void setLastmod(String lastmod) {
		this.lastmod = lastmod;
	}

	@XmlElement(name = "changefreq")
	public String getChangefreq() {
		return changefreq;
	}

	public void setChangefreq(String changefreq) {
		this.changefreq = changefreq;
	}

	@XmlElement(name = "priority")
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
