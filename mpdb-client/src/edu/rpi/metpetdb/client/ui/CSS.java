package edu.rpi.metpetdb.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/** CSS and HTML constants */
public class CSS {
	/* ---------------- ids declared by our host page ---------------- */
	public static final String LOGBAR_ID = "logbar";
	public static final String HDRNAV_ID = "hdrnav";
	public static final String CONTENT_ID = "content";
	public static final String TOOLS_ID = "tools";
	public static final String LEFTCOL_ID = "leftcol";
	public static final String NOTICE_ID = "mpdb-notice";
	public static final String LOADINGMESSAGE_ID = "loadingmessage";
	public static final String BREADCRUMBS_ID = "breadcrumbs";
	public static final String FOOTER_ID = "nsf";
	public static final String USERNAME_ID = "u";
	public static final String PASSWORD_ID = "pw";
	public static final String LOGIN_SUBMIT_ID = "submit";
	public static final String IN_PAGE_NOTICE_ID = "in-page-notice";

	/* ---------------- utility ---------------- */
	public static final String HIDE = "hide";
	public static final String SHOW_BLOCK = "block";
	public static final String SHOW_INLINE = "inline";
	public static final String INVISIBLE = "invisible";
	public static final String VISIBLE = "visible";
	public static final String BETA = "beta";
	public static final String CLEARFIX = "clearfix";
	public static final String ELEMENT_MARGIN = "element-margin";
	
	/* ---------------- typography ---------------- */
	public static final String TYPE_SMALL_CAPS = "type-small-caps";
	public static final String TYPE_LARGE_NUMBER = "type-large-number";
	public static final String IGSN_LABEL = "igsn";
	public static final String PDF_LABEL = "pdf";
	
	/* ---------------- icons ---------------- */
	public static final String ICON_WARNING = "icon-warning";

	/* ---------------- states/modes ---------------- */	
	public static final String CHECKED = "checked";
	public static final String REQUIRED = "required";
	public static final String INVALID = "invalid";
	public static final String EVEN = "even";
	public static final String ODD = "odd";
	public static final String FIRST = "first";
	public static final String CURRENT = "current";
	public static final String EMPTY = "empty";
	public static final String DISABLED = "disabled";
	
	/* ---------------- forms ---------------- */
	public static final String SUBMIT = "submit";
	public static final String SHOWMODE = "showMode";
	public static final String EDITMODE = "editMode";
	public static final String INVALID_MESSAGE = "invalid-message";
	public static final String CHECKBOX = "checkbox";

	/* ---------------- links and buttons ---------------- */
	public static final String LIGHT_LINK = "light-link";
	public static final String LINK_LARGE_ICON = "large-icon-link";
	public static final String LINK_UPLOAD = "upload-link";
	public static final String LINK_UPLOAD_MULTI = "upload-multi-link";
	public static final String LINK_ADD = "add-link";
	public static final String LINK_HELP = "help-link";
	public static final String LINK_INFO = "info-link";
	public static final String ADDLINK = "addlink"; // kill
	public static final String PRIMARY_BUTTON = "btnPrimary";
	public static final String SECONDARY_BUTTON = "btnSecondary";
	public static final String HELP = "help";
	
	/* ---------------- widgets ---------------- */
	public static final String BREADCRUMBS = "bcrumbs";
	public static final String HEADERCELL = "headerCell";
	public static final String TWO_COLUMN_PANEL = "two-cols";
	public static final String LEFT_COL = "left-col";
	public static final String RIGHT_COL = "right-col";
	public static final String PARAGRAPH = "p";
	public static final String COL_1_OF_2 = "col-1-2";
	public static final String COL_2_OF_2 = "col-2-2";
	public static final String CONTENT = "content";
	public static final String MAIN = "main";
	
	/* ---------------- notices ---------------- */
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String WORKING = "working";
	public static final String WARNING = "warning";
	public static final String ALERT = "alert";
	public static final String NOTICE = "notice";
	public static final String NOTICE_PANEL = "notice-panel";
	public static final String NOTICE_MESSAGE = "notice-message";
	public static final String NOTICE_HIDE = "notice-hide";
	public static final String NOTICE_URGENT = "notice-urgent";
	
	/* ---------------- Widget: DataTable ---------------- */
	public static final String DATATABLE = "mpdb-dataTable";
	public static final String PAGETABLE = "mpdb-pageTable";
	public static final String OBJECTDETAIL = "mpdb-objectDetail";
	public static final String DATATABLE_HEADER_FILTERS = "datatable-header-filters";
	public static final String DATATABLE_FOOTER = "datatable-footer";
	public static final String DATATABLE_FOOTER_WRAPPER = "datatable-footer-wrapper";
	public static final String DATATABLE_FOOTER_LABEL = "datatable-footer-label";
	public static final String DATATABLE_FOOTER_ITEM = "datatable-footer-item";
	public static final String DATATABLE_FOOTER_SUBITEM = "datatable-footer-subitem";
	public static final String POPUP_CUSTOM_COLS_CONTAINER = "popup-custom-cols-container";
	public static final String POPUP_CUSTOM_COLS = "popup-custom-cols";
	public static final String POPUP_CUSTOM_COLS_BOTTOM = "popup-custom-cols-bottom";

