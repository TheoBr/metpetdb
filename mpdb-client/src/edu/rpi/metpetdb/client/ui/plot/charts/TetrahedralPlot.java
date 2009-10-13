package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.gfx.Circle;
import com.objetdirect.tatami.client.gfx.Color;
import com.objetdirect.tatami.client.gfx.GraphicCanvas;
import com.objetdirect.tatami.client.gfx.GraphicObject;
import com.objetdirect.tatami.client.gfx.GraphicObjectListener;
import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Point;
import com.objetdirect.tatami.client.gfx.Polyline;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaElement;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaOxide;
import edu.rpi.metpetdb.client.ui.plot.threeD.Circle3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Line3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.PlaneEquation;
import edu.rpi.metpetdb.client.ui.plot.threeD.Point3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Polyline3D;
import edu.rpi.metpetdb.client.ui.plot.threeD.Rotation;
import edu.rpi.metpetdb.client.ui.plot.threeD.Text3D;

public class TetrahedralPlot extends MPlot implements MouseListener{
	private HorizontalPanel container;
	private FlowPanel monoContainer;
	private static double sin60 = .866;
	private static double cos60 = .5;
	private static double tan60 = 1.732;
	private GraphicCanvas canvas;
	public Rotation theta;
	public Point3D camera;
	public Rotation thetaStereo;
	
	private double leftBoundary;
	private double sideLength;
	private double top;
	private double height;
	private double centerX;
	private double centerY;
	private double centerZ;
	
	private double tempCenterX1;
	private double tempCenterX2;
	
	private boolean mouseDown = false;
	private boolean zooming = false;
	private double lastX = -1;
	private double lastY = -1;
	
	private Set<Circle> points = new HashSet<Circle>();
	private Set<Circle> stereoPoints = new HashSet<Circle>();
	private Set<Circle3D> points3D = new HashSet<Circle3D>();
	
	private Map<Integer,Circle3D> groupPoints= new HashMap<Integer,Circle3D>();
	private Set<Line3D> lines3D = new HashSet<Line3D>();
	private Set<Line> lines = new HashSet<Line>();
	private Set<Polyline3D> polylines = new HashSet<Polyline3D>();
	
	private Point3D bottomLeft;
	private Point3D bottomRight;
	private Point3D bottomBack;
	private Point3D topPoint;
	
	private Point3D bottomLeftStereo;
	private Point3D bottomRightStereo;
	private Point3D bottomBackStereo;
	private Point3D topPointStereo;
	
	public static enum VIEW_TYPE {MONO, STEREO};
	
	private VIEW_TYPE view = VIEW_TYPE.MONO;
	
	private FocusPanel panelZoomIn = new FocusPanel();
	private FocusPanel panelZoomOut = new FocusPanel();
	
	
	public TetrahedralPlot(final int width, final int height, final int sideLength){
		super();
		Button zoomIn = new Button("+");
		Button zoomOut = new Button("-");
		panelZoomIn.add(zoomIn);
		panelZoomOut.add(zoomOut);
		panelZoomIn.addMouseListener(this);
		panelZoomOut.addMouseListener(this);

		container = new HorizontalPanel();
		container.add(panelZoomOut);
		container.add(panelZoomIn);
		monoContainer = new FlowPanel();
		container.add(monoContainer);
		canvas = new GraphicCanvas();
		container.add(canvas);
		theta = new Rotation(0*(Math.PI/180),0*(Math.PI/180),0);
		canvas.setPixelSize(width,height);
		
		leftBoundary = (width-sideLength)/2;
		this.sideLength = sideLength;
		this.height = Math.sqrt(Math.pow(sideLength,2)-Math.pow(sideLength/2,2));
		top = (height-this.height)/2;
		
		centerX = leftBoundary+(sideLength/2);
		centerY = top+(this.height/2);
		centerZ = this.height-((sideLength/2)/sin60);
		
		camera = translateToRotationOrigin(new Point3D(centerX,centerY,centerZ),centerX);
		thetaStereo = new Rotation(0,-2*(Math.PI/180),0);
		
		canvas.clear();
		drawSkeleton(canvas);
		setupMouseListener(canvas);
	}
	
	public VIEW_TYPE getViewType(){
		return view;
	}
	
