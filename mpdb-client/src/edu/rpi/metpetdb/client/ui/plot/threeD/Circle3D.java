package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Circle;
import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Point;
import com.objetdirect.tatami.client.gfx.Text;

public class Circle3D extends GraphicObject3D {
	private double x;
	private double y;
	private double z;
	private double radius;
	
	public Circle3D(double radius){
		x = 0;
		y = 0;
		z = 0;
		this.radius = radius;
	}
	
	public Circle3D(double radius, double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
	}
	
	public Circle3D(double radius, Point3D p){
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
		this.radius = radius;
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
	
	public void setRadius(double radius){
		this.radius = radius;
	}
	
	public double getRadius(){
		return radius;
	}
	
	public Point getDisplayPosition(Rotation theta, Point3D camera, double centerX, double centerY){
		Point p = projectPoint(theta,new Point3D(x,y,z),camera);
		p.setX(p.getX()+centerX);
		p.setY(p.getY()+centerY);
		return new Point(p.getX(),p.getY());
	}
	
	public double getDisplayRadius(Rotation theta,Point3D camera, double centerZ,double sideLength){
		Point3D p = projectPoint3D(theta,new Point3D(x,y,z),camera);
		p.setZ(p.getZ()+centerZ);
		double scale = 2 - ((3/2)*(p.getZ()/(sideLength-centerZ)));
		double newradius = radius*(1-camera.getZ());
		return newradius*scale;
	}
	
}
