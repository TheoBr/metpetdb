package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MultipleInputPanel;

public class MetamorphicGradeAttribute extends GenericAttribute {	
	private MHtmlList editList = new MHtmlList();
	protected final ArrayList<Widget> realEditWidgets = new ArrayList();
	
	public MetamorphicGradeAttribute(final ObjectConstraint sc) {
		super(new PropertyConstraint[] {sc});
	}
	public MetamorphicGradeAttribute(final StringConstraint sc) {
		super(sc);
	}
	public MetamorphicGradeAttribute(final PropertyConstraint sc) {
		super(sc);
	}

	public Set<MetamorphicGrade> get(MObject obj) {
		return (Set<MetamorphicGrade>) obj.mGet(this.getConstraint().property);
	}
	
	protected Set<MetamorphicGrade> get(final Widget editWidget) {
		Set<MetamorphicGrade> mgs = new HashSet();
		Iterator<Widget> itr = realEditWidgets.iterator();
		while(itr.hasNext()){
			ListBox lb = (ListBox) itr.next();
			if (!lb.getValue(lb.getSelectedIndex()).equals("")){
				mgs.add(getMetamorphicGradeWithID(lb.getValue(lb.getSelectedIndex())));
			}
		}
		return mgs;
	}


	public Widget[] createDisplayWidget(final MObject obj) {
		final MHtmlList list = new MHtmlList();

		final Set<MetamorphicGrade> s = get(obj);
		if (s != null) {
			final Iterator<MetamorphicGrade> itr = s.iterator();
			while (itr.hasNext()) {
				final Label r = new Label(itr.next().toString());
				list.add(r);
			}
		}
		return new Widget[] {
			list
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id, final GenericAttribute attr) {
		editList = new MHtmlList();

		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set<MetamorphicGrade> s = get(obj);
		if (s != null) {
			final Iterator<MetamorphicGrade> iter = s.iterator();
			while (iter.hasNext()) {
				final MetamorphicGrade mg = iter.next();
				MultipleInputPanel t = MetamorphicGradeAttribute.this
				.createOptionalDropDown(mg,((HasValues)attr.getConstraint()).getValues());
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
			}
		}

		MultipleInputPanel t = createOptionalDropDown(null,((HasValues)attr.getConstraint()).getValues());
		editList.add(t);
		realEditWidgets.add(t.getInputWidget());
		setStyles();
		return new Widget[] {
			editList
		};
	}

	public MultipleInputPanel createOptionalDropDown(final MetamorphicGrade mg, final Collection<?> vals) {
		final MultipleInputPanel panel = new MultipleInputPanel();
		final ListBox lb = new ListBox(); 
		Iterator<?> itr = vals.iterator();
		lb.addItem(LocaleHandler.lc_text.listBoxSelect(), "");
		while (itr.hasNext()){
			MetamorphicGrade metamorphicGrade = (MetamorphicGrade) itr.next();
			lb.addItem(metamorphicGrade.getName(), String.valueOf(metamorphicGrade.getId()));
		}
		if (mg != null){
			for (int i = 0; i < lb.getItemCount(); i++){
				if (lb.getValue(i).equals(String.valueOf(mg.getId()))){
					lb.setSelectedIndex(i);
				}
			}
		}
		panel.setInputWidget(lb);
		panel.addButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				MultipleInputPanel t = MetamorphicGradeAttribute.this.createOptionalDropDown(null,vals);
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
					realEditWidgets.remove(lb);
				}
				setStyles();
			}
		});
		return panel;
	}

	private MetamorphicGrade getMetamorphicGradeWithID(final String id) {
		MetamorphicGrade metamorphicGradeWithID = new MetamorphicGrade();
		Collection<?> metamorphicGrades = ((HasValues)this.getConstraint()).getValues();
		final Iterator<?> itr = metamorphicGrades.iterator();
		while (itr.hasNext()) {
			final MetamorphicGrade metamorphicGrade = (MetamorphicGrade) itr.next();
			if (String.valueOf(metamorphicGrade.getId()).equals(id))
				metamorphicGradeWithID = metamorphicGrade;
		}
		return metamorphicGradeWithID;
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
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

}
