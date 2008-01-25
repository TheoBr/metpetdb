package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class NoObjectDialog extends MDialogBox implements ClickListener {
	private final Button close;

	public NoObjectDialog(final NoSuchObjectException nsoe) {
		close = new Button(LocaleHandler.lc_text.buttonClose(), this);
		final FlowPanel p = new FlowPanel();
		p.add(new Label(LocaleHandler.lc_text.errorTitle_NoObject()));
		p.add(new Label(LocaleHandler.lc_text.errorDesc_NoObject(nsoe.getType(), nsoe
				.getId())));
		p.add(close);
		setWidget(p);
	}

	public void show() {
		super.show();
		//History.newItem(TokenSpace.introduction.makeToken(null));
	}

	public void onClick(final Widget sender) {
		if (close == sender)
			hide();
	}
}
