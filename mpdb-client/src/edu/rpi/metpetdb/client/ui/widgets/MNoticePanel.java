package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;

/**
 * Panel for inline notice messages, i.e. "File uploaded successfully"
 * 
 * @author zak
 * 
 */
public class MNoticePanel extends SimplePanel {

	public enum NoticeType {
		GENERIC, WORKING, SUCCESS, ERROR, WARNING, ALERT
	}

	private MNotice notice;

	public MNoticePanel() {
		super();
		setStyleName(CSS.NOTICE_PANEL);
		hide();
	}

	public void sendNotice(NoticeType type, String msg) {
		notice = new MNotice(type);
		notice.setMessage(msg);
		notice.setParent(this);
		setWidget(notice);
		show();
	}

	public void sendNotice(String msg) {
		sendNotice(NoticeType.GENERIC, msg);
	}
	
	public void sendNotice(NoticeType type, Widget w) {
		notice = new MNotice(type);
		notice.setMessage(w);
		notice.setParent(this);
		setWidget(notice);
		show();
	}

	public void sendNotice(Widget w) {
		sendNotice(NoticeType.GENERIC, w);
	}

	public void hide() {
		addStyleName(CSS.HIDE);
	}

	public void show() {
		removeStyleName(CSS.HIDE);
		setStyleName(CSS.NOTICE_PANEL);
	}

}
