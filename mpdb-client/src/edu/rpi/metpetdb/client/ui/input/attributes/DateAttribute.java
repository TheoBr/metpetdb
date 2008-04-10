package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.events.ChangeEvent;
import com.google.gwt.widgetideas.client.events.ChangeHandler;
import com.google.gwt.widgetideas.datepicker.client.CalendarModel;
import com.google.gwt.widgetideas.datepicker.client.DatePicker;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ShortConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateAttribute extends GenericAttribute {
	private static final String[] months = {
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December",
	};
	private Timestamp newDate = new Timestamp(0);
	private final TextBox month = new TextBox();
	private final TextBox day = new TextBox();
	private final TextBox year = new TextBox();
	private int daysInMonth = 30;
	private int daysInYear = 365;

	public DateAttribute(final TimestampConstraint sc, final ShortConstraint ic) {
		super(new PropertyConstraint[] {
				sc, ic
		});
	}
	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[] {
			new MText(dateToString(get(obj), getPrecision(obj)))
		};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		final HorizontalPanel hp = new HorizontalPanel();

		final Timestamp currentDate = get(obj);

		if (currentDate != null) {
			month.setText(currentDate.getMonth() + 1 + "");
			day.setText(currentDate.getDate() + "");
			year.setText(currentDate.getYear() + 1900 + "");
		}

		month.setMaxLength(2);
		day.setMaxLength(2);
		year.setMaxLength(4);
		hp.add(month);
		hp.add(day);
		hp.add(year);
		final Button chooseDate = new Button("Choose Date",
				new ClickListener() {
					public void onClick(final Widget sender) {
						final PopupPanel p = new PopupPanel(true);
						final CalendarModel cm = new CalendarModel();
						final DatePicker to = new DatePicker();
						to.addChangeHandler(new ChangeHandler<Date>() {
							public void onChange(ChangeEvent<Date> change) {
								month.setText(change.getNewValue().getMonth()
										+ "");
								day
										.setText(change.getNewValue().getDate()
												+ "");
								year.setText(change.getNewValue().getYear()
										+ 1900 + "");
								newDate = new Timestamp(change.getNewValue()
										.getTime());
								cm.setCurrentMonthAndYear(newDate);
								daysInMonth = cm
										.getCurrentNumberOfDaysInMonth();

							}
						});
						p.setWidget(to);
						p.addPopupListener(new PopupListener() {

							public void onPopupClosed(PopupPanel sender,
									boolean autoClosed) {

							}

						});
						p.show();
					}
				});
		return new Widget[] {
				chooseDate, hp
		};
	}
	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) {
		if (constraint instanceof TimestampConstraint) {
			if (month.getText().length() != 0)
				newDate.setMonth(Integer.parseInt(month.getText()) - 1);
			if (day.getText().length() != 0)
				newDate.setDate(Integer.parseInt(day.getText()));
			if (year.getText().length() != 0)
				newDate.setYear(Integer.parseInt(year.getText()) - 1900);
			return newDate;
		} else if (constraint instanceof ShortConstraint) {
			short datePrecision = 0;
			if (month.getText().length() == 0)
				datePrecision += 365;
			else if (day.getText().length() == 0)
				datePrecision += daysInMonth;
			return datePrecision;
		}
		return null;
	}
	protected Timestamp get(final MObjectDTO obj) {
		return (Timestamp) mGet(obj);
	}

	protected Short getPrecision(final MObjectDTO obj) {
		return (Short) obj.mGet(this.getConstraints()[1].property);
	}

	protected void set(final MObjectDTO obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}

	protected void set(final MObjectDTO obj, final Object v) {

	}

	public final static String dateToString(final Date dt, final Short precision) {
		if (dt == null)
			return "";

		final int year = dt.getYear() + 1900;
		final int month = dt.getMonth();
		final int day = dt.getDate();

		final String m = months[month];
		final String d = day < 10 ? "0" + day : String.valueOf(day);

		if (precision == 0)
			return m + " " + d + ", " + String.valueOf(year);
		if (precision >= 365)
			return year + "";
		else
			return m + " " + year;
	}
}
