package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.BooleanConstraint;
import edu.rpi.metpetdb.client.model.validation.MineralConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;

/**
 * Basically a mineral attribute but the user can choose if it
 * is not a mineral but part of a large rock as well
 *
 */
public class AnalysisMaterialAttribute extends GenericAttribute {
	
	private MineralAttribute ma;
	private RadioButtonAttribute rb;
	public AnalysisMaterialAttribute(final MineralConstraint mc, final BooleanConstraint bc) {
		super(new PropertyConstraint[] {mc, bc});
		ma = new MineralAttribute(mc, 1);
		rb = new RadioButtonAttribute(bc);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		final HashMap values = this.mGetAll(obj);
		if (((Boolean)values.get(this.getConstraints()[1])).booleanValue()) {
			return new Widget[] {new Label(LocaleHandler.lc_entity.MineralAnalysis_largeRock())} ;
		} else {
			return ma.createDisplayWidget(obj);
		}
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final Widget[] mineralWidgets = ma.createEditWidget(obj, id, ma);
		final Widget[] rbWidgets = rb.createEditWidget(obj, id);
		return new Widget[]{mineralWidgets[0], rbWidgets[0]};
	}

	protected Object get(final Widget editWidget, final PropertyConstraint constraint) {
		if (constraint == ma.getConstraint()) {
			return ma.get(editWidget);
		} else if (constraint == rb.getConstraint()) {
			return rb.get(editWidget);
		}
		return null;
	}
	protected void set(final MObject obj, final Object v, final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}
	protected void set(final MObject obj, final Object v) {
		
	}
}
