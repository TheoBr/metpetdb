package edu.rpi.metpetrest.model;

public class ChemicalData {

	private String name;
	private String quality;
	private String type;
	private String units;
	private String value;

	public ChemicalData(String name, String quality, String type, String units,
			String value) {
		super();
		this.name = name;
		this.quality = quality;
		this.type = type;
		this.units = units;
		this.value = value;
	}

	public void toXML(StringBuilder sb) {

		sb.append("<item group=\"chemical\" name=\"" + this.name
				+ "\" qualityrank=\"-1\" type=\"major_oxides\" units=\""
				+ this.units + "\" value=\"" + this.value + "\"/>");

	}

}
