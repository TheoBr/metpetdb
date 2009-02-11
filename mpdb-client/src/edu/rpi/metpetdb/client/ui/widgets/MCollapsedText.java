package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.Label;
import edu.rpi.metpetdb.client.ui.widgets.TooltipListener;


public class MCollapsedText extends Label {
	
	public MCollapsedText(String text){
		this(text, 8000, 15);
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
	
}