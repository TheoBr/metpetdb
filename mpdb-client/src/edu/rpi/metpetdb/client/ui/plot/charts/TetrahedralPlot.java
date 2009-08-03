package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.gfx.Circle;
import com.objetdirect.tatami.client.gfx.GraphicCanvas;
import com.objetdirect.tatami.client.gfx.Line;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;
import edu.rpi.metpetdb.client.ui.plot.threeD.Line3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Point3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Rotation;

public class TetrahedralPlot extends MPlot{
	private FlowPanel container;
	private static double sin60 = .866;
	private static double cos60 = .5;
	private static double tan60 = 1.732;
	private GraphicCanvas canvas;
	private Rotation theta;
	
	public TetrahedralPlot(){
		super();
		container = new FlowPanel();
		canvas = new GraphicCanvas();
		container.add(canvas);
		theta = new Rotation(0,0,0);
		canvas.setPixelSize(600,600);
		drawSkeleton();
	}
	
	public Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas){
		return container;
	}
	
	public void setRotation(double x, double y, double z){
		theta.setX((Math.PI/180)*x);
		theta.setY((Math.PI/180)*y);
		theta.setZ((Math.PI/180)*z);
		drawSkeleton();
	}
	
	private void drawSkeleton(){
		canvas.clear();
		Point3D bottomLeft = new Point3D(200,300,0);
		Point3D bottomRight = new Point3D(300,300,0);
		Point3D bottomBack = new Point3D(250,300,86);
		Point3D top = new Point3D(250,214,86);
		Line3D baseFront = new Line3D(bottomLeft,bottomRight);
		Line3D baseLeft = new Line3D(bottomLeft,bottomBack);
		Line3D baseRight = new Line3D(bottomRight,bottomBack);
		Line3D upperLeft = new Line3D(bottomLeft,top);
		Line3D upperRight = new Line3D(bottomRight,top);
		Line3D upperBack = new Line3D(bottomBack,top);
		Line lBaseFront = baseFront.getGraphicObject(theta);
		Line lBaseLeft = baseLeft.getGraphicObject(theta);
		Line lBaseRight = baseRight.getGraphicObject(theta);
		Line lUpperLeft = upperLeft.getGraphicObject(theta);
		Line lUpperRight = upperRight.getGraphicObject(theta);
		Line lUpperBack = upperBack.getGraphicObject(theta);
		canvas.add(lBaseFront, 0, 0);
		canvas.add(lBaseLeft, 0, 0);
		canvas.add(lBaseRight, 0, 0);
		canvas.add(lUpperLeft, 0, 0);
		canvas.add(lUpperRight, 0, 0);
		canvas.add(lUpperBack, 0, 0);
	}
	
	public int getAxisCount(){
		return 4;
	}
}
