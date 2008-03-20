package edu.rpi.metpetdb.client.ui.image.browser.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.widgetideas.client.ProgressBar;

public class DraggableProgressBar extends ProgressBar implements
		SourcesMouseEvents, SourcesClickEvents {

	private MouseListenerCollection mouseListeners;
	private ClickListenerCollection clickListeners;

	public DraggableProgressBar(final int elements) {
		super(elements);
		sinkEvents(Event.MOUSEEVENTS);
		sinkEvents(Event.ONCHANGE);
	}

	public DraggableProgressBar(final int elements, final int options) {
		super(elements, options);
		sinkEvents(Event.MOUSEEVENTS);
		sinkEvents(Event.ONCHANGE);
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

	public void addClickListener(final ClickListener listener) {
		if (clickListeners == null)
			clickListeners = new ClickListenerCollection();
		clickListeners.add(listener);
	}

	public void removeClickListener(final ClickListener listener) {
		if (clickListeners != null)
			clickListeners.remove(listener);
	}

	public void onBrowserEvent(Event event) {
		if (mouseListeners != null) {
			switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
			case Event.ONMOUSEMOVE:
			case Event.ONMOUSEOUT:
			case Event.ONMOUSEOVER:
			case Event.ONMOUSEUP:
				mouseListeners.fireMouseEvent(this, event);
				break;
			case Event.ONCLICK:
				clickListeners.fireClick(this);
				break;
			}
			;
			// DOM.eventPreventDefault(event);
		}
	}
}
