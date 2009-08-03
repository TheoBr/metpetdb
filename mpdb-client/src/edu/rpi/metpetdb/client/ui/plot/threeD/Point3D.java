package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Point;

public class Point3D extends GraphicObject3D {
	private double x;
	private double y;
	private double z;
	
	public Point3D(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Point3D(double x, double y, double z){
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
	
	public Point getProjection(Rotation theta){
		return projectPoint(theta,this);
	}
}
