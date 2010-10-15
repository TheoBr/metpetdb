	package edu.rpi.metpetdb.client.ui.input;

	import com.google.gwt.user.client.ui.FlowPanel;
	import com.google.gwt.user.client.ui.HasText;

	import edu.rpi.metpetdb.client.ui.CSS;
	import edu.rpi.metpetdb.client.ui.widgets.MText;

	public class CurrentMessage extends FlowPanel implements HasText {

		private final MText text = new MText("","span");

		CurrentMessage() {
			setStyleName(CSS.INVALID_MESSAGE);
			setVisible(false);

			add(text);
			text.setStyleName(CSS.ICON_WARNING);
		}

		public String getText() {
			return text.getText();
		}

		public void setText(final String msg) {
			if (msg == null || msg.length() == 0) {
				setVisible(false);
			} else {
				text.setText(msg);
				setVisible(true);
			}
		}
	}