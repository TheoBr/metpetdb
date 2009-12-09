package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MLinkandText;

public class PointPopup extends DialogBox implements ClickListener {

	private final ImageOnGridContainer imageOnGrid;
	private final ChemicalAnalysis chemicalAnalysis;
	private final ImageBrowserDetails imageBrowser;
	private final FocusPanel fp;
	private final MLink remove;
	private final MLink lock;
	private boolean locked;

	public PointPopup(final ChemicalAnalysis ma,
			final ImageOnGridContainer iog, final int x, final int y, ImageBrowserDetails ibd) {
		this.imageOnGrid = iog;
		this.chemicalAnalysis = ma;
		this.imageBrowser = ibd;

		this.locked = ma.isLocked();

		final FlowPanel panel = new FlowPanel();
		panel.add(new MLinkandText("Spot Id: ", ma.getSpotId(), "", TokenSpace
				.detailsOf(ma)));
		final MHtmlList ul = new MHtmlList();
		ul.setStyleName("options");
		this.remove = new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", this, false);
		this.lock = new MLink(this.locked ? "Unlock" : "Lock", this);
		ul.add(this.remove);
		ul.add(this.lock);
		panel.add(ul);

		this.fp = new FocusPanel();
		this.fp.addClickListener(this);
		this.fp.add(panel);

		this.setWidget(this.fp);

		this.setStyleName("mpdb-pointPopup");
		this.setPopupPosition(x + 10, y);
	}

	public void onClick(final Widget sender) {
		if (sender == this.fp)
			this.hide();
		else if (sender == this.remove) {
			this.imageOnGrid.getImagePanel().remove(
					this.chemicalAnalysis.getActualImage());
			this.chemicalAnalysis.setImage(null);
			this.chemicalAnalysis.setActualImage(null);
			this.hide();
		} else if (sender == this.lock) {
			this.lock.setText(this.locked ? "Lock" : "Unlock");
			this.locked = !this.locked;
			this.chemicalAnalysis.setLocked(this.locked);
		}
	}

}
