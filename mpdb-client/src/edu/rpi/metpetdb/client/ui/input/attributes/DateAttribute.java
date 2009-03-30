package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;
import com.google.gwt.widgetideas.datepicker.client.CalendarModel;
import com.google.gwt.widgetideas.datepicker.client.DatePicker;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.ShortConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateAttribute extends GenericAttribute implements ChangeListener {
	
	private Timestamp newDate;
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
	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new MText(Sample.dateToString(get(obj), getPrecision(obj)))
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final Timestamp currentDate = get(obj);

		if (currentDate != null) {
			month.setText(currentDate.getMonth() + 1 + "");
			day.setText(currentDate.getDate() + "");
			year.setText(currentDate.getYear() + 1900 + "");
		}

		/* Setup textboxes for user input */
		final HorizontalPanel dateInput = new HorizontalPanel();
		dateInput.add(new Label("Month:"));
		dateInput.add(month);
		dateInput.add(new Label("Day:"));
		dateInput.add(day);
		dateInput.add(new Label("Year:"));
		dateInput.add(year);

		month.setMaxLength(2);
		month.setVisibleLength(2);
		month.addChangeListener(this);
		day.setMaxLength(2);
		day.setVisibleLength(2);
		day.addChangeListener(this);
		year.setMaxLength(4);
		year.setVisibleLength(4);
		year.addChangeListener(this);
		final Button chooseDate = new Button("Choose Date",
				new ClickListener() {
					public void onClick(final Widget sender) {
						final PopupPanel p = new PopupPanel(true);
						final CalendarModel cm = new CalendarModel();
						final DatePicker to = new DatePicker();
						if (currentDate != null) {
							to.showDate(currentDate);
							to.setSelectedDate(currentDate);
						} else if (newDate != null) {
							to.showDate(newDate);
							to.setSelectedDate(newDate);
						}
						to.addChangeHandler(new ChangeHandler<Date>() {
							public void onChange(ChangeEvent<Date> change) {
								month.setText(change.getNewValue().getMonth()
										+ 1 + "");
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
								p.hide();
							}
						});
						p.setWidget(to);
						p.show();
						p.setPopupPositionAndShow(new PositionCallback() {

							public void setPosition(int offsetWidth,
									int offsetHeight) {
								p.setPopupPosition(sender.getAbsoluteLeft(),
										sender.getAbsoluteTop());

							}

						});
					}
				});
		return new Widget[] {
				chooseDate, dateInput
		};
	}
	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) {
		if (constraint instanceof TimestampConstraint) {
			createDateFromInput();
			return newDate;
		} else if (constraint instanceof ShortConstraint) {
			short datePrecision = 0;
			if (month.getText().length() == 0)
				datePrecision += daysInYear;
			else if (day.getText().length() == 0)
				datePrecision += daysInMonth;
			return datePrecision;
		}
		return null;
	}
	protected Timestamp get(final MObject obj) {
		return (Timestamp) mGet(obj);
	}

	protected short getPrecision(final MObject obj) {
		return obj.mGet(this.getConstraints()[1].property) == null ? 0
				: (Short) obj.mGet(this.getConstraints()[1].property);
	}

	protected void set(final MObject obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}

	protected void set(final MObject obj, final Object v) {

	}

	

	private void createDateFromInput() {
		if (newDate == null && month.getText().length() != 0
				&& day.getText().length() != 0 && year.getText().length() != 0)
			newDate = new Timestamp(0);
		try {
			if (month.getText().length() != 0)
				newDate.setMonth(Integer.parseInt(month.getText()) - 1);
			if (day.getText().length() != 0)
				newDate.setDate(Integer.parseInt(day.getText()));
			if (year.getText().length() != 0)
				newDate.setYear(Integer.parseInt(year.getText()) - 1900);
		} catch (NumberFormatException nfe) {
			// TODO display validation exception
		}
	}

	public void onChange(Widget sender) {
		createDateFromInput();
	}
}
