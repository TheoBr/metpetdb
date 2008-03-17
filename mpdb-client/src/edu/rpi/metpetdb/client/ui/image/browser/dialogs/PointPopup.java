package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class PointPopup extends DialogBox implements ClickListener {

	private final ImageOnGrid imageOnGrid;
	private final MineralAnalysisDTO mineralAnalysis;
	private final FocusPanel fp;
	private final MLink remove;
	private final MLink lock;
	private boolean locked;

	public PointPopup(final MineralAnalysisDTO ma, final ImageOnGrid iog,
			final int x, final int y) {
		imageOnGrid = iog;
		mineralAnalysis = ma;

		locked = ma.getIsLocked();

		final FlowPanel panel = new FlowPanel();
		panel.add(new Label("Spot Id: " + ma.getSpotId()));
		final MUnorderedList ul = new MUnorderedList();
		ul.setStyleName("options");
		remove = new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", this, false);
		lock = new MLink(locked ? "Unlock" : "Lock", this);
		ul.add(remove);
		ul.add(lock);
		panel.add(ul);

		fp = new FocusPanel();
		fp.addClickListener(this);
		fp.add(panel);

		this.setWidget(fp);

		this.setStyleName("mpdb-pointPopup");
		this.setPopupPosition(x + 10, y);

	}

	public void onClick(final Widget sender) {
		if (sender == fp) {
			this.hide();
		} else if (sender == remove) {
			imageOnGrid.getImagePanel()
					.remove(mineralAnalysis.getActualImage());
			mineralAnalysis.setImage(null);
			mineralAnalysis.setActualImage(null);
			this.hide();
		} else if (sender == lock) {
			lock.setText(locked ? "Lock" : "Unlock");
			locked = !locked;
			mineralAnalysis.setIsLocked(locked);
		}
	}

}
