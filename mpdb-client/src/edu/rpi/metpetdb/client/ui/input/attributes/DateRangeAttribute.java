package edu.rpi.metpetdb.client.ui.input.attributes;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
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
		ChangeListener, KeyboardListener, ClickListener {
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
	private Button dpTo;
	private Button dpFrom;

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
		int numDay = Integer.parseInt(month_day.split("/")[1]);
		int numMonth = Integer.parseInt(month_day.split("/")[0])-1;
		if (daysInEachMonth[numMonth] < numDay)
			return false;
		return true;
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		from = new TextBox();
		from.addKeyboardListener(this);
		to = new TextBox();
		to.addKeyboardListener(this);
		dpTo = new Button("+",this);
		dpFrom = new Button("+",this);
		final FlowPanel container = new FlowPanel();
		container.add(from);
		container.add(dpFrom);
		container.add(to);
		container.add(dpTo);
		return new Widget[]{
				container
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
			fromDate = new Timestamp(0);
		if (toDate == null)
			toDate = new Timestamp(0);
		try {
			String[] fromSplit = from.getText().split("/");
			String[] toSplit = to.getText().split("/");
			if (fromSplit.length == 3 && toSplit.length == 3)
			{
				fromDate.setMonth(Integer.parseInt(fromSplit[0]) - 1);
				fromDate.setDate(Integer.parseInt(fromSplit[1]));
				fromDate.setYear(Integer.parseInt(fromSplit[2]) - 1900);
				toDate.setMonth(Integer.parseInt(toSplit[0]) - 1);
				toDate.setDate(Integer.parseInt(toSplit[1]));
				toDate.setYear(Integer.parseInt(toSplit[2]) - 1900);
				dateRange = new DateSpan(fromDate, toDate);
			}
		} catch (NumberFormatException nfe) {
			// TODO display validation exception
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
	private boolean validateMonth(final String s){
		if (Integer.parseInt(s) < 13 && Integer.parseInt(s) > 0 )
			return true;
		return false;
	}
	
	public void onKeyPress(final Widget sender, final char ch, final int m){
		try{
			TextBox tb = (TextBox) sender;
			// save position and original text
			int pos = tb.getCursorPos();
			String orig = tb.getText();
			// remove selected text
			if (!tb.getSelectedText().equals("")){
				String minusSelected = tb.getText().substring(0,tb.getCursorPos());
				minusSelected+=tb.getText().substring(tb.getCursorPos()+tb.getSelectionLength());
				tb.setText(minusSelected);
				tb.setSelectionRange(0, 0);
				tb.setCursorPos(pos);
			}
			String c = "" + ch;
			String t = tb.getText();
			// get text left of the cursor
			String left = t.substring(0, pos);
			String right = t.substring(pos);
			// get text right of cursor up to the first '/'
			String partright = t.substring(pos).split("/")[0];
			String[] allleft = left.split("/");
			String partleft = allleft[allleft.length-1];
			
			// User entering the month
			if (getMatchCount(left,"/")==0){
				if (validateMonth(left+c+partright)){
					if ((left+c+partright).equals("1")){
						tb.setText(left+c+right);
						tb.cancelKey();
					} else {
						if ((right.length() > 0 && right.charAt(0) != '/') || right.length() == 0){
							tb.setText(left+c+"/"+right);
							tb.cancelKey();
						} else{
							tb.setText(left+c+right);
							tb.cancelKey();
						}
					}
				} else {
					tb.cancelKey();
				}
			}
			// User entering the day
			if (getMatchCount(left,"/")==1){
				if (c.equals("/")){
					if (left.charAt(left.length()-1) == '/'){
						tb.cancelKey();
					}
					else if (validateDay(left) && right.charAt(0) !='/'){
						tb.setText(left+c+right);
						tb.cancelKey();
					} else {
						tb.cancelKey();
					}
				}
				if (validateDay(left+c+partright)){
						if (right.length() > 0 && partleft.length() == 1){
							if(right.charAt(0) != '/'){
								tb.setText(left+c+"/"+right);
								tb.cancelKey();
							} else {
								tb.setText(left+c+right);
								tb.cancelKey();
							}
						} else if (allleft.length == 1){
							tb.setText(left+c+right);
							tb.cancelKey();
						} else {
							tb.setText(left+c+"/"+right);
							tb.cancelKey();
						}
				} else {
					tb.cancelKey();
				}
			}
			// User entering year
			if (getMatchCount(left,"/")==2){
				if (c.equals("/") || (partleft+c+partright).length() > 4){
					tb.cancelKey();
				} else {
					tb.setText(left+c+right);
					tb.cancelKey();
				}
			}
			else{
				tb.cancelKey();
			}
		}
		catch (Exception e){
			
		}
	}
	public void onKeyUp(final Widget sender, final char c, final int m){
		
	}
	public void onKeyDown(final Widget sender, final char c, final int m){
		
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
				if (!from.getText().equals("")) {
					final Timestamp t = getTimeInput(from.getText());
					if (t != null){
						dp.showDate(t);
						dp.setSelectedDate(t);
					}
				}
				dp.addChangeHandler(new ChangeHandler<Date>() {
					public void onChange(ChangeEvent<Date> change) {
						fromDate = new Timestamp(change.getNewValue().getTime());
						cm.setCurrentMonthAndYear(fromDate);
						from.setText(String.valueOf(cm.getCurrentMonth()+1) + "/");
						from.setText(from.getText() + String.valueOf(change.getNewValue().getDate() + "/"));
						from.setText(from.getText() + String.valueOf(cm.getCurrentYear() + 1900));
						p.hide();
					}
				});
			}
			else if (sender == dpTo){
				if (!to.getText().equals("")) {
					final Timestamp t = getTimeInput(to.getText());
					if (t != null){
						dp.showDate(t);
						dp.setSelectedDate(t);
					}
				}
				dp.addChangeHandler(new ChangeHandler<Date>() {
					public void onChange(ChangeEvent<Date> change) {
						toDate = new Timestamp(change.getNewValue().getTime());
						cm.setCurrentMonthAndYear(toDate);
						to.setText(String.valueOf(cm.getCurrentMonth()+1) + "/");
						to.setText(to.getText() + String.valueOf(change.getNewValue().getDate() + "/"));
						to.setText(to.getText() + String.valueOf(cm.getCurrentYear() + 1900));
						p.hide();
					}
				});
			}
			p.setWidget(dp);
			p.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop());
			p.show();
	}
}
