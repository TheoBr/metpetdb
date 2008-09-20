package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.widgets.DraggableProgressBarWidget;

public class OpacityPopup extends DialogBox implements ClickListener {

	final ServerOp continuation;
	final Button cancel;
	final Button ok;
	final ImageOnGridContainer iog;
	final DraggableProgressBarWidget dpb;

	public OpacityPopup(final ImageOnGridContainer iog, final ServerOp r,
			final int x, final int y) {
		this.iog = iog;
		continuation = r;
		dpb = new DraggableProgressBarWidget(100, 0, 100, iog.getIog()
				.getOpacity(), r);
		ok = new Button("Ok", this);
		cancel = new Button("Cancel", this);
		final VerticalPanel vp = new VerticalPanel();

		vp.add(dpb);

		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(ok);
		hp.add(cancel);
		vp.add(hp);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);

		this.setPopupPosition(x, y - this.getOffsetWidth());

		// this.setStyleName("gwt-DialogBox");
		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");

		this.setText("Click here to drag this dialog box");

		this.setStyleName("mpdb-pointPopup");
		this.addStyleName("dialogBox-caption-msg");
		this.setWidget(vp);
	}

	public void onClick(final Widget sender) {
		if (sender == ok) {
			iog.getIog().setOpacity(dpb.getProgress());
			this.hide();
			if (continuation != null)
				continuation.onSuccess(String.valueOf(dpb.getProgress()));
		} else if (sender == cancel) {
			this.hide();
			if (continuation != null)
				continuation.onSuccess(String
						.valueOf(iog.getIog().getOpacity()));
		}
	}

}
