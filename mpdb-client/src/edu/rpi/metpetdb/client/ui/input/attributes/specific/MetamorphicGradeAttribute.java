package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class MetamorphicGradeAttribute extends GenericAttribute implements
		ClickListener {

	private MUnorderedList editList;

	private final ArrayList realEditWidgets;

	private String[] metgrades = { "zeolite", "prehnite-pumpellyite",
			"greenschist", "amphibolite", "epidote amphibolite", "granulite",
			"blueschist", "eclogite", "hornfels", "chlorite zone",
			"biotite zone", "garnet zone", "staurolite zone",
			"staurolite-kyanite zone", "kyanite zone", "sillimanite zone",
			"andalusite zone", "sillimanite-K feldspar zone",
			"garnet-cordierite zone", "migmatite zone", "ultra high pressure",
			"ultra high temperature" };

	public MetamorphicGradeAttribute(final StringConstraint sc) {
		super(sc);
		realEditWidgets = new ArrayList();
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		final MUnorderedList list = new MUnorderedList();

		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				final Label r = new Label(object.toString());
				list.add(r);
			}
		}
		return new Widget[] { list };
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		editList = new MUnorderedList();

		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);
		
		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				editList.add(MetamorphicGradeAttribute.this.createOptionalSuggestBox(object.toString()));
			}
		}

		editList.add(createOptionalSuggestBox(null));

		return new Widget[] { editList };
	}
	
	public SuggestBox createSuggestions(){

			MultiWordSuggestOracle suggestions = new MultiWordSuggestOracle();  
		   
			for(int i = 0; i < metgrades.length; i++){
				suggestions.add(metgrades[i]); 
			}
     		
			SuggestBox box = new SuggestBox(suggestions);
		
			return box;
	}

	public HorizontalPanel createOptionalSuggestBox(String s) {
		final HorizontalPanel hp = new HorizontalPanel();
		final SuggestBox rText = MetamorphicGradeAttribute.this.createSuggestions();
		if(s != null)
			rText.setText(s);
		final Button addButton = new Button("Add", new ClickListener() {
			public void onClick(final Widget sender) {
				editList.add(MetamorphicGradeAttribute.this
						.createOptionalSuggestBox(null));

			}
		});
		final Button removeButton = new Button("Remove", new ClickListener() {
			public void onClick(final Widget sender) {
				editList.remove(hp);
				realEditWidgets.remove(rText);
			}
		});

		realEditWidgets.add(rText);

		hp.add(rText);
		hp.add(addButton);
		hp.add(removeButton);

		return hp;
	}

	protected Object get(final Widget editWidget) throws ValidationException { 
		final HashSet metamorphicGrades = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final MetamorphicGrade m = new MetamorphicGrade();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				metamorphicGrades.add(m);
			}
		}
		return metamorphicGrades;
	}

	public Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}
}