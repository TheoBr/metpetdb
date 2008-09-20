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

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.DateSpanConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateRangeAttribute extends GenericAttribute implements
		ChangeListener {
	private static final String[] months = {
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December",
	};
	private Timestamp fromDate;
	private Timestamp toDate;
	private DateSpan dateRange;
	private final TextBox fromMonth = new TextBox();
	private final TextBox fromDay = new TextBox();
	private final TextBox fromYear = new TextBox();
	private final TextBox toMonth = new TextBox();
	private final TextBox toDay = new TextBox();
	private final TextBox toYear = new TextBox();
	private int daysInMonth = 30;
	private int daysInYear = 365;

	public DateRangeAttribute(final DateSpanConstraint dsc) {
		super(new PropertyConstraint[] {
			dsc
		});
	}
	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new MText(dateToString(get(obj)))
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final Timestamp currentDate = get(obj);

		if (currentDate != null) {
			fromMonth.setText(currentDate.getMonth() + 1 + "");
			fromDay.setText(currentDate.getDate() + "");
			fromYear.setText(currentDate.getYear() + 1900 + "");
		}

		/* Setup textboxes for user input */
		final HorizontalPanel dateInput = new HorizontalPanel();
		dateInput.add(new Label("FromMonth:"));
		dateInput.add(fromMonth);
		dateInput.add(new Label("FromDay:"));
		dateInput.add(fromDay);
		dateInput.add(new Label("FromYear:"));
		dateInput.add(fromYear);
		dateInput.add(new Label("ToMonth:"));
		dateInput.add(toMonth);
		dateInput.add(new Label("ToDay:"));
		dateInput.add(toDay);
		dateInput.add(new Label("ToYear:"));
		dateInput.add(toYear);

		fromMonth.setMaxLength(2);
		fromMonth.setVisibleLength(2);
		fromMonth.addChangeListener(this);
		fromDay.setMaxLength(2);
		fromDay.setVisibleLength(2);
		fromDay.addChangeListener(this);
		fromYear.setMaxLength(4);
		fromYear.setVisibleLength(4);
		fromYear.addChangeListener(this);
		toMonth.setMaxLength(2);
		toMonth.setVisibleLength(2);
		toMonth.addChangeListener(this);
		toDay.setMaxLength(2);
		toDay.setVisibleLength(2);
		toDay.addChangeListener(this);
		toYear.setMaxLength(4);
		toYear.setVisibleLength(4);
		toYear.addChangeListener(this);
		final Button chooseFromDate = new Button("Choose From Date",
				new ClickListener() {
					public void onClick(final Widget sender) {
						final PopupPanel p = new PopupPanel(true);
						final CalendarModel cm = new CalendarModel();
						final DatePicker to = new DatePicker();
						if (currentDate != null) {
							to.showDate(currentDate);
							to.setSelectedDate(currentDate);
						} else if (fromDate != null) {
							to.showDate(fromDate);
							to.setSelectedDate(fromDate);
						}
						to.addChangeHandler(new ChangeHandler<Date>() {
							public void onChange(ChangeEvent<Date> change) {
								fromMonth.setText(change.getNewValue()
										.getMonth()
										+ 1 + "");
								fromDay.setText(change.getNewValue().getDate()
										+ "");
								fromYear.setText(change.getNewValue().getYear()
										+ 1900 + "");
								fromDate = new Timestamp(change.getNewValue()
										.getTime());
								cm.setCurrentMonthAndYear(fromDate);
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
		final Button chooseToDate = new Button("Choose To Date",
				new ClickListener() {
					public void onClick(final Widget sender) {
						final PopupPanel p = new PopupPanel(true);
						final CalendarModel cm = new CalendarModel();
						final DatePicker to = new DatePicker();
						if (currentDate != null) {
							to.showDate(currentDate);
							to.setSelectedDate(currentDate);
						} else if (toDate != null) {
							to.showDate(toDate);
							to.setSelectedDate(toDate);
						}
						to.addChangeHandler(new ChangeHandler<Date>() {
							public void onChange(ChangeEvent<Date> change) {
								toMonth.setText(change.getNewValue().getMonth()
										+ 1 + "");
								toDay.setText(change.getNewValue().getDate()
										+ "");
								toYear.setText(change.getNewValue().getYear()
										+ 1900 + "");
								toDate = new Timestamp(change.getNewValue()
										.getTime());
								cm.setCurrentMonthAndYear(toDate);
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
				chooseFromDate, chooseToDate, dateInput
		};
	}
	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) {
		if (constraint instanceof DateSpanConstraint) {
			createDateInfoFromInput();
			return dateRange;
		}
		return null;
	}
	protected Timestamp get(final MObject obj) {
		return (Timestamp) mGet(obj);
	}

	protected void set(final MObject obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}

	protected void set(final MObject obj, final Object v) {

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

	private void createDateInfoFromInput() {
		if (fromDate == null)
			fromDate = new Timestamp(0);
		if (toDate == null)
			toDate = new Timestamp(0);
		try {
			if (fromMonth.getText().length() != 0)
				fromDate.setMonth(Integer.parseInt(fromMonth.getText()) - 1);
			if (fromDay.getText().length() != 0)
				fromDate.setDate(Integer.parseInt(fromDay.getText()));
			if (fromYear.getText().length() != 0)
				fromDate.setYear(Integer.parseInt(fromYear.getText()) - 1900);
			if (toMonth.getText().length() != 0)
				toDate.setMonth(Integer.parseInt(fromMonth.getText()) - 1);
			if (toDay.getText().length() != 0)
				toDate.setDate(Integer.parseInt(fromDay.getText()));
			if (fromYear.getText().length() != 0)
				toDate.setYear(Integer.parseInt(fromYear.getText()) - 1900);
			dateRange = new DateSpan(fromDate, toDate);
		} catch (NumberFormatException nfe) {
			// TODO display validation exception
		}
	}

	public void onChange(Widget sender) {
		createDateInfoFromInput();
	}
}
