package edu.rpi.metpetdb.client.ui.widgets;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class MSuggestText extends FlowPanel{
	public SuggestBox suggestBox;
	public MSuggestText(){
		this(new HashSet<String>(), false);
	}
	
	public MSuggestText(final Set<String> suggestions, final boolean addShowAll){
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();	
		oracle.addAll(suggestions);
		suggestBox = new SuggestBox(oracle);
		add(suggestBox);
		if (addShowAll){
			final Button showAll = new Button("+");
			add(showAll);
		}
	}
}
