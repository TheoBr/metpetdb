/*
 * Copyright 2007 Google Inc.
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
package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A form of popup that has a caption area at the top and can be dragged by the
 * user.
 * <p>
 * <img class='gallery' src='DialogBox.png'/>
 * </p>
 * <h3>CSS Style Rules</h3> <ul class='css'> <li>.gwt-DialogBox { the outside of
 * the dialog }</li> <li>.gwt-DialogBox .Caption { the caption }</li> </ul>
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.DialogBoxExample}
 * </p>
 */
public class DialogBox extends PopupPanel implements HasHTML, MouseListener {

	private HTML caption = new HTML();
	private Widget child;
	private boolean dragging;
	private int dragStartX, dragStartY;
	private FlexTable panel = new FlexTable();
	private String captionText;
	private String captionHTML;
	private final String STYLENAME_DEFAULT = "dialogBox";

	/**
	 * Creates an empty dialog box. It should not be shown until its child
	 * widget has been added using {@link #add(Widget)}.
	 */
	public DialogBox() {
		this(false);
	}

	/**
	 * Creates an empty dialog box specifying its "auto-hide" property. It
	 * should not be shown until its child widget has been added using {@link
	 * #add(Widget)}.
	 * 
	 * @param autoHide
	 * 		<code>true</code> if the dialog should be automatically hidden when the
	 * 		user clicks outside of it
	 */
	public DialogBox(boolean autoHide) {
		this(autoHide, true);
	}

	/**
	 * Creates an empty dialog box specifying its "auto-hide" property. It
	 * should not be shown until its child widget has been added using {@link
	 * #add(Widget)}.
	 * 
	 * @param autoHide
	 * 		<code>true</code> if the dialog should be automatically hidden when the
	 * 		user clicks outside of it
	 * @param modal
	 * 		<code>true</code> if keyboard and mouse events for widgets not contained
	 * 		by the dialog should be ignored
	 */
	public DialogBox(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		panel.setWidget(0, 0, caption);
		panel.setStyleName(STYLENAME_DEFAULT);
		panel.setCellPadding(0);
		panel.setCellSpacing(0);
		panel.getCellFormatter().setStyleName(1, 0,
				STYLENAME_DEFAULT + "-content");
		panel.getCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		super.setWidget(panel);

		setStyleName(STYLENAME_DEFAULT + "-container");
		caption.setStyleName(STYLENAME_DEFAULT + "-caption");
		caption.addMouseListener(this);
	}

	public String getHTML() {
		return captionHTML;
	}

	public String getText() {
		return captionText;
	}

	public boolean onEventPreview(Event event) {
		// We need to preventDefault() on mouseDown events (outside of the
		// DialogBox content) to keep text from being selected when it
		// is dragged.
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			if (DOM.isOrHasChild(caption.getElement(), DOM
					.eventGetTarget(event))) {
				DOM.eventPreventDefault(event);
			}
		}
		return super.onEventPreview(event);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		dragging = true;
		DOM.setCapture(caption.getElement());
		dragStartX = x;
		dragStartY = y;
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (dragging) {
			int absX = x + getAbsoluteLeft();
			int absY = y + getAbsoluteTop();
			setPopupPosition(absX - dragStartX, absY - dragStartY);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		dragging = false;
		DOM.releaseCapture(caption.getElement());
	}

	public boolean remove(Widget w) {
		if (child != w) {
			return false;
		}
		panel.remove(w);
		return true;
	}

	public void setHTML(String html) {
		captionHTML = html;
		caption.setHTML("<div class=\"" + STYLENAME_DEFAULT + "-caption-end\">"
				+ "<div class=\"" + STYLENAME_DEFAULT + "-caption-msg\">"
				+ html + "</div></div>");
	}

	public void setText(String text) {
		captionText = text;
		caption.setHTML("<div class=\"" + STYLENAME_DEFAULT + "-caption-end\">"
				+ "<div class=\"" + STYLENAME_DEFAULT + "-caption-msg\">"
				+ text + "</div></div>");
	}

	public void setWidget(Widget w) {
		// If there is already a widget, remove it.
		if (child != null) {
			panel.remove(child);
		}
		// Add the widget to the center of the cell.
		if (w != null) {
			panel.setWidget(1, 0, w);
		}
		w.setStyleName(STYLENAME_DEFAULT + "-wrapper");
		child = w;
	}

	/**
	 * Override, so that interior panel reflows to match parent's new width.
	 * 
	 * @Override
	 */
	public void setWidth(String width) {
		super.setWidth(width);

		// note that you CANNOT call panel.setWidth("100%") until parent's width
		// has been explicitly set, b/c until then parent's width is
		// unconstrained
		// and setting panel's width to 100% will flow parent to 100% of browser
		// (i.e. can't do this in constructor)
		panel.setWidth("100%");
	}

}
