package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

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
				result.setCurrentWidth((int)Math
						.round((result.getIog().getGwidth() * (result.getIog().getActualCurrentResizeRatio()))));
				result.setCurrentHeight((int)Math
						.round((result.getIog().getGheight() * (result.getIog().getActualCurrentResizeRatio()))));
				
				result.getImagePanel().setHeight(result.getCurrentHeight() + "px");
				result.getImagePanel().setWidth(result.getCurrentWidth() + "px");
				
				result.getActualImage().setWidth(result.getCurrentWidth() + "px");
				result.getActualImage().setHeight(result.getCurrentHeight() + "px");
				
				//final double heightRatio = iog.getCurrentHeight()
				//		/  iog.getIog().getImage().getHeight();
				iog.getIog().setImage(
						(((ImageOnGridContainer) result).getIog().getImage()));
				iog.getActualImage()
						.setUrl(
								((ImageOnGridContainer) result)
										.getGoodLookingPicture());
				imageBrowser.updatePoints(iog);
				
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
