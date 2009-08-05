package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.gfx.Circle;
import com.objetdirect.tatami.client.gfx.GraphicCanvas;
import com.objetdirect.tatami.client.gfx.GraphicObject;
import com.objetdirect.tatami.client.gfx.GraphicObjectListener;
import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Text;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaElement;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaOxide;
import edu.rpi.metpetdb.client.ui.plot.threeD.Line3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Point3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Rotation;
import edu.rpi.metpetdb.client.ui.plot.threeD.Text3D;

public class TetrahedralPlot extends MPlot{
	private FlowPanel container;
	private static double sin60 = .866;
	private static double cos60 = .5;
	private static double tan60 = 1.732;
	private GraphicCanvas canvas;
	public Rotation theta;
	
	private double leftBoundary;
	private double sideLength;
	private double top;
	private double height;
	private double centerX;
	private double centerY;
	private double centerZ;
	
	private boolean mouseDown = false;
	private double lastX = -1;
	private double lastY = -1;
	
	private Set<Circle> points = new HashSet<Circle>();
	private Text labelBottomLeft;
	private Text labelBottomRight;
	private Text labelBottomBack;
	private Text labelTop;
	
	public TetrahedralPlot(final int width, final int height, final int sideLength){
		super();
		container = new FlowPanel();
		canvas = new GraphicCanvas();
		container.add(canvas);
		theta = new Rotation(-12*(Math.PI/180),-30*(Math.PI/180),0);
		canvas.setPixelSize(width,height);
		
		leftBoundary = (width-sideLength)/2;
		this.sideLength = sideLength;
		this.height = Math.sqrt(Math.pow(sideLength,2)-Math.pow(sideLength/2,2));
		top = (height-this.height)/2;
		
		centerX = leftBoundary+(sideLength/2);
		centerY = top+(this.height/2);
		centerZ = this.height-((sideLength/2)/sin60);
		
		drawSkeleton();
		setupMouseListener();
	}
	
	private void setupMouseListener(){
		canvas.addGraphicObjectListener(new GraphicObjectListener(){

			public void mouseClicked(GraphicObject graphicObject, Event event) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDblClicked(GraphicObject graphicObject, Event event) {
				// TODO Auto-generated method stub
				
			}

			public void mouseMoved(GraphicObject graphicObject, Event event) {
				if (mouseDown){
					double sensitivity = 3;
					double dx = event.getClientX()-lastX;
					double dy = event.getClientY()-lastY;
					dx/=(sideLength*sensitivity);
					dy/=(sideLength*sensitivity);
					dx*=360;
					dy*=360;
					
					theta.setY(theta.getY()+((Math.PI/180)*((dx*Math.cos(theta.getX())))));//+(dx*Math.sin(theta.getX())))));
					theta.setX(theta.getX()+((Math.PI/180)*((-dy*Math.cos(theta.getY())))));//-(dy*Math.sin(theta.getY())))));//+(dy*Math.sin(theta.getY())));
					//theta.setZ(theta.getZ()+((Math.PI/180)*(((+dx*Math.sin(theta.getX()))))));//+(dy*Math.sin(theta.getY())))));
					onRotate();
					lastX = event.getClientX();
					lastY = event.getClientY();
				}
			}

			public void mousePressed(GraphicObject graphicObject, Event event) {
				// TODO Auto-generated method stub
				mouseDown = true;
				lastX = event.getClientX();
				lastY = event.getClientY();
			}

			public void mouseReleased(GraphicObject graphicObject, Event event) {
				// TODO Auto-generated method stub
				mouseDown = false;
				lastX = -1;
				lastY = -1;
			}
			
		});
	}
	
