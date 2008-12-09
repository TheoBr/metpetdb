package edu.rpi.metpetdb.client.ui.objects.details;

import java.util.List;

import org.postgis.Point;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.CommentAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.CountryAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.LocationAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MetamorphicGradeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MineralAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ReferenceAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.RegionAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.SubsampleListEx;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;

public class SampleDetails extends MPagePanel {
	private LatLng samplePosition;
	private MapWidget map;
	private MTwoColPanel panel = new MTwoColPanel();

	private static GenericAttribute[] sampleAtts = {
			new TextAttribute(MpDb.doc.Sample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Sample_sesarNumber).setImmutable(true),
			new TextAttribute(MpDb.doc.Sample_alias),
			new DateAttribute(MpDb.doc.Sample_collectionDate,
					MpDb.doc.Sample_datePrecision),
			new ListboxAttribute(MpDb.doc.Sample_rockType),
			new RadioButtonAttribute(MpDb.doc.Sample_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new LocationAttribute(MpDb.doc.Sample_location),
			new TextAttribute(MpDb.doc.Sample_latitudeError),
			new TextAttribute(MpDb.doc.Sample_longitudeError),
			new CountryAttribute(MpDb.doc.Sample_country),
			new TextAttribute(MpDb.doc.Sample_description),
			new TextAttribute(MpDb.doc.Sample_collector),
			new TextAttribute(MpDb.doc.Sample_locationText),
			new MineralAttribute(MpDb.doc.Sample_minerals),
			new RegionAttribute(MpDb.doc.Sample_regions),
			new MetamorphicGradeAttribute(MpDb.doc.Sample_metamorphicGrades),
			new ReferenceAttribute(MpDb.doc.Sample_references),
	//		new CommentAttribute(MpDb.doc.Sample_comments),
			new TextAttribute(MpDb.doc.Sample_subsampleCount).setReadOnly(true),
	};

	private final ObjectEditorPanel<Sample> p_sample;
	private long sampleId;

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
				//TODO temporary while testing permissions
//				final Sample s = (Sample) getBean();
//				if (s.isPublicData())
//					return false;
//				if (MpDb.isCurrentUser(s.getOwner()))
//					return true;
//				return false;
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
				final String title;
				if (result.getAlias() != null || !result.getAlias().equals(""))
					title = result.getAlias();
				else
					title = "<span class=\""+CSS.IGSN_LABEL+"\">IGSN</span> " + result.getSesarNumber();
				setPageTitle(title, LocaleHandler.lc_text.sample());
				samplePosition = LatLng.newInstance(((Point) result.getLocation()).y,
						((Point) result.getLocation()).x);
				updateGoogleMaps();
			}

			protected void onDeleteCompletion(final Object result) {
				History.newItem(TokenSpace.samplesForUser.makeToken(null));
			}
		};
		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_sample);
		map = new MapWidget();
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
				new ServerOp<Object>() {
					public void begin() {
						if (MpDb.isLoggedIn())
							History.newItem(TokenSpace
									.createNewSubsample((Sample) p_sample
											.getBean()));
						else
							onFailure(new LoginRequiredException());
					}

					public void onSuccess(Object result) {
						History.newItem(TokenSpace
								.detailsOf(((Subsample) result).getSample()));
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

	public void addComments() {
		final FlexTable comments_ft = new FlexTable();
		Label comments_label = new Label(LocaleHandler.lc_text.comments());
		comments_ft.setWidget(0, 0, comments_label);
		
		final FlowPanel fp = new FlowPanel();
		comments_ft.setWidget(1,0,fp);
		
//		new ServerOp<List<SampleComment>>() {
//			public void begin() {
//				MpDb.sampleComment_svc.all(sampleId, this);
//			}
//
//			public void onSuccess(List<SampleComment> result) {
//				for (SampleComment sc : result)
//					fp.add(new Label(sc.getText()));
//			}
//		}.begin();

		// format to look pretty
		comments_ft.setWidth("100%");
		comments_label.addStyleName("bold");
		comments_ft.getFlexCellFormatter().setWidth(0, 0, "100%");
		comments_ft.getFlexCellFormatter().setHeight(0, 0, "35px");
		comments_ft.getRowFormatter()
				.setStyleName(0, "mpdb-dataTableLightBlue");
		comments_ft.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);

		this.add(comments_ft);
	}

	private void addGoogleMaps() {
		map.setZoomLevel(4);
		map.addControl(new LargeMapControl());
		map.addControl(new MapTypeControl());
		map.addControl(new ScaleControl());
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
	}

	public SampleDetails showById(final long id) {
		sampleId = id;
		p_sample.load();
		addExtraElements();
		addComments();
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
