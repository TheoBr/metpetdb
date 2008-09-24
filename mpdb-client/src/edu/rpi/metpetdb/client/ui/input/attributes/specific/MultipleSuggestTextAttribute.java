package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public abstract class MultipleSuggestTextAttribute extends GenericAttribute{
	private final boolean addShow;
	private Set<String> suggestions;
	private MHtmlList editList;
	protected final ArrayList<Widget> realEditWidgets;
	
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

		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				editList.add(MultipleSuggestTextAttribute.this
						.createOptionalSuggestBox(object.toString()));
			}
		}

		editList.add(createOptionalSuggestBox(null));
		setSuggest();
		return new Widget[] {
			editList
		};
	}

	public FlowPanel createOptionalSuggestBox(final String s) {
		final FlowPanel fp = new FlowPanel();
		final MSuggestText st = new MSuggestText(suggestions,false);
		st.suggestBox.setText(s);
		final Button addButton = new Button("Add", new ClickListener() {
			public void onClick(final Widget sender) {
				editList.add(MultipleSuggestTextAttribute.this
						.createOptionalSuggestBox(null));

			}
		});
		final Button removeButton = new Button("Remove", new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...
				if (realEditWidgets.size() > 1) {
					// remove one
					editList.remove(fp);
					realEditWidgets.remove(st);
				}
			}
		});
		realEditWidgets.add(st);
		st.addStyleName("inline");
		fp.add(st);
		fp.add(addButton);
		fp.add(removeButton);

		return fp;
	}
	
	public void createSuggest(final Set<String> options){
		suggestions = options;
		ArrayList<Widget> realEditList = new ArrayList();
		for (int i = 0; i < realEditWidgets.size(); i++){
			final FlowPanel fp = new FlowPanel();
			final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();	
			final FlowPanel container = new FlowPanel();
			oracle.addAll(suggestions);
			final SuggestBox sb = new SuggestBox(oracle);
			container.add(sb);
			final Button addButton = new Button("Add", new ClickListener() {
				public void onClick(final Widget sender) {
					editList.add(MultipleSuggestTextAttribute.this
							.createOptionalSuggestBox(null));

				}
			});
			final Button removeButton = new Button("Remove", new ClickListener() {
				public void onClick(final Widget sender) {
					// If there are more than one entry spaces...
					if (realEditWidgets.size() > 1) {
						// remove one
						editList.remove(fp);
						realEditWidgets.remove(sb);
					}
				}
			});
			sb.setText(((SuggestBox)((FlowPanel)((FlowPanel) editList.getWidget(i)).getWidget(0)).getWidget(0)).getText());
			container.add(sb);
			if (addShow){
				final Button showAll = new Button("+");
				container.add(showAll);
			}
			realEditWidgets.set(i, sb);
			container.addStyleName("inline");
			fp.add(container);
			fp.add(addButton);
			fp.add(removeButton);
			realEditList.add(fp);
		}
		editList.clear();
		while (realEditList.size() > 0){
			final Widget w = realEditList.get(0);
			realEditList.remove(w);
			editList.add(w);
		}

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

}
