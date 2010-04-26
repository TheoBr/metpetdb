package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.HashSet;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class LockListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private HashSet<MLink> notifiers = new HashSet<MLink>();
	private final ImageBrowserDetails imageBrowser;

	public LockListener(final ImageOnGridContainer imageOnGrid, final ImageBrowserDetails imageBrowser) {
		iog = imageOnGrid;
		this.imageBrowser = imageBrowser;
	}

	public void onClick(final Widget sender) {
		List<ImageOnGridContainer> imagesToLock;
		if (imageBrowser.getSelectedImages().size() == 0) 
			imagesToLock = imageBrowser.selectionHandler.getGroupByImage(iog);
		else 
			imagesToLock = imageBrowser.getSelectedImages();
		for (ImageOnGridContainer iog : imagesToLock) {
			updateLock(iog);
		}
		if (imagesToLock.get(0).isLocked())
			imageBrowser.selectionHandler.unselectAll();
		else 
			imageBrowser.selectionHandler.selectImage(imagesToLock.get(0));
			
	}
	
	public void updateLock(final ImageOnGridContainer iog) {
		String text;
		if (iog.isLocked()) {
			text = "Lock";
		} else {
			text = "Unlock";
		}
		iog.setLocked(!iog.isLocked());
		iog.getLockedLink().setText(text);
	}

	public LockListener addNotifier(final MLink link) {
		notifiers.add(link);
		return this;
	}

}
