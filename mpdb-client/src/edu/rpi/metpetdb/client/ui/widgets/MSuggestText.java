package edu.rpi.metpetdb.client.ui.widgets;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class MSuggestText extends FlowPanel {
	
	public SuggestBox suggestBox;
	private final String STYLENAME = "suggest";
	private String popupStyleName;
	
	public MSuggestText(){
		this(new HashSet<String>(), false);
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll){
		setStylePrimaryName(STYLENAME + "-box-wrap");
		final MMultiWordSuggestOracle oracle = new MMultiWordSuggestOracle();	
		oracle.addAll(suggestions);
		suggestBox = new SuggestBox(oracle);
		suggestBox.setStyleName(STYLENAME+"-box");
		setPopupStyleName(STYLENAME+"-popup");
		add(suggestBox);
		
		if (addShowAll){
			final Button showAll = new Button("+");
			add(showAll);
		}
	}
	
	public void setPopupStyleName(String name) {
		suggestBox.setPopupStyleName(name.trim());
		popupStyleName = name.trim();
	}
	
	public void addPopupStyleName(String name) {
		String stylename = (popupStyleName + " " + name).trim();
		suggestBox.setPopupStyleName(stylename);
		popupStyleName = stylename;
	}
	
	public String getText(){
		return suggestBox.getText();
	}
	
	public void setText(final String s){
		suggestBox.setText(s);
	}
	
}
