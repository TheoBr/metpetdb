package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.RotateDialog;

public class RotateListener implements ClickListener {

	private final ImageOnGridContainer iog;

	public RotateListener(final ImageOnGridContainer imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		new ServerOp<ImageOnGridContainer>() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					new RotateDialog(iog, this).show();
				} else {
					onFailure(new LoginRequiredException());
				}
			}
			public void onSuccess(final ImageOnGridContainer result) {
				// final float widthRatio = iog.getWidth()
				// / (float) iog.getImage().getWidth();
				final float heightRatio = iog.getCurrentHeight()
						/ (float) iog.getIog().getImage().getHeight();
				iog.getIog().setImage(
						(((ImageOnGridContainer) result).getIog().getImage()));
				iog.getActualImage()
						.setUrl(
								((ImageOnGridContainer) result)
										.getGoodLookingPicture());
				iog.resizeImage(Math.round(iog.getIog().getImage().getWidth()
						* heightRatio), Math.round(iog.getIog().getImage()
						.getHeight()
						* heightRatio), false);

			}
		}.begin();
	}

}
