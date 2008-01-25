package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class RotateDialog extends MDialogBox implements ClickListener {

	private final Button rotateCW90;
	private final Button rotateCCW90;
	private final Button rotate180;
	private final Button ok;
	private final Button cancel;
	private final ServerOp continuation;
	private final ImageOnGrid imageOnGrid;
	private final Image image;
	private final Label loading;
	private final TextBox angle;
	private final Button update;

	public RotateDialog(final ImageOnGrid iog, final ServerOp r) {
		final VerticalPanel vp = new VerticalPanel();
		final FocusPanel fp = new FocusPanel();
		image = new Image(iog.getGoodLookingPicture(true));
		image.setWidth("256px");
		fp.add(image);
		vp.add(fp);

		final HorizontalPanel hpControls = new HorizontalPanel();
		ok = new Button("Ok", this);
		cancel = new Button("Cancel", this);
		rotateCW90 = new Button("CW 90", this);
		rotateCCW90 = new Button("CCW 90", this);
		rotate180 = new Button("180", this);
		hpControls.add(cancel);
		hpControls.add(rotateCCW90);
		hpControls.add(rotate180);
		hpControls.add(rotateCW90);
		hpControls.add(ok);

		loading = new Label();

		angle = new TextBox();
		update = new Button("Update", this);

		final HorizontalPanel hpAngle = new HorizontalPanel();
		hpAngle.add(new Label("Enter the number of degrees:"));
		hpAngle.add(angle);
		hpAngle.add(update);

		vp.add(hpControls);
		vp.add(hpAngle);
		vp.add(loading);

		continuation = r;
		imageOnGrid = iog;

		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");

		this.setWidget(vp);
	}

	public void onClick(final Widget sender) {
		if (sender == rotateCW90) {
			rotate(90);
		} else if (sender == rotateCCW90) {
			rotate(-90);
		} else if (sender == rotate180) {
			rotate(180);
		} else if (sender == update) {
			rotate(Integer.parseInt(angle.getText()));
		} else if (sender == ok) {
			continuation.onSuccess(imageOnGrid);
			this.hide();
		} else if (sender == cancel) {
			this.hide();
		}
	}

	public void rotate(final int degrees) {
		new ServerOp() {
			public void begin() {
				MpDb.image_svc.rotate(imageOnGrid, degrees, this);
				loading.setText("Please Wait");
			}
			public void onSuccess(final Object result) {
				final ImageOnGrid iog = (ImageOnGrid) result;
				imageOnGrid.setGchecksum(iog.getGchecksum());
				imageOnGrid.setGchecksum64x64(iog.getGchecksum64x64());
				imageOnGrid.setGchecksumHalf(iog.getGchecksumHalf());
				imageOnGrid.setGheight(iog.getGheight());
				imageOnGrid.setGwidth(iog.getGwidth());
				image.setUrl(imageOnGrid.getGoodLookingPicture());
				loading.setText("Done");
			}
		}.begin();
	}

}
