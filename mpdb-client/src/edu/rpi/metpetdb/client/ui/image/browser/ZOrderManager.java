package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;

public class ZOrderManager {

	private Set<ImageOnGrid> imagesOnGrid = new HashSet<ImageOnGrid>();

	public ZOrderManager() {

	}

	public void register(final ImageOnGrid iog) {
		// iog.setZorder(this.getHighestZOrder()+1);
		imagesOnGrid.add(iog);
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getIog().getZorder()));
	}

	public void reset() {
		imagesOnGrid.clear();
	}

	public void bringToFront(final ImageOnGrid iog) {
		modifyAllZOrders(-1);
		iog.getIog().setZorder(getHighestZOrder() + 1);
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getIog().getZorder()));
	}

	public void sendToBack(final ImageOnGrid iog) {
		modifyAllZOrders(1);
		iog.getIog().setZorder((getLowestZOrder() - 1));
		DOM.setStyleAttribute(iog.getImageContainer().getElement(), "zIndex",
				String.valueOf(iog.getIog().getZorder()));
	}
	public void modifyAllZOrders(final int offset) {
		final Iterator<ImageOnGrid> itr = imagesOnGrid.iterator();
		while (itr.hasNext()) {
			final ImageOnGrid iog = itr.next();
			iog.getIog().setZorder(iog.getIog().getZorder() + offset);
			DOM.setStyleAttribute(iog.getImageContainer().getElement(),
					"zIndex", String.valueOf(iog.getIog().getZorder()));
		}
	}

	public int getHighestZOrder() {
		final Iterator<ImageOnGrid> itr = imagesOnGrid.iterator();
		int highestZOrder = 0;
		while (itr.hasNext()) {
			final ImageOnGrid iog = itr.next();
			if (iog.getIog().getZorder() > highestZOrder) {
				highestZOrder = iog.getIog().getZorder();
			}
		}
		return highestZOrder;
	}

	public int getLowestZOrder() {
		final Iterator<ImageOnGrid> itr = imagesOnGrid.iterator();
		int lowestZOrder = 9999;
		while (itr.hasNext()) {
			final ImageOnGrid iog =  itr.next();
			if (iog.getIog().getZorder() < lowestZOrder) {
				lowestZOrder = iog.getIog().getZorder();
			}
		}
		return lowestZOrder;
	}

}
