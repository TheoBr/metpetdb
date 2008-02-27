package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.input.CurrentError;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;

/**
 * Every attribute that goes on a DetailsPanel will extend this class
 */
public abstract class GenericAttribute {
	protected PropertyConstraint[] constraints;
	protected String[] labels;
	private boolean readOnly; // Cannot be modified or created
	private boolean immutable; // Cannot be modified, but can be created
	private DetailsPanel myPanel;

	/**
	 * Creates a new generic attribute and fetches the labels from the locale
	 * 
	 * @param pcs
	 *            the constraints
	 *            {@link edu.rpi.metpetdb.client.model.validation.PropertyConstraint}
	 *            that are a part of this attribute
	 */
	protected GenericAttribute(final PropertyConstraint[] pcs) {
		if (pcs.length == 1) {
			constraints = pcs;
			try {
				labels = new String[]{LocaleHandler.lc_entity
						.getString(pcs[0].entityName + "_"
								+ pcs[0].propertyName)};
			} catch (MissingResourceException mre) {
				// could be a string array so check that before failing
				// completey
				try {
					labels = LocaleHandler.lc_entity.getStringArray(pcs[0].entityName
							+ "_" + pcs[0].propertyName);
				} catch (MissingResourceException mre2) {
					labels = new String[]{pcs[0].propertyName};
				}
			}
		} else if (pcs.length > 1) {
			final ArrayList al = new ArrayList();
			for (int i = 0; i < pcs.length; ++i) {
				constraints = pcs;
				try {
					al.add(LocaleHandler.lc_entity.getString(pcs[i].entityName + "_"
							+ pcs[i].propertyName));
				} catch (MissingResourceException mre) {
					al.add(pcs[i].propertyName);
				}
			}
			labels = (String[]) al.toArray(new String[al.size()]);
		} else {
			throw new RuntimeException(
					"Needs constraints for a generic attribute.");
		}
	}

	/**
	 * Wrapper for
	 * {@link GenericAttribute#GenericAttribute(PropertyConstraint[])} that
	 * allows the developer to just specify one constraint
	 * 
	 * @param pc
	 *            the constraint
	 *            {@link edu.rpi.metpetdb.client.model.validation.PropertyConstraint}
	 *            for this attribute
	 */
	protected GenericAttribute(final PropertyConstraint pc) {
		this(new PropertyConstraint[]{pc});
	}

	/**
	 * Gets the panel that this attribute is contained on.
	 * 
	 * @return the panel
	 */
	public DetailsPanel getMyPanel() {
		return myPanel;
	}
	/**
	 * Sets the panel that this attribute is contained on.
	 * 
	 * @param dp
	 *            the details panel
	 */
	public void setMyPanel(final DetailsPanel dp) {
		myPanel = dp;
	}

	/**
	 * Gets the primary label for this attribute.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		if (labels != null && labels.length > 0)
			return labels[0];
		else
			return constraints[0].propertyName;
	}
	/**
	 * Gets a the label for a row of the attribute. This is used in cases in
	 * which an attribute takes up more than one table row there will be more
	 * than one label.
	 * 
	 * @param row Which label you want
	 * @return the label
	 */
	public String getLabel(final int row) {
		if (labels != null && row < labels.length)
			return labels[row];
		else
			return constraints[0].propertyName;
	}
	/**
	 * Sets the label for a specified row
	 * @param s the label 
	 * @param row the row
	 */
	public void setLabel(final String s, final int row) {
		if (labels != null && row < labels.length) {
			labels[row] = s;
		}
	}

	/**
	 * Gets the default constraint for this attribute.
	 * @return the property constraint
	 */
	public PropertyConstraint getConstraint() {
		return constraints[0];
	}
	/**
	 * Gets the whole array of property constraints for this attribute
	 * @return array of {@link edu.rpi.metpetdb.client.model.validation.PropertyConstraint}
	 */
	public PropertyConstraint[] getConstraints() {
		return constraints;
	}

