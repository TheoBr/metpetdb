package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MPartialCheckBox extends FocusPanel implements ClickListener{
	public static final int unchecked = 0;
	public static final int partiallyChecked = 1;
	public static final int checked = 2;
	
	private int state = 0; // 0 = unchecked, 1 = partial check, 2 = checked
	private Label statelbl;
	private Label text;
	private HorizontalPanel hp;
	public MPartialCheckBox(){
		this("");
	}
	public MPartialCheckBox(final String text){
		hp = new HorizontalPanel();
		statelbl = new Label();
		this.text = new Label(text);
		hp.add(statelbl);
		hp.add(this.text);
		add(hp);
		setState(unchecked);
		this.setStyleName("inline");
		this.addClickListener(this);
	}
	
	public void setText(final String text){
		this.text.setText(text);
	}
	
	public void setState(final int state){
		if (state >= unchecked && state <= checked){
			this.state = state;
			if (state == unchecked){
				statelbl.setText("0");
			} else if (state == partiallyChecked){
				statelbl.setText("1");
			} else if (state == MPartialCheckBox.checked){
				statelbl.setText("2");
			} 
		}
	}
	
	public int getState(){
		return state;
	}
	
	public void onClick (final Widget sender){
		if (state == unchecked){
			setState(checked);
		} else if (state == checked){
			setState(unchecked);
		} else if (state == partiallyChecked){
			setState(unchecked);
		}
	}
}
