package edu.rpi.metpetdb.client.ui.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;


public class MSuggestText extends FlowPanel implements ClickListener {
	public SuggestBox suggestBox;
	private final String STYLENAME = "suggest";
	private String popupStyleName;
	private Set<String> MySuggestions;
	private final ToggleButton showAll = new ToggleButton(
			new Image("images/btn-all-suggest-up.png"),new Image("images/btn-all-suggest-down.png"));
	private final PopupPanel db = new PopupPanel(true);
	private final static String[] alphabet = {"0-9","@#$","A","B","C","D","E","F","G","H","I",
		"J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private final static String[] linkOptions = {"0-9","@#$","A-F","G-L","M-R","S-Z"};
	
	public MSuggestText(){
		this(new HashSet<String>(), false);
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll){
		this(suggestions,addShowAll, false);
		
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll, final boolean isChemistry){
		setStylePrimaryName(STYLENAME + "-box-wrap");
		MySuggestions = suggestions;
		final MMultiWordSuggestOracle oracle = new MMultiWordSuggestOracle(isChemistry);	
		oracle.addAll(suggestions);
		suggestBox = new SuggestBox(oracle);
		suggestBox.setStyleName(STYLENAME+"-box");
		setPopupStyleName(STYLENAME+"-popup");
		add(suggestBox);
		
		if (addShowAll){
			setAllSuggestPopupStyleName("all-" + STYLENAME + "-popup");
			showAll.addClickListener(this);
			showAll.setStylePrimaryName("all-suggest");
			add(showAll);
			db.addPopupListener(new PopupListener(){
				public void onPopupClosed(final PopupPanel sender, final boolean autoClosed){
					showAll.setDown(false);
				}
			});
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
	
	public void setAllSuggestPopupStyleName(String name) {
		db.setStylePrimaryName(name);
	}
	
	public void addAllSuggestPopupStyleName(String name) {
		db.addStyleName(name);
	}
	
	public String getText(){
		return suggestBox.getText();
	}
	
	public void setText(final String s){
		suggestBox.setText(s);
	}
	
	public void onClick(final Widget sender){
		final FlowPanel container = new FlowPanel();
		final ScrollPanel sp = new ScrollPanel();
		final FlowPanel fp = new FlowPanel();
		final FlowPanel linker = new FlowPanel();
		linker.setStyleName("header");
		final ArrayList<String> values = new ArrayList<String>(MySuggestions);
		
		ArrayList<String> existingAlpha = new ArrayList<String>();
		for (String s : alphabet) {
			for (String v : values) {
				if (v.startsWith(s)) {
					existingAlpha.add(s);
					break;
				}
			}
		}
		values.addAll(existingAlpha);
		Collections.sort(values);
		
		for (String s : linkOptions){
			final HTML link = new HTML(s);
			link.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					// find first match of letter
					final int index = findFirstMatch(((HTML)sender).getText(),values);
					sp.setScrollPosition(fp.getWidget(index).getAbsoluteTop()-fp.getWidget(0).getAbsoluteTop());
				}
			});
			linker.add(link);
		}	
		
		sp.setHeight("350px");
		for (String s : values){
			if (isHeader(s)){
				Label header = new Label(s);
				header.setStyleName("section-header");
				fp.add(header);
			} else {
				final HTML value = new HTML(s);
				value.setStylePrimaryName("item");
				value.addClickListener(new ClickListener(){
					public void onClick(final Widget sender){
						setText(getValue(value.getHTML()));
						hide();
					}
				});
				fp.add(value);
			}
		}
		sp.add(fp);
		
		
		container.add(linker);		
		container.add(sp);
		
		db.setWidget(container);

		db.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()+this.getOffsetHeight());
		db.show();
	}
	
	private int findFirstMatch(final String criteria, final ArrayList<String> values){
		for (int i = 0; i < values.size(); i++){
			if (values.get(i).equalsIgnoreCase(criteria.substring(0, 1))){
				return i;
			}
		}
		return 0;
	}
	
	private boolean isHeader(final String value){
		for (String s : alphabet){
			if (s.equals(value)) return true;
		}
		return false;
	}
	
	protected String getValue(String input) {
		return input;
	}
	
	private void hide(){
		db.hide();
	}
}