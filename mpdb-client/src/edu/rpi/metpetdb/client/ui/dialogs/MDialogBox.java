package edu.rpi.metpetdb.client.ui.dialogs;

import org.gwtwidgets.client.ui.PNGImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;


public class MDialogBox extends DialogBox
		implements
			PopupListener,
			WindowResizeListener {
	private PNGImage png;
	private final PopupPanel background;

	public MDialogBox() {
		background = new PopupPanel();
		addPopupListener(this);
		super.setHTML("&nbsp;");
		
		DOM.setStyleAttribute(this.getElement(), "zIndex", String.valueOf(1000));
	}

	public void onWindowResized(final int width, final int height) {
		background.setWidth(Integer.toString(width));
		background.setHeight(Integer.toString(height));
		png.setPixelSize(width, height);
		background.setPopupPosition(0, 0);
	}

	public void onPopupClosed(final PopupPanel me, final boolean autoClosed) {
		if (png != null)
			png.removeFromParent();
		png = null;
		showSelects();
		background.hide();
		Window.removeWindowResizeListener(this);
	}

	public void show() {
		final int windowWidth = Window.getClientWidth();
		final int windowHeight = Window.getClientHeight();

		hideSelects();
		png = new PNGImage(GWT.getHostPageBaseURL() + "images/lightbox.png", windowWidth, windowHeight);
		background.setWidth(Integer.toString(windowWidth));
		background.setHeight(Integer.toString(windowHeight));
		background.setWidget(png);
		background.setPopupPosition(0, 0);
		background.show();
		backgroundFixup(background.getElement());
		Window.addWindowResizeListener(this);

		setPopupPosition(25, 25);
		super.show();
	}

	protected void onLoad() {
		final int left = (Window.getClientWidth() - getOffsetWidth()) / 2;
		final int top = (Window.getClientHeight() - getOffsetHeight()) / 2;
		setPopupPosition(left, top);
	}

	private native void hideSelects() /*-{
	 var selects = $doc.getElementsByTagName("select");
	 for (i = 0; i != selects.length; i++) {
	 selects[i].style.visibility = "hidden";
	 }
	 }-*/;

	private native void showSelects() /*-{
	 var selects = $doc.getElementsByTagName("select");
	 for (i = 0; i != selects.length; i++) {
	 selects[i].style.visibility = "visible";
	 }
	 }-*/;

	private native void backgroundFixup(Element e)
	/*-{
	 // fixes issue with GWT 1.1.10 by hiding the iframe
	 if (e.__frame) {
	 e.__frame.style.visibility = 'hidden';
	 }
	 }-*/;
	
}