	/* ---------------- popups ---------------- */
	public static final String DIALOG_SETTINGS = "dialog-settings";

	/* ---------------- Page: Sample Details ---------------- */
	public static final String SD_GOOGLE_MAP = "sd-gmap";
	public static final String PAGE_SAMPLE_DETAILS = "p-sample-details";
	public static final String ENTER_SAMPLE = "page-EnterSample";
	public static final String SAMPLE_ACTIONS = "sd-actions";
	public static final String SAMPLE_DETAILS = "sd-details";
	public static final String SAMPLE_SUBSAMPLES = "sd-subsamples";

	/* ---------------- Page: My Samples ---------------- */
	public static final String SAMPLES_CONTAINER = "samples-container";

	/* ---------------- Page: Bulk Upload ---------------- */
	public static final String BULK_UPLOAD = "bulk-upload";
	public static final String BULK_UPLOAD_FORM = "bulk-upload-form";
	public static final String BULK_TYPES = "bulk-upload-types";
	public static final String BULK_NEXTSTEP = "bulk-next";
	public static final String BULK_RESULTS = "bulk-results";
	public static final String BULK_RESULTS_PARSED = "parsed";
	public static final String BULK_RESULTS_SSCOLNUM = "parsed-num";
	public static final String BULK_RESULTS_SSCOL = "parsed-sscol";
	public static final String BULK_RESULTS_MPDBCOL = "parsed-mpdbcol";
	public static final String BULK_RESULTS_SUMMARY = "summary";
	public static final String BULK_RESULTS_ERRORS = "errors";
	public static final String BULK_RESULTS_WARNINGS = "warnings";
	
	public static final String PROGRESSBAR_CONTAINER = "progressbar-container";
	
	/* ---------------- Page: Image Map ---------------- */
	public static final String ADD_IMAGE_DIALOG = "mpdb-addImageDialog";
	
	/* ---------------- Page: Search ---------------- */
	public static final String SEARCH = "search";
	public static final String SEARCH_TAB_OPTIONS = "search-taboptions";
	public static final String SEARCH_BUTTON = "search-button";
	public static final String SEARCH_ROCKTYPES = "search-rt";
	public static final String SEARCH_ROCKTYPES_TABLE = "search-rt-table";
	public static final String SEARCH_LABEL = "label";
	public static final String SEARCH_INPUT = "input";
	public static final String SEARCH_PROVENANCE = "search-prov";
	
	public static final String LAST_ROW = "last-row";
	public static final String ACTIONS = "actions";
	public static final String LOGIN_DIALOG = "login-dialog";
	public static final String LOGIN = "login";
	public static final String FORGOT_PASS = "forgot-pass";
	
	public static final String REGISTER = "register";
	public static final String ICON_PLUS = "icon-plus";
	public static final String ICON_MINUS = "icon-minus";
	
	
	
	
	
	
	
	
	
	

	private CSS() {}
	
	public void hide(Element e) {
		addStyleName(e, HIDE);
	}
	
	public void show(Element e) {
		removeStyleName(e, HIDE);
	}
	
	public static void hide(Widget w) {
		w.addStyleName(HIDE);
	}
	
	public static void show(Widget w) {
		w.removeStyleName(HIDE);
	}
	
	// Methods for getting, setting, adding, and removing styleNames from Elements. 
	// Ripped from com.google.gwt.user.client.ui.UIObject
	
	public static void setStyleName(Element elem, String styleName) {
		DOM.setElementProperty(elem.<com.google.gwt.user.client.Element> cast(), "className", styleName);
	}
	
	public static void addStyleName(Element elem, String style) {
		setStyleName(elem, style, true);
	}
	
	public void removeStyleName(Element elem, String style) {
		setStyleName(elem, style, false);
	}
	
	protected static void setStyleName(Element elem, String style, boolean add) {
	    if (elem == null) return;

	    style = style.trim();
	    if (style.length() == 0) return;

	    String oldStyle = getStyleName(elem);
	    int idx = oldStyle.indexOf(style);

	    while (idx != -1) {
	    	if (idx == 0 || oldStyle.charAt(idx - 1) == ' ') {
	    		int last = idx + style.length();
	    		int lastPos = oldStyle.length();
	    		if ((last == lastPos) || ((last < lastPos) && (oldStyle.charAt(last) == ' '))) break;
	    	}
	    	idx = oldStyle.indexOf(style, idx + 1);
	    }

	    if (add) {
	    	if (idx == -1) {
	    		if (oldStyle.length() > 0) oldStyle += " ";
	    		DOM.setElementProperty(elem.<com.google.gwt.user.client.Element> cast(), "className", oldStyle + style);
	    	}
	    } else {
	    	if (idx != -1) {
	    		String begin = oldStyle.substring(0, idx).trim();
	    		String end = oldStyle.substring(idx + style.length()).trim();

	    		String newClassName;
	    		if (begin.length() == 0) newClassName = end;
	    		else if (end.length() == 0) newClassName = begin;
	    		else newClassName = begin + " " + end;

	    		DOM.setElementProperty(elem.<com.google.gwt.user.client.Element> cast(), "className", newClassName);
	    	}
	    }
	}
	
	public static String getStyleName(Element elem) {
		return DOM.getElementProperty(elem.<com.google.gwt.user.client.Element> cast(), "className");
	}
}
