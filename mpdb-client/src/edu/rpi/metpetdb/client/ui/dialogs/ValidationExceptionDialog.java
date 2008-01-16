package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.ui.LocaleHandler;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MDialogBox;

public class ValidationExceptionDialog extends MDialogBox implements ClickListener {
	private final Button close;
	private final ServerOp continuation;

	public ValidationExceptionDialog(final ValidationException err, final ServerOp r) {
		close = new Button(LocaleHandler.lc_text.buttonClose(), this);
		continuation = r;

		final FlowPanel p = new FlowPanel();
		p.add(new Label(LocaleHandler.lc_text.errorTitle_InvalidData()));
		p.add(new Label(err.format()));
		p.add(close);
		setWidget(p);
	}

	public void onClick(final Widget sender) {
		if (close == sender)
			hide();
		if (continuation != null)
			continuation.cancel();
	}
}
