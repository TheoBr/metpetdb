package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;
import com.google.gwt.widgetideas.datepicker.client.CalendarModel;
import com.google.gwt.widgetideas.datepicker.client.DatePicker;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.DateSpanConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MButton;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class DateRangeAttribute extends SearchGenericAttribute implements
		ChangeListener, ClickListener {
	private static final String ints= "0123456789";
	private static final int[] daysInEachMonth = {31,29,31,30,31,30,31,31,30,31,30,31};
	private static final String[] months = {
			"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December",
	};
	private Timestamp fromDate;
	private Timestamp toDate;
	private DateSpan dateRange;
	private TextBox to;
	private TextBox from;
	private MButton dpTo;
	private MButton dpFrom;
	private FlowPanel ew;
	private static final String STYLENAME = "daterange";

	public DateRangeAttribute(final DateSpanConstraint dsc) {
		super(new PropertyConstraint[] {
			dsc
		});
	}
	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
//			new MText(dateToString(get(obj)))
		};
	}
	
	public boolean validateDay(final String month_day)
	{
		try {
			int numDay = Integer.parseInt(month_day.split("/")[1]);
			int numMonth = Integer.parseInt(month_day.split("/")[0])-1;
			if (daysInEachMonth[numMonth] >= numDay)
				return true;
		} catch (Exception e){
			// TODO display error to user
		}
		return false;
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final MText toLbl = new MText("to");
		dpTo = new MButton(null,this);
		dpFrom = new MButton(null,this);
		ew = new FlowPanel();
		from = new TextBox();
		from.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender){
				DateRangeAttribute.this.getSearchInterface().createCritera();
			}
		});
		//from.addKeyboardListener(this);
		to = new TextBox();
		to.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender){
				DateRangeAttribute.this.getSearchInterface().createCritera();
			}
		});
		ew.setStyleName(STYLENAME);
		dpTo.setStyleName(STYLENAME + "-btn");
		dpFrom.setStyleName(STYLENAME + "-btn");
		//to.addKeyboardListener(this);
		ew.add(from);
		ew.add(dpFrom);
		ew.add(toLbl);
		ew.add(to);
		ew.add(dpTo);
		return new Widget[]{ ew };

	}
	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) throws ValidationException{
		if (constraint instanceof DateSpanConstraint) {
			createDateInfoFromInput();
			if (fromDate != null && toDate != null) {
				dateRange = new DateSpan(fromDate, toDate);
			}
			else
				dateRange = null;
			return dateRange;
		}
		return null;
	}
	protected Object get(final MObject obj) {
		return  mGet(obj);
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
			fromDate = new Timestamp(System.currentTimeMillis());
		if (toDate == null)
			toDate = new Timestamp(System.currentTimeMillis());
		try {
			String[] fromSplit = from.getText().split("/");
			String[] toSplit = to.getText().split("/");
			if (fromSplit.length == 0){
				fromDate = null;
			} else if (fromSplit.length == 1){
				if (fromSplit[0].equals("")){
					fromDate = null;
				} else {
					fromDate.setMonth(0);
					fromDate.setDate(1);
					fromDate.setYear(Integer.parseInt(fromSplit[0]) - 1900);
				}
			} else if (fromSplit.length == 2){
				fromDate.setMonth(Integer.parseInt(fromSplit[0])-1);
				fromDate.setDate(1);
				fromDate.setYear(Integer.parseInt(fromSplit[1]) - 1900);
			} else if (fromSplit.length == 3){
				fromDate.setMonth(Integer.parseInt(fromSplit[0]) - 1);
				fromDate.setDate(Integer.parseInt(fromSplit[1]));
				fromDate.setYear(Integer.parseInt(fromSplit[2]) - 1900);
			}
			if (toSplit.length == 0){
				toDate = new Timestamp(System.currentTimeMillis());
			} else if (toSplit.length == 1){
				if (toSplit[0].equals("")){
					toDate = new Timestamp(System.currentTimeMillis());
				} else {
					toDate.setMonth(11);
					toDate.setDate(31);
					toDate.setYear(Integer.parseInt(toSplit[0]) - 1900);
				}
			} else if (toSplit.length == 2){
				toDate.setMonth(Integer.parseInt(toSplit[0])-1);
				toDate.setDate(daysInEachMonth[toDate.getMonth()]);
				toDate.setYear(Integer.parseInt(toSplit[1]) - 1900);
			} else if (toSplit.length == 3){
				toDate.setMonth(Integer.parseInt(toSplit[0]) - 1);
				toDate.setDate(Integer.parseInt(toSplit[1]));
				toDate.setYear(Integer.parseInt(toSplit[2]) - 1900);
			}
			
			// TODO: make an error message show when invalid dates input

		} catch (NumberFormatException nfe) {
			toDate = null;
			fromDate = null;
		}
	}

	public void onChange(Widget sender) {
		createDateInfoFromInput();
	}
	
	private int getMatchCount(final String s, final String m){
		String temp = s;
		int count = 0;
		while (temp.indexOf(m) >= 0){
			temp = temp.substring(temp.indexOf(m)+m.length());
			count++;
		}
		return count;
	}
	
	private boolean validateDate(final String[] mdy)
	{
		if(mdy.length == 3)
		{
			if(!validateMonth(mdy[0]))
			{
				return false;
			}
			if(!validateDay(mdy[0] + "/" + mdy[1]))
			{
				return false;
			}
			if(!validateYear(mdy[2]))
			{
				return false;
			}
			return true;
		}
		if(mdy.length == 2)
		{
			if(!validateMonth(mdy[0]))
			{
				return false;
			}
			if(!validateYear(mdy[1]))
			{
				return false;
			}
			return true;
		}
		if(mdy.length == 1)
		{
			if(!validateYear(mdy[0]))
			{
				return false;
			}
			return true;
		}
		return false;
	}

	private boolean validateMonth(final String s){
		try{
			if (Integer.parseInt(s) < 13 && Integer.parseInt(s) > 0 )
				return true;
		} catch (Exception e){
			// TODO display error to user
		}
		return false;
	}
	
	private boolean validateYear(final String s){
		try {
			if (Integer.parseInt(s) > 0)
				return true;
		} catch (Exception e){
			// TODO display error to user
		}
		return false;
	}
	
	private Timestamp getTimeInput(final String date){
		String[] parsedDate = date.split("/");
		final Timestamp t = new Timestamp(0);
		if (parsedDate.length == 3){
			try
			{
				t.setMonth(Integer.parseInt(parsedDate[0])- 1);
				t.setDate(Integer.parseInt(parsedDate[1]));
				t.setYear(Integer.parseInt(parsedDate[2]) - 1900);
				return t;
			}
			catch(Exception e){
				
			}
		}
		return null;
	}
	
	public void onClick(final Widget sender){
			final PopupPanel p = new PopupPanel(true);
			final CalendarModel cm = new CalendarModel();
			final DatePicker dp = new DatePicker();
			if (sender == dpFrom){
				if (fromDate != null){
					dp.showDate(fromDate);
					dp.setSelectedDate(fromDate);
				}
				dp.addChangeHandler(new ChangeHandler<Date>() {
					public void onChange(ChangeEvent<Date> change) {
						fromDate = new Timestamp(change.getNewValue().getTime());
						cm.setCurrentMonthAndYear(fromDate);
						from.setText(String.valueOf(cm.getCurrentMonth()+1) + "/");
						from.setText(from.getText() + String.valueOf(change.getNewValue().getDate() + "/"));
						from.setText(from.getText() + String.valueOf(cm.getCurrentYear() + 1900));
						DateRangeAttribute.this.getSearchInterface().createCritera();
						p.hide();
					}
				});
			}
			else if (sender == dpTo){
				if (toDate != null){
					dp.showDate(toDate);
					dp.setSelectedDate(toDate);
				}
				dp.addChangeHandler(new ChangeHandler<Date>() {
					public void onChange(ChangeEvent<Date> change) {
						toDate = new Timestamp(change.getNewValue().getTime());
						cm.setCurrentMonthAndYear(toDate);
						to.setText(String.valueOf(cm.getCurrentMonth()+1) + "/");
						to.setText(to.getText() + String.valueOf(change.getNewValue().getDate() + "/"));
						to.setText(to.getText() + String.valueOf(cm.getCurrentYear() + 1900));
						DateRangeAttribute.this.getSearchInterface().createCritera();
						p.hide();
					}
				});
			}
			p.setWidget(dp);
			p.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
			p.show();
	}
	public void onClear(){
		to.setText("");		
		from.setText("");
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		if (validateDate(to.getText().split("/")) && validateDate(from.getText().split("/")))
			criteria.add(createCritRow("Date Range: "+ from.getText() + " to " + to.getText()));
		return criteria;
	}
}
