package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.validation.ChemistryConstraint;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class ChemistryAttribute extends GenericAttribute implements ClickListener   {

	private MUnorderedList editList;
	private final ArrayList realEditWidgets;

	public ChemistryAttribute(final ChemistryConstraint sc) {
		super(sc);
		realEditWidgets = new ArrayList();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		
		final MUnorderedList list = new MUnorderedList();
		
		final HorizontalPanel hp = new HorizontalPanel();
		final VerticalPanel vp = new VerticalPanel();
		/*
		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				final Label r = new Label(object.toString());
				list.add(r);
			}
		}
		*/
		
		final ListBox species = new ListBox();
		species.addItem("Species...", "Species...");
		species.addItem("Element", "Element");
		species.addItem("Oxide", "Oxide");
		species.setVisibleItemCount(1);
		
		final ListBox type = new ListBox();
		type.addItem("Type...","Type...");
		type.addItem("Silicate", "Silicate");
		type.addItem("Oxide", "Oxide");
		type.addItem("Carbonate", "Carbonate");
		type.addItem("Phosphate", "Phosphate");
		type.addItem("Other", "Other");
		type.setVisibleItemCount(1);
		
		hp.add(species);
		hp.add(type);

		if(species.getSelectedIndex() != -1 && !(species.isItemSelected(1)) ){
			if(type.getSelectedIndex() != -1 && !(type.isItemSelected(1)) ){
				
			}
		}

		final Button adder = new Button("Add", new ClickListener() {
			public void onClick(final Widget sender) {
				if(species.getSelectedIndex() != -1 && !(species.isItemSelected(1)) ){
					if(type.getSelectedIndex() != -1 && !(type.isItemSelected(1)) ){
						// Add Carbonate Phospate here
						final ListBox chemicals = new ListBox();
						chemicals.addItem("ss", "ss");
						chemicals.addItem("ff", "ff");
						hp.add(chemicals);
					}
				}
			}
		});
		
		hp.add(adder);

		vp.add(hp);
		
		//The table 
		final FlexTable table = new FlexTable();
		table.setStyleName(Styles.DATATABLE);
		
		table.setWidget(0, 0, new Label("Amount(%wt)"));
		table.setWidget(0, 1, new Label("Precision"));
		table.setWidget(0, 2, new Label(""));
	
		
		
		return new Widget[]{vp};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		editList = new MUnorderedList();
		
		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set s = get(obj);
		if (s != null) {
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				editList.add(ChemistryAttribute.this.createOptionalTextBox(object.toString()));
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
				editList.add(ChemistryAttribute.this.createOptionalTextBox(null));
				
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
		final HashSet references = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while(itr.hasNext()) {
			final Object obj = itr.next();
			final ReferenceDTO r = new ReferenceDTO();
			String name = ((HasText) obj).getText();
			if(!name.equals("")){
				r.setName(name);
				references.add(r);
			}
		}
		return references;
	}

	public Set get(final MObjectDTO obj) {
		return (Set) mGet(obj);
	}
	
	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}
}