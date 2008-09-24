package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.Set;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class SuggestTextAttribute extends GenericAttribute {
	private SimplePanel container;
	private final boolean addShow;
	private MSuggestText st;
	private Set<String> suggestions;
	
	public SuggestTextAttribute(final PropertyConstraint sc) {
		this(sc,false);
	}
	
	public SuggestTextAttribute(final PropertyConstraint sc, final boolean addShow) {
		super(sc);
		this.addShow = addShow;
	}
	
	public Widget[] createDisplayWidget(final MObject obj){
		return new Widget[] {
				new MText(get(obj))
			};
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		setSuggest();
		container = new SimplePanel();
		st = new MSuggestText();
		final String s = get(obj);
		if (s != null) {
			st.suggestBox.setText(s);
		}
		container.add(st);
		return new Widget[] {
				container
		};
	}
	
	public void createSuggest(final Set<String> options){
		final String originaltext = st.suggestBox.getText();
		st = new MSuggestText(options,addShow);
		st.suggestBox.setText(originaltext);
		container.clear();
		container.add(st);
	}
	
	
	public void set(final MObject obj, Object o){
		mSet(obj, o);
	}
	
	protected String get(final MObject obj) {
		final Object value = mGet(obj);
		if (value != null)
			return value.toString();
		else
			return "";
	}

	
	public abstract void setSuggest();
}
