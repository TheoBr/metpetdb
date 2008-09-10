package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.rpi.metpetdb.client.ui.Styles;

public class MTwoColPanel extends FlowPanel {
	
	private final SimplePanel left = new SimplePanel();
	private final SimplePanel right = new SimplePanel();
	private final FlowPanel leftContent = new FlowPanel();
	private final FlowPanel rightContent = new FlowPanel();
	private final Element clr = DOM.createDiv();
	
	public MTwoColPanel() {
		super();
		setStylePrimaryName(Styles.TWO_COLUMN_PANEL);
		left.setStyleName(Styles.LEFT_COL);
		right.setStyleName(Styles.RIGHT_COL);
		leftContent.setStyleName(Styles.CONTENT);
		rightContent.setStyleName(Styles.CONTENT);
		clr.setAttribute("class", Styles.CLEARFIX);
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

}
