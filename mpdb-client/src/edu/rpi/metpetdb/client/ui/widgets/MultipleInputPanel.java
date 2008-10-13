package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;

public class MultipleInputPanel extends FlowPanel {

	public final MButton addButton = new MButton();
	public final MButton removeButton = new MButton();
	private final MSimplePanel inputContainer = new MSimplePanel();
	private static final String STYLENAME = "multi-input";

	public MultipleInputPanel() {
		super();
		setStylePrimaryName(STYLENAME);
		add(inputContainer);
		inputContainer.setStyleName(STYLENAME + "-wrap");
		add(removeButton);
		removeButton.setStylePrimaryName(CSS.ICON_MINUS);
		add(addButton);
		addButton.setStylePrimaryName(CSS.ICON_PLUS);
		setAlone(true);
	}
	
	public MultipleInputPanel(Widget input) {
		this();
		setInputWidget(input);
	}
	
	public void setInputWidget(Widget input) {
		inputContainer.setWidget(input);
	}
	
	public Widget getInputWidget() {
		return inputContainer.getWidget();
	}
	
	public void setAlone(boolean alone) {
		removeButton.setEnabled(!alone);
	}

}
