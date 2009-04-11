package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
/**
 * 
 * @author goldfd, lindez
 * 
 * The MPartialCheckBox is a three-state checkbox, meaning it can be checked, 
 * unchecked, and partially checked. This is particularly useful in a tree 
 * structure when the user can select children of a parent.
 *
 */
public class MPartialCheckBox extends FlowPanel implements ClickListener, HasText {
	public static enum CheckedState {
		CHECKED, PARTIALLY_CHECKED, UNCHECKED
	};
	
	private CheckedState state;
	private FocusPanel checkbox;
	private InlineLabel label;
	private boolean labelClickable;
	
	private final Image checkedImg = new Image(GWT.getModuleBaseURL() + "/images/icon-checkbox.png");
	private final Image uncheckedImg = new Image(GWT.getModuleBaseURL() + "/images/icon-checkbox-un.png");
	private final Image partiallyCheckedImg = new Image(GWT.getModuleBaseURL() + "/images/icon-checkbox-semi.png");
	
	/**
	 * Creates an MPartialCheckBox with an empty text label
	 */
	public MPartialCheckBox(){
		this("", false);
	}
	
	/**
	 * Creates an MPartialCheckBox with a clickable text label
	 * @param text The text label to appear to the right of the checkbox
	 */
	public MPartialCheckBox(final String text) {
		this(text, true);
	}
	
	/**
	 * Creates an MPartialCheckBox with a text label
	 * @param text The text label to appear to the right of the checkbox
	 * @param labelClickable Whether or not the label is clickable
	 */
	public MPartialCheckBox(final String text, boolean labelClickable){
		this.setStyleName("mpcheckbox");
		
		checkbox = new FocusPanel();
		checkbox.addClickListener(this);
		checkbox.setStyleName("checkbox-img");
		setState(CheckedState.UNCHECKED);
		add(checkbox);
		
		label = new InlineLabel(text);
		label.setStyleName("label");
		setLabelClickable(labelClickable);
		add(label);
	}
	
	/**
	 * Sets the label's text.
	 * @param text The text
	 */
	public void setText(final String text){
		label.setText(text);
	}
	
	/**
	 * Gets the label's text
	 * @return The label's text
	 */
	public String getText() {
		return label.getText();
	}
	
	/**
	 * Set whether or not the label should be clickable.
	 * @param clickable Is the label clickable? (true = yes)
	 */
	public void setLabelClickable(boolean clickable) {
		this.labelClickable = clickable;
		if (clickable) label.addClickListener(this);
		else label.removeClickListener(this);
	}
	
	/**
	 * Set the state of the checkbox
	 * @param state The state
	 */
	public void setState(CheckedState state){
		this.state = state;
		if (state == CheckedState.UNCHECKED){
			checkbox.setWidget(uncheckedImg);
		} else if (state == CheckedState.PARTIALLY_CHECKED){
			checkbox.setWidget(partiallyCheckedImg);
		} else if (state == CheckedState.CHECKED){
			checkbox.setWidget(checkedImg);
		} 
	}
	
	/**
	 * Gets the state of the checkbox
	 * @return the state
	 */
	public CheckedState getState(){
		return state;
	}
	
	/**
	 * Sets the state of the checkbox when the user clicks on it
	 */
	public void onClick(final Widget sender) {
		if (state == CheckedState.CHECKED) {
			setState(CheckedState.UNCHECKED);
		} else {
			setState(CheckedState.CHECKED);
		}
	}

	/**
	 * Adds a ClickListener, honoring the labelClickable setting
	 * @param listener The listener to add
	 */
	public void addClickListener(ClickListener listener) {
		checkbox.addClickListener(listener);
		if (labelClickable) label.addClickListener(listener);
	}
	
	/**
	 * Removes a ClickListener
	 * @param listener The listener to remove
	 */
	public void removeClickListener(ClickListener listener) {
		checkbox.removeClickListener(listener);
		label.removeClickListener(listener);
	}
	
}
