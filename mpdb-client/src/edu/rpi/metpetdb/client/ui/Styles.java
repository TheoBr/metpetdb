package edu.rpi.metpetdb.client.ui;

/** CSS and HTML constants */
public class Styles {
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

	/* ---------------- "standard" CSS classes we reuse ---------------- */
	public static final String HEADERCELL = "headerCell";
	public static final String ADDLINK = "addlink";
	public static final String PRIMARY_BUTTON = "btnPrimary";
	public static final String SECONDARY_BUTTON = "btnSecondary";

	/* ---------------- bad classes; these should go away ---------------- */
	public static final String DATATABLE = "mpdb-dataTable";
	public static final String PAGETABLE = "mpdb-pageTable";
	public static final String OBJECTDETAIL = "mpdb-objectDetail";

	/* ---------------- our private CSS classes ---------------- */
	public static final String SUBMIT = "submit";
	public static final String SHOWMODE = "showMode";
	public static final String EDITMODE = "editMode";
	public static final String REQUIRED_FIELD = "req";
	public static final String INVALID_FIELD = "invalid";
	public static final String INVALID_REQUIRED_FIELD = "invalidreq";
	public static final String INVALID_FIELD_ERR = "invalid-msg";

	/* ---------------- images created by GWT ---------------- */
	public static final String ICON_WARNING = "images/iconWarning.gif";

	/* ---------------- Sample Styles ---------------- */
	public static final String ENTER_SAMPLE = "page-EnterSample";
	public static final String SAMPLE_ACTIONS = "sd-actions";
	public static final String SAMPLE_DETAILS = "sd-details";
	public static final String SAMPLE_SUBSAMPLES = "sd-subsamples";

	/* ---------------- Image Styles ---------------- */
	public static final String ADD_IMAGE_DIALOG = "mpdb-addImageDialog";

	/* ---------------- Utility Styles ---------------- */
	public static final String HIDE = "hide";
	public static final String SHOW_BLOCK = "block";
	public static final String SHOW_INLINE = "inline";
	public static final String INVISIBLE = "invisible";
	public static final String VISIBLE = "visible";
	public static final String EVEN = "even";
	public static final String ODD = "odd";

	public static final String FIRST = "first";
	public static final String BREADCRUMBS = "bcrumbs";

	private Styles() {
	}
}
