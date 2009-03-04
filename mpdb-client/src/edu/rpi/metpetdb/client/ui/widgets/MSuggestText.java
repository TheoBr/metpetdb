package edu.rpi.metpetdb.client.ui.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class MSuggestText extends FlowPanel implements ClickListener {
	public SuggestBox suggestBox;
	private final String STYLENAME = "suggest";
	private String popupStyleName;
	private Set<String> MySuggestions;
	private final Button showAll = new Button("+");
	private final PopupPanel db = new PopupPanel(true);
	private final static String[] linkOptions = {"A","B","C","D","E","F","G","H","I","J","K",
		"L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	public MSuggestText(){
		this(new HashSet<String>(), false);
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll){
		setStylePrimaryName(STYLENAME + "-box-wrap");
		MySuggestions = suggestions;
		final MMultiWordSuggestOracle oracle = new MMultiWordSuggestOracle();	
		oracle.addAll(suggestions);
		suggestBox = new SuggestBox(oracle);
		suggestBox.setStyleName(STYLENAME+"-box");
		setPopupStyleName(STYLENAME+"-popup");
		add(suggestBox);
		
		if (addShowAll){
			showAll.addClickListener(this);
			add(showAll);
			db.addPopupListener(new PopupListener(){
				public void onPopupClosed(final PopupPanel sender, final boolean autoClosed){
					showAll.setText("+");
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
	
	public String getText(){
		return suggestBox.getText();
	}
	
	public void setText(final String s){
		suggestBox.setText(s);
	}
	
	public void onClick(final Widget sender){
		if (showAll.getText().equals("+")){
			show();
			final MTwoColPanel container = new MTwoColPanel();
			final ScrollPanel sp = new ScrollPanel();
			final FlowPanel fp = new FlowPanel();
			final FlowPanel linker = new FlowPanel();
			final ArrayList<String> values = new ArrayList<String>(MySuggestions);
			
			for (String s : linkOptions){
				final HTML link = new HTML(s);
				values.add(s);
				link.addClickListener(new ClickListener(){
					public void onClick(final Widget sender){
						// find first match of letter
						final int index = findFirstMatch(((HTML)sender).getText(),values);
						sp.setScrollPosition(fp.getWidget(index).getAbsoluteTop()-fp.getWidget(0).getAbsoluteTop());
					}
				});
				linker.add(link);
			}
			
			
			Collections.sort(values);
			sp.setHeight("350px");
			for (String s : values){
				if (isHeader(s)){
					fp.add(new Label(s));
				} else {
					final HTML value = new HTML(s);
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
			container.getLeftCol().add(sp);
			
			container.getRightCol().add(linker);		
			container.setLeftColWidth("100%");
	
			db.setWidget(container);
	
			db.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()+this.getOffsetHeight());
			db.show();
		} else {
			hide();
		}
	}
	
	private int findFirstMatch(final String criteria, final ArrayList<String> values){
		for (int i = 0; i < values.size(); i++){
			if (values.get(i).equals(criteria)){
				return i;
			}
		}
		return -1;
	}
	
	private boolean isHeader(final String value){
		for (String s : linkOptions){
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
	
	private void show(){
		showAll.setText("-");
	}
}
