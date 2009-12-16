package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.model.SampleAlias;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleTextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public class MetamorphicRegionAttribute extends MultipleTextAttribute {

	public MetamorphicRegionAttribute(ObjectConstraint sc) {
		super(sc);
	}

	public MetamorphicRegionAttribute(ValueInCollectionConstraint vc) {
		super(new PropertyConstraint[] {vc});
	}

	
	@Override
	public Set<MetamorphicRegion> get(MObject obj) {
		return (Set<MetamorphicRegion>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet<MetamorphicRegion> metamorphicRegions = new HashSet<MetamorphicRegion>();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final MetamorphicRegion mr = new MetamorphicRegion();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				metamorphicRegions.add(getMetamorphicRegionWithName(name));
			}
		}
		return metamorphicRegions;
	}
	
	public Widget[] createDisplayWidget(final MObject obj) {
		final MHtmlList list = new MHtmlList();

		final Set<MetamorphicRegion> s = get(obj);
		if (s != null) {
			final Iterator<MetamorphicRegion> itr = s.iterator();
			while (itr.hasNext()) {
				final Label r = new Label(itr.next().toString());
				list.add(r);
			}
		}
		return new Widget[] {
			list
		};
	}
	
	private MetamorphicRegion getMetamorphicRegionWithName(final String name) {
		MetamorphicRegion metamorphicRegionWithName = new MetamorphicRegion();
		Collection<?> metamorphicRegions = ((HasValues)this.getConstraint()).getValues();
		final Iterator<?> itr = metamorphicRegions.iterator();
		while (itr.hasNext()) {
			final MetamorphicRegion metamorphicRegion = (MetamorphicRegion) itr.next();
			if (String.valueOf(metamorphicRegion.getName()).equals(name))
				metamorphicRegionWithName = metamorphicRegion;
		}
		return metamorphicRegionWithName;
	}

	public Widget[] createEditWidget(final MObject obj, final String id, final GenericAttribute attr) {
		return createDisplayWidget(obj);
	}

}
