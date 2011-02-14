package edu.rpi.metpetrest.model;

import javax.xml.bind.annotation.XmlElement;


public class Geography {


	private Location location = null;
	
	private String locationPrecision = null;
	
	public Geography()
	{
		
	}
	
	public Geography(double x, double y, Double locationError) {

		location = new Location(x, y);
		locationPrecision = locationError.toString();

	}

	@XmlElement(name="Location")
	public Location getLocation()
	{
		return this.location;
	}
	
	@XmlElement(name="Location_Precision")
	public String getLocationPrecision()
	{
		return this.locationPrecision;
	}
	

}



class Location
{
	private Point point = null;

	public Location()
	{
	}
	
	public Location(double x, double y)
	{
		this.point = new Point(x, y);
	}
	@XmlElement(name="Point")
	public Point getPoint()
	{
		return this.point;
	}
	
}


class Point
{
	private Coord coord = null;
	
	public Point()
	{	
	}
	
	public Point(double x, double y)
	{
		this.coord = new Coord(x, y);
	}
	
	@XmlElement(name="coord")
	public Coord getCoord()
	{
		return this.coord;
	}
}


class Coord 
{
	private double x;
	private double y;
	
	public Coord()
	{
		
	}
	
	public Coord(double x, double y)
	{
		this.x = x;
		this.y = y;
		
	}
	
	@XmlElement(name="X")
	public String getX()
	{
		return Double.toString(x);
	}
	
	@XmlElement(name="Y")
	public String getY()
	{
		return Double.toString(y);
	}
	
	
}
