package edu.rpi.metpetdb.client.ui.objects.details;

import java.util.List;

import org.postgis.Point;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.EarthInstanceHandler;
import com.google.gwt.maps.client.event.MapTypeChangedHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.LoggedInServerOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.image.AddImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.CountryAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.LocationAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.MetamorphicGradeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.MineralAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.ReferenceAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.RegionAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.SubsampleListEx;
import edu.rpi.metpetdb.client.ui.user.UsesCurrentUser;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarth;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MNoticePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MNoticePanel.NoticeType;

public class SampleDetails extends MPagePanel implements UsesCurrentUser{
	private LatLng samplePosition;
	private MapWidget map;
	private MTwoColPanel panel = new MTwoColPanel();
	private boolean geInit = false;
	private FlowPanel commentsContainer = new FlowPanel();
	private TextArea commentBox;
	private MNoticePanel commentNotice;
	private Label empty = new Label("No comments yet.");
	private HTMLPanel commentPanel;

	private static GenericAttribute[] sampleAtts = {
			new AddImageAttribute<Sample>(MpDb.doc.Sample_images),
			new TextAttribute(MpDb.doc.Sample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Sample_sesarNumber).setImmutable(true),
			new TextAttribute(MpDb.doc.Sample_number),
			new DateAttribute(MpDb.doc.Sample_collectionDate,
					MpDb.doc.Sample_datePrecision),
			new ListboxAttribute(MpDb.doc.Sample_rockType),
			new RadioButtonAttribute(MpDb.doc.Sample_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new LocationAttribute(MpDb.doc.Sample_location),
			new TextAttribute(MpDb.doc.Sample_locationError),
			new CountryAttribute(MpDb.doc.Sample_country),
			new TextAttribute(MpDb.doc.Sample_description),
			new TextAttribute(MpDb.doc.Sample_collector),
			new TextAttribute(MpDb.doc.Sample_locationText),
			new MineralAttribute(MpDb.doc.Sample_minerals),
			new RegionAttribute(MpDb.doc.Sample_regions),
			new MetamorphicGradeAttribute(MpDb.doc.Sample_metamorphicGrades),
			new ReferenceAttribute(MpDb.doc.Sample_references),
			// new CommentAttribute(MpDb.doc.Sample_comments),
			new TextAttribute(MpDb.doc.Sample_subsampleCount).setReadOnly(true)
	};

	private final ObjectEditorPanel<Sample> p_sample;
	private long sampleId;
	private Sample sample;

	public SampleDetails() {
		p_sample = new ObjectEditorPanel<Sample>(sampleAtts,
				LocaleHandler.lc_text.addSample(), LocaleHandler.lc_text
						.addSampleDescription()) {
			protected void loadBean(final AsyncCallback<Sample> ac) {
				final Sample s = (Sample) getBean();
				MpDb.sample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: sampleId, ac);
			}

			protected void saveBean(final AsyncCallback<Sample> ac) {
				MpDb.sample_svc.save((Sample) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback<Object> ac) {
				MpDb.sample_svc.delete(((Sample) getBean()).getId(), ac);
			}

			protected boolean canEdit() {
				// TODO temporary while testing permissions
				// final Sample s = (Sample) getBean();
				// if (s.isPublicData())
				// return false;
				// if (MpDb.isCurrentUser(s.getOwner()))
				// return true;
				// return false;
				return true;
			}

			protected void onSaveCompletion(final Sample result) {
				if (History.getToken().equals(
						TokenSpace.detailsOf((Sample) result))) {
					TokenSpace.dispatch(TokenSpace.detailsOf((Sample) result));
				} else {
					History.newItem(TokenSpace.detailsOf((Sample) result));
				}
			}

			protected void onLoadCompletion(final Sample result) {
				super.onLoadCompletion(result);
				sample = result;
				populateComments();
				final String title;
				if (result.getNumber() != null
						|| !result.getNumber().equals(""))
					title = result.getNumber();
				else
					title = "<span class=\"" + CSS.IGSN_LABEL
							+ "\">IGSN</span> " + result.getSesarNumber();
				setPageTitle(title, LocaleHandler.lc_text.sample());
				samplePosition = LatLng.newInstance(((Point) result
						.getLocation()).y, ((Point) result.getLocation()).x);
				updateGoogleMaps();
			}

			protected void onDeleteCompletion(final Object result) {
				History.newItem(TokenSpace.samplesForUser.makeToken(null));
			}
		};
		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_sample);
		try {
			map = new MapWidget();
		} catch (Exception e) {
			//ignore any expections from google about the map
		}
		oep.setStylePrimaryName("sd-details");
		oep.addStyleName("mpdb-dataTable");
		oep.addStyleName("inline");
		panel.getLeftCol().add(oep);
		add(panel);
		setStyleName(CSS.PAGE_SAMPLE_DETAILS);
	}
	private void addExtraElements() {

		final FlexTable subsamples_ft = new FlexTable();

		final MLink addSubsample = new MLink(LocaleHandler.lc_text
				.addSubsample(), new ClickListener() {
			public void onClick(final Widget sender) {
				new LoggedInServerOp<Subsample>() {
					@Override
					public void command() {
						History.newItem(TokenSpace.createNewSubsample(p_sample
								.getBean()));
					}
				}.begin();
			}
		});
		addSubsample.addStyleName(CSS.ADDLINK);
		subsamples_ft.setWidget(0, 1, addSubsample);

		Label Subsamples_label = new Label(LocaleHandler.lc_text.subsamples());;
		subsamples_ft.setWidget(0, 0, Subsamples_label);

		final SubsampleListEx list = new SubsampleListEx(LocaleHandler.lc_text
				.noSubsamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Subsample>> ac) {
				MpDb.subsample_svc.all(p, sampleId, ac);
			}
		};

