package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.widgets.MDialogBox;

public class UnknownErrorDialog extends MDialogBox implements ClickListener {
	private final Button close;

	public UnknownErrorDialog(final Throwable err) {
		this(err, true);
	}

	public UnknownErrorDialog(final Throwable err, final boolean canClose) {
		final FlowPanel p = new FlowPanel();
		p.add(new Label(LocaleHandler.lc_text.errorTitle_UnknownError()));
		p.add(new Label(LocaleHandler.lc_text.errorDesc_UnknownError()));

		if (err instanceof InvocationException && isHTML(err.getMessage()))
			p.add(new HTML(err.getMessage()));
		else
			p.add(new Label(err.toString() + err.getMessage()));
		err.printStackTrace();

		if (canClose) {
			close = new Button(LocaleHandler.lc_text.buttonClose(), this);
			p.add(close);
		} else {
			close = null;
		}

		setWidget(p);
	}

	private static boolean isHTML(final String m) {
		return m.startsWith("<html") || m.startsWith("<HTML");
	}

	public void onClick(final Widget sender) {
		if (close == sender) {
			hide();
			//History.newItem(TokenSpace.introduction.makeToken(null));
		}
	}
}
