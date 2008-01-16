package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;

public class MSimplePanel extends SimplePanel {
	

	private KeyboardListenerCollection keyboardListeners;

	public MSimplePanel() {
		sinkEvents(Event.KEYEVENTS);
	}

	public void addKeyboardListener(final KeyboardListener listener) {
		if (keyboardListeners == null)
			keyboardListeners = new KeyboardListenerCollection();
		keyboardListeners.add(listener);
	}

	public void removeKeyboardListener(final KeyboardListener listener) {
		if (keyboardListeners != null)
			keyboardListeners.remove(listener);
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.KEYEVENTS) {
			if (keyboardListeners != null)
				keyboardListeners.fireKeyboardEvent(this, event);
		}
	}

}
