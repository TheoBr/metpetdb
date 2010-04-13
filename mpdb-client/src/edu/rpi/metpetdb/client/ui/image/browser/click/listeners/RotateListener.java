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
						imageBrowser.getSelectedImages().add(iog);
						iog.getImageContainer().addStyleDependentName("selected");
					}
					new RotateDialog(imageBrowser.getSelectedImages(), this).show();
				} else {
					onFailure(new LoginRequiredException());
				}
			}
			public void onSuccess(final List<ImageOnGridContainer> result) {
				// final float widthRatio = iog.getWidth()
				// / (float) iog.getImage().getWidth();
				for (ImageOnGridContainer imageOnGrid : result) {
					imageOnGrid.setCurrentWidth((int)Math
							.round((imageOnGrid.getIog().getGwidth() * (imageOnGrid.getIog().getActualCurrentResizeRatio()))));
					imageOnGrid.setCurrentHeight((int)Math
							.round((imageOnGrid.getIog().getGheight() * (imageOnGrid.getIog().getActualCurrentResizeRatio()))));
					
					imageOnGrid.getImagePanel().setHeight(imageOnGrid.getCurrentHeight() + "px");
					imageOnGrid.getImagePanel().setWidth(imageOnGrid.getCurrentWidth() + "px");
					
					imageOnGrid.getActualImage().setWidth(imageOnGrid.getCurrentWidth() + "px");
					imageOnGrid.getActualImage().setHeight(imageOnGrid.getCurrentHeight() + "px");
					
					//final double heightRatio = iog.getCurrentHeight()
					//		/  iog.getIog().getImage().getHeight();
					iog.getIog().setImage(
							(((ImageOnGridContainer) imageOnGrid).getIog().getImage()));
					iog.getActualImage()
							.setUrl(
									((ImageOnGridContainer) imageOnGrid)
											.getGoodLookingPicture());
					imageBrowser.updatePoints(iog);
				}
				
				//TODO Update the chemical analysis points to correctly rotate with the image
				
				/*final Iterator<ChemicalAnalysis> itr = iog.getChemicalAnalyses().iterator();
				while (itr.hasNext()) {
					final ChemicalAnalysis ma = itr.next();
					final com.google.gwt.user.client.ui.Image i = (com.google.gwt.user.client.ui.Image) ma.getActualImage();				
					
					int pointX = (int)Math.round(ma.getReferenceX()/this.scale*this.pps) - this.chemImageWidth;
					int pointY = (int)Math.round(ma.getReferenceY()/this.scale*this.pps) - this.chemImageHeight;
					this.grid.add(i,(int)iog.getCurrentContainerPosition().x + pointX, (int)iog.getCurrentContainerPosition().y + pointY);
					
				}*/
				//iog.resizeImage(Math.round(iog.getIog().getImage().getWidth()
				//		* heightRatio), Math.round(iog.getIog().getImage()
				//		.getHeight()
				//		* heightRatio), false);

			}
		}.begin();
	}

}
