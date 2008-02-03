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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class ReferenceAttribute extends GenericAttribute implements ClickListener {

	private MUnorderedList editList;
	private final ArrayList realEditWidgets;

	public ReferenceAttribute(final StringConstraint sc) {
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
		return new Widget[]{list};
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
				editList.add(ReferenceAttribute.this.createOptionalTextBox(object.toString()));
			}
		}
		
		editList.add(createOptionalTextBox(null));

		return new Widget[]{editList};
	}

	public HorizontalPanel createOptionalTextBox(String s) {
		final HorizontalPanel hp = new HorizontalPanel();
		final TextBox rText = new TextBox();
		if(s != null)
			rText.setText(s);
		final Button addButton = new Button("Add", new ClickListener() {
			public void onClick(final Widget sender) {
				editList.add(ReferenceAttribute.this.createOptionalTextBox(null));
				
			}
		});
		final Button removeButton = new Button("Remove", new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...				
				if(realEditWidgets.size() > 1)
				{
					// remove one
					editList.remove(hp);
					realEditWidgets.remove(rText);					
				}

			}
		});
		
		realEditWidgets.add(rText);

		hp.add(rText);
		hp.add(addButton);
		hp.add(removeButton);

		return hp;
	}

	protected Object get(final Widget editWidget) throws ValidationException {
		final HashSet references = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while(itr.hasNext()) {
			final Object obj = itr.next();
			final Reference r = new Reference();
			String name = ((HasText) obj).getText();
			if(!name.equals("")){
				r.setName(name);
				references.add(r);
			}
		}
		return references;
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