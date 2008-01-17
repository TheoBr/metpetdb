package edu.rpi.metpetdb.client.ui.image.browser.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.ui.image.browser.dialogs.PopupMenu;

public class DCFlowPanel extends FlowPanel {

	private PopupMenu popupMenu;

	public DCFlowPanel() {
		super();
		sinkEvents(Event.ONCLICK);
		sinkEvents(Event.ONDBLCLICK);
	}

	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
			case Event.ONDBLCLICK :
				// Window.alert("dblclick");
				if (popupMenu != null) {
					popupMenu.show();
					popupMenu.setPopupPosition(DOM.eventGetClientX(event), DOM
							.eventGetClientY(event));
				}
				break;
			case Event.ONCLICK :
				if (DOM.eventGetCtrlKey(event)) {
					if (popupMenu != null) {
						popupMenu.show();
						popupMenu.setPopupPosition(DOM.eventGetClientX(event),
								DOM.eventGetClientY(event));
					}
				}
				break;
		}
	}

	public void setPopupMenu(final PopupMenu pm) {
		popupMenu = pm;
	}

}
