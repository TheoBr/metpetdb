package edu.rpi.metpetdb.client.ui.image.browser.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel;

public class ResizableWidget extends Composite {

	public ResizableWidget(final Widget w, final MAbsolutePanel ma,
			final FlowPanel imageContainer, final ImageOnGrid iog) {
		final Grid g = new Grid(3, 3);

		final Label topLeft = new Label();
		topLeft.setStyleName("topLeftResizer");
		final Label topRight = new Label();
		topRight.setStyleName("topRightResizer");
		final Label bottomLeft = new Label();
		bottomLeft.setStyleName("bottomLeftResizer");
		final Label bottomRight = new Label();
		bottomRight.setStyleName("bottomRightResizer");

		final SimplePanel sp = new SimplePanel();
		sp.setStyleName("resizerSize");

		topLeft.addStyleName("resizerSize");
		topRight.addStyleName("resizerSize");
		bottomLeft.addStyleName("resizerSize");
		bottomRight.addStyleName("resizerSize");

		g.setWidget(0, 0, topLeft);
		g.setWidget(0, 1, sp);
		g.setWidget(0, 2, topRight);
		g.setWidget(1, 0, sp);
		g.setWidget(1, 1, w);
		g.setWidget(1, 2, sp);
		g.setWidget(2, 0, bottomLeft);
		g.setWidget(2, 1, sp);
		g.setWidget(2, 2, bottomRight);

		g.setCellPadding(0);
		g.setCellSpacing(0);
		g.setBorderWidth(0);

		for (int i = 0; i < g.getRowCount(); ++i) {
			for (int j = 0; j < g.getColumnCount(); ++j) {
				g.getCellFormatter().setStyleName(i, j, "resizeable");
			}
		}

		g.setStyleName("resizeable2");

		this.initWidget(g);
	}

}
