package edu.rpi.metpetdb.client.model;

public class ImageOnGrid extends MObject {
	private static final long serialVersionUID = 1L;
	private int id;
	private Grid grid;
	private Image image;
	private double topLeftX;
	private double topLeftY;
	private int zOrder;
	private int opacity;
	private double resizeRatio;
	private int gwidth;
	private int gheight;
	private String gchecksum;
	private String gchecksum64x64;
	private String gchecksumHalf;
	private Boolean locked; 
	private double actualCurrentResizeRatio;
	private double angle;
	
	public ImageOnGrid(){
		locked = false;
	}

	public int getId() {
		return id;
	}

	public void setId(final int i) {
		id = i;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(final Grid g) {
		grid = g;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(final Image i) {
		image = i;
	}

	public int getGwidth() {
		return gwidth;
	}

	public void setGwidth(final int l) {
		gwidth = l;
	}

	public int getGheight() {
		return gheight;
	}

	public void setGheight(final int l) {
		gheight = l;
	}

	public String getGchecksum() {
		return gchecksum;
	}

	public void setGchecksum(final String i) {
		gchecksum = i;
	}

	public String getGchecksum64x64() {
		return gchecksum64x64;
	}

	public void setGchecksum64x64(final String s) {
		gchecksum64x64 = s;
	}

	public String getGchecksumHalf() {
		return gchecksumHalf;
	}

	public void setGchecksumHalf(final String s) {
		gchecksumHalf = s;
	}

	public double getTopLeftX() {
		return topLeftX;
	}

	public void setTopLeftX(final double s) {
		topLeftX = s;
		// originalTopLeftX = s;
	}

	public double getTopLeftY() {
		return topLeftY;
	}

	public void setTopLeftY(final double s) {
		topLeftY = s;
		// originalTopLeftY = s;
	}

	public int getZorder() {
		return zOrder;
	}

	public void setZorder(final int s) {
		zOrder = s;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(final int s) {
		opacity = s;
	}

	public double getResizeRatio() {
		return resizeRatio;
	}

	public void setResizeRatio(final double s) {
		resizeRatio = s;
	}

	public boolean equals(final Object o) {
		return o instanceof ImageOnGrid && grid == ((ImageOnGrid) o).getGrid()
				&& image == ((ImageOnGrid) o).getImage();
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public void delete() {
		this.getGrid().getImagesOnGrid().remove(this);
		this.setGrid(null);
	}
	
	public Boolean isLocked(){
		return locked;
	}
	
	public void setLocked(final Boolean l) {
		locked = l;
	}
	
	public double getActualCurrentResizeRatio(){
		return actualCurrentResizeRatio;
	}
	
	public void setActualCurrentResizeRatio(final double s) {
		actualCurrentResizeRatio = s;
	}
	
	public double getAngle(){
		return angle;
	}
	
	public void setAngle(final double angle) {
		this.angle = angle;
	}
}
