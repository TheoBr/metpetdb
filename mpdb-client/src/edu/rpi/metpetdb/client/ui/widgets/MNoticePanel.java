package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;

/**
 * Panel for inline notice messages, i.e. "File uploaded successfully"
 * 
 * @author zak
 *
 */
public class MNoticePanel extends FlowPanel implements ClickListener {
	
	private Widget parent;
	
	private final MLink hide = new MLink("Hide", this);
	private final SimplePanel msgContainer = new SimplePanel();
	
	/**
	 * All notices have a message and a way to remove the messsage from view.
	 * 
	 * @param msg
	 */
	public MNoticePanel(String msg) {
		setStylePrimaryName(CSS.NOTICE_PANEL);
		add(hide);
		hide.addClickListener(this);
		hide.addStyleName(CSS.NOTICE_HIDE);
		msgContainer.setStyleName(CSS.NOTICE_MESSAGE);
		add(msgContainer);
		setMessage(msg);
	}
	
	public MNoticePanel() {
		this("");
	}
	
	public void setMessage(String s) {
		setMessage(new HTML(s));
	}
	
	public void setMessage(Widget w) {
		msgContainer.setWidget(w);
	}
	
	public void setStyleName(String s) {
		this.getElement().setAttribute("class",CSS.NOTICE_PANEL);
		addStyleDependentName(s);
	}

	public void attachTo(SimplePanel container) {
		this.parent = container;
		container.clear();
		container.setWidget(this);
		container.removeStyleName(CSS.HIDE);
	}
	
	public void onClick(Widget sender) {
		if (sender == hide) {
			if (isAttached() && getParent() == parent)
				parent.addStyleName(CSS.HIDE);
			removeFromParent();
		}
	}

}