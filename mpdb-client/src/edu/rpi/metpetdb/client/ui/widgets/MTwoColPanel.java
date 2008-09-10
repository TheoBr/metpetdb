package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.ui.Styles;

public class MTwoColPanel extends FlowPanel {
	
	private final FlowPanel left = new FlowPanel();
	private final FlowPanel right = new FlowPanel();
	private final Element clr = DOM.createDiv();
	
	public MTwoColPanel() {
		super();
		setStylePrimaryName(Styles.TWO_COLUMN_PANEL);
		left.setStyleName(Styles.LEFT_COL);
		right.setStyleName(Styles.RIGHT_COL);
		clr.setAttribute("class", Styles.CLEARFIX);
		add(left);
		add(right);
		DOM.appendChild(this.getElement(), clr);
	}
	
	public FlowPanel getLeftCol() {
		return left;
	}
	
	public FlowPanel getRightCol() {
		return right;
	}

}