	public void setViewType(VIEW_TYPE view){
		this.view = view;
		canvas.clear();
		if (view == VIEW_TYPE.STEREO){
			camera.setZ(.5);
			drawSkeleton(canvas);
			drawPoints(canvas);
		} else if (view == VIEW_TYPE.MONO){
			camera.setZ(0);
			drawSkeleton(canvas);
			drawPoints(canvas);
		}
	}
	
	private void setupMouseListener(GraphicCanvas canvas){
		canvas.addGraphicObjectListener(new GraphicObjectListener(){

			public void mouseClicked(GraphicObject graphicObject, Event event) {

			}

			public void mouseDblClicked(GraphicObject graphicObject, Event event) {

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
					thetaStereo.setY(thetaStereo.getY()+((Math.PI/180)*((dx*Math.cos(thetaStereo.getX())))));//+(dx*Math.sin(theta.getX())))));
					thetaStereo.setX(thetaStereo.getX()+((Math.PI/180)*((-dy*Math.cos(thetaStereo.getY())))));
					onRotate();
					lastX = event.getClientX();
					lastY = event.getClientY();
				}
			}

			public void mousePressed(GraphicObject graphicObject, Event event) {
				mouseDown = true;
				lastX = event.getClientX();
				lastY = event.getClientY();
			}

			public void mouseReleased(GraphicObject graphicObject, Event event) {
				mouseDown = false;
				lastX = -1;
				lastY = -1;
			}
			
		});
	}
	
	public Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas, Map<Integer,Set<Integer>> groups){
		for (Circle c : points){
			canvas.remove(c);
		}
		points.clear();
		for (Line l : lines){
			canvas.remove(l);
		}
		lines.clear();
		lines3D.clear();
		points.clear();
		stereoPoints.clear();
		points3D.clear();
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
						bottomLeftAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaOxide i : formulaBottomRight.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomRightAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaOxide i : formulaBottomBack.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomBackAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaOxide i : formulaTop.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						topAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
			}
			Iterator<ChemicalAnalysisElement> itr2 = ca.getElements().iterator();
			while (itr2.hasNext()){
				ChemicalAnalysisElement o = itr2.next();
				for (AxisFormulaElement i : formulaBottomLeft.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomLeftAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaElement i : formulaBottomRight.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomRightAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaElement i : formulaBottomBack.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomBackAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaElement i : formulaTop.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						topAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
			}
			float sum = bottomLeftAxisTotal+bottomRightAxisTotal+bottomBackAxisTotal+topAxisTotal;
			if (sum != 0){
				double ratio1 = bottomLeftAxisTotal/sum;
				double ratio2 = bottomRightAxisTotal/sum;
				double ratio3 = bottomBackAxisTotal/sum;
				double ratio4 = topAxisTotal/sum;
				
				// find normal of axis 3
				Point3D new1 = new Point3D(bottomLeft);
				new1.setZ(new1.getZ() + ratio3 * height);
				Point3D new2 = new Point3D(bottomRight);
				new2.setZ(new2.getZ() + ratio3 * height);
				Point3D new3 = new Point3D(topPoint);
				new3.setZ(new3.getZ() + ratio3 * height);
				// calculate equation
				PlaneEquation p1 = new PlaneEquation(new1,new2,new3);
				

				// find normal of axis 4
				new1 = new Point3D(bottomLeft);
				new1.setY(new1.getY() - ratio4 * height);
				new2 = new Point3D(bottomRight);
				new2.setY(new2.getY() - ratio4 * height);
				new3 = new Point3D(bottomBack);
				new3.setY(new3.getY() - ratio4 * height);
				// calculate equation
				PlaneEquation p2 = new PlaneEquation(new1,new2,new3);
				
				// find normal of axis 1
				new1 = new Point3D(bottomBack);
				new1.setX(new1.getX() - ratio1 * sideLength);
				new2 = new Point3D(bottomRight);
				new2.setX(new2.getX() - ratio1 * sideLength);
				new3 = new Point3D(topPoint);
				new3.setX(new3.getX() - ratio1 * sideLength);
				// calculate equation
				PlaneEquation p3 = new PlaneEquation(new1,new2,new3);
				
				Point3D intersection = PlaneEquation.getPlaneIntersection(p1, p2, p3);
				Circle3D datum = new Circle3D(4,intersection);
				groupPoints.put(ca.getId(),datum);
				points3D.add(datum);

				
			}
			drawPoints(canvas);
		}
		for (Integer groupId : groups.keySet()){
			if (groups.get(groupId).size() == 4){
				List<Point3D> pointsArray = new ArrayList<Point3D>();
				for (Integer caId : groups.get(groupId)){
					Circle3D c1 = groupPoints.get(caId);
					Point3D p1 = new Point3D(c1.getX(),c1.getY(),c1.getZ());
					pointsArray.add(p1);
				}
				
				Polyline3D object3D = new Polyline3D(pointsArray);
				polylines.add(object3D);
				for (Polyline p : object3D.getGraphicObject(theta, camera, centerX, centerY)){
					canvas.add(p,0,0);
				}

			} else {
				for (Integer caId : groups.get(groupId)){
					for (Integer caId2 : groups.get(groupId)){
						if (caId != caId2){
							Circle3D c1 = groupPoints.get(caId);
							Circle3D c2 = groupPoints.get(caId2);
							Point3D p1 = new Point3D(c1.getX(),c1.getY(),c1.getZ());
							Point3D p2 = new Point3D(c2.getX(),c2.getY(),c2.getZ());
							Line3D association = new Line3D(p1,p2);
							lines3D.add(association);
							Line association2D = association.getGraphicObject(theta,camera, tempCenterX1, centerY);
							Line association2DStereo = association.getGraphicObject(theta,camera, tempCenterX2, centerY);
							lines.add(association2D);
							lines.add(association2DStereo);
							canvas.add(association2D, 0, 0);
							canvas.add(association2DStereo, 0, 0);
						}
					}
				}
			}
		}
		return container;
	}
	
	public void setRotation(double x, double y, double z){
		theta.setX(((Math.PI/180)*x)%(2*Math.PI));
		theta.setY(((Math.PI/180)*y)%(2*Math.PI));
		theta.setZ(((Math.PI/180)*z)%(2*Math.PI));
		canvas.clear();
		drawSkeleton(canvas);
		if (view == VIEW_TYPE.STEREO) {

		}
	}
	
	public void onRotate(){
		canvas.clear();
		theta.setX(theta.getX()%(2*Math.PI));
		theta.setY(theta.getY()%(2*Math.PI));
		theta.setZ(theta.getZ()%(2*Math.PI));
		thetaStereo.setX(thetaStereo.getX()%(2*Math.PI));
		thetaStereo.setY(thetaStereo.getY()%(2*Math.PI));
		thetaStereo.setZ(thetaStereo.getZ()%(2*Math.PI));
		canvas.clear();
		drawSkeleton(canvas);
		drawPoints(canvas);
		if (view == VIEW_TYPE.STEREO) {

		}
	}
	
	public void drawPoints(GraphicCanvas canvas){
		points.clear();
		lines.clear();
		for (Circle3D c : points3D){
			if (view == VIEW_TYPE.STEREO) {
				Circle datum2D = new Circle((int)c.getDisplayRadius(theta,camera, centerZ, sideLength));
				Circle datum2DStereo = new Circle((int)c.getDisplayRadius(thetaStereo,camera, centerZ, sideLength));
				points.add(datum2D);
				points.add(datum2DStereo);
				canvas.add(datum2D,
						(int)c.getDisplayPosition(theta,camera, tempCenterX1, centerY).getX(),
						(int)c.getDisplayPosition(theta,camera, tempCenterX1, centerY).getY());
				canvas.add(datum2DStereo,
						(int)c.getDisplayPosition(thetaStereo,camera, tempCenterX2, centerY).getX(),
						(int)c.getDisplayPosition(thetaStereo,camera, tempCenterX2, centerY).getY());
				for (Line3D l : lines3D){
					Line association2D = l.getGraphicObject(theta,camera, tempCenterX1, centerY);
					Line association2DStereo = l.getGraphicObject(theta,camera, tempCenterX2, centerY);
					lines.add(association2D);
					lines.add(association2DStereo);
					canvas.add(association2D, 0, 0);
					canvas.add(association2DStereo, 0, 0);
				}
			} else {
				Circle datum2D = new Circle((int)c.getDisplayRadius(theta,camera, centerZ, sideLength));
				points.add(datum2D);
				canvas.add(datum2D,
						(int)c.getDisplayPosition(theta,camera, centerX, centerY).getX(),
						(int)c.getDisplayPosition(theta,camera, centerX, centerY).getY());
				for (Line3D l : lines3D){
					Line association2D = l.getGraphicObject(theta,camera, centerX, centerY);
					lines.add(association2D);
					canvas.add(association2D, 0, 0);
				}
				for (Polyline3D p3D : polylines){
					for (Polyline p : p3D.getGraphicObject(theta, camera, centerX, centerY)){
						canvas.add(p,0,0);
					}
				}
			}
		}
	}
	
	private Point3D translateToRotationOrigin(final Point3D p, final double tempCenterX){
		p.setX(p.getX()-tempCenterX);
		p.setY(p.getY()-centerY);
		p.setZ(p.getZ()-centerZ);
		return(p);
	}
	
	private Point3D translateToCanvasOrigin(final Point3D p){
		p.setX(p.getX()+centerX);
		p.setY(p.getY()+centerY);
		p.setZ(p.getZ()+centerZ);
		return(p);
	}
	
	private void drawSkeleton(GraphicCanvas canvas){
		double tempLeftBoundary = leftBoundary;
		tempCenterX1 = centerX;
		double tempLeftBoundary2 = leftBoundary;
		tempCenterX2 = centerX;
		if (view == VIEW_TYPE.STEREO) {
			tempLeftBoundary -= sideLength/(4.5-camera.getZ());
			tempCenterX1 -= sideLength/(4.5-camera.getZ());
			
			tempLeftBoundary2 += (20)+sideLength/(4.5-camera.getZ());
			tempCenterX2 += (20)+sideLength/(4.5-camera.getZ());
		}
		bottomLeft = translateToRotationOrigin(new Point3D(tempLeftBoundary,top+height,0),tempCenterX1);
		Text3D bottomLeftLabel = new Text3D("1",bottomLeft);
		bottomRight = translateToRotationOrigin(new Point3D(tempLeftBoundary+sideLength,top+height,0),tempCenterX1);
		Text3D bottomRightLabel = new Text3D("2",bottomRight);
		bottomBack = translateToRotationOrigin(new Point3D(tempCenterX1,top+height,height),tempCenterX1);
		Text3D bottomBackLabel = new Text3D("3",bottomBack);
		topPoint = translateToRotationOrigin(new Point3D(tempCenterX1,this.top,centerZ),tempCenterX1);
		Text3D topLabel = new Text3D("4",topPoint);
		Line3D baseFront = new Line3D(bottomLeft,bottomRight);
		Line3D baseLeft = new Line3D(bottomLeft,bottomBack);
		Line3D baseRight = new Line3D(bottomRight,bottomBack);
		Line3D upperLeft = new Line3D(bottomLeft,topPoint);
		Line3D upperRight = new Line3D(bottomRight,topPoint);
		Line3D upperBack = new Line3D(bottomBack,topPoint);
		Line lineBaseFront = baseFront.getGraphicObject(theta,camera, tempCenterX1, centerY);
		Line lineUpperBack = upperBack.getGraphicObject(theta,camera, tempCenterX1, centerY);
		canvas.add(lineBaseFront, 0, 0);
		canvas.add(baseLeft.getGraphicObject(theta,camera,tempCenterX1,centerY), 0, 0);
		canvas.add(baseRight.getGraphicObject(theta,camera,tempCenterX1,centerY), 0, 0);
		canvas.add(upperLeft.getGraphicObject(theta,camera,tempCenterX1,centerY), 0, 0);
		canvas.add(upperRight.getGraphicObject(theta,camera,tempCenterX1,centerY), 0, 0);
		canvas.add(lineUpperBack, 0, 0);
		canvas.add(bottomLeftLabel.getText(theta,camera, centerZ, sideLength), (int)lineBaseFront.getPointA().getX(), (int)lineBaseFront.getPointA().getY());
		canvas.add(bottomRightLabel.getText(theta,camera, centerZ, sideLength), (int)lineBaseFront.getPointB().getX(), (int)lineBaseFront.getPointB().getY());
		canvas.add(bottomBackLabel.getText(theta,camera, centerZ, sideLength), (int)lineUpperBack.getPointA().getX(), (int)lineUpperBack.getPointA().getY());
		canvas.add(topLabel.getText(theta,camera, centerZ, sideLength), (int)lineUpperBack.getPointB().getX(), (int)lineUpperBack.getPointB().getY());
		if (view == VIEW_TYPE.STEREO) {
			bottomLeftStereo = translateToRotationOrigin(new Point3D(tempLeftBoundary2,top+height,0),tempCenterX2);
			Text3D bottomLeftLabelStereo = new Text3D("1",bottomLeftStereo);
			bottomRightStereo = translateToRotationOrigin(new Point3D(tempLeftBoundary2+sideLength,top+height,0),tempCenterX2);
			Text3D bottomRightLabelStereo = new Text3D("2",bottomRightStereo);
			bottomBackStereo = translateToRotationOrigin(new Point3D(tempCenterX2,top+height,height),tempCenterX2);
			Text3D bottomBackLabelStereo = new Text3D("3",bottomBackStereo);
			topPointStereo = translateToRotationOrigin(new Point3D(tempCenterX2,this.top,centerZ),tempCenterX2);
			Text3D topLabelStereo = new Text3D("4",topPointStereo);
			Line3D baseFrontStereo = new Line3D(bottomLeftStereo,bottomRightStereo);
			Line3D baseLeftStereo = new Line3D(bottomLeftStereo,bottomBackStereo);
			Line3D baseRightStereo = new Line3D(bottomRightStereo,bottomBackStereo);
			Line3D upperLeftStereo = new Line3D(bottomLeftStereo,topPointStereo);
			Line3D upperRightStereo = new Line3D(bottomRightStereo,topPointStereo);
			Line3D upperBackStereo = new Line3D(bottomBackStereo,topPointStereo);
			Line lineBaseFrontStereo = baseFrontStereo.getGraphicObject(thetaStereo,camera, tempCenterX2, centerY);
			Line lineUpperBackStereo = upperBackStereo.getGraphicObject(thetaStereo,camera, tempCenterX2, centerY);
			canvas.add(lineBaseFrontStereo, 0, 0);
			canvas.add(baseLeftStereo.getGraphicObject(thetaStereo,camera,tempCenterX2,centerY), 0, 0);
			canvas.add(baseRightStereo.getGraphicObject(thetaStereo,camera,tempCenterX2,centerY), 0, 0);
			canvas.add(upperLeftStereo.getGraphicObject(thetaStereo,camera,tempCenterX2,centerY), 0, 0);
			canvas.add(upperRightStereo.getGraphicObject(thetaStereo,camera,tempCenterX2,centerY), 0, 0);
			canvas.add(lineUpperBackStereo, 0, 0);
			canvas.add(bottomLeftLabelStereo.getText(thetaStereo,camera, centerZ, sideLength), (int)lineBaseFrontStereo.getPointA().getX(), (int)lineBaseFrontStereo.getPointA().getY());
			canvas.add(bottomRightLabelStereo.getText(thetaStereo,camera, centerZ, sideLength), (int)lineBaseFrontStereo.getPointB().getX(), (int)lineBaseFrontStereo.getPointB().getY());
			canvas.add(bottomBackLabelStereo.getText(thetaStereo,camera, centerZ, sideLength), (int)lineUpperBackStereo.getPointA().getX(), (int)lineUpperBackStereo.getPointA().getY());
			canvas.add(topLabelStereo.getText(thetaStereo,camera, centerZ, sideLength), (int)lineUpperBackStereo.getPointB().getX(), (int)lineUpperBackStereo.getPointB().getY());
		}
	}
	
	public int getAxisCount(){
		return 4;
	}
	
	public String getSVG(){
		String innerhtml = canvas.getElement().getInnerHTML();
		return innerhtml.substring(0, innerhtml.indexOf(">")) + "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"" + innerhtml.substring(innerhtml.indexOf(">"));
	}

	public void onMouseDown(final Widget sender, int x, int y) {
		zooming = true;
		Timer togglebuttonTimer = new Timer() {
		    public void run() {
		    	if (zooming){
					if (sender == panelZoomOut){
						if (camera.getZ() < 1) {
							camera.setZ(camera.getZ() + .015);
						}
					} else if (sender == panelZoomIn) {
						if (camera.getZ() > -4) {
							camera.setZ(camera.getZ() - .015);
						}
					}
					onRotate();	
					this.schedule(40); 
		    	}
		    }
		};
		togglebuttonTimer.run();

	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
		zooming = false;
	}
}
