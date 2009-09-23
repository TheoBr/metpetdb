package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Point;

public abstract class GraphicObject3D {
	
	public GraphicObject3D(){

	}
	
	protected Point projectPoint(Rotation theta, Point3D p, Point3D c){
		
		double dx = (Math.cos(theta.getY())*((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX())))-
							(Math.sin(theta.getY())*p.getZ());
		
		double dy = (Math.sin(theta.getX())*((Math.cos(theta.getY())*p.getZ())+Math.sin(theta.getY())*
							((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX()))))+
							(Math.cos(theta.getX())*((Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX())));
		
		double dz = (Math.cos(theta.getX())*((Math.cos(theta.getY())*p.getZ())+Math.sin(theta.getY())*
				((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX()))))-
				(Math.sin(theta.getX())*((Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX())));
		
		
		return new Point((dx-c.getX())*(1-c.getZ()),(dy-c.getY())*(1-c.getZ()));
	}
	
	protected Point3D projectPoint3D(Rotation theta, Point3D p, Point3D c){
		
		double dx = (Math.cos(theta.getY())*((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX())))-
							(Math.sin(theta.getY())*p.getZ());
		
		double dy = (Math.sin(theta.getX())*((Math.cos(theta.getY())*p.getZ())+Math.sin(theta.getY())*
							((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX()))))+
							(Math.cos(theta.getX())*((Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX())));
		
		double dz = (Math.cos(theta.getX())*((Math.cos(theta.getY())*p.getZ())+Math.sin(theta.getY())*
				((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ())*p.getX()))))-
				(Math.sin(theta.getX())*((Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX())));
		
		
		return new Point3D((dx-c.getX())*(1-c.getZ()),(dy-c.getY())*(1-c.getZ()),dz);
	}
	
}
