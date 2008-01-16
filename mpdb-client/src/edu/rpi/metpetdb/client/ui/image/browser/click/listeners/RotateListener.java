package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.RotateDialog;

public class RotateListener implements ClickListener {

	private final ImageOnGrid iog;

	public RotateListener(final ImageOnGrid imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		new ServerOp() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					new RotateDialog(iog, this).show();
				} else {
					onFailure(new LoginRequiredException());
				}
			}
			public void onSuccess(final Object result) {
				// final float widthRatio = iog.getWidth()
				// / (float) iog.getImage().getWidth();
				final float heightRatio = iog.getHeight()
						/ (float) iog.getImage().getHeight();
				iog.setImage((((ImageOnGrid) result).getImage()));
				iog.getActualImage().setUrl(
						((ImageOnGrid) result).getGoodLookingPicture());
				iog.resizeImage(Math.round(iog.getImage().getWidth()
						* heightRatio), Math.round(iog.getImage().getHeight()
						* heightRatio), false);

			}
		}.begin();
	}

}
