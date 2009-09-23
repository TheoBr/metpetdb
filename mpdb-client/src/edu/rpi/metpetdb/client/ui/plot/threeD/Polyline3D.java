package edu.rpi.metpetdb.client.ui.plot.threeD;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.tatami.client.gfx.Color;
import com.objetdirect.tatami.client.gfx.Point;
import com.objetdirect.tatami.client.gfx.Polyline;

public class Polyline3D {
	List<Point3D> points = new ArrayList<Point3D>();
	
	public Polyline3D(){
		
	}
	
	public Polyline3D(List<Point3D> points){
		this.points = points;
	}
	
	public List<Polyline> getGraphicObject(Rotation theta,Point3D camera, double centerX, double centerY){
		List<Point> pointsArray = new ArrayList<Point>();
		for (Point3D p : points) {
			Point p2d = p.getProjection(theta, camera);
			p2d.setX(p2d.getX()+centerX);
			p2d.setY(p2d.getY()+centerY);
			pointsArray.add(p2d);
		}
		Polyline face1 = new Polyline(new Point[] {pointsArray.get(0),pointsArray.get(1),pointsArray.get(2),pointsArray.get(0)});
		Polyline face2 = new Polyline(new Point[] {pointsArray.get(1),pointsArray.get(2),pointsArray.get(3),pointsArray.get(1)});
		Polyline face3 = new Polyline(new Point[] {pointsArray.get(0),pointsArray.get(2),pointsArray.get(3),pointsArray.get(0)});
		Polyline face4 = new Polyline(new Point[] {pointsArray.get(0),pointsArray.get(1),pointsArray.get(3),pointsArray.get(0)});
		face1.setFillColor(new Color(255,0,0,255));
		face2.setFillColor(new Color(255,0,0,255));
		face3.setFillColor(new Color(255,0,0,255));
		face4.setFillColor(new Color(255,0,0,255));
		List<Polyline> polyline3D = new ArrayList<Polyline>();
		polyline3D.add(face1);
		polyline3D.add(face2);
		polyline3D.add(face3);
		polyline3D.add(face4);
		
		return (polyline3D);
	}
}
