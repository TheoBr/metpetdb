package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

public class LayerDragMouseListener implements MouseListener {

	private final MHtmlList ul;
	private final HashSet layerItems;
	private MHtmlList.ListItem currentItem;
	private boolean currentCheck;

	private EventPreview eventPreview = new EventPreview() {
		public boolean onEventPreview(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONMOUSEDOWN :
					// before we prevent default make sure the action is within
					// our widget
					final int eventX = DOM.eventGetClientX(event);
					final int eventY = DOM.eventGetClientY(event);
					final int x = ul.getAbsoluteLeft();
					final int y = ul.getAbsoluteTop();
					final int width = ul.getOffsetWidth();
					final int height = ul.getOffsetHeight();
					if (eventX > x && eventX < x + width) {
						if (eventY > y && eventY < y + height) {
							DOM.eventPreventDefault(event);
						}
					}
			}
			return true;
		}
	};

	public LayerDragMouseListener(final MHtmlList mol, final HashSet hs) {
		ul = mol;
		layerItems = hs;
	}

	private MHtmlList.ListItem findListItem(final int x, final int y) {
		final Iterator itr = ul.getItems().iterator();
		final int absoluteX = ul.getAbsoluteLeft() + x;
		final int absoluteY = ul.getAbsoluteTop() + y;
		while (itr.hasNext()) {
			final MHtmlList.ListItem item = (MHtmlList.ListItem) itr
					.next();
			final int itemX = DOM.getAbsoluteLeft(item.getLi());
			final int itemY = DOM.getAbsoluteTop(item.getLi());
			final int itemWidth = DOM.getElementPropertyInt(item.getLi(),
					"offsetWidth");
			final int itemHeight = DOM.getElementPropertyInt(item.getLi(),
					"offsetHeight");
			if (absoluteX > itemX && absoluteX < itemX + itemWidth) {
				if (absoluteY > itemY && absoluteY < itemY + itemHeight)
					return item;
			}
		}
		return null;
	}

	public void onMouseDown(final Widget sender, final int x, final int y) {
		DOM.setCapture(sender.getElement());
		currentItem = findListItem(x, y);
		if (currentItem != null) {
			currentItem.getWidget().setStyleName("layer-Drag");
			currentCheck = ((CheckBox) ((SimplePanel) currentItem.getWidget())
					.getWidget()).isChecked();
		}
	}

	public void onMouseUp(final Widget sender, final int x, final int y) {
		DOM.releaseCapture(sender.getElement());
		if (currentItem != null) {
			currentItem.getWidget().removeStyleName("layer-Drag");
			((CheckBox) ((SimplePanel) currentItem.getWidget()).getWidget())
					.setChecked(currentCheck);
		}
		currentItem = null;

	}

	public void onMouseMove(final Widget sender, final int x, final int y) {
		if (currentItem != null) {
			final MHtmlList.ListItem target = findListItem(x, y);
			if (target != null && target != currentItem) {
				moveLayer(currentItem.getIndex(), target.getIndex());
				ul.moveItem(currentItem, target.getIndex());
			}
			if (((CheckBox) ((SimplePanel) currentItem.getWidget()).getWidget())
					.isChecked() == currentCheck) {
				currentCheck = !currentCheck;
			}
		}
	}

	public void moveLayer(final int source, final int target) {
		final Iterator itr = layerItems.iterator();
		final boolean movedDown = source < target ? true : false;
		while (itr.hasNext()) {
			final LeftSideLayer.LayerItem layerItem = (LeftSideLayer.LayerItem) itr
					.next();
			if (movedDown) {
				if (layerItem.getIndex() <= target
						&& layerItem.getIndex() >= source) {
					if (layerItem.getIndex() != source) {
						layerItem.getImageOnGrid().getIog().setZorder(
								layerItem.getImageOnGrid().getIog().getZorder() + 1);
						layerItem.decrementIndex();
					} else {
						layerItem.getImageOnGrid().getIog().setZorder(
								layerItem.getImageOnGrid().getIog().getZorder() - 1);
						layerItem.incrementIndex();
					}

				}
			} else {
				if (layerItem.getIndex() >= target
						&& layerItem.getIndex() <= source) {
					if (layerItem.getIndex() != source) {
						layerItem.incrementIndex();
						layerItem.getImageOnGrid().getIog().setZorder(
								layerItem.getImageOnGrid().getIog().getZorder() - 1);
					} else {
						layerItem.decrementIndex();
						layerItem.getImageOnGrid().getIog().setZorder(
								layerItem.getImageOnGrid().getIog().getZorder() + 1);
					}

				}
			}
			DOM.setStyleAttribute(layerItem.getImageOnGrid()
					.getImageContainer().getElement(), "zIndex", String
					.valueOf(layerItem.getImageOnGrid().getIog().getZorder()));
		}
	}

	public void onMouseEnter(final Widget sender) {
		DOM.addEventPreview(eventPreview);
	}

	public void onMouseLeave(final Widget sender) {
		DOM.removeEventPreview(eventPreview);
	}

}
