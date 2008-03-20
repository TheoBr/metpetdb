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

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGrid;

public class AddPointDialog extends DialogBox implements ClickListener,
		KeyboardListener {

	private final Button submit;
	private final Button cancel;
	private final VerticalPanel vp;
	private final HorizontalPanel hp;
	private final ListBox lb;
	private final Set chemicalAnalyses;
	private final ServerOp continuation;

	public AddPointDialog(final SubsampleDTO s, final ImageOnGrid iog,
			final ServerOp r, final int x, final int y) {
		this.continuation = r;
		this.submit = new Button(LocaleHandler.lc_text.buttonSubmit(), this);

		this.cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		this.lb = new ListBox();
		this.lb.setVisibleItemCount(1);

		this.chemicalAnalyses = s.getChemicalAnalyses();
		final Iterator itr = this.chemicalAnalyses.iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysisDTO ma = (ChemicalAnalysisDTO) itr.next();
			if (ma.getImage() == null)
				this.lb.addItem(ma.getSpotId());
		}

		this.hp = new HorizontalPanel();
		this.hp.add(this.submit);
		this.hp.add(this.cancel);

		this.setText("Please select which spot this point corresponds to");

		this.vp = new VerticalPanel();
		this.vp.add(this.lb);
		this.vp.add(this.hp);

		final FocusPanel f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(this.vp);

		this.setPopupPosition(x, y);

		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");

		this.setWidget(f);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void onClick(final Widget sender) {
		if (this.submit == sender)
			this.doSubmit();
		if (this.cancel == sender)
			this.cancel();
	}

	public void doSubmit() {
		this.hide();
		if (this.continuation != null)
			if (this.lb.getItemCount() > 0) {
				final Iterator itr = this.chemicalAnalyses.iterator();
				while (itr.hasNext()) {
					final ChemicalAnalysisDTO ma = (ChemicalAnalysisDTO) itr
							.next();
					if (ma.getSpotId().equals(
							this.lb.getItemText(this.lb.getSelectedIndex()))) {
						this.continuation.onSuccess(ma);
						break;
					}
				}
			} else
				this.continuation.onSuccess(null);
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {

	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	private void cancel() {
		this.hide();
		if (this.continuation != null)
			this.continuation.onSuccess(null);
	}

}
