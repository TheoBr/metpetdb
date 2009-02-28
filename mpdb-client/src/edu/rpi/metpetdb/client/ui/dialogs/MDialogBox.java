package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.widgetideas.client.GlassPanel;

import edu.rpi.metpetdb.client.ui.JS;

public class MDialogBox extends DialogBox implements PopupListener,
		WindowResizeListener {
	private GlassPanel glassPanel;

	public MDialogBox() {
		addPopupListener(this);
		super.setHTML("&nbsp;");

		DOM.setStyleAttribute(this.getElement(), "zIndex", String.valueOf(1000));
		glassPanel = new GlassPanel(false);
	}

	public void onWindowResized(final int width, final int height) {

	}

	public void onPopupClosed(final PopupPanel me, final boolean autoClosed) {
		glassPanel.removeFromParent();
	}

	public void show() {
		// hideSelects();
		//RootPanel.get().add(glassPanel, 0, 0);
		Window.addWindowResizeListener(this);	
		setPopupPosition(25, 25);
		super.show();
		JS.scrollWindowToTop();
	}

	protected void onLoad() {
		final int left = (Window.getClientWidth() - getOffsetWidth()) / 2;
		final int top = (Window.getClientHeight() - getOffsetHeight()) / 2;
		setPopupPosition(left, top);
	}
	
	
}
