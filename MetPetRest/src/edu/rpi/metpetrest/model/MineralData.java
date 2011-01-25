package edu.rpi.metpetrest.model;

import java.io.Serializable;

public class MineralData implements Serializable{

	private String name;
	
	public MineralData()
	{
		
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
