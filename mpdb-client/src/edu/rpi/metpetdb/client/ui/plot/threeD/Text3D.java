package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Text;

public class Text3D extends GraphicObject3D {
	private double x;
	private double y;
	private double z;
	private String text;
	
	public Text3D(String text){
		x = 0;
		y = 0;
		z = 0;
		this.text = text;
	}
	
	public Text3D(String text, double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.text = text;
	}
	
	public Text3D(String text, Point3D p){
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
		this.text = text;
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
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	public Text getText(Rotation theta,double centerZ,double sideLength){
		Point3D p = projectPoint3D(theta,new Point3D(x,y,z));
		p.setZ(p.getZ()+centerZ);
		Text t = new Text(text);
		double scale = 2 - ((3/2)*(p.getZ()/(sideLength-centerZ)));
		t.scale(new Double(scale).floatValue());
		return (t);
	}
}