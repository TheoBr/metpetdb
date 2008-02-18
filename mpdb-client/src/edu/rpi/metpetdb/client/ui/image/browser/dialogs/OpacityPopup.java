package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.widgets.DraggableProgressBarWidget;

public class OpacityPopup extends DialogBox implements ClickListener {

	final ServerOp continuation;
	final Button cancel;
	final Button ok;
	final ImageOnGridDTO iog;
	final DraggableProgressBarWidget dpb;

	public OpacityPopup(final ImageOnGridDTO iog, final ServerOp r, final int x,
			final int y) {
		this.iog = iog;
		continuation = r;
		dpb = new DraggableProgressBarWidget(100, 0, 100, iog.getOpacity(), r);
		ok = new Button("Ok", this);
		cancel = new Button("Cancel", this);
		final VerticalPanel vp = new VerticalPanel();

		vp.add(dpb);
		vp.add(ok);
		vp.add(cancel);

		this.setPopupPosition(x, y - this.getOffsetWidth());

		this.setStyleName("gwt-DialogBox");
		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");

		this.setText("Click here to drag this dialog box");

		this.setWidget(vp);
	}

	public void onClick(final Widget sender) {
		if (sender == ok) {
			iog.setOpacity(dpb.getProgress());
			((DialogBox) sender.getParent().getParent().getParent()).hide();
			if (continuation != null)
				continuation.onSuccess(String.valueOf(dpb.getProgress()));
		} else if (sender == cancel) {
			((DialogBox) sender.getParent().getParent().getParent()).hide();
			if (continuation != null)
				continuation.onSuccess(String.valueOf(iog.getOpacity()));
		}
	}

}
