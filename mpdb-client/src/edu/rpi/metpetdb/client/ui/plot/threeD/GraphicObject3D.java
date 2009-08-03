package edu.rpi.metpetdb.client.ui.plot.threeD;

import com.objetdirect.tatami.client.gfx.Point;

public abstract class GraphicObject3D {
	
	public GraphicObject3D(){

	}
	
	protected Point projectPoint(Rotation theta, Point3D p){
/*		double[][] arrayA = {{1,0,0},
							{0,Math.cos(theta.getX()),Math.sin(theta.getX())},
							{0,-Math.sin(theta.getX()),Math.cos(theta.getX())}};
		Matrix A = new Matrix(arrayA,3,3);
		double[][] arrayB = {{Math.cos(theta.getY()),0,-Math.sin(theta.getY())},
							{0,1,0},
							{Math.sin(theta.getY()),0,Math.cos(theta.getY())}};
		Matrix B = new Matrix(arrayB,3,3);
		double[][] arrayC = {{Math.cos(theta.getZ()),Math.sin(theta.getZ()),0},
							{-Math.sin(theta.getZ()),Math.cos(theta.getX()),0},
							{0,0,1}};
		Matrix C = new Matrix(arrayC,3,3);
		double[][] arrayD = {{p.getX()},
							{p.getY()},
							{p.getZ()}};
		Matrix D = new Matrix(arrayD,3,1);
		
		Matrix translation = A.times(B).times(C).times(D);*/
		
		
		double dx = (Math.cos(theta.getY()) * 
			((Math.sin(theta.getZ())*p.getY())+(Math.cos(theta.getZ()*p.getX())))) -
			(Math.sin(theta.getY())*p.getZ());
		double dy = Math.sin(theta.getX())*((Math.cos(theta.getY())*p.getZ()+
			Math.sin(theta.getY())*(Math.sin(theta.getZ())*p.getY()+Math.cos(theta.getZ())*p.getX()))) +
			(Math.cos(theta.getX()) * (Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX()));
//		double dz = Math.cos(theta.getX())*((Math.cos(theta.getY())*p.getZ()+
//			Math.sin(theta.getY())*(Math.sin(theta.getZ())*p.getY()+Math.cos(theta.getZ())*p.getX()))) -
//			(Math.sin(theta.getX()) * (Math.cos(theta.getZ())*p.getY())-(Math.sin(theta.getZ())*p.getX()));
		
		return new Point(dx,dy);
	}
	
}
