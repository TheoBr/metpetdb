package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.rpi.metpetdb.client.ui.CSS;

public class MTwoColPanel extends FlowPanel {
	
	private final SimplePanel left = new SimplePanel();
	private final SimplePanel right = new SimplePanel();
	private final FlowPanel leftContent = new FlowPanel();
	private final FlowPanel rightContent = new FlowPanel();
	private final Element clr = DOM.createDiv();
	
	public MTwoColPanel() {
		super();
		setStylePrimaryName(CSS.TWO_COLUMN_PANEL);
		left.setStyleName(CSS.LEFT_COL);
		right.setStyleName(CSS.RIGHT_COL);
		leftContent.setStyleName(CSS.CONTENT);
		rightContent.setStyleName(CSS.CONTENT);
		clr.setAttribute("class", CSS.CLEARFIX);
		left.add(leftContent);
		right.add(rightContent);
		add(left);
		add(right);
		DOM.appendChild(this.getElement(), clr);
	}
	
	public FlowPanel getLeftCol() {
		return leftContent;
	}
	
	public FlowPanel getRightCol() {
		return rightContent;
	}
	
	public void setLeftColWidth(String w) {
		left.setWidth(w);
	}
	
	public void setRightColWidth(String w) {
		right.setWidth(w);
	}

}
