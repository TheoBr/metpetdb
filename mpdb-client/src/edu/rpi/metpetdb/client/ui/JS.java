package edu.rpi.metpetdb.client.ui;

import com.google.gwt.dom.client.Element;

public class JS {
	
	private JS() {}
	
	public static native void blur(Element elem) /*-{
		elem.blur();
	}-*/;
	
	/**
	 * Wraps all digits in &lt;sub&gt; tags. Used in displaying oxides.
	 * @param s - string to be modified
	 * @return modified string
	 */
	public static native String subscript(String s) /*-{
		var str = s.replace(/(\d+)/g, "<sub>$1</sub>");
		return str;
	}-*/;
	
	/**
	 * Strips HTML tags from a string.
	 * @param s - string to be modified
	 * @return modified string
	 */
	public static native String stripTags(String s) /*-{
		var str = s.replace(/<[\/]{0,1}(U|u)[^><]*>/g,"");
		return str;
	}-*/; 
	
	/**
	 * Scrolls users browser window to the tippity top
	 */
	public static  native void scrollWindowToTop() /*-{
	$wnd.scrollTo(0,0);
	}-*/;
}
