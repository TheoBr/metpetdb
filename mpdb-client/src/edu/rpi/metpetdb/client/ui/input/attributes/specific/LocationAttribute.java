package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import org.postgis.Point;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidGeometryException;
import edu.rpi.metpetdb.client.error.validation.InvalidLatitudeException;
import edu.rpi.metpetdb.client.error.validation.InvalidLongitudeException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.GeometryConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class LocationAttribute extends GenericAttribute {

	public LocationAttribute(final GeometryConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		final Point p = get(obj);
		final MText xText = new MText();
		final MText yText = new MText();
		if (p != null) {
			xText.setText(new Double(p.x).toString());
			yText.setText(new Double(p.y).toString());
		}
		return new Widget[] {
				xText, yText
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final TextBox pointX = new TextBox();
		final TextBox pointY = new TextBox();

		DOM.setElementAttribute(pointX.getElement(), "id", id);
		DOM.setElementAttribute(pointY.getElement(), "id", id);

		if (get(obj) != null) {
			pointX.setText((new Double(get(obj).x)).toString());
			pointY.setText((new Double(get(obj).y)).toString());
		} else {
			pointX.setText("");
			pointY.setText("");
		}

		pointX.setMaxLength(20);
		applyStyle(pointX, true);

		pointY.setMaxLength(20);
		applyStyle(pointY, true);
		return new Widget[] {
				pointX, pointY
		};
	}

	protected Object get(final Widget[] editWidget,
			final PropertyConstraint constraint, final int i)
			throws ValidationException {
		final String latitude = ((TextBox) editWidget[0]).getText();
		final String longitude = ((TextBox) editWidget[1]).getText();
		final Point p = new Point();
		p.srid = MpDbConstants.WGS84;
		p.dimension = 2;
		if (latitude.length() > 0) {
			try {
				final double x = Double.parseDouble(latitude);
				if (x > 90 || x < -90)
					throw new InvalidLatitudeException(this.getConstraint(),
							LocaleHandler.lc_entity
									.getString("Sample_latitude"));
				else
					p.x = x;
			} catch (NumberFormatException nfe) {
				throw new InvalidGeometryException(this.getConstraint(),
						LocaleHandler.lc_entity.getString("Sample_latitude"));
			}
		}
		if (longitude.length() > 0) {

			try {
				final double y = Double.parseDouble(longitude);
				if (y > 180 || y < -180)
					throw new InvalidLongitudeException(this.getConstraint(),
							LocaleHandler.lc_entity
									.getString("Sample_longitude"));
				else
					p.y = y;
			} catch (NumberFormatException nfe) {
				throw new InvalidGeometryException(this.getConstraint(),
						LocaleHandler.lc_entity.getString("Sample_longitude"));
			}
		}
		if (latitude.length() == 0)
			throw new PropertyRequiredException(this.getConstraint(),
					LocaleHandler.lc_entity.getString("Sample_latitude"));
		if (longitude.length() == 0)
			throw new PropertyRequiredException(this.getConstraint(),
					LocaleHandler.lc_entity.getString("Sample_longitude"));
		else
			return p;
	}

	protected Point get(final MObject obj) {
		final Point p = (Point) mGet(obj);
		if (p != null) {
			p.srid = MpDbConstants.WGS84;
			p.dimension = 2;
		}
		return p;
	}

	protected void set(final MObject obj, final Object p) {
		mSet(obj, p);
	}

}
