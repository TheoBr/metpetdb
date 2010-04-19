package edu.rpi.metpetdb.client.ui.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;


public class MSuggestText extends FlowPanel implements ClickListener, KeyboardListener, MouseListener {
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
	private int selected;
	private FlowPanel fp;
	private ScrollPanel sp;
	private FlowPanel linker;
	private boolean isChemistry = false;
	private boolean didType = false;
	
	public MSuggestText(){
		this(new HashSet<String>(), false);
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll){
		this(suggestions,addShowAll, false);
		
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll, final boolean isChemistry){
		setStylePrimaryName(STYLENAME + "-box-wrap");
		this.isChemistry = isChemistry;
		MySuggestions = suggestions;
		final MMultiWordSuggestOracle oracle = new MMultiWordSuggestOracle(isChemistry);	
		oracle.addAll(suggestions);
		suggestBox = new SuggestBox(oracle);
		suggestBox.setStyleName(STYLENAME+"-box");
		suggestBox.addKeyboardListener(this);
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
	
	public void onKeyUp(final Widget sender, final char c, final int i){
		
	}
	
	public void onKeyDown(final Widget sender, final char c, final int i){
		if (sender == fp) {
			if (Integer.valueOf(c) == KEY_DOWN){
				selectedDown(sender);
				setSelected();
			} else if (Integer.valueOf(c) == KEY_UP){
				selectedUp(sender);
				setSelected();
			} else if (Integer.valueOf(c) == KEY_ENTER){
				selectionComplete((HTML)fp.getWidget(selected));
			}
		} else if (sender == suggestBox) {
			if (Integer.valueOf(c) == KEY_ENTER){
				if (didType == true) {
					OnEnterPanel.cancelEnter = true;
					didType = false;
				}
			} else {
				didType = true;
			}
		}
	}
	
	public void onKeyPress(final Widget sender, final char c, final int i){

	}
	
	public void onMouseUp(final Widget sender, final int x, final int y){
		
	}
	
	public void onMouseDown(final Widget sender, final int x, final int y){
		
	}
	
	public void onMouseMove(final Widget sender, final int x, int y){
		if (selected >= 0) removeSelected(selected);
		int low = 0;
		int high = fp.getWidgetCount()-1;
		int middle;
		y+= sender.getAbsoluteTop();
		while(high >= low){
			middle = (low+high)/2;
			if (y > fp.getWidget(middle).getAbsoluteTop() && y < fp.getWidget(middle).getAbsoluteTop() + fp.getWidget(middle).getOffsetHeight()){
				if (fp.getWidget(middle) instanceof HTML){
					selected = middle;
				}
				setSelected();
				return;
			} else if (y > fp.getWidget(middle).getAbsoluteTop() + fp.getWidget(middle).getOffsetHeight()){
				low = middle+1;
			} else {
				high = middle-1;
			}
		}
	}
	
	public void onMouseEnter(final Widget sender){	
	}
	
	public void onMouseLeave(final Widget sender){
	}
	
	
	
	public void onClick(final Widget sender){
		final FocusPanel focusContainer = new FocusPanel();
		final FlowPanel container = new FlowPanel();
		sp = new ScrollPanel();
		fp = new FlowPanel();
		linker = new FlowPanel();
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
					if (fp.getWidgetCount() > index)
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
						selectionComplete(value);
					}
				});
				fp.add(value);
			}
		}
		sp.add(fp);
		selected = getStringIndex(suggestBox.getText());
		setSelected();
		
		container.add(linker);		
		container.add(sp);
		
		
		focusContainer.setWidget(container);
		focusContainer.addKeyboardListener(this);
		focusContainer.addMouseListener(this);
		db.setWidget(focusContainer);

		db.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop()+this.getOffsetHeight());
		db.show();
		focusContainer.setFocus(true);
	}
	
	private int getStringIndex(String input){
		for (int i = 0; i < fp.getWidgetCount(); i++) {
			final Widget w = fp.getWidget(i);
			if (w instanceof HTML){
				final String val = ((HTML) w).getText();
				if (isChemistry){
					final int spaceIndex = val.indexOf(160);	
					if (spaceIndex > 0 && val.substring(0, spaceIndex).equalsIgnoreCase(input)){
						return i;
					}
				} else {
					if (val.equalsIgnoreCase(input)){
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	private void selectedDown(final Widget sender){
		removeSelected(selected);
		if (selected < fp.getWidgetCount()-1){
			if (fp.getWidget(selected+1) instanceof HTML){
				selected++;
			} else selected+=2;
			final Widget w = fp.getWidget(selected);
			if (w.getAbsoluteTop()+w.getOffsetHeight() > sender.getAbsoluteTop() + sender.getOffsetHeight()){
				sp.setScrollPosition(((w.getAbsoluteTop()-fp.getWidget(0).getAbsoluteTop())-sender.getOffsetHeight())+w.getOffsetHeight()+linker.getOffsetHeight());	
			}
		}
	}
	
	private void selectedUp(final Widget sender){
		removeSelected(selected);
		if (selected > 1) {
			if (fp.getWidget(selected-1) instanceof HTML){
				selected--;
			} else selected-=2;
			final Widget w = fp.getWidget(selected);
			if (w.getAbsoluteTop() < sender.getAbsoluteTop() + linker.getOffsetHeight()){
				sp.setScrollPosition(w.getAbsoluteTop()-fp.getWidget(0).getAbsoluteTop());
			}
		}
	}
	
	private void selectionComplete(final HTML selection){
		setText(getValue(selection.getHTML()));
		notifyCompletion();
		hide();
	}
	
	public void notifyCompletion(){}
	
	private void removeSelected(final int index){
		fp.getWidget(index).removeStyleDependentName("selected");
	}
	
	private void setSelected(){
		if (selected < fp.getWidgetCount() && selected >= 0){
			fp.getWidget(selected).addStyleDependentName("selected");
		}
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