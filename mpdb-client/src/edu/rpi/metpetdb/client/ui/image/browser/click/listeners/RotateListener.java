package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.RotateDialog;

public class RotateListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final ImageBrowserDetails imageBrowser;

	public RotateListener(final ImageOnGridContainer imageOnGrid, final ImageBrowserDetails imageBrowser) {
		iog = imageOnGrid;
		this.imageBrowser = imageBrowser;
	}

	public void onClick(final Widget sender) {
		new ServerOp<List<ImageOnGridContainer>>() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					if (!imageBrowser.getSelectedImages().contains(iog)){
						imageBrowser.selectionHandler.selectImage(iog);
						List<ImageOnGridContainer> groupPlusSelected = imageBrowser.selectionHandler.getGroupByImage(iog);
						groupPlusSelected.addAll(imageBrowser.getSelectedImages());
						new RotateDialog(groupPlusSelected, this).show();
					} else 
						new RotateDialog(imageBrowser.getSelectedImages(), this).show();
				} else {
					onFailure(new LoginRequiredException());
				}
			}
			public void onSuccess(final List<ImageOnGridContainer> result) {
				for (ImageOnGridContainer imageOnGrid : result) {
					imageOnGrid.setCurrentWidth((int)Math
							.round((imageOnGrid.getIog().getGwidth() * (imageOnGrid.getIog().getActualCurrentResizeRatio()))));
					imageOnGrid.setCurrentHeight((int)Math
							.round((imageOnGrid.getIog().getGheight() * (imageOnGrid.getIog().getActualCurrentResizeRatio()))));
					
					imageOnGrid.getImagePanel().setHeight(imageOnGrid.getCurrentHeight() + "px");
					imageOnGrid.getImagePanel().setWidth(imageOnGrid.getCurrentWidth() + "px");
					
					imageOnGrid.getActualImage().setWidth(imageOnGrid.getCurrentWidth() + "px");
					imageOnGrid.getActualImage().setHeight(imageOnGrid.getCurrentHeight() + "px");

					imageOnGrid.getActualImage()
							.setUrl(
									((ImageOnGridContainer) imageOnGrid)
											.getGoodLookingPicture());
					imageBrowser.updatePoints(iog);
				}
			}
		}.begin();
	}

}
