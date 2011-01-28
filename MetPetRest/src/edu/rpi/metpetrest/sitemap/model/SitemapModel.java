package edu.rpi.metpetrest.sitemap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

@XmlRootElement(name = "urlset")
public class SitemapModel {

	private List<Url> urls = new ArrayList<Url>();

	private Map<QName, Object> any;

	public SitemapModel() {

		any = new HashMap<QName, Object>();

		any.put(new QName("xmlns"),
				"http://www.sitemaps.org/schemas/sitemap/0.9");

	}
	
	@XmlAnyAttribute()
	public Map<QName, Object> getAny() {

		return any;
	}

	public void addUrl(Url url) {
		urls.add(url);
	}

	@XmlElement(name = "url")
	public List<Url> getUrls() {
		return urls;
	}
}
