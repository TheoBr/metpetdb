package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class NumericKeyboardListener implements KeyboardListener{
	private boolean integer;
	private boolean negative;
	public NumericKeyboardListener(){
		this(false,false);
	}
	
	public NumericKeyboardListener(boolean integer){
		this(integer,false);
	}
	
	public NumericKeyboardListener(boolean integer, boolean negative){
		this.integer = integer;
		this.negative = negative;
	}
	
	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
		if (modifiers == MODIFIER_CTRL || modifiers == MODIFIER_META){
			return;
		}
		if (((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
				&& (keyCode != (char) KEY_BACKSPACE)
				&& (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER) 
				&& (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
				&& (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
				&& (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN))
				|| (keyCode == '.' && integer)) {
			// TextBox.cancelKey() suppresses the current keyboard event.
			if ((keyCode == '-' && !negative) || keyCode != '-' || (((TextBox)sender).getText().contains("-") && keyCode == '-')) {
				((TextBox)sender).cancelKey();
			}
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
