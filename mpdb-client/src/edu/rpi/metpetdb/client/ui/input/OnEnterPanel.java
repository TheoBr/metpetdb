package edu.rpi.metpetdb.client.ui.input;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.widgets.MSimplePanel;

public abstract class OnEnterPanel extends MSimplePanel implements
		KeyboardListener {
	public OnEnterPanel(final Widget child) {
		addKeyboardListener(this);
		setWidget(child);
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ENTER)
			onEnter();
		else if (kc == KEY_ESCAPE)
			onEscape();
	}
	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}
	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	public abstract void onEnter();

	public void onEscape() {
	}

	public static class ObjectEditor extends OnEnterPanel {
		public ObjectEditor(final ObjectEditorPanel p) {
			super(p);
		}
		public void onEnter() {
			final ObjectEditorPanel p = ((ObjectEditorPanel) getWidget());
			if (p.save.isVisible() && p.save.isEnabled())
				p.doSave();
		}
		public void onEscape() {
			final ObjectEditorPanel p = ((ObjectEditorPanel) getWidget());
			if (p.cancel.isVisible() && p.cancel.isEnabled())
				p.doCancel();
		}
	}
}
