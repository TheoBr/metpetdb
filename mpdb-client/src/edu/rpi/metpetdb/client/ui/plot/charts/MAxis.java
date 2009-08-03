package edu.rpi.metpetdb.client.ui.plot.charts;

import java.util.HashMap;
import java.util.Map;

import com.objetdirect.tatami.client.charting.Axis;
import com.objetdirect.tatami.client.gfx.Color;

import edu.rpi.metpetdb.client.ui.plot.AxisFormula;

public class MAxis {
	final public static int VERTICAL = 1;
	
	final public static int HORIZONTAL = 0;
	
	final public static int LEFT = 0;
	
	final public static int RIGHT = 2;
	
	final public static int BOTTOM = 0;
	
	final public static int TOP = 2;
	
	final public static String FIX_TYPE_NONE = "none";
	final public static String FIX_TYPE_MINOR = "minor";
	final public static String FIX_TYPE_MAJOR = "major";
	final public static String FIX_TYPE_MICRO = "micro";
	
	public static enum LabelOrientation {
		HORIZONTAL, VERTICAL, DIAGONAL
	};
	
	public static enum GridLine {
		NONE, MAJOR, MINOR, MICRO
	};
	

	private String fixUpper = "none";	// align the upper on ticks: "major", "minor", "micro", "none"
	private String fixLower=    "none";	// align the lower on ticks= "major"; "minor"; "micro"; "none"
	private Boolean natural=     false;		// all tick marks should be made on natural numbers
	private Boolean fixed=       true;		// all labels are fixed numbers
	private Boolean majorLabels= true;		// draw major labels
	private Boolean minorTicks=  true;		// draw minor ticks
	private Color minorTicksColor;
	private double minorTickStep = .05;
	private int minorTicksLength = 5;
	private Boolean majorTicks=  true;		// draw minor ticks
	private Color majorTicksColor;
	private double majorTickStep = .1;
	private int majorTicksLength = 7;
	private Boolean minorLabels= true;		// draw minor labels
	private Boolean microTicks=  true;		// draw micro ticks
	private Color microTicksColor;
	private double microTickStep = .01;
	private int microTicksLength = 3;
	private GridLine gridLine = GridLine.MAJOR;
	private Color gridLineColor;
	private Boolean htmlLabels=  true;		// use HTML to draw labels
	private LabelOrientation orientation = LabelOrientation.HORIZONTAL; // labels are down horizontally
	private int majorTickPrecision = 1;
	private int minorTickPrecision = 2;
	private int microTickPrecision = 2;
	
	

	
	private String name ;
	
	private Map<String,Object> options;
	private MAxisLabel label;
	private AxisFormula formula;
	
	public AxisFormula getAxisFormula(){
		return formula;
	}
	
	public void setAxisFormula(final AxisFormula formula){
		this.formula = formula;
	}
	
	
	public LabelOrientation getLabelOrientation() {
		return orientation;
	}

	public void setLabelOrientation(LabelOrientation orientation) {
		this.orientation = orientation;
		options.put("labelOrientation", orientation);
	}
	/**
	 * Constructs a default bottom horizontal axis
	 */
	public MAxis(){
		options = new HashMap<String,Object>();
		label = new MAxisLabel("");
		majorTicksColor = new Color(0,0,0,255);
		minorTicksColor = new Color(0,0,0,255);
		microTicksColor = new Color(0,0,0,255);
		gridLineColor = new Color(0,0,0,255);
	}
	
	
	/**
	 * @return this axis options
	 */
	public Map<String,?> getOptions() {
		return options;
	}

	/**
	 * @return: the axis id.
	 */
	public String getId() {
		if(name != null){
			return name;
		}
		return this.toString();
	}

	/**
	 * @return a default horizontal axis instance
	 */
	public static Axis simpleXAxis() {
		return new Axis(BOTTOM | HORIZONTAL);
	}

	
	/**
	 * @return a default vertical axis instance
	 */
	public static Axis simpleYAxis() {
		return new Axis(LEFT | VERTICAL);
	}