	public boolean getReadOnly() {
		return readOnly;
	}
	public GenericAttribute setReadOnly(final boolean b) {
		readOnly = b;
		return this;
	}

	public boolean getImmutable() {
		return immutable;
	}
	public GenericAttribute setImmutable(final boolean b) {
		immutable = b;
		return this;
	}

	protected MObjectDTO resolve(final MObjectDTO obj) {
		return obj;
	}
	protected Object mGet(final MObjectDTO obj) {
		return resolve(obj).mGet(constraints[0].propertyId);
	}
	protected HashMap mGetAll(final MObjectDTO obj) {
		final HashMap map = new HashMap();
		for (int i = 0; i < constraints.length; ++i) {
			map.put(constraints[i], resolve(obj)
					.mGet(constraints[i].propertyId));
		}
		return map;
	}
	protected void mSet(final MObjectDTO obj, final Object val) {
		mSet(obj, val, constraints[0]);
	}
	protected void mSet(final MObjectDTO obj, final Object val,
			final PropertyConstraint constraint) {
		resolve(obj).mSet(constraint.propertyId, val);
	}

	public abstract Widget[] createDisplayWidget(MObjectDTO obj);

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		return createDisplayWidget(obj);
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id,
			final GenericAttribute ga) {
		return createEditWidget(obj, id);
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id,
			final GenericAttribute ga, final int row) {
		return createEditWidget(obj, id, ga);
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id,
			final int row) {
		return createEditWidget(obj, id);
	}

	protected void applyStyle(final Widget[] widgets, final boolean valid) {
		for (int i = 0; i < widgets.length; ++i) {
			applyStyle(widgets[i], valid);
		}
	}

	protected void applyStyle(final Widget editWidget, final boolean valid) {
		if (valid) {
			if (constraints[0].required)
				editWidget.setStyleName(Styles.REQUIRED_FIELD);
			else
				editWidget.setStyleName("");
		} else {
			if (constraints[0].required)
				editWidget.setStyleName(Styles.INVALID_REQUIRED_FIELD);
			else
				editWidget.setStyleName(Styles.INVALID_FIELD);
		}
	}

	public void commitEdit(final MObjectDTO obj, final Widget[] editWidget,
			final CurrentError err, final ServerOp r) {
		for (int i = 0; i < constraints.length; ++i) {
			try {
				constraints[i]
						.validateValue(get(editWidget, constraints[i], i));
				set(obj, get(editWidget, constraints[i], i), constraints[i]);
				applyStyle(editWidget, true);
				err.setText("");
				if (r != null) {
					r.onSuccess(null);
				}
			} catch (ValidationException whybad) {
				showError(editWidget, err, whybad);
			}
		}
	}

	public void showError(final Widget[] w, final HasText err,
			final ValidationException whybad) {
		applyStyle(w, false);
		err.setText(whybad.format());
	}

	protected Object get(final Widget[] editWidgets,
			final PropertyConstraint constraint, final int i)
			throws ValidationException {
		return get(editWidgets[i], constraint);
	}

	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) throws ValidationException {
		return get(editWidget);
	}

	//TODO maybe make this abstract???
	protected Object get(final Widget editWidget) throws ValidationException {
		return null;
		
	}

	protected void set(final MObjectDTO obj, final Object value,
			final PropertyConstraint constraint) {
		set(obj, value);
	}

	protected abstract void set(final MObjectDTO obj, final Object value);

	public void setFocus(final Widget editWidget, final boolean focus) {
		if (editWidget instanceof HasFocus)
			((HasFocus) editWidget).setFocus(true);
	}

	public void setEnabled(final Widget[] editWidgets, final boolean enable) {
		for (int i = 0; i < editWidgets.length; ++i) {
			if (editWidgets[i] instanceof FocusWidget)
				((FocusWidget) editWidgets[i]).setEnabled(enable);
		}
	}
}
