package edu.rpi.metpetrest.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Item {

	public Item() {

	}

	@XmlAttribute(name = "group")
	private String group;

	@XmlAttribute(name = "name")
	private String name;

	@XmlAttribute(name = "qualityrank")
	private String qualityrank;

	@XmlAttribute(name = "type")
	private String type;

	@XmlAttribute(name = "units")
	private String units;

	@XmlAttribute(name = "value")
	private String value;

	public Item(String group, String name, String qualityrank, String type,
			String units, String value) {
		super();
		this.group = group;
		this.name = name;
		this.qualityrank = qualityrank;
		this.type = type;
		this.units = units;
		this.value = value;
	}

	public String getName() {
		return name;
	}

}
