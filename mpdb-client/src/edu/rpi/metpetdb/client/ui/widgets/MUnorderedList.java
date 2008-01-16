package edu.rpi.metpetdb.client.ui.widgets;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class MUnorderedList extends Panel
		implements
			IndexedPanel,
			SourcesMouseEvents {

	private final Element ul;
	private final HashSet items;
	private MouseListenerCollection mouseListeners;

	public MUnorderedList() {
		super();
		ul = DOM.createElement("ul");
		this.setElement(ul);
		items = new HashSet();
		sinkEvents(Event.MOUSEEVENTS);
	}

	public HashSet getItems() {
		return items;
	}

	public void add(final Widget w) {
		String className = "oddRow";
		if (items.size() % 2 == 0) {
			className = "evenRow";
		}
		add(w, className);
	}

	public void add(final Widget w, final String liStyle) {
		final Element li = DOM.createElement("li");
		w.removeFromParent();
		DOM.setElementAttribute(li, "class", liStyle);
		DOM.appendChild(li, w.getElement());
		DOM.appendChild(ul, li);
		final ListItem item = new ListItem(w, li, items.size());
		items.add(item);
		this.adopt(w);
	}

	public void add(final Widget w, final int index) {
		final Element li = DOM.createElement("li");
		w.removeFromParent();
		DOM.appendChild(li, w.getElement());
		DOM.insertChild(ul, li, index);
		final ListItem item = new ListItem(w, li, index);
		final Iterator itr = items.iterator();
		while (itr.hasNext()) {
			final ListItem listItem = (ListItem) itr.next();
			if (listItem.getIndex() >= index) {
				listItem.incrementIndex();
			}
		}
		items.add(item);
		this.adopt(w);
	}

	public void setLiStyle(final String styleName, final Widget w) {
		final ListItem item = getListItemWithWidget(w);
		if (item != null) {
			DOM.setElementAttribute(item.getLi(), "class", styleName);
		}
	}

	public boolean remove(final Widget w) {
		final ListItem item = getListItemWithWidget(w);
		if (item == null) {
			return false;
		} else {
			DOM.removeChild(ul, item.getLi());
			items.remove(item);
			final Iterator itr = items.iterator();
			while (itr.hasNext()) {
				final ListItem listItem = (ListItem) itr.next();
				if (listItem.getIndex() > item.index) {
					listItem.decrementIndex();
				}
			}
			return true;
		}
	}

	public boolean remove(final int i) {
		if (i < 0 || i >= items.size())
			return false;
		else {
			final ListItem item = getListItemAtIndex(i);
			if (item != null) {
				this.remove(item.getWidget());
				return true;
			} else {
				return false;
			}

		}
	}

	public int getWidgetCount() {
		return items.size();
	}

	public int getWidgetIndex(final Widget w) {
		return getListItemWithWidget(w).getIndex();
	}

	public Widget getWidget(final int i) {
		if (i < 0 || i >= items.size())
			return null;
		else
			return ((ListItem) getListItemAtIndex(i)).getWidget();
	}

	public Iterator iterator() {
		return new Iterator() {
			Widget returned = null;
			int currentIndex = 0;

			public boolean hasNext() {
				if (items.size() > 0) {
					if (currentIndex < items.size()) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

			public Object next() {
				if (hasNext()) {
					final ListItem item = ((ListItem) getListItemAtIndex(currentIndex));
					if (item != null) {
						returned = item.getWidget();
						currentIndex++;
						return returned;
					} else
						return null;
				} else {
					return null;
				}
			}

			public void remove() {
				if (returned != null) {
					MUnorderedList.this.remove(returned);
				}
			}
		};
	}

	public void moveItem(final ListItem item, final int index) {
		final int newIndex = index;
		DOM.removeChild(ul, item.getLi());
		DOM.insertChild(ul, item.getLi(), newIndex);
		final Iterator itr = items.iterator();
		final boolean movedDown = item.getIndex() < index ? true : false;
		while (itr.hasNext()) {
			final ListItem listItem = (ListItem) itr.next();
			if (!listItem.equals(item)) {
				if (movedDown) {
					if (listItem.getIndex() <= index
							&& listItem.getIndex() >= item.getIndex())
						listItem.decrementIndex();
				} else {
					if (listItem.getIndex() >= index
							&& listItem.getIndex() <= item.getIndex())
						listItem.incrementIndex();
				}
			}
		}
		item.setIndex(index);
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
			case Event.ONMOUSEDOWN :
			case Event.ONMOUSEUP :
			case Event.ONMOUSEMOVE :
			case Event.ONMOUSEOUT :
			case Event.ONMOUSEOVER :
				if (mouseListeners != null)
					mouseListeners.fireMouseEvent(this, event);
				break;
		};

	}

	public ListItem getListItemAtIndex(final int index) {
		final Iterator itr = items.iterator();
		while (itr.hasNext()) {
			final ListItem item = (ListItem) itr.next();
			if (item.getIndex() == index) {
				return item;
			}
		}
		return null;
	}

	public ListItem getListItemWithWidget(final Widget w) {
		final Iterator itr = items.iterator();
		while (itr.hasNext()) {
			final ListItem item = (ListItem) itr.next();
			if (item.getWidget().equals(w)) {
				return item;
			}
		}
		return null;
	}

	public class ListItem {
		private Widget widget;
		private Element li;
		private int index;
		public ListItem(final Widget w, final Element e, final int i) {
			widget = w;
			li = e;
			index = i;
		}

		public Widget getWidget() {
			return widget;
		}
		public void setWidget(final Widget w) {
			widget = w;
		}

		public Element getLi() {
			return li;
		}
		public void setLi(final Element e) {
			li = e;
		}

		public int getIndex() {
			return index;
		}
		public void setIndex(final int i) {
			index = i;
		}

		public void incrementIndex() {
			++index;
		}
		public void decrementIndex() {
			--index;
		}
	}

}
