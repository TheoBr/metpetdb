package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.MNoticePanel.NoticeType;

/**
 * Panel for inline notice messages, i.e. "File uploaded successfully"
 * 
 * @author zak
 * 
 */

public class MNotice extends FlowPanel {

	private MNoticePanel parent;
	private final SimplePanel msgContainer = new SimplePanel();
	private final MLink hide = new MLink("Hide", new ClickListener() {
		public void onClick(Widget sender) {
			if (isAttached() && getParent() == parent)
				parent.hide();
			removeFromParent();
		}
	});

	/**
	 * All notices have a message and a way to remove the messsage from view.
	 * 
	 * @param msg
	 */
	public MNotice(NoticeType type) {
		setStylePrimaryName(CSS.NOTICE);
		setType(type);

		add(hide);
		hide.addStyleName(CSS.NOTICE_HIDE);

		add(msgContainer);
		msgContainer.setStyleName(CSS.NOTICE_MESSAGE);
	}

	public MNotice() {
		this(NoticeType.GENERIC);
	}

	public void setMessage(String s) {
		setMessage(new HTML(s));
	}

	public void setMessage(Widget w) {
		msgContainer.setWidget(w);
	}

	public void setStyleName(String s) {
		this.getElement().setAttribute("class", CSS.NOTICE);
		if (s != "")
			addStyleDependentName(s);
	}

	public void setParent(MNoticePanel parent) {
		this.parent = parent;
	}

	private void setType(NoticeType type) {
		switch (type) {
		case GENERIC:
			setStyleName("");
			break;
		case SUCCESS:
			setStyleName(CSS.SUCCESS);
			break;
		case WORKING:
			setStyleName(CSS.WORKING);
			break;
		case ERROR:
			setStyleName(CSS.ERROR);
			break;
		case WARNING:
			setStyleName(CSS.WARNING);
			break;
		case ALERT:
			setStyleName(CSS.ALERT);
			break;
		default:
		}
	}

}
