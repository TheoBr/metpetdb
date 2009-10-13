package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.tatami.client.gfx.Circle;
import com.objetdirect.tatami.client.gfx.Color;
import com.objetdirect.tatami.client.gfx.GraphicCanvas;
import com.objetdirect.tatami.client.gfx.Line;
import com.objetdirect.tatami.client.gfx.Point;
import com.objetdirect.tatami.client.gfx.Polyline;
import com.objetdirect.tatami.client.gfx.Text;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.ui.plot.AxisFormula;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaElement;
import edu.rpi.metpetdb.client.ui.plot.AxisFormulaOxide;

public class TernaryPlot extends MPlot {
	private FlowPanel container;
	
	private static double sin60 = .866;
	private static double cos60 = .5;
	private static double tan60 = 1.732;
	private GraphicCanvas canvas;
	private int sideLength = 500;
	private int leftBoundary = 0;
	private int rightBoundary = 500;
	private double top = 0;
	private double center = 250;
	private MAxis defaultBottomAxis;
	private MAxis defaultLeftAxis;
	private MAxis defaultRightAxis;
	
	private List<Line> bottomMajorTicks;
	private List<Line> bottomMinorTicks;
	private List<Line> bottomMicroTicks;
	
	private List<Line> leftMajorTicks;
	private List<Line> leftMinorTicks;
	private List<Line> leftMicroTicks;
	
	private List<Line> rightMajorTicks;
	private List<Line> rightMinorTicks;
	private List<Line> rightMicroTicks;
	
	private List<Line> bottomGridLines;	
	private List<Line> leftGridLines;
	private List<Line> rightGridLines;
	
	private List<Text> bottomMajorLabels;
	
	private List<Circle> points;
	
	private Text bottomLabel;
	private Text leftLabel;
	private Text rightLabel;
	
	private Polyline border;
	
	public TernaryPlot(){
		super();
		canvas = new GraphicCanvas();
		container = new FlowPanel();
		container.add(canvas);	
		defaultBottomAxis = new MAxis();
		defaultBottomAxis.setLabel(new MAxisLabel("3"));
		defaultLeftAxis = new MAxis();
		defaultLeftAxis.setLabel(new MAxisLabel("1"));
		defaultRightAxis = new MAxis();
		defaultRightAxis.setLabel(new MAxisLabel("2"));
		bottomMajorTicks = new ArrayList<Line>();
		bottomMinorTicks = new ArrayList<Line>();
		bottomMicroTicks = new ArrayList<Line>();
		leftMajorTicks = new ArrayList<Line>();
		leftMinorTicks = new ArrayList<Line>();
		leftMicroTicks = new ArrayList<Line>();
		rightMajorTicks = new ArrayList<Line>();
		rightMinorTicks = new ArrayList<Line>();
		rightMicroTicks = new ArrayList<Line>();
		bottomGridLines = new ArrayList<Line>();
		leftGridLines = new ArrayList<Line>();
		rightGridLines = new ArrayList<Line>();
		bottomMajorLabels = new ArrayList<Text>();
		points = new ArrayList<Circle>();
	}
	
	public TernaryPlot(final int width, final int height, final int sideLength){
		this();
		canvas.setPixelSize(width,height);
		this.sideLength = sideLength;
		leftBoundary = (width-sideLength)/2;
		rightBoundary = sideLength+leftBoundary;
		top = sideLength-Math.sqrt(Math.pow(sideLength,2)-Math.pow(sideLength/2,2));
		center = sideLength/2+leftBoundary;
		drawSkeleton();
		drawAxes();
	}
	
