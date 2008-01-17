package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.MineralAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.LocaleHandler;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class AddPointDialog extends DialogBox
		implements
			ClickListener,
			KeyboardListener {

	private final Button submit;
	private final Button cancel;
	private final VerticalPanel vp;
	private final HorizontalPanel hp;
	private final ListBox lb;
	private Set mineralAnalyses;
	private final ServerOp continuation;

	public AddPointDialog(final Subsample s, final ImageOnGrid iog,
			final ServerOp r, final int x, final int y) {
		continuation = r;
		submit = new Button(LocaleHandler.lc_text.buttonSubmit(), this);

		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		lb = new ListBox();
		lb.setVisibleItemCount(1);

		mineralAnalyses = s.getMineralAnalyses();
		final Iterator itr = mineralAnalyses.iterator();
		while (itr.hasNext()) {
			final MineralAnalysis ma = (MineralAnalysis) itr.next();
			if (ma.getImage() == null)
				lb.addItem(ma.getSpotId());
		}

		hp = new HorizontalPanel();
		hp.add(submit);
		hp.add(cancel);

		this.setText("Please select which spot this point corresponds to");

		vp = new VerticalPanel();
		vp.add(lb);
		vp.add(hp);

		final FocusPanel f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(vp);

		this.setPopupPosition(x, y);

		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");

		setWidget(f);
	}

	protected void onLoad() {
		super.onLoad();
	}

	public void onClick(final Widget sender) {
		if (submit == sender)
			doSubmit();
		if (cancel == sender)
			cancel();
	}

	public void doSubmit() {
		this.hide();
		if (continuation != null) {
			if (lb.getItemCount() > 0) {
				final Iterator itr = mineralAnalyses.iterator();
				while (itr.hasNext()) {
					final MineralAnalysis ma = (MineralAnalysis) itr.next();
					if (ma.getSpotId().equals(
							lb.getItemText(lb.getSelectedIndex()))) {
						continuation.onSuccess(ma);
						break;
					}
				}
			} else {
				continuation.onSuccess(null);
			}
		}
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {

	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	private void cancel() {
		hide();
		if (continuation != null) {
			continuation.onSuccess(null);
		}
	}

}
