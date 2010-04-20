package edu.rpi.metpetdb.client.ui.image.browser;

public class ImageBrowserUtil {
	public static float pps = 20;
	public static int mmPerCanvas = 50;
	public static int chemImageWidthHalf = 4;
	public static int chemImageHeight = 13;
	public static int magicNumber = 9; // Don't know why it's 9 or where it comes from, it just makes things work
	
	public static double pixelsToMM(int pixels, float mmPerSquare, double resizeRatio){	
		return (pixels/(pps/mmPerSquare))/resizeRatio;
	}	
	public static double pixelsToMM(double pixels, float mmPerSquare, double resizeRatio){	
		return (pixels/(pps/mmPerSquare))/resizeRatio;
	}
	
	public static int MMToPixels(int mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round(mm*pps/mmPerSquare*resizeRatio);
	}
	
	public static int MMToPixels(double mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round(mm*pps/mmPerSquare*resizeRatio);
	}
	
	public static int MMToPixelsChemWidth(int mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round((mm*pps/mmPerSquare)*resizeRatio)-chemImageWidthHalf+magicNumber;
	}
	
	public static int MMToPixelsChemWidth(double mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round((mm*pps/mmPerSquare)*resizeRatio)-chemImageWidthHalf+magicNumber;
	}
	
	public static int MMToPixelsChemHeight(int mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round((mm*pps/mmPerSquare)*resizeRatio)-chemImageHeight+magicNumber;
	}
	
	public static int MMToPixelsChemHeight(double mm, float mmPerSquare, double resizeRatio) {
		return (int) Math.round((mm*pps/mmPerSquare)*resizeRatio)-chemImageHeight+magicNumber;
	}
	
	public static float calculateCurrentResizeRatio(float imageScale, int imageWidth, float mmPerSquare) {
		return ((imageScale/imageWidth)/mmPerSquare)*pps;
	}
	
	public static float calculateCurrentResizeRatio(int imageWidth, float mmPerSquare) {
		return ((mmPerCanvas/imageWidth)/mmPerSquare)*pps;
	}
	
	public static int rotateWidth(int width, int height, double angle) {
		return (int) Math.round((height*Math.cos(Math.toRadians(90-angle)))+(width*Math.cos(Math.toRadians(angle))));
	}
	
	public static int rotateHeight(int width, int height, double angle) {
		return (int) Math.round((height*Math.sin(Math.toRadians(90-angle)))+(width*Math.sin(Math.toRadians(angle))));
	}
}