		subsamples_ft.setWidget(1, 0, list);
		subsamples_ft.getFlexCellFormatter().setColSpan(1, 0, 2);

		// format to look pretty
		subsamples_ft.setWidth("100%");
		Subsamples_label.addStyleName("bold");
		subsamples_ft.getFlexCellFormatter().setWidth(0, 0, "50%");
		subsamples_ft.getFlexCellFormatter().setWidth(0, 1, "50%");
		subsamples_ft.getFlexCellFormatter().setHeight(0, 0, "35px");
		subsamples_ft.getRowFormatter().setStyleName(0,
				"mpdb-dataTableLightBlue");
		subsamples_ft.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		subsamples_ft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		subsamples_ft.addStyleName("mpdb-dataTable");
		this.add(subsamples_ft);

	}
	
	public void onCurrentUserChanged(final User u){
		if (commentPanel.getParent() != null){
			remove(commentPanel);
		}
		addCommentsModule();
		populateComments();
	}
	
	private void addCommentsModule() {
		commentPanel = new HTMLPanel(
				"<h2>"+LocaleHandler.lc_text.comments()+"</h2>" +
				"<div id=\"comments-notice\"></div>" +
				"<div id=\"comments-container\"></div>" +
				"<div class=\"comment-form\">" +
				"<h4>Add Comment</h4>" +
				"<span id=\"comment-form-textarea\"></span>" +
				"<span id=\"comment-form-submit\"></span>" +
				"<span id=\"comment-form-clear\"></span>" +
				"</div>"
		);
		commentBox = new TextArea();
		Button submit = new Button(LocaleHandler.lc_text.buttonSubmit());
		submit.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				final SampleComment comment = new SampleComment();
				comment.setOwner(MpDb.currentUser());
				comment.setText(commentBox.getText());
				comment.setSample(sample);
				addNewComment(comment);
			}
		});
		final MLink clear = new MLink("Clear", new ClickListener(){
			public void onClick(Widget sender){
				commentBox.setText("");
			}
		});
		if (!MpDb.isLoggedIn()){
			commentBox.setText("You must be logged in to add comments");
			commentBox.setEnabled(false);
			clear.setVisible(false);
			submit.setEnabled(false);
		} else if (!MpDb.currentUser().getEnabled()){
			commentBox.setText("You must activate your account to add comments");
			commentBox.setEnabled(false);
			clear.setVisible(false);
			submit.setEnabled(false);
		}
		
		commentNotice = new MNoticePanel();
		commentsContainer.setStyleName("comment-list");
		commentPanel.addAndReplaceElement(commentNotice, "comments-notice");
		commentPanel.addAndReplaceElement(commentsContainer, "comments-container");
		commentPanel.addAndReplaceElement(commentBox, "comment-form-textarea");
		commentPanel.addAndReplaceElement(submit, "comment-form-submit");
		commentPanel.addAndReplaceElement(clear, "comment-form-clear");
		commentPanel.setStyleName("comments");
		add(commentPanel);
	}
	
	private void addNewComment(final SampleComment comment) {
		new ServerOp<SampleComment>() {
			public void begin() {				
				MpDb.sampleComment_svc.save(comment, this);
			}

			public void onSuccess(SampleComment result) {
				if (empty.getParent() != null){
					commentsContainer.remove(empty);
				}
				commentsContainer.add(new SampleCommentPanel(result,true));
				commentBox.setText("");
				commentNotice.hide();
			}
		}.begin();
	}

	private void populateComments() {
		new ServerOp<List<SampleComment>>() {
			public void begin() {
				MpDb.sampleComment_svc.all(sampleId, this);
			}

			public void onSuccess(final List<SampleComment> result) {
				new ServerOp<Long>(){
					public void begin(){
						MpDb.mpdbGeneric_svc.getCurrentTime(this);
					}
					
					public void onSuccess(Long now){
						commentsContainer.clear();
						commentNotice.hide();
						if (result.isEmpty()) {				
							empty.setStyleName(CSS.NULLSET);
							commentsContainer.add(empty);
						} else {
							for (SampleComment sc : result){
								final boolean isEditable = now<(sc.getDateAdded().getTime()+SampleComment.EDIT_TIMER);
								commentsContainer.add(new SampleCommentPanel(sc, isEditable));
							}
						}
					}
				}.begin();
			}
			
			public void onFailure(final Throwable e) {
				super.onFailure(e);
				commentNotice.sendNotice(NoticeType.ERROR, "Sorry, could not load comments at this time.");
			}
		}.begin();
	}
	
	protected class SampleCommentPanel extends FlowPanel implements ClickListener {
		private SampleComment comment;
		
		private HTML authorLine = new HTML();
		private Image trash = new Image(GWT.getModuleBaseURL() + "/images/icon-trash-small.png");
		private HTML body = new HTML();
		
		private MLink editLink = new MLink("Edit");
		private FlowPanel editPanel = new FlowPanel();
		private TextArea editTextArea = new TextArea();
		private Button editSubmit = new Button(LocaleHandler.lc_text.buttonSubmit());
		private MLink editCancel = new MLink(LocaleHandler.lc_text.buttonCancel());
		
		private boolean isCommentOwner;
		private boolean isSampleOwnerComment;
		private boolean canDelete;
		
		protected SampleCommentPanel(final SampleComment sc, final boolean isEditable) {
			this.comment = sc;
			isCommentOwner = MpDb.isLoggedIn() && MpDb.currentUser().getId() == sc.getOwner().getId();
			isSampleOwnerComment = sc.getOwner().getId() == sample.getOwner().getId();
			canDelete = MpDb.isLoggedIn() && (isCommentOwner || MpDb.currentUser().getId() == sample.getOwner().getId());
			setStyleName("comment");
			
			//if the sample comment isn't loaded from the database then
			//sc.getOwnerName will be null
			final String ownerName;
			if (sc.getOwnerName() == null) {
				ownerName = sc.getOwner() == null ? "" : sc.getOwner().getName();
			} else {
				ownerName = sc.getOwnerName();
			}
			if (isSampleOwnerComment) {
				authorLine.setHTML("<strong>" + ownerName + "</strong>" +
						" <span>" + sc.getDateAddedDisplay() + "</span>");
			} else {
				authorLine.setHTML(ownerName +
						" <span>" + sc.getDateAddedDisplay() + "</span>");
			}
			authorLine.setStyleName("author");
			add(authorLine);
			
			body.setHTML(sc.getText());
			body.setStyleName("body");
			body.setVisible(true);
			add(body);
			
			if (isCommentOwner && isEditable) {
				editLink.addClickListener(this);
				editLink.setVisible(true);
				editLink.setStyleName("edit");
				add(editLink);
				
				editPanel.add(editTextArea);
				
				editSubmit.addClickListener(this);
				editPanel.add(editSubmit);
				
				editCancel.addClickListener(this);
				editPanel.add(editCancel);
				
				editPanel.setStyleName("edit-panel");
				editPanel.setVisible(false);
				add(editPanel);
			}
			
			if (canDelete) {
				trash.setStyleName("trash");
				trash.addClickListener(this);
				add(trash);
			}
			
		}
		
		private void editMode(boolean edit) {
			body.setVisible(!edit);
			editLink.setVisible(!edit);
			editPanel.setVisible(edit);
		}
		
		public void onClick(Widget sender){
			if (sender == trash) {
				new ServerOp<Object>() {
					public void begin() {
						MpDb.sampleComment_svc.delete(comment.getId(), this);
					}
	
					public void onSuccess(Object result) {
						commentsContainer.remove(trash.getParent());
						if(sample.getComments().size()==0){
							commentsContainer.add(empty);
						}
					}
				}.begin();
			} else if (sender == editSubmit) {
				new ServerOp<SampleComment>() {
					public void begin() {
						comment.setText(editTextArea.getText());
						MpDb.sampleComment_svc.save(comment, this);
					}

					public void onSuccess(SampleComment result) {
						body.setHTML(result.getText());
						editMode(false);
					}
				}.begin();
			} else if (sender == editLink) {
				editTextArea.setText(comment.getText());
				editMode(true);
			} else if (sender == editCancel) {
				editMode(false);
			}
		}
	}

	private void addGoogleMaps() {
		map.setZoomLevel(4);
		map.addControl(new LargeMapControl());
		map.addControl(new MapTypeControl());
		map.addControl(new ScaleControl());
		map.addMapType(MapType.getEarthMap());
		map.setStyleName(CSS.SD_GOOGLE_MAP);
		panel.getRightCol().add(map);
	}

	private void updateGoogleMaps() {
		final Marker sampleMarker = new Marker(samplePosition);
		sampleMarker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent sender) {
				map.getInfoWindow().open(
						sampleMarker,
						new InfoWindowContent("Lat: "
								+ samplePosition.getLatitude() + " Lng: "
								+ samplePosition.getLongitude()));
			}
		});

		map.addOverlay(sampleMarker);
		map.setCenter(samplePosition);

		map.addMapTypeChangedHandler(new MapTypeChangedHandler() {
			public void onTypeChanged(final MapTypeChangedEvent e) {
				map.getEarthInstance(new EarthInstanceHandler() {
					public void onEarthInstance(final EarthInstanceEvent e) {
						if (!geInit) {
							MGoogleEarth.addControls(e);
							geInit = true;
						}
					}
				});
			}
		});
	}

	public SampleDetails showById(final long id) {
		sampleId = id;
		p_sample.load();
		addExtraElements();
		addCommentsModule();
		addGoogleMaps();
		return this;
	}

	public SampleDetails createNew() {
		final Sample s = new Sample();
		s.setOwner(MpDb.currentUser());
		p_sample.edit(s);
		return this;
	}
}
