package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumericKeyboardListener implements KeyboardListener{
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
				&& (keyCode != (char) KEY_BACKSPACE)
				&& (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER) 
				&& (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
				&& (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
				&& (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN)) {
			// TextBox.cancelKey() suppresses the current keyboard event.
			((TextBox)sender).cancelKey();
		}
		if (((TextBox)sender).getText().contains(".") && keyCode == '.'){
			((TextBox)sender).cancelKey();
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub

	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		// TODO Auto-generated method stub

	}
}
