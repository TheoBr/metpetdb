package edu.rpi.metpetdb.client.ui.plot.threeD;



/**
 * Camera Rotation
 * @author Dennis
 *
 */
public class Rotation {
	private double x;
	private double y;
	private double z;
	
	public Rotation(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Rotation(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getX(){
		return x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getY(){
		return y;
	}
	
	public void setZ(double z){
		this.z = z;
	}
	
	public double getZ(){
		return z;
	}
}
