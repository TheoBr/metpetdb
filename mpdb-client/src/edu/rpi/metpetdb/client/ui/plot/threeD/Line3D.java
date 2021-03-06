package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Point;

public class Line3D extends GraphicObject3D{
	private Point3D p1;
	private Point3D p2;
	
	public Line3D(){
		p1 = new Point3D();
		p2 = new Point3D();
	}
	
	public Line3D(Point3D p1, Point3D p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Line getGraphicObject(Rotation theta,Point3D camera, double centerX, double centerY){
		Point p1Projection = projectPoint(theta,p1,camera);
		Point p2Projection = projectPoint(theta,p2,camera);
		p1Projection.setX(p1Projection.getX()+centerX);
		p1Projection.setY(p1Projection.getY()+centerY);
		p2Projection.setX(p2Projection.getX()+centerX);
		p2Projection.setY(p2Projection.getY()+centerY);
		return new Line(p1Projection,p2Projection);
	}
	
	public void setStartPoint(Point3D start){
		p1 = start;
	}
	
	public Point3D getStartPoint(){
		return p1;
	}
	
	public void setEndPoint(Point3D end){
		p2 = end;
	}
	
	public Point3D getEndPoint(){
		return p2;
	}
}
