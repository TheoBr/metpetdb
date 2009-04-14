package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public abstract class MultipleSuggestTextAttribute extends GenericAttribute implements ChangeListener, SuggestionHandler{
	private final boolean addShow;
	private Set<String> suggestions;
	private MHtmlList editList;
	protected final ArrayList<Widget> realEditWidgets;
	private static final String STYLENAME = "multi-suggest";
	
	public ArrayList<Widget> getRealEditWidgets(){
		return realEditWidgets;
	}
	public MHtmlList getEditList(){
		return editList;
	}
	
	public MultipleSuggestTextAttribute(final PropertyConstraint sc) {
		this(sc,false);
	}
	
	public MultipleSuggestTextAttribute(final PropertyConstraint sc, final boolean addShow) {
		super(sc);
		this.addShow = addShow;
		realEditWidgets = new ArrayList<Widget>();
		suggestions = new HashSet<String>();
	}
	
	public Widget[] createDisplayWidget(final MObject obj){
		final MHtmlList list = new MHtmlList();

		final Set s = get(obj);
		if (s != null) {
			final Iterator itr = s.iterator();
			while (itr.hasNext()) {
				final Label r = new Label(itr.next().toString());
				list.add(r);
			}
		}
		return new Widget[] {
			list
		};
	}

	
	public Widget[] createEditWidget(final MObject obj, final String id) {
		editList = new MHtmlList();
		editList.setStylePrimaryName(STYLENAME);

		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				MultipleInputPanel t = MultipleSuggestTextAttribute.this.createOptionalSuggestBox(object.toString());
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
			}
		}
		MultipleInputPanel t = createOptionalSuggestBox(null);
		editList.add(t);
		realEditWidgets.add(t.getInputWidget());
		setSuggest();
		setStyles();
		return new Widget[] {
			editList
		};
	}

	public MultipleInputPanel createOptionalSuggestBox(final String s) {
		final MultipleInputPanel panel = new MultipleInputPanel();
		final MSuggestText st = new MSuggestText(suggestions,true);
		st.suggestBox.setText(s);
		st.suggestBox.addChangeListener(this);
		st.suggestBox.addEventHandler(this);
		panel.setInputWidget(st);
		panel.addButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				MultipleInputPanel t = MultipleSuggestTextAttribute.this.createOptionalSuggestBox(null);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				setStyles();
			}
		});
		panel.removeButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...
				if (realEditWidgets.size() > 1) {
					// remove one
					editList.remove(panel);
					realEditWidgets.remove(st);
				}
				setStyles();
			}
		});
		return panel;
	}
	
	public void createSuggest(final Set<String> options){
		setSuggestions(options);
		ArrayList<MultipleInputPanel> realEditList = new ArrayList<MultipleInputPanel>();
		for (int i = 0; i < realEditWidgets.size(); i++) {
			MSuggestText temp = (MSuggestText) ((MultipleInputPanel) editList.getWidget(i)).getInputWidget();
			MultipleInputPanel p = createOptionalSuggestBox(temp.getText());
			realEditList.add(p);
			realEditWidgets.set(i, p.getInputWidget());
			temp.suggestBox.addChangeListener(this);
			temp.suggestBox.addEventHandler(this);
		}
		editList.clear();
		while (realEditList.size() > 0){
			final MultipleInputPanel w = (MultipleInputPanel) realEditList.get(0);
			realEditList.remove(w);
			editList.add(w);
		}
		setStyles();
	}
	
	public abstract void setSuggest();
	
	public Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}
	
	public void setSuggestions(final Set<String> suggestions){
		this.suggestions = suggestions;
	}
	
	public void setStyles() {
		if (editList.getWidgetCount() == 1) {
			MultipleInputPanel p = (MultipleInputPanel) editList.getWidget(0);
			p.setAlone(true);
			CSS.show(p.addButton);
		} else {
			for (int i=0; i<editList.getWidgetCount(); i++) {
				MultipleInputPanel p = (MultipleInputPanel) editList.getWidget(i);
				p.setAlone(false);
				if (i < editList.getWidgetCount()-1) CSS.hide(p.addButton);
				else CSS.show(p.addButton);
			}
		}
	}
	
//	protected class MultipleInputEntry{
//		private MultipleInputPanel p;
//		public MultipleInputEntry(final MultipleInputPanel p){
//			this.p = p;
//		}
//		public String getText(){
//			String val= "";
//			if (p.getInputWidget() instanceof MSuggestText){
//				val = ((MSuggestText) p.getInputWidget()).getText();
//			}
//			return val;
//		}
//		
//		public void setText(final String text){
//			if (p.getInputWidget() instanceof MSuggestText){
//				((MSuggestText) p.getInputWidget()).setText(text);
//			}
//		}
//	}

}
