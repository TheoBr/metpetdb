package edu.rpi.metpetdb.client.ui;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.ui.input.FocusSupport;

public abstract class FormOp<T> extends ServerOp<T> {
	private final Widget root;

	protected FormOp(final Widget p) {
		root = p;
	}

	public final void begin() {
		if (FocusSupport.validateEdit(root)) {
			enable(false);
			onSubmit();
		}
	}

	public void cancel() {
		enable(true);
	}

	public void onFailure(final Throwable e) {
		if (e instanceof ValidationException && show((ValidationException) e)) {
			enable(true);
			FocusSupport.showValidationException(root, (ValidationException) e);
		} else
			super.onFailure(e);
	}

	private boolean show(final ValidationException e) {
		return FocusSupport.handlesValidationException(root, e);
	}

	protected void enable(final boolean enabled) {
		FocusSupport.setEnabled(root, enabled);
	}

	protected abstract void onSubmit();
}