	public Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas){
		if (formulas.size() != 4){
			return container;
		}
		
		AxisFormula formulaBottomLeft = formulas.get(0);
		AxisFormula formulaBottomRight = formulas.get(1);
		AxisFormula formulaBottomBack = formulas.get(2);
		AxisFormula formulaTop = formulas.get(3);
		
		for (Circle point : points){
			canvas.remove(point);
		}
		for (ChemicalAnalysis ca : data){
			float bottomLeftAxisTotal = new Double(formulaBottomLeft.getConstant()).floatValue();
			float bottomRightAxisTotal = new Double(formulaBottomRight.getConstant()).floatValue();
			float bottomBackAxisTotal = new Double(formulaBottomBack.getConstant()).floatValue();
			float topAxisTotal = new Double(formulaTop.getConstant()).floatValue();
			Iterator<ChemicalAnalysisOxide> itr = ca.getOxides().iterator();
			while (itr.hasNext()){
				ChemicalAnalysisOxide o = itr.next();
				for (AxisFormulaOxide i : formulaBottomLeft.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomLeftAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaOxide i : formulaBottomRight.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomRightAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaOxide i : formulaBottomBack.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomBackAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaOxide i : formulaTop.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						topAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
			}
			Iterator<ChemicalAnalysisElement> itr2 = ca.getElements().iterator();
			while (itr2.hasNext()){
				ChemicalAnalysisElement o = itr2.next();
				for (AxisFormulaElement i : formulaBottomLeft.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomLeftAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaElement i : formulaBottomRight.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomRightAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaElement i : formulaBottomBack.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomBackAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
				for (AxisFormulaElement i : formulaTop.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						topAxisTotal += i.getCoefficient()*o.getAmount();
					}
				}
			}
			float sum = bottomLeftAxisTotal+bottomRightAxisTotal+bottomBackAxisTotal+topAxisTotal;
			if (sum != 0){
//				double tempX = leftBoundary+((bottomAxisTotal/sum)*sideLength);
//				double tempY = top+(sideLength*sin60*(1-(rightAxisTotal/sum)));
//				double y = tempY;
//				double x = tempX+(top+(sideLength*sin60)-tempY)/tan60;
//				Circle point = new Circle(3);
//				points.add(point);
//				canvas.add(point, (int)x, (int)y);
				
			}
		}
		return container;
	}
	
	public void setRotation(double x, double y, double z){
		theta.setX(((Math.PI/180)*x)%(2*Math.PI));
		theta.setY(((Math.PI/180)*y)%(2*Math.PI));
		theta.setZ(((Math.PI/180)*z)%(2*Math.PI));
		drawSkeleton();
	}
	
	public void onRotate(){
		theta.setX(theta.getX()%(2*Math.PI));
		theta.setY(theta.getY()%(2*Math.PI));
		theta.setZ(theta.getZ()%(2*Math.PI));
		drawSkeleton();
	}
	
	private Point3D translateToRotationOrigin(final Point3D p){
		p.setX(p.getX()-centerX);
		p.setY(p.getY()-centerY);
		p.setZ(p.getZ()-centerZ);
		return(p);
	}
	
	private void drawSkeleton(){
		canvas.clear();
		Point3D bottomLeft = translateToRotationOrigin(new Point3D(leftBoundary,top+height,0));
		Text3D bottomLeftLabel = new Text3D("1",bottomLeft);
		Point3D bottomRight = translateToRotationOrigin(new Point3D(leftBoundary+sideLength,top+height,0));
		Text3D bottomRightLabel = new Text3D("2",bottomRight);
		Point3D bottomBack = translateToRotationOrigin(new Point3D(centerX,top+height,height));
		Text3D bottomBackLabel = new Text3D("3",bottomBack);
		Point3D top = translateToRotationOrigin(new Point3D(centerX,this.top,centerZ));
		Text3D topLabel = new Text3D("4",top);
		Line3D baseFront = new Line3D(bottomLeft,bottomRight);
		Line3D baseLeft = new Line3D(bottomLeft,bottomBack);
		Line3D baseRight = new Line3D(bottomRight,bottomBack);
		Line3D upperLeft = new Line3D(bottomLeft,top);
		Line3D upperRight = new Line3D(bottomRight,top);
		Line3D upperBack = new Line3D(bottomBack,top);
		Line lineBaseFront = baseFront.getGraphicObject(theta, centerX, centerY);
		Line lineUpperBack = upperBack.getGraphicObject(theta, centerX, centerY);
		canvas.add(lineBaseFront, 0, 0);
		canvas.add(baseLeft.getGraphicObject(theta,centerX,centerY), 0, 0);
		canvas.add(baseRight.getGraphicObject(theta,centerX,centerY), 0, 0);
		canvas.add(upperLeft.getGraphicObject(theta,centerX,centerY), 0, 0);
		canvas.add(upperRight.getGraphicObject(theta,centerX,centerY), 0, 0);
		canvas.add(lineUpperBack, 0, 0);
		canvas.add(bottomLeftLabel.getText(theta, centerZ, sideLength), (int)lineBaseFront.getPointA().getX(), (int)lineBaseFront.getPointA().getY());
		canvas.add(bottomRightLabel.getText(theta, centerZ, sideLength), (int)lineBaseFront.getPointB().getX(), (int)lineBaseFront.getPointB().getY());
		canvas.add(bottomBackLabel.getText(theta, centerZ, sideLength), (int)lineUpperBack.getPointA().getX(), (int)lineUpperBack.getPointA().getY());
		canvas.add(topLabel.getText(theta, centerZ, sideLength), (int)lineUpperBack.getPointB().getX(), (int)lineUpperBack.getPointB().getY());
	}
	
	public int getAxisCount(){
		return 4;
	}
}
