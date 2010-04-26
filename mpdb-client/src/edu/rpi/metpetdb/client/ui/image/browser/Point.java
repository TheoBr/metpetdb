package edu.rpi.metpetdb.client.ui.image.browser;

public class Point {
	
	public double x = 0;
	public double y = 0;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y) {
		this((double) x, (double) y);
	}
	
	public Point() {
		this(0,0);
	}

}
