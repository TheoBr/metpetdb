package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class MAbsolutePanel extends AbsolutePanel implements
		SourcesMouseEvents, SourcesClickEvents {

	private MouseListenerCollection mouseListeners;
	private ClickListenerCollection clickListeners;
	private boolean canDrag;
	private boolean ctrlDown;
	private ZMode zMode = ZMode.NO_ZMODE;
	
	public enum ZMode {
		NO_ZMODE,
		SEND_TO_BACK,
		BRING_TO_FRONT,
	}

	public MAbsolutePanel() {
		super();
		sinkEvents(Event.MOUSEEVENTS);
		sinkEvents(Event.ONCLICK);
		// sinkEvents(Event.KEYEVENTS);
		canDrag = true;
	}

	public ZMode getZMode() {
		return zMode;
	}

	public void setZMode(final ZMode i) {
		zMode = i;
		if (i == ZMode.NO_ZMODE) {
			DOM.setStyleAttribute(this.getElement(), "cursor", "default");
		} else {
			DOM.setStyleAttribute(this.getElement(), "cursor", "crosshair");
		}
	}

	public boolean getCanDrag() {
		return canDrag;
	}

	public void setCanDrag(final boolean b) {
		canDrag = b;
	}

	public boolean getCtrlDown() {
		return ctrlDown;
	}
	public void setCtrlDown(final boolean b) {
		ctrlDown = b;
	}

	public void addClickListener(final ClickListener listener) {
		if (clickListeners == null)
			clickListeners = new ClickListenerCollection();
		clickListeners.add(listener);
	}

	public void removeClickListener(final ClickListener listener) {
		if (clickListeners != null)
			clickListeners.remove(listener);
	}

	public void addMouseListener(final MouseListener listener) {
		if (mouseListeners == null)
			mouseListeners = new MouseListenerCollection();
		mouseListeners.add(listener);
	}
	public void removeMouseListener(final MouseListener listener) {
		if (mouseListeners != null)
			mouseListeners.remove(listener);
	}

	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN:
		case Event.ONMOUSEUP:
			// before we prevent default make sure the action is within our
			// widget
			final int eventX = DOM.eventGetClientX(event);
			final int eventY = DOM.eventGetClientY(event);
			final int x = this.getAbsoluteLeft();
			final int y = this.getAbsoluteTop();
			final int width = this.getOffsetWidth();
			final int height = this.getOffsetHeight();
			if (eventX > x && eventX < x + width) {
				if (eventY > y && eventY < y + height) {
					DOM.eventPreventDefault(event);
				}
			}
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOUT:
		case Event.ONMOUSEOVER:
			if (mouseListeners != null)
				mouseListeners.fireMouseEvent(this, event);
			// DOM.eventPreventDefault(event);
			break;
		case Event.ONCLICK:
			if (clickListeners != null)
				clickListeners.fireClick(this);
			break;
		};

	}

	public void setWidgetPosition(final Widget w, final int x, final int y) {
		// account for borders on the images
		int newX = x + 1; // - 16;
		int newY = y + 1; // - 16;
		if (w.getStyleName().equals("imageContainer")) {

		}
		super.setWidgetPosition(w, newX, newY);
	}

}
