package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.Date;

import org.gwtwidgets.client.ui.cal.CalendarDate;
import org.gwtwidgets.client.ui.cal.CalendarEvent;
import org.gwtwidgets.client.ui.cal.CalendarListener;
import org.gwtwidgets.client.ui.cal.CalendarMonth;
import org.gwtwidgets.client.ui.cal.CalendarPanel;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateAttribute extends GenericAttribute {
	private Timestamp newValue;
	private static final String[] months = {"January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December",};

	public DateAttribute(final TimestampConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[]{new MText(dateToString(get(obj)))};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final CalendarPanel cp = new CalendarPanel();
		final PopupPanel pp = new PopupPanel(true);
		CalendarEvent event;

		newValue = get(obj);
		event = newValue != null ? cp.addEvent(newValue, false) : null;

		final MLink nextMonth = new MLink("next", "");
		final MLink prevMonth = new MLink("prev", "");
		final MLink nextYear = new MLink("next", "");
		final MLink prevYear = new MLink("prev", "");
		final Label currentDate = new Label();
		if (event != null)
			currentDate.setText(dateToString(event.getEnd()));
		final ListBox monthLb = new ListBox();
		String[] monthName = {"January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December"};
		for (int i = 0; i < monthName.length; ++i) {
			monthLb.addItem(monthName[i]);
		}
		monthLb.addChangeListener(new ChangeListener() {
			public void onChange(Widget w) {
				cp.setCalendarMonth(((ListBox) w).getSelectedIndex(), Integer
						.parseInt(cp.getCurrentYear()));
			}
		});
		monthLb.setSelectedIndex(cp.getCurrentMonth());

		final ListBox yearLb = new ListBox();
		yearLb.addItem(String
				.valueOf(Integer.parseInt(cp.getCurrentYear()) - 1));
		yearLb.addItem(String.valueOf(Integer.parseInt(cp.getCurrentYear())));
		yearLb.addItem(String
				.valueOf(Integer.parseInt(cp.getCurrentYear()) + 1));
		yearLb.addChangeListener(new ChangeListener() {
			public void onChange(Widget w) {
				cp.setCalendarMonth(cp.getCurrentMonth(),
						Integer.parseInt(yearLb.getItemText(yearLb
								.getSelectedIndex())));
			}
		});
		yearLb.setSelectedIndex(1);

		final HorizontalPanel topNav = new HorizontalPanel();
		topNav.add(prevYear);
		topNav.add(yearLb);
		topNav.add(nextYear);

		final HorizontalPanel bottomNav = new HorizontalPanel();
		bottomNav.add(prevMonth);
		bottomNav.add(monthLb);
		bottomNav.add(nextMonth);

		final CalendarListener eventListener = new CalendarListener() {
			public void onDateClick(final CalendarDate date) {
				final String text = dateToString(date.getDate());
				currentDate.setText(text);
				newValue = new Timestamp(date.getDate().getTime());
				pp.hide();
			}
			public boolean onEventDateClick(final CalendarDate date) {
				return false;
			}
			public void onMonthChange(final CalendarMonth month) {
				// monthName.setText(cp.getCurrentMonthName());
				monthLb.setItemSelected(cp.getCurrentMonth(), true);
				// yearName.setText(cp.getCurrentYear());
				if (cp.getCurrentYear() != yearLb.getItemText(yearLb
						.getSelectedIndex())) {
					// update lb
					yearLb.removeItem(0);
					yearLb.removeItem(0);
					yearLb.removeItem(0);
					yearLb.addItem(String.valueOf(Integer.parseInt(cp
							.getCurrentYear()) - 1));
					yearLb.addItem(String.valueOf(Integer.parseInt(cp
							.getCurrentYear())));
					yearLb.addItem(String.valueOf(Integer.parseInt(cp
							.getCurrentYear()) + 1));
					yearLb.setSelectedIndex(1);
				}
			}
		};

		cp.addNextMonthActivator(nextMonth);
		cp.addPrevMonthActivator(prevMonth);
		cp.addNextYearActivator(nextYear);
		cp.addPrevYearActivator(prevYear);
		cp.addCalendarListener(eventListener);

		final VerticalPanel vpCal = new VerticalPanel();
		// vpCal.add(currentDate);
		vpCal.add(topNav);
		vpCal.add(cp);
		vpCal.add(bottomNav);

		pp.setWidget(vpCal);
		pp.setStyleName("calendar");
		pp.setVisible(false);

		final HorizontalPanel hp = new HorizontalPanel();
		//TODO this is where we change what the calendar link looks like
		final MLink showCal = new MLink("show cal", "");

		pp.addPopupListener(new PopupListener() {
			public void onPopupClosed(PopupPanel pp, boolean b) {
			}
		});

		showCal.addClickListener(new ClickListener() {
			public void onClick(final Widget w) {
				pp.show();
				pp.setVisible(true);
				pp.setPopupPosition(w.getAbsoluteLeft() - 25, w
						.getAbsoluteTop() + 25);
			}
		});

		hp.add(currentDate);
		hp.add(showCal);

		return new Widget[]{hp};
	}

	protected Object get(final Widget editWidget) {
		return newValue;
	}
	protected Timestamp get(final MObject obj) {
		return (Timestamp) mGet(obj);
	}
	protected void set(final MObject obj, final Object v) {
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