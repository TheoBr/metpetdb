package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;

import edu.rpi.metpetdb.client.model.ImageOnGrid;

public class ZOrderManager {

	private Set imagesOnGrid = new HashSet();

	public ZOrderManager() {

	}

	public void register(final ImageOnGrid iog) {
		// iog.setZorder(this.getHighestZOrder()+1);
		imagesOnGrid.add(iog);
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getZorder()));
	}

	public void reset() {
		imagesOnGrid.clear();
	}

	public void bringToFront(final ImageOnGrid iog) {
		modifyAllZOrders(-1);
		iog.setZorder(getHighestZOrder() + 1);
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getZorder()));
	}

	public void sendToBack(final ImageOnGrid iog) {
		modifyAllZOrders(1);
		iog.setZorder((getLowestZOrder() - 1));
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getZorder()));
	}
	public void modifyAllZOrders(final int offset) {
		final Iterator itr = imagesOnGrid.iterator();
		while (itr.hasNext()) {
			final ImageOnGrid iog = (ImageOnGrid) itr.next();
			iog.setZorder(iog.getZorder() + offset);
			DOM.setStyleAttribute(iog.getImageContainer().getElement(),
					"zIndex", String.valueOf(iog.getZorder()));
		}
	}

	public int getHighestZOrder() {
		final Iterator itr = imagesOnGrid.iterator();
		int highestZOrder = 0;
		while (itr.hasNext()) {
			final ImageOnGrid iog = (ImageOnGrid) itr.next();
			if (iog.getZorder() > highestZOrder) {
				highestZOrder = iog.getZorder();
			}
		}
		return highestZOrder;
	}

	public int getLowestZOrder() {
		final Iterator itr = imagesOnGrid.iterator();
		int lowestZOrder = 9999;
		while (itr.hasNext()) {
			final ImageOnGrid iog = (ImageOnGrid) itr.next();
			if (iog.getZorder() < lowestZOrder) {
				lowestZOrder = iog.getZorder();
			}
		}
		return lowestZOrder;
	}

}
