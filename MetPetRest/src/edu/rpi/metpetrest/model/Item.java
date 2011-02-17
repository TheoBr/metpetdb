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
		if (name != null)
			this.name = name.toUpperCase();

		this.qualityrank = qualityrank;
		
		if (type!= null && type.equals("oxides"))
			this.type = "major_oxides";
		else			
			this.type = type;
		//end if
		
		this.units = units;
		this.value = value;
	}

	public String getName() {
		return name;
	}

}