	/**
	 * @return where the upper value is fixed
	 */
	public String getFixUpper() {
		return fixUpper;
	}

	/**
	 * @param fixUpper : Forces the upper value on the axis to be aligned with the corresponding tick
	 * Possible value are :
	 * -Axis.FIX_TYPE_NONE
	 * -Axis.FIX_TYPE_MINOR
	 * -Axis.FIX_TYPE_MAJOR
	 * -Axis.FIX_TYPE_MICRO
	 */
	public void setFixUpper(String fixUpper) {
		this.fixUpper = fixUpper;
		options.put("fixUpper", fixUpper);
	}

	/**
	 * @return where the upper lower is fixed
	 */
	public String getFixLower() {
		return fixLower;
	}

	/**
	 * @param fixLower : Forces the lower value on the axis to be aligned with the corresponding tick
	 * Possible value are :
	 * -Axis.FIX_TYPE_NONE
	 * -Axis.FIX_TYPE_MINOR
	 * -Axis.FIX_TYPE_MAJOR
	 * -Axis.FIX_TYPE_MICRO
	 */
	public void setFixLower(String fixLower) {
		this.fixLower = fixLower;
		options.put("fixLower", fixLower);
	}

	/**
	 * @return: whether the ticks are forced to be on natural numbers
	 */
	public Boolean getNatural() {
		return natural;
	}

	/**
	 * @param natural : forces all ticks to be on natural numbers
	 */
	public void setNatural(Boolean natural) {
		this.natural = natural;
		options.put("natural", natural);
	}

	/**
	 * @return: fixes the precision on the labels
	 */
	public Boolean getFixed() {
		return fixed;
	}

