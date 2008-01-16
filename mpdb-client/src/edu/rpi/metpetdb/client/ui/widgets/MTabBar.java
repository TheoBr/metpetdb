/*
 * Copyright 2006 Google Inc.
 * 
 * modified by Zak Linder 2007
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabListenerCollection;
import com.google.gwt.user.client.ui.Widget;

public class MTabBar extends Composite implements SourcesTabEvents, ClickListener {
	
	private SimplePanel panelContainer = new SimplePanel();
	private SimplePanel panelWrapper = new SimplePanel();	
	private HorizontalPanel panel = new HorizontalPanel();
	private Widget selectedTab;
	private TabListenerCollection tabListeners;
	private static final String STYLENAME_DEFAULT = "tabBar";

  /**
   * <code>ClickDecoratorPanel</code> decorates any widget with the minimal
   * amount of machinery to receive clicks for delegation to the parent.
   * {@link SourcesClickEvents} is not implemented due to the fact that only a
   * single observer is needed.
   */
  private static final class ClickDecoratorPanel extends SimplePanel {
    ClickListener delegate;

    ClickDecoratorPanel(Widget child, ClickListener delegate) {
      this.delegate = delegate;
      setWidget(child);
      sinkEvents(Event.ONCLICK);
    }

    public void onBrowserEvent(Event event) {
      // No need for call to super.
      switch (DOM.eventGetType(event)) {
        case Event.ONCLICK:
          delegate.onClick(this);
      }
    }
  }
  
  private static final class TabBarItem extends SimplePanel {
	  
	  TabBarItem() {
		  this("&nbsp;");
	  }
	  
	  TabBarItem(String text) {
		  this(new HTML(text));
	  }
	  
	  TabBarItem(Widget w) {
		  w.addStyleName(STYLENAME_DEFAULT + "Item-content");
		  setStyleName(STYLENAME_DEFAULT + "Item-end");
		  add(w);
	  }
	  
  }

  /**
   * Creates an empty tab bar.
   * 
   * @modified by zak: removed 'tabBarRest', added a few containing SimplePanels
   */
  public MTabBar() {
    initWidget(panelContainer);
    sinkEvents(Event.ONCLICK);
    setStyleName(STYLENAME_DEFAULT);
    panelWrapper.setStyleName(STYLENAME_DEFAULT + "-end");

    panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

    HTML first = new HTML("&nbsp;", true);
    first.setStyleName(STYLENAME_DEFAULT + "First");
    panel.add(first);
    panelWrapper.add(panel);
    panelContainer.add(panelWrapper);
  }

  /**
   * Adds a new tab with the specified text.
   * 
   * @param text the new tab's text
   */
  public void addTab(String text) {
    insertTab(new TabBarItem(text), getTabCount());
  }

  /**
   * Adds a new tab with the specified widget.
   * 
   * @param widget the new tab's widget.
   */
  public void addTab(Widget widget) {
    insertTab(new TabBarItem(widget), getTabCount());
  }

  public void addTabListener(TabListener listener) {
    if (tabListeners == null) {
      tabListeners = new TabListenerCollection();
    }
    tabListeners.add(listener);
  }

  /**
   * Gets the tab that is currently selected.
   * 
   * @return the selected tab
   */
  public int getSelectedTab() {
    if (selectedTab == null) {
      return -1;
    }
    return panel.getWidgetIndex(selectedTab) - 1;
  }

  /**
   * Gets the number of tabs present.
   * 
   * @return the tab count
   */
  public int getTabCount() {
    return panel.getWidgetCount() - 1;
  }

  /**
   * Gets the specified tab's HTML.
   * 
   * @param index the index of the tab whose HTML is to be retrieved
   * @return the tab's HTML
   */
  public String getTabHTML(int index) {
    if (index >= getTabCount()) {
      return null;
    }
    Widget widget = panel.getWidget(index + 1);
    if (widget instanceof HTML) {
      return ((HTML) widget).getHTML();
    } else if (widget instanceof Label) {
      return ((Label) widget).getText();
    } else {
      // This will be a ClickDecorator holding a user-supplied widget.
      return DOM.getInnerHTML(widget.getElement());
    }
  }

  /**
   * Inserts a new tab at the specified index.
   * 
   * @param widget widget to be used in the new tab.
   * @param beforeIndex the index before which this tab will be inserted.
   */
  public void insertTab(Widget widget, int beforeIndex) {
    checkInsertBeforeTabIndex(beforeIndex);

    ClickDecoratorPanel decWidget = new ClickDecoratorPanel(new TabBarItem(widget), this);
    decWidget.addStyleName(STYLENAME_DEFAULT + "Item");
    panel.insert(decWidget, beforeIndex + 1);
  }

  public void onClick(Widget sender) {
    for (int i = 1; i < panel.getWidgetCount(); ++i) {
      if (panel.getWidget(i) == sender) {
        selectTab(i - 1);
        return;
      }
    }
  }

  /**
   * Removes the tab at the specified index.
   * 
   * @param index the index of the tab to be removed
   */
  public void removeTab(int index) {
    checkTabIndex(index);

    // (index + 1) to account for 'first' placeholder widget.
    Widget toRemove = panel.getWidget(index + 1);
    if (toRemove == selectedTab) {
      selectedTab = null;
    }
    panel.remove(toRemove);
  }

  public void removeTabListener(TabListener listener) {
    if (tabListeners != null) {
      tabListeners.remove(listener);
    }
  }

  /**
   * Programmatically selects the specified tab. Use index -1 to specify that no
   * tab should be selected.
   * 
   * @param index the index of the tab to be selected.
   * @return <code>true</code> if successful, <code>false</code> if the
   *         change is denied by the {@link TabListener}.
   */
  public boolean selectTab(int index) {
    checkTabIndex(index);

    if (tabListeners != null) {
      if (!tabListeners.fireBeforeTabSelected(this, index)) {
        return false;
      }
    }

    // Check for -1.
    setSelectionStyle(selectedTab, false);
    if (index == -1) {
      selectedTab = null;
      return true;
    }

    selectedTab = panel.getWidget(index + 1);
    setSelectionStyle(selectedTab, true);

    if (tabListeners != null) {
      tabListeners.fireTabSelected(this, index);
    }
    return true;
  }

  private void checkInsertBeforeTabIndex(int beforeIndex) {
    if ((beforeIndex < 0) || (beforeIndex > getTabCount())) {
      throw new IndexOutOfBoundsException();
    }
  }

  private void checkTabIndex(int index) {
    if ((index < -1) || (index >= getTabCount())) {
      throw new IndexOutOfBoundsException();
    }
  }

  private void setSelectionStyle(Widget item, boolean selected) {
    if (item != null) {
      if (selected) {
        item.addStyleName(STYLENAME_DEFAULT + "Item-selected");
      } else {
        item.removeStyleName(STYLENAME_DEFAULT + "Item-selected");
      }
    }
  }


}
