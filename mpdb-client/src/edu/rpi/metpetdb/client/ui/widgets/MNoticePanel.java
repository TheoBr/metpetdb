package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.SimplePanel;

import edu.rpi.metpetdb.client.ui.CSS;

/**
 * Panel for inline notice messages, i.e. "File uploaded successfully"
 * 
 * @author zak
 * 
 */
public class MNoticePanel extends SimplePanel {

	public enum NoticeType {
		GENERIC, WORKING, SUCCESS, ERROR, WARNING
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

	public void hide() {
		addStyleName(CSS.HIDE);
	}

	public void show() {
		removeStyleName(CSS.HIDE);
		setStyleName(CSS.NOTICE_PANEL);
	}

}
