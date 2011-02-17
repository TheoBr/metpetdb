package edu.rpi.metpetrest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(propOrder={"phase", "materials"})


public class SampleType {
	private Phase phase;

	private List<Material> materials = new ArrayList<Material>();

	public SampleType() {
	}

	public SampleType(String rockclass, String rockname) {
		
		this.phase = new Phase(rockclass, rockname);

	}

	@XmlElement(name = "Phase")
	public Phase getPhase() {
		return this.phase;
	}

	@XmlElement(name = "Material", required=true)
	public List<Material> getMaterials() {
		if (materials.size() == 0)
		{
			this.materials.add(new Material(null, "unspecified", null));
		}
		return this.materials;
		
	}

	public void addMinerals(List<String> minerals) {
		for (String currMineral : minerals) {
			if (currMineral != null && !currMineral.equals(""))
				this.materials.add(new Material("", "mineral", currMineral));
		}

	}

	public void addMethod(Method method) {

		Material wholeRockMaterial = new Material("", "whole rock", "");
		this.materials.add(wholeRockMaterial);

		wholeRockMaterial.setMethod(method);

	}

}

class Material {

	@XmlAttribute(name = "materialdescription")
	private String materialdescription;

	@XmlAttribute(name = "materialtype")
	private String materialtype;

	@XmlAttribute(name = "mineralname")
	private String mineralname;

	private Method method;

	public Material() {
	}

	public Material(String materialdescription, String materialtype,
			String mineralName) {
		this.materialdescription = materialdescription;
		this.materialtype = materialtype;
		this.mineralname = mineralName;
	}

	@XmlElement(name = "method")
	public Method getMethod() {
		return this.method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}

class Phase {
	private Rock rock = null;

	public Phase() {

	}

	public Phase(String rockclass, String rockname) {
		this.rock = new Rock(rockclass, rockname);
	}

	@XmlElement(name = "ROCK")
	public Rock getRock() {
		return this.rock;
	}
}

class Rock {
	private String rockclass;
	private String rockname;

	public Rock() {

	}

	public Rock(String rockclass, String rockname) {
		this.rockclass = rockclass;
		this.rockname = rockname;
	}

	@XmlElement(name = "rockclass")
	public String getRockClass() {
		    if (this.rockclass == null)
		    	return "metamorphic";
		    else
		    	return "metamorphic : " + this.rockclass;
	}

	@XmlElement(name = "rockname")
	public String getRockName() {
		return this.rockname;
	}
}