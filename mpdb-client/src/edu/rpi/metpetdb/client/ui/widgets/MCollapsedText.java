package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.Label;


public class MCollapsedText extends Label {
	private String textFull;
	
	public MCollapsedText(String text){
		this(text, 20000, 15);
	}
	
	public MCollapsedText(String text, String tooltipData){
		this(text, tooltipData, 20000, 15);
	}

	public MCollapsedText(String textFull, int timeout, int truncLength) {
		String textTruncated = textFull;
		if(textTruncated.length() > truncLength){
			addMouseListener(new TooltipListener(textFull, timeout, "dialogBox-content"));
			textTruncated = textTruncated.substring(0, truncLength - 3);
			textTruncated += "...";
		}
		setText(textTruncated);
	}
	
	public MCollapsedText(String textFull, String tooltipData, int timeout, int truncLength){
		String textTruncated = textFull;
		if(textTruncated.length() > truncLength){
			addMouseListener(new TooltipListener(tooltipData, timeout, "dialogBox-content"));
			textTruncated = textTruncated.substring(0, truncLength - 3);
			textTruncated += "...";
		}
		setText(textTruncated);
	}
	
	public String getText(){
		return textFull;
	}
	
}