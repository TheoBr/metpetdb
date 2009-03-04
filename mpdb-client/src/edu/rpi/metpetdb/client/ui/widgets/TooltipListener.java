package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * MouseListener that shows/hides a tooltip
 * 
 * example use:
 * 
 * String nameFull = "Atalay, Hasan MD, CCA";
 * String nameTruncated = nameFull;
 * Label nameLabel;
 * TooltipListener nameListener;
 * if (nameTruncated.length() > MAX_LENGTH) {
 *   nameTruncated = nameTruncated.substring(0, MAX_LENGTH - 3);
 *   nameTruncated += "...";
 *   nameListener = new TooltipListener(nameFull, 8000, "name-tooltip");
 *   nameLabel.addMouseListener(nameListener);
 * }
 * nameLabel.setText(nameTruncated);
 */
public class TooltipListener extends MouseListenerAdapter {
	private static final String DEFAULT_TOOLTIP_STYLE = "tooltip-popup";
	private static final int DEFAULT_OFFSET_X = 10;
	private static final int DEFAULT_OFFSET_Y = 25;

	private Widget widgetLaunching;

	public class Tooltip extends PopupPanel {
		private final int delay;
		protected final HTML contents = new HTML();

		public Tooltip(String text, int delay, String styleName) {
			super(true);
			this.delay = delay;

			contents.setHTML(text);
			contents.addClickListener(hideClickListener);
			contents.addStyleName("tooltip-content");
			add(contents);

			setStyleName(styleName);
		}

		private ClickListener hideClickListener = new ClickListener(){
			public void onClick(Widget sender) {
				hide();
			}
		};

		private Timer hideTimer = new Timer() {
			public void run() {
				hide();
			}
		};

		public void show() {
			if(widgetLaunching != null){
				int left = widgetLaunching.getAbsoluteLeft() + offsetX;
				int top = widgetLaunching.getAbsoluteTop() + offsetY;
				setPopupPosition(left, top);
			}
			super.show();
			if (delay > 0) hideTimer.schedule(delay);
		}
		
		public void setContents(String html) {
			contents.setHTML(html);
		}

	}

	private final Tooltip tooltip;
	private int offsetX = DEFAULT_OFFSET_X;
	private int offsetY = DEFAULT_OFFSET_Y;

	public TooltipListener(String text, int delay) {
		this(text, delay, DEFAULT_TOOLTIP_STYLE);
	}

	public TooltipListener(String text, int delay, String styleName) {
		tooltip = new Tooltip(text, delay, styleName);
	}

	public void onMouseEnter(Widget sender) {
		widgetLaunching = sender;
		setTooltipContents(getTooltipContents());
		tooltip.show();
	}

	public void onMouseLeave(Widget sender) {
		tooltip.hide();
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public void setTooltipContents(String html) {
		tooltip.setContents(html);
	}
	
	public String getTooltipContents() {
		return tooltip.contents.getHTML();
	}

}