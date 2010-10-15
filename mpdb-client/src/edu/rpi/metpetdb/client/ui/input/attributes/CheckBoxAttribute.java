package edu.rpi.metpetdb.client.ui.input.attributes;





import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/* TODO make generic, i.e. CheckBoxesAttribute<T>, where T is the type of object that the checkbox is representing */
public class CheckBoxAttribute extends GenericAttribute implements
		ClickListener {
	private int cols;
	private FixedWidthFlexTable editList;
	/* To keep track of the physical object that is attached to the checkbox */
	CheckBox item;

	public CheckBoxAttribute(final PropertyConstraint sc) {
		super(sc);
		item = new CheckBox(this.getLabel());
		cols = 1;
	}


	public Widget[] createDisplayWidget(final MObject obj) {
		
		final Boolean s = get(obj);
		
		item.setValue(s);
		
		return new Widget[] {
			item
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
	
		final Boolean object = get(obj);
		
		CheckBox cb = this.createCheckBox(object.toString(), true, object);
		
		DOM.setElementAttribute(cb.getElement(), "id", id);

		return new Widget[] {
				cb
		};
	}

	public CheckBox createCheckBox(final String s, final boolean chosen,
			final Object value) {
		final HorizontalPanel hp = new HorizontalPanel();
		final CheckBox rCheck = new CheckBox();
		rCheck.addClickListener(this);
	
		rCheck.setValue((Boolean)value);

		hp.add(rCheck);

		return rCheck;
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}

	public Boolean get(final MObject obj) {
		return (Boolean) obj.mGet(this.getConstraint().property);
	}

	protected Object get(Widget editWidget) throws ValidationException {
	
		//item = editWidget;
		CheckBox editedCheckbox = (CheckBox)editWidget;
		
		item = editedCheckbox;
		
			return item.getValue();
	}

}
