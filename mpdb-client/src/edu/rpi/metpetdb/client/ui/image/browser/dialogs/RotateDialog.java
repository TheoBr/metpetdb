package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;

public class RotateDialog extends MDialogBox implements ClickListener {

	private final Button rotateCW90;
	private final Button rotateCCW90;
	private final Button rotate180;
	private final Button ok;
	private final Button cancel;
	private final ServerOp<List<ImageOnGridContainer>> continuation;
	private final List<ImageOnGridContainer> imagesOnGrid;
	private final List<Image> images;
	private final Label loading;
	private final TextBox angle;
	private final Button update;

	public RotateDialog(final List<ImageOnGridContainer> iogs, final ServerOp<List<ImageOnGridContainer>> r) {
		images = new ArrayList<Image>();
		final VerticalPanel vp = new VerticalPanel();
		final HorizontalPanel hp = new HorizontalPanel();
		for (ImageOnGridContainer iog : iogs) {
			Image i = new Image(iog.getGoodLookingPicture(true));
			images.add(i);
			i.setWidth("256px");
			hp.add(i);
		}
		vp.add(hp);

		final HorizontalPanel hpControls = new HorizontalPanel();
		rotateCW90 = new Button("CW 90", this);
		rotateCCW90 = new Button("CCW 90", this);
		rotate180 = new Button("180", this);
		hpControls.add(rotateCCW90);
		hpControls.add(rotate180);
		hpControls.add(rotateCW90);
		hpControls.add(new Label("Custom:"));
		angle = new TextBox();
		angle.setWidth("40px");
		angle.addKeyboardListener(new NumericKeyboardListener(false,true));
		update = new Button("Rotate", this);
		hpControls.add(angle);
		hpControls.add(update);

		loading = new Label();



		final HorizontalPanel controls = new HorizontalPanel();
		ok = new Button("Finish", this);
		cancel = new Button("Cancel", this);
		controls.add(ok);
		controls.add(cancel);


		vp.add(hpControls);
		vp.add(controls);
		vp.add(loading);

		continuation = r;
		imagesOnGrid = iogs;

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
			if (!angle.getText().equals(""))
				rotate(Integer.parseInt(angle.getText()));
			else
				loading.setText("Please input an amount of degrees to rotate by");
		} else if (sender == ok) {
			continuation.onSuccess(imagesOnGrid);
			this.hide();
		} else if (sender == cancel) {
			this.hide();
		}
	}

	public void rotate(final int degrees) {
		final Timer t = new Timer(){
			@Override
			public void run() {
				int count=0;
				if (loading.getText().endsWith("...")) count = 3;
				else if (loading.getText().endsWith("..")) count = 2;
				else if (loading.getText().endsWith(".")) count = 1;
				count = (++count)%4;
				String myText = "Please Wait";
				int i = 0;
				while (i < count){
					myText += ".";
					i++;
				}
				loading.setText(myText);
			}		
		};
		
		new ServerOp<List<ImageOnGrid>>() {
			public void begin() {
				List<ImageOnGrid> imagesOnGrid2 = new ArrayList<ImageOnGrid>();
				for (ImageOnGridContainer iog : imagesOnGrid) {
					imagesOnGrid2.add(iog.getImageOnGrid());
				}
				MpDb.image_svc.rotate(imagesOnGrid2, degrees, this);
				t.scheduleRepeating(500);
				t.run();
			}
			public void onSuccess(final List<ImageOnGrid> result) {
				for (int i = 0; i < result.size(); i++) {
					final ImageOnGrid iog = (ImageOnGrid) result.get(i);
					imagesOnGrid.get(i).getIog().setGchecksum(iog.getGchecksum());
					imagesOnGrid.get(i).getIog().setGchecksum64x64(iog.getGchecksum64x64());
					imagesOnGrid.get(i).getIog().setGchecksumHalf(iog.getGchecksumHalf());
					imagesOnGrid.get(i).getIog().setGheight(iog.getGheight());
					imagesOnGrid.get(i).getIog().setGwidth(iog.getGwidth());
					imagesOnGrid.get(i).getIog().setAngle(iog.getAngle());
					
					
					images.get(i).setUrl(imagesOnGrid.get(i).getGoodLookingPicture());
					images.get(i).setWidth("256px");
					loading.setText("Done");
				}
				t.cancel();
			}
		}.begin();
	}

}
