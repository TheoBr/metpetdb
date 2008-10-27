package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public class RegionAttribute extends MultipleSuggestTextAttribute {

	public RegionAttribute(final ObjectConstraint sc) {
		super(sc);
	}
	public RegionAttribute(final StringConstraint sc) {
		super(sc);
	}
	@Override
	public Set get(MObject obj) {
		return (Set<Region>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet regions = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final Region r = new Region();
			String name = ((MSuggestText) obj).getText();
			if (!name.equals("")) {
				r.setName(name);
				regions.add(r);
			}
		}
		return regions;
	}
	public void setSuggest(){
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.region_svc.allNames(this);
			}
			public void onSuccess(final Object result) {
				createSuggest((Set<String>) result);
			}
		}.begin();
	}
	public void onChange(final Widget sender){

	}
}
