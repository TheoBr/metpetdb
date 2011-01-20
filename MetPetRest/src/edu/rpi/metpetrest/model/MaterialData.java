package edu.rpi.metpetrest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialData {
	
	
	private List<MineralData> mineralData = new ArrayList<MineralData>();
	
	private Map<String, ChemicalData> chemicalData = new HashMap<String, ChemicalData>();
	
	
	private String rockClass = null;
	private String rockName = null;
	
	public MaterialData(String rockClass, String rockName)
	{
		this.rockClass = rockClass;
		this.rockName = rockName;
		
	}
	
	public void addMineral(MineralData mineralData)
	{
		this.mineralData.add(mineralData);		
	}
	
	public void addChemical(String name, ChemicalData chemicalData)
	{
	
		this.chemicalData.put(name, chemicalData);
		
	}

	public String getRockClass() {
		return rockClass;
	}

	public void setRockClass(String rockClass) {
		this.rockClass = rockClass;
	}

	public String getRockName() {
		return rockName;
	}

	public void setRockName(String rockName) {
		this.rockName = rockName;
	}

	public List<MineralData> getMineralData() {
		return mineralData;
	}

	public void setMineralData(List<MineralData> mineralData) {
		this.mineralData = mineralData;
	}

}
