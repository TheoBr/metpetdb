package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateAttribute extends GenericAttribute {
	private Timestamp newValue;
	private static final String[] months = {
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December",
	};

	public DateAttribute(final TimestampConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[] {
			new MText(dateToString(get(obj)))
		};
	}

	// FIXME update to use gwt incubator date picker
	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		// final DatePicker to = new DatePicker();
		// to.addChangeHandler(new ChangeHandler<Date>() {
		// public void onChange(ChangeEvent<Date> change) {
		// newValue.setTime(to.getSelectedDate().getTime());
		// / }
		// });
		// return new Widget[] {
		// to
		// };
		return new Widget[] {
			new Label("broken")
		};
	}
	protected Object get(final Widget editWidget) {
		return newValue;
	}

	protected Timestamp get(final MObjectDTO obj) {
		return (Timestamp) mGet(obj);
	}

	protected void set(final MObjectDTO obj, final Object v) {
		mSet(obj, v);
	}

	public final static String dateToString(final Date dt) {
		if (dt == null)
			return "";

		final int year = dt.getYear() + 1900;
		final int month = dt.getMonth();
		final int day = dt.getDate();

		final String m = months[month];
		final String d = day < 10 ? "0" + day : String.valueOf(day);

		return m + " " + d + ", " + String.valueOf(year);
	}
}