	public Widget createWidget(List<ChemicalAnalysis> data, List<AxisFormula> formulas, Map<Integer,Set<Integer>> groups){
		if (formulas.size() < 3){
			return container;
		}
		
		AxisFormula formulaLeft = formulas.get(0);
		AxisFormula formulaRight = formulas.get(1);
		AxisFormula formulaBottom = formulas.get(2);
		
		defaultBottomAxis.setLabel(new MAxisLabel(formulaBottom.getAxisLabel()));
		drawBottomAxisLabel();
		defaultLeftAxis.setLabel(new MAxisLabel(formulaLeft.getAxisLabel()));
		drawLeftAxisLabel();
		defaultRightAxis.setLabel(new MAxisLabel(formulaRight.getAxisLabel()));
		drawRightAxisLabel();
		
		for (Circle point : points){
			canvas.remove(point);
		}
		for (ChemicalAnalysis ca : data){
			float bottomAxisTotal = new Double(formulaBottom.getConstant()).floatValue();
			float leftAxisTotal = new Double(formulaLeft.getConstant()).floatValue();
			float rightAxisTotal = new Double(formulaRight.getConstant()).floatValue();
			Iterator<ChemicalAnalysisOxide> itr = ca.getOxides().iterator();
			while (itr.hasNext()){
				ChemicalAnalysisOxide o = itr.next();
				for (AxisFormulaOxide i : formulaBottom.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						bottomAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaOxide i : formulaLeft.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						leftAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaOxide i : formulaRight.getOxides()){
					if (i.getOxide().getOxideId() == o.getOxide().getOxideId()){
						rightAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
			}
			Iterator<ChemicalAnalysisElement> itr2 = ca.getElements().iterator();
			while (itr2.hasNext()){
				ChemicalAnalysisElement o = itr2.next();
				for (AxisFormulaElement i : formulaBottom.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						bottomAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaElement i : formulaLeft.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						leftAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
				for (AxisFormulaElement i : formulaRight.getElements()){
					if (i.getElement().getId() == o.getElement().getId()){
						rightAxisTotal += i.getCoefficient()*o.getAmount()*ChemicalAnalysis.getUnitOffset(o.getMeasurementUnit());
					}
				}
			}
			float sum = bottomAxisTotal+leftAxisTotal+rightAxisTotal;
			if (sum != 0){
				double tempX = leftBoundary+((bottomAxisTotal/sum)*sideLength);
				double tempY = top+(sideLength*sin60*(1-(rightAxisTotal/sum)));
				double y = tempY;
				double x = tempX+(top+(sideLength*sin60)-tempY)/tan60;
				Circle point = new Circle(3);
				points.add(point);
				canvas.add(point, (int)x, (int)y);
				
			}
		}
		return container;
	}
	
	public GraphicCanvas getGraphicCanvas(){
		return canvas;
	}
	
	public MAxis getDefaultBottomAxis(){
		return defaultBottomAxis;
	}
	public MAxis getDefaultLeftAxis(){
		return defaultLeftAxis;
	}
	public MAxis getDefaultRightAxis(){
		return defaultRightAxis;
	}
	public void refreshBottomAxisLabel(){
		drawBottomAxisLabel();
	}
	public void setBottomAxisMajorTickStep(final double step){
		removeBottomMajorTicks();
		defaultBottomAxis.setMajorTickStep(step);
		drawBottomAxisMajorTicks(defaultBottomAxis.getMajorTickStep()*sideLength);
	}
	public void refreshBottomAxisMajorTicks(){
		removeBottomMajorTicks();
		drawBottomAxisMajorTicks(defaultBottomAxis.getMajorTickStep()*sideLength);
	}
	public void refreshBottomAxisMajorLabels(){
		removeTextSet(bottomMajorLabels);
		drawBottomAxisMajorLabels();
	}
	public void refreshBottomAxisMinorTicks(){
		removeBottomMinorTicks();
		drawBottomAxisMinorTicks(defaultBottomAxis.getMinorTickStep()*sideLength);
	}
	public void refreshBottomAxisMicroTicks(){
		removeBottomMicroTicks();
		drawBottomAxisMicroTicks(defaultBottomAxis.getMicroTickStep()*sideLength);
	}
	public void refreshBottomAxisGridLines(){
		drawBottomAxisGridLines();
	}
	public void refreshLeftAxisLabel(){
		drawLeftAxisLabel();
	}
	public void refreshLeftAxisMajorTicks(){
		removeLeftMajorTicks();
		double yStep = (defaultLeftAxis.getMajorTickStep()*sideLength*sin60);
		double xStep = (defaultLeftAxis.getMajorTickStep()*sideLength*cos60);
		drawLeftAxisMajorTicks(xStep,yStep);
	}
	public void refreshLeftAxisMinorTicks(){
		removeLeftMinorTicks();
		double yStep = (defaultLeftAxis.getMinorTickStep()*sideLength*sin60);
		double xStep = (defaultLeftAxis.getMinorTickStep()*sideLength*cos60);
		drawLeftAxisMinorTicks(xStep, yStep);
	}
	public void refreshLeftAxisMicroTicks(){
		removeLeftMicroTicks();
		double yStep = (defaultLeftAxis.getMicroTickStep()*sideLength*sin60);
		double xStep = (defaultLeftAxis.getMicroTickStep()*sideLength*cos60);
		drawLeftAxisMicroTicks(xStep,yStep);
	}
	public void refreshLeftAxisGridLines(){
		drawLeftAxisGridLines();
	}
	public void refreshRightAxisLabel(){
		drawRightAxisLabel();
	}
	public void refreshRightAxisMajorTicks(){
		removeRightMajorTicks();
		double yStep = (defaultRightAxis.getMajorTickStep()*sideLength*sin60);
		double xStep = (defaultRightAxis.getMajorTickStep()*sideLength*cos60);
		drawRightAxisMajorTicks(xStep,yStep);
	}
	public void refreshRightAxisMinorTicks(){
		removeRightMinorTicks();
		double yStep = (defaultRightAxis.getMinorTickStep()*sideLength*sin60);
		double xStep = (defaultRightAxis.getMinorTickStep()*sideLength*cos60);
		drawRightAxisMinorTicks(xStep,yStep);
	}
	public void refreshRightAxisMicroTicks(){
		removeRightMicroTicks();
		double yStep = (defaultRightAxis.getMicroTickStep()*sideLength*sin60);
		double xStep = (defaultRightAxis.getMicroTickStep()*sideLength*cos60);
		drawRightAxisMicroTicks(xStep, yStep);
	}
	public void refreshRightAxisGridLines(){
		drawRightAxisGridLines();
	}
	public void setDefaultBottomAxis(MAxis axis){
		defaultBottomAxis = axis;
		drawBottomAxis();
	}
	public void setDefaultLeftAxis(MAxis axis){
		defaultLeftAxis = axis;
		drawLeftAxis();
	}
	public void setDefaultRightAxis(MAxis axis){
		defaultRightAxis = axis;
		drawRightAxis();
	}
	
	private void drawAxes(){
		drawBottomAxis();
		drawLeftAxis();
		drawRightAxis();
	}
	
	private void drawBottomAxisLabel(){
		if (bottomLabel!=null)
			canvas.remove(bottomLabel);
		if (defaultBottomAxis.getLabelOrientation().equals(MAxis.LabelOrientation.HORIZONTAL)){
			bottomLabel = new Text(defaultBottomAxis.getLabel().getText());
			double x = rightBoundary-(bottomLabel.getWidth()/2)+10;
			double y = sideLength+25;
			canvas.add(bottomLabel,(int)x,(int)y);
		}
	}
	
	private void drawBottomAxisMajorLabels(){
		if (defaultBottomAxis.getMajorLabels()){
			for (Line l : bottomMajorTicks){
				double value = (bottomMajorTicks.indexOf(l)*defaultBottomAxis.getMajorTickStep());		
				Text label = new Text(formatDouble(value,defaultBottomAxis.getMajorTickPrecision()));
				canvas.add(label, (int) l.getPointB().getX()+5, (int) l.getPointB().getY()+15);
				bottomMajorLabels.add(label);
			}
		}
	}
	
	private void drawBottomAxisMajorTicks(final double xStep){
		if (defaultBottomAxis.getMajorTicks()){
			for (double x = leftBoundary; x <= rightBoundary; x+=xStep){
				// Add Ticks
				Point a = new Point(x,sideLength);
				Point b = new Point(x,sideLength+defaultBottomAxis.getMajorTicksLength());
				Line majorTick = new Line(a,b);
				if (defaultBottomAxis.getMajorTicksColor() != null)
					majorTick.setStrokeColor(defaultBottomAxis.getMajorTicksColor());
				canvas.add(majorTick, 0, 0);
				bottomMajorTicks.add(majorTick);
			}
			cleanForMajorTicks(defaultBottomAxis,bottomMinorTicks,bottomMicroTicks);
		}
	}
	
	private void removeBottomMajorTicks(){
		removeLineSet(bottomMajorTicks);
		final double xStep = defaultBottomAxis.getMajorTickStep()*sideLength;
		final int minorSkip = (int)(defaultBottomAxis.getMajorTickStep()/defaultBottomAxis.getMinorTickStep());
		final int microSkip = (int)(defaultBottomAxis.getMajorTickStep()/defaultBottomAxis.getMicroTickStep());
		drawBottomAxisMinorTicks(xStep,minorSkip);
		drawBottomAxisMicroTicks(xStep,microSkip);
	}
	
	
	private void cleanForMajorTicks(final MAxis axis, final List<Line> minorTicks,final List<Line> microTicks){
		final int minorTicksStep = (int) (axis.getMajorTickStep()/axis.getMinorTickStep());
		final int microTicksStep = (int) (axis.getMajorTickStep()/axis.getMicroTickStep());
		for (int i = 0; i < minorTicks.size(); i+= minorTicksStep){
			canvas.remove(minorTicks.get(i));
			minorTicks.set(i, null);
		}
			for (int i = 0; i < microTicks.size(); i+= microTicksStep){
			canvas.remove(microTicks.get(i));
			microTicks.set(i, null);
		}		
			
	}
	private void drawBottomAxisMinorTicks(final double xStep, final int skip){
		if (defaultBottomAxis.getMinorTicks()){
			int count = 0;
			for (double x = leftBoundary; x <= rightBoundary; x+=xStep, count+=skip){
				Point a = new Point(x,sideLength);
				Point b = new Point(x,sideLength+defaultBottomAxis.getMinorTicksLength());
				Line minorTick = new Line(a,b);
				if (defaultBottomAxis.getMinorTicksColor() != null)
					minorTick.setStrokeColor(defaultBottomAxis.getMinorTicksColor());			
				if (skip > 1 && bottomMinorTicks.get(count) == null){
					bottomMinorTicks.set(count, minorTick);
					canvas.add(minorTick, 0, 0);
				} else if (skip == 1) {
					bottomMinorTicks.add(minorTick);
					canvas.add(minorTick, 0, 0);
				}
				
			}
			cleanForMinorTicks(defaultBottomAxis,bottomMinorTicks,bottomMicroTicks);
		}	
	}
	
	private void drawBottomAxisMinorTicks(final double xStep){
		drawBottomAxisMinorTicks(xStep, 1);
	}
	
	private void removeBottomMinorTicks(){
		removeLineSet(bottomMinorTicks);
		final int microSkip = (int)(defaultBottomAxis.getMinorTickStep()/defaultBottomAxis.getMicroTickStep());
		drawBottomAxisMicroTicks(defaultBottomAxis.getMinorTickStep()*sideLength, microSkip);
	}
	
	private void cleanForMinorTicks(final MAxis axis, final List<Line> minorTicks,final List<Line> microTicks){
		final int microTicksStep = (int) (axis.getMinorTickStep()/axis.getMicroTickStep());
		for (int i = 0; i < microTicks.size(); i+= microTicksStep){
			canvas.remove(microTicks.get(i));
			microTicks.set(i, null);
		}
		if (axis.getMajorTicks()){
			final int minorTicksStep = (int) (axis.getMajorTickStep()/axis.getMinorTickStep());
			for (int i = 0; i < minorTicks.size(); i+= minorTicksStep){
				canvas.remove(minorTicks.get(i));
				minorTicks.set(i, null);
			}
		}
	}
	
	private void drawBottomAxisMicroTicks(final double xStep, final int skip){
		if (defaultBottomAxis.getMicroTicks()){
			int count = 0;
			for (double x = leftBoundary; x <= rightBoundary; x+=xStep, count+=skip){
				Point a = new Point(x,sideLength);
				Point b = new Point(x,sideLength+defaultBottomAxis.getMicroTicksLength());
				Line microTick = new Line(a,b);
				if (defaultBottomAxis.getMicroTicksColor() != null)
					microTick.setStrokeColor(defaultBottomAxis.getMicroTicksColor());				
				if (skip > 1 && bottomMicroTicks.get(count) == null){
					bottomMicroTicks.set(count, microTick);
					canvas.add(microTick, 0, 0);
				}else if (skip == 1){
					bottomMicroTicks.add(microTick);
					canvas.add(microTick, 0, 0);
				}
			}
			cleanForMicroTicks(defaultBottomAxis, bottomMicroTicks);
		}
	}
	
	private void drawBottomAxisMicroTicks(final double xStep){
		drawBottomAxisMicroTicks(xStep, 1);
	}
	
	private void removeBottomMicroTicks(){
		removeLineSet(bottomMicroTicks);
	}
	
	private void cleanForMicroTicks(final MAxis axis, final List<Line> ticks){
		final int majorTicks = (int) (axis.getMajorTickStep()/axis.getMicroTickStep());
		final int minorTicks = (int) (axis.getMinorTickStep()/axis.getMicroTickStep());
		if (axis.getMajorTicks()){
			for (int i = 0; i < ticks.size(); i+= majorTicks){
				canvas.remove(ticks.get(i));
				ticks.set(i, null);
			}
		}
		if (axis.getMinorTicks()){
			for (int i = 0; i < ticks.size(); i+= minorTicks){
				canvas.remove(ticks.get(i));
				ticks.set(i, null);
			}		
		}
	}

	
	private void drawBottomAxisGridLines(){
		double xStep = 1;
		removeLineSet(bottomGridLines);
		if (defaultBottomAxis.getGridLines().equals(MAxis.GridLine.NONE)){
			return;
		}else if (defaultBottomAxis.getGridLines().equals(MAxis.GridLine.MAJOR)){
			xStep = defaultBottomAxis.getMajorTickStep()*sideLength;
		} else if (defaultBottomAxis.getGridLines().equals(MAxis.GridLine.MINOR)){
			xStep = defaultBottomAxis.getMinorTickStep()*sideLength;
		} else if (defaultBottomAxis.getGridLines().equals(MAxis.GridLine.MICRO)){
			xStep = defaultBottomAxis.getMicroTickStep()*sideLength;
		}	
		for (double x = leftBoundary+xStep; x < rightBoundary; x+=xStep){
			Point a = new Point(x,sideLength);			
			Point c = new Point(leftBoundary+cos60*(x-leftBoundary),sideLength-(sin60*((x-leftBoundary))));
			Line gridLine = new Line(a,c);
			if (defaultBottomAxis.getGridLineColor() != null)
				gridLine.setStrokeColor(defaultBottomAxis.getGridLineColor());
			canvas.add(gridLine,0,0);
			bottomGridLines.add(gridLine);
		}
	}
	
	private void drawLeftAxisLabel(){
		if (leftLabel!=null)
			canvas.remove(leftLabel);
		if (defaultLeftAxis.getLabelOrientation().equals(MAxis.LabelOrientation.HORIZONTAL)){
			leftLabel = new Text(defaultLeftAxis.getLabel().getText());
			double x = leftBoundary-10;
			double y = sideLength+25;
			canvas.add(leftLabel,(int) x, (int)y);
		}
	}
	
	private void drawLeftAxisMajorTicks(final double xStep, final double yStep){
		if (defaultLeftAxis.getMajorTicks()){	
			for (double x = leftBoundary, y = sideLength; x <= sideLength/2 + leftBoundary && y >= top; x+=xStep, y-= yStep){
				Point a = new Point(x,y);
				Point b = new Point(x-(sin60*defaultLeftAxis.getMajorTicksLength()),y-(cos60*defaultLeftAxis.getMajorTicksLength()));
				Line majorTick = new Line(a,b);
				if (defaultLeftAxis.getMajorTicksColor() != null)
					majorTick.setStrokeColor(defaultLeftAxis.getMajorTicksColor());
				canvas.add(majorTick, 0, 0);
				leftMajorTicks.add(majorTick);
			}
			cleanForMajorTicks(defaultLeftAxis,leftMinorTicks,leftMicroTicks);
		}
	}
	
	private void removeLeftMajorTicks(){
		removeLineSet(leftMajorTicks);
		final double xStep = (defaultLeftAxis.getMajorTickStep()*sideLength*cos60);
		final double yStep = (defaultLeftAxis.getMajorTickStep()*sideLength*sin60);
		final int minorSkip = (int)(defaultLeftAxis.getMajorTickStep()/defaultLeftAxis.getMinorTickStep());
		final int microSkip = (int)(defaultLeftAxis.getMajorTickStep()/defaultLeftAxis.getMicroTickStep());
		drawLeftAxisMinorTicks(xStep,yStep,minorSkip);
		drawLeftAxisMicroTicks(xStep,yStep,microSkip);
	}
	
	private void drawLeftAxisMinorTicks(final double xStep, final double yStep, final int skip){
		if (defaultLeftAxis.getMinorTicks()){
			int count = 0;
			for (double x = leftBoundary,y = sideLength; x <= sideLength/2 + leftBoundary && y >= top; x+=xStep, y-= yStep, count+=skip){
				Point a = new Point(x,y);
				Point b = new Point(x-(sin60*defaultLeftAxis.getMinorTicksLength()),y-(cos60*defaultLeftAxis.getMinorTicksLength()));
				Line minorTick = new Line(a,b);
				if (defaultLeftAxis.getMinorTicksColor() != null)
					minorTick.setStrokeColor(defaultLeftAxis.getMinorTicksColor());
				if (skip > 1 && leftMinorTicks.get(count) == null){
					leftMinorTicks.set(count, minorTick);
					canvas.add(minorTick, 0, 0);
				}else if (skip == 1){
					leftMinorTicks.add(minorTick);
					canvas.add(minorTick, 0, 0);
				}
			}
			cleanForMinorTicks(defaultLeftAxis, leftMinorTicks, leftMicroTicks);
		}
	}
	
	private void drawLeftAxisMinorTicks(final double xStep, final double yStep){
		drawLeftAxisMinorTicks(xStep,yStep,1);
	}
	
	private void removeLeftMinorTicks(){
		removeLineSet(leftMinorTicks);
		final int microSkip = (int)(defaultLeftAxis.getMinorTickStep()/defaultLeftAxis.getMicroTickStep());
		drawLeftAxisMicroTicks(defaultLeftAxis.getMinorTickStep()*sideLength*cos60,defaultLeftAxis.getMinorTickStep()*sideLength*sin60, microSkip);
	}

	private void drawLeftAxisMicroTicks(final double xStep, final double yStep, final int skip){
		if (defaultLeftAxis.getMicroTicks()){
			int count = 0;
			for (double x = leftBoundary,y = sideLength; x <= sideLength/2 + leftBoundary && y >= top; x+=xStep, y-= yStep, count+=skip){
				Point a = new Point(x,y);
				Point b = new Point(x-(sin60*defaultLeftAxis.getMicroTicksLength()),y-(cos60*defaultLeftAxis.getMicroTicksLength()));
				Line microTick = new Line(a,b);
				if (defaultLeftAxis.getMicroTicksColor() != null)
					microTick.setStrokeColor(defaultLeftAxis.getMicroTicksColor());
				if (skip > 1 && leftMicroTicks.get(count) == null){
					leftMicroTicks.set(count, microTick);
					canvas.add(microTick, 0, 0);
				}else if (skip == 1){
					leftMicroTicks.add(microTick);
					canvas.add(microTick, 0, 0);
				}
			}
			cleanForMicroTicks(defaultLeftAxis,leftMicroTicks);
		}
	}
	
	private void drawLeftAxisMicroTicks(final double xStep, final double yStep){
		drawLeftAxisMicroTicks(xStep,yStep,1);
	}
	
	private void removeLeftMicroTicks(){
		removeLineSet(leftMicroTicks);
	}
	
	private void drawLeftAxisGridLines(){
		double step = 1;
		removeLineSet(leftGridLines);
		if (defaultLeftAxis.getGridLines().equals(MAxis.GridLine.NONE)){
			return;
		}else if (defaultLeftAxis.getGridLines().equals(MAxis.GridLine.MAJOR)){
			step = defaultLeftAxis.getMajorTickStep();
		} else if (defaultLeftAxis.getGridLines().equals(MAxis.GridLine.MINOR)){
			step = defaultLeftAxis.getMinorTickStep();
		} else if (defaultLeftAxis.getGridLines().equals(MAxis.GridLine.MICRO)){
			step = defaultLeftAxis.getMicroTickStep();
		}	
		double yStep = step*sideLength*sin60;
		double xStep = step*sideLength*cos60;
		for (double x = leftBoundary+xStep,y = sideLength-yStep; x < sideLength/2 + leftBoundary&& y > top; x+=xStep, y-= yStep){
			Point a = new Point(x,y);
			Point c = new Point(x+(2*(center-x)),y);
			Line gridLine = new Line(a,c);
			if (defaultLeftAxis.getGridLineColor() != null)
				gridLine.setStrokeColor(defaultLeftAxis.getGridLineColor());
			canvas.add(gridLine,0,0);
			leftGridLines.add(gridLine);
		}
	}
	
	private void drawRightAxisLabel(){
		if (rightLabel!=null)
			canvas.remove(rightLabel);
		if (defaultRightAxis.getLabelOrientation().equals(MAxis.LabelOrientation.HORIZONTAL)){
			rightLabel = new Text(defaultRightAxis.getLabel().getText());
			double x = center-(rightLabel.getWidth()/2);
			double y = top-25;
			canvas.add(rightLabel,(int)x,(int)y);
		}
	}
	
	private void drawRightAxisMajorTicks(final double xStep, final double yStep){
		if (defaultRightAxis.getMajorTicks()){			
			for (double x = rightBoundary,y = sideLength; x >= sideLength/2 + leftBoundary && y >= top; x-=xStep, y-= yStep){
				Point a = new Point(x,y);
				Point b = new Point(x+(sin60*defaultRightAxis.getMajorTicksLength()),y-(cos60*defaultRightAxis.getMajorTicksLength()));
				Line majorTick = new Line(a,b);
				if (defaultRightAxis.getMajorTicksColor() != null)
					majorTick.setStrokeColor(defaultRightAxis.getMajorTicksColor());
				canvas.add(majorTick, 0, 0);
				rightMajorTicks.add(majorTick);
			}
			cleanForMajorTicks(defaultRightAxis,rightMinorTicks,rightMicroTicks);
		}
	}
	
	private void removeRightMajorTicks(){
		removeLineSet(rightMajorTicks);
		final double xStep = (defaultRightAxis.getMajorTickStep()*sideLength*cos60);
		final double yStep = (defaultRightAxis.getMajorTickStep()*sideLength*sin60);
		final int minorSkip = (int)(defaultRightAxis.getMajorTickStep()/defaultRightAxis.getMinorTickStep());
		final int microSkip = (int)(defaultRightAxis.getMajorTickStep()/defaultRightAxis.getMicroTickStep());
		drawRightAxisMinorTicks(xStep,yStep,minorSkip);
		drawRightAxisMicroTicks(xStep,yStep,microSkip);
	}
	
	private void drawRightAxisMinorTicks(final double xStep, final double yStep, final int skip){
		if (defaultRightAxis.getMinorTicks()){
			int count = 0;
			for (double x = rightBoundary,y = sideLength; x >= sideLength/2 + leftBoundary && y >= top; x-=xStep, y-= yStep, count+=skip){
				Point a = new Point(x,y);
				Point b = new Point(x+(sin60*defaultRightAxis.getMinorTicksLength()),y-(cos60*defaultRightAxis.getMinorTicksLength()));
				Line minorTick = new Line(a,b);
				if (defaultRightAxis.getMinorTicksColor() != null)
					minorTick.setStrokeColor(defaultRightAxis.getMinorTicksColor());;
				if (skip > 1 && rightMinorTicks.get(count) == null){
					rightMinorTicks.set(count, minorTick);
					canvas.add(minorTick, 0, 0);
				}else if (skip == 1){
					rightMinorTicks.add(minorTick);
					canvas.add(minorTick, 0, 0);
				}
			}
			cleanForMinorTicks(defaultRightAxis,rightMinorTicks, rightMicroTicks);
		}
	}
	
	private void drawRightAxisMinorTicks(final double xStep, final double yStep){
		drawRightAxisMinorTicks(xStep,yStep,1);
	}
	
	private void removeRightMinorTicks(){
		removeLineSet(rightMinorTicks);
		final int microSkip = (int)(defaultRightAxis.getMinorTickStep()/defaultRightAxis.getMicroTickStep());
		drawRightAxisMicroTicks(defaultRightAxis.getMinorTickStep()*sideLength*cos60,defaultRightAxis.getMinorTickStep()*sideLength*sin60, microSkip);
	}
	
	private void removeRightMicroTicks(){
		removeLineSet(rightMicroTicks);
	} 
	
	private void drawRightAxisMicroTicks(final double xStep, final double yStep, final int skip){
		if (defaultRightAxis.getMicroTicks()){
			int count = 0;
			for (double x = rightBoundary,y = sideLength; x >= sideLength/2 + leftBoundary && y >= top; x-=xStep, y-= yStep, count+=skip){
				Point a = new Point(x,y);
				Point b = new Point(x+(sin60*defaultRightAxis.getMicroTicksLength()),y-(cos60*defaultRightAxis.getMicroTicksLength()));
				Line microTick = new Line(a,b);
				if (defaultRightAxis.getMicroTicksColor() != null)
					microTick.setStrokeColor(defaultRightAxis.getMicroTicksColor());
				if (skip > 1 && rightMicroTicks.get(count) == null){
					rightMicroTicks.set(count, microTick);
					canvas.add(microTick, 0, 0);
				}else if (skip == 1){
					rightMicroTicks.add(microTick);
					canvas.add(microTick, 0, 0);
				}
			}
			cleanForMicroTicks(defaultRightAxis,rightMicroTicks);
		}
	}
	
	private void drawRightAxisMicroTicks(final double xStep, final double yStep){
		drawRightAxisMicroTicks(xStep, yStep, 1);
	}
	
	private void drawRightAxisGridLines(){
		double step = 1;
		removeLineSet(rightGridLines);
		if (defaultRightAxis.getGridLines().equals(MAxis.GridLine.NONE)){
			return;
		} else if (defaultRightAxis.getGridLines().equals(MAxis.GridLine.MAJOR)){
			step = defaultRightAxis.getMajorTickStep();
		} else if (defaultRightAxis.getGridLines().equals(MAxis.GridLine.MINOR)){
			step = defaultRightAxis.getMinorTickStep();
		} else if (defaultRightAxis.getGridLines().equals(MAxis.GridLine.MICRO)){
			step = defaultRightAxis.getMicroTickStep();
		}	
		double yStep = step*sideLength*sin60;
		double xStep = step*sideLength*cos60;
		for (double x = rightBoundary-xStep,y = sideLength-yStep; x > sideLength/2 + leftBoundary && y > top; x-=xStep, y-= yStep){
			Point a = new Point(x,y);
			Point c = new Point(x-((sideLength-y)/tan60),sideLength);
			Line gridLine = new Line(a,c);
			if (defaultRightAxis.getGridLineColor() != null)
				gridLine.setStrokeColor(defaultRightAxis.getGridLineColor());
			canvas.add(gridLine,0,0);
			rightGridLines.add(gridLine);
		}
	}
	
	private void drawBottomAxis(){
		drawBottomAxisLabel();
		drawBottomAxisMicroTicks(defaultBottomAxis.getMicroTickStep()*sideLength);
		drawBottomAxisMinorTicks(defaultBottomAxis.getMinorTickStep()*sideLength);
		drawBottomAxisMajorTicks(defaultBottomAxis.getMajorTickStep()*sideLength);
//		drawBottomAxisMajorLabels();
		drawBottomAxisGridLines();
	}
	
	private void drawLeftAxis(){
		drawLeftAxisLabel();
		drawLeftAxisMicroTicks(defaultLeftAxis.getMicroTickStep()*sideLength*cos60,defaultLeftAxis.getMicroTickStep()*sideLength*sin60);	
		drawLeftAxisMinorTicks(defaultLeftAxis.getMinorTickStep()*sideLength*cos60,defaultLeftAxis.getMinorTickStep()*sideLength*sin60);
		drawLeftAxisMajorTicks(defaultLeftAxis.getMajorTickStep()*sideLength*cos60,defaultLeftAxis.getMajorTickStep()*sideLength*sin60);
		drawLeftAxisGridLines();
	}
	
	private void drawRightAxis(){
		drawRightAxisLabel();
		drawRightAxisMicroTicks(defaultRightAxis.getMicroTickStep()*sideLength*cos60,defaultRightAxis.getMicroTickStep()*sideLength*sin60);
		drawRightAxisMinorTicks(defaultRightAxis.getMinorTickStep()*sideLength*cos60,defaultRightAxis.getMinorTickStep()*sideLength*sin60);		
		drawRightAxisMajorTicks(defaultRightAxis.getMajorTickStep()*sideLength*cos60,defaultRightAxis.getMajorTickStep()*sideLength*sin60);		
		drawRightAxisGridLines();
	}
	
	private void removeLineSet(List<Line> s){
		for (Line l : s){
			canvas.remove(l);
		}
		s.clear();
	}
	
	private void removeTextSet(List<Text> s){
		for (Text t : s){
			canvas.remove(t);
		}
		s.clear();
	}
	
	private String formatDouble(final double d, final int precision){ 
		return "."+String.valueOf((int) (Math.pow(10,precision)*d));
	}
	
	private void drawSkeleton(){
		final Point lowerLeft = new Point(leftBoundary,sideLength);
		final Point lowerRight = new Point(rightBoundary,sideLength);
		final Point upperMiddle = new Point(sideLength/2+leftBoundary,sideLength-Math.sqrt(Math.pow(sideLength,2)-Math.pow(sideLength/2,2)));
		final Line lowerAxis = new Line(lowerLeft,lowerRight);
		final Line leftAxis = new Line(lowerLeft,upperMiddle);
		final Line rightAxis = new Line(lowerRight,upperMiddle);
		border = new Polyline(new Point[] {lowerLeft,lowerRight,upperMiddle,lowerLeft});
		border.setFillColor(new Color(255,255,255,255));
		canvas.add(border,0,0);
	}
	
	public void setBackgroundColor(final Color color){
		border.setFillColor(color);
	}
	
	public int getAxisCount(){
		return 3;
	}
	
	public String getSVG(){
		String innerhtml = canvas.getElement().getInnerHTML();
		return innerhtml.substring(0, innerhtml.indexOf(">")) + "version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\"" + innerhtml.substring(innerhtml.indexOf(">"));
	}
}