	/**
	 * @param fixed : fixes the precision on the labels
	 */
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
		options.put("fixed", fixed);
	}

	/**
	 * @return: whether the major labels are displayed
	 */
	public Boolean getMajorLabels() {
		return majorLabels;
	}

	/**
	 * @param majorLabels : whether the major ticks should be labeled
	 */
	public void setMajorLabels(Boolean majorLabels) {
		this.majorLabels = majorLabels;
		options.put("majorLabels", majorLabels);
	}

	public Boolean getMinorTicks() {
		return minorTicks;
	}

	/**
	 * @param minorTicks : whether the minor ticks should be displayed
	 */
	public void setMinorTicks(Boolean minorTicks) {
		this.minorTicks = minorTicks;
		options.put("minorTicks", minorTicks);
	}

	public Boolean getMinorLabels() {
		return minorLabels;
	}

	/**
	 * @param minorLabels : whether the minor ticks should be labeled
	 */
	public void setMinorLabels(Boolean minorLabels) {
		this.minorLabels = minorLabels;
		options.put("minorLabels", minorLabels);
	}

	public Boolean getMicroTicks() {
		return microTicks;
	}

	/**
	 * @param microTicks : whether the micro ticks should be displayed
	 */
	public void setMicroTicks(Boolean microTicks) {
		this.microTicks = microTicks;
		options.put("microTicks", microTicks);
	}

	public Boolean getMajorTicks() {
		return majorTicks;
	}

	/**
	 * @param majorTicks : whether the major ticks should be displayed
	 */
	public void setMajorTicks(Boolean majorTicks) {
		this.majorTicks = majorTicks;
		options.put("majorTicks", microTicks);	
	}

	public Boolean getHtmlLabels() {
		return htmlLabels;
	}

	/**
	 * @param htmlLabels 
	 */
	public void setHtmlLabels(Boolean htmlLabels) {
		this.htmlLabels = htmlLabels;
		options.put("htmlLabels", htmlLabels);
	}

	/**
	 * @param options : options to be passed to the Chart. Prefer using the 
	 * various setters.
	 */
	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
	
	
	/**
	 * @param min : min value for the axis
	 */
	public void setMin(double min){
		options.put("min", min);
	}
	
	/**
	 * @param max : max value for the axis
	 */
	public void setMax(double max){
		options.put("max", max);
	}
	
	protected void setName(String name){
		this.name = name;
	}
	
	/**
	 * @param color : minor ticks color
	 */
	public void setMinorTicksColor(Color color){
		minorTicksColor = color;
	}
	/**
	 * @param color : major ticks color
	 */
	public void setMajorTicksColor(Color color){
		majorTicksColor = color;
	}
	
	/**
	 * @param color : micro ticks color
	 */
	public void setMicroTicksColor(Color color){
		microTicksColor = color;
	}
	public void setGridLineColor(Color color){
		gridLineColor = color;
	}
	
	public Color getMajorTicksColor(){
		return majorTicksColor;
	}
	public Color getMinorTicksColor(){
		return minorTicksColor;
	}
	public Color getMicroTicksColor(){
		return microTicksColor;
	}
	public Color getGridLineColor(){
		return gridLineColor;
	}
	
	
	/**
	 * @param length : major ticks length
	 */
	public void setMajorTicksLength(int length){
		majorTicksLength = length;
	}
	
	/**
	 * @param length : major ticks length
	 */
	public int getMajorTicksLength(){
		return majorTicksLength;
	}
	
	/**
	 * @param step : step between the major ticks
	 */
	public void setMajorTickStep(double step) {
		majorTickStep = step;
	}
	
	public double getMajorTickStep() {
		return majorTickStep;
	}
	
	/**
	 * @param step : step between the minor ticks
	 */
	public void setMinorTickStep(double step) {
		minorTickStep = step;
	}
	
	public double getMinorTickStep() {
		return minorTickStep;
	}
	
	/**
	 * @param length : minor ticks length
	 */
	public void setMinorTicksLength(int length){
		minorTicksLength = length;
	}
	
	/**
	 * @param length : micro ticks length
	 */
	public int getMinorTicksLength(){
		return minorTicksLength;
	}
	
	/**
	 * @param step : step between the micro ticks
	 */
	public void setMicroTickStep(double step) {
		microTickStep = step;
	}
	
	public double getMicroTickStep() {
		return microTickStep;
	}
	
	/**
	 * @param length : micro ticks length
	 */
	public void setMicroTicksLength(int length){
		microTicksLength = length;
	}
	
	/**
	 * @param length : micro ticks length
	 */
	public int getMicroTicksLength(){
		return microTicksLength;
	}
	
	
	
	
	/**
	 * @param color : axis labels font color 
	 */
	public void setFontColor(String color){
		options.put("fontColor", color);
	}
	/**
	 * @param font : axis labels font (ex: "normal normal bold 14pt Tahoma")  
	 */
	public void setFont(String font){
		options.put("font", font);
	}
	
	/**
	 * @param text : the label itself
	 * @param forValue : the value for which we define a label
	 */
	public void setLabel(String text, double forValue){
		label = new MAxisLabel(text,forValue);
	}
	
	/**
	 * @param label
	 */
	public void setLabel(MAxisLabel label){
		this.label = label;
	}
	
	
	public MAxisLabel getLabel() {
		return label;
	}
	
	public void setGridLines(final GridLine g){
		gridLine = g;
	}
	
	public GridLine getGridLines(){
		return gridLine;
	}
	
	public int getMajorTickPrecision(){
		return majorTickPrecision;
	}
	public int getMinorTickPrecision(){
		return minorTickPrecision;
	}
	public int getMicroTickPrecision(){
		return microTickPrecision;
	}
	public void setMajorTickPrecision(final int precision){
		majorTickPrecision = precision;
	}
	public void setMinorTickPrecision(final int precision){
		minorTickPrecision = precision;
	}
	public void setMicroTickPrecision(final int precision){
		microTickPrecision = precision;
	}
	
}
