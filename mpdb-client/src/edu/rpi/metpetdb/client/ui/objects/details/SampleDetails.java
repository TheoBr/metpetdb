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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.CommentAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.LocationAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MetamorphicGradeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MineralAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ReferenceAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.RegionAttribute;
import edu.rpi.metpetdb.client.ui.left.side.MySubsamples;
import edu.rpi.metpetdb.client.ui.objects.list.SubsampleListEx;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class SampleDetails extends MPagePanel {
	private String sampleHeader;
	private MySubsamples subsamplesLeft;
	private LatLng samplePosition;
	private double latError;
	private double lngError;

	private static GenericAttribute[] sampleAtts = {
			new TextAttribute(MpDb.doc.Sample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Sample_sesarNumber).setImmutable(true),
			new TextAttribute(MpDb.doc.Sample_alias),
			new DateAttribute(MpDb.doc.Sample_collectionDate,
					MpDb.doc.Sample_datePrecision),
			new ListboxAttribute(MpDb.doc.Sample_rockType),
			new RadioButtonAttribute(MpDb.doc.Sample_publicData),
			new LocationAttribute(MpDb.doc.Sample_location),
			new TextAttribute(MpDb.doc.Sample_latitudeError),
			new TextAttribute(MpDb.doc.Sample_longitudeError),
			new TextAttribute(MpDb.doc.Sample_country),
			new TextAttribute(MpDb.doc.Sample_description),
			new TextAttribute(MpDb.doc.Sample_collector),
			new TextAttribute(MpDb.doc.Sample_locationText),
			new MineralAttribute(MpDb.doc.Sample_minerals),
			new RegionAttribute(MpDb.doc.Sample_regions),
			new MetamorphicGradeAttribute(MpDb.doc.Sample_metamorphicGrades),
			new ReferenceAttribute(MpDb.doc.Sample_references),
			new CommentAttribute(MpDb.doc.Sample_comments),
			new TextAttribute(MpDb.oc.Sample_subsampleCount).setReadOnly(true),
	};

	private final ObjectEditorPanel<SampleDTO> p_sample;
	private long sampleId;

	public SampleDetails() {
		p_sample = new ObjectEditorPanel<SampleDTO>(sampleAtts,
				LocaleHandler.lc_text.addSample(), LocaleHandler.lc_text
						.addSampleDescription()) {
			protected void loadBean(final AsyncCallback<SampleDTO> ac) {
				final SampleDTO s = (SampleDTO) getBean();
				MpDb.sample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: sampleId, ac);
			}

			protected void saveBean(final AsyncCallback<SampleDTO> ac) {
				MpDb.sample_svc.save((SampleDTO) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback<Object> ac) {
				MpDb.sample_svc.delete(((SampleDTO) getBean()).getId(), ac);
			}

			protected boolean canEdit() {
				final SampleDTO s = (SampleDTO) getBean();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}

			protected void onSaveCompletion(final MObjectDTO result) {
				super.onSaveCompletion(result);
			}

			protected void onLoadCompletion(final MObjectDTO result) {
				super.onLoadCompletion(result);
				final SampleDTO s = (SampleDTO) result;
				sampleHeader = LocaleHandler.lc_text.sample() + " "
						+ s.getName();

				samplePosition = new LatLng(((Point) s.getLocation()).x,
						((Point) s.getLocation()).y);
				latError = s.getLatitudeError();
				lngError = s.getLongitudeError();
				addGoogleMaps();
			}

			protected boolean onFailure2(final Throwable e) {
				Window.alert(e.getMessage());
				return true;
			}

		};
		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_sample);

		setPageHeader(sampleHeader);
		oep.setStylePrimaryName("sd-details");
		oep.addStyleName("mpdb-dataTable");
		add(oep);
	}
	private void addExtraElements() {

		final FlexTable subsamples_ft = new FlexTable();

		final MLink addSubsample = new MLink(LocaleHandler.lc_text
				.addSubsample(), new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp<Object>() {
					public void begin() {
						if (MpDb.isLoggedIn())
							MetPetDBApplication.show(new SubsampleDetails()
									.createNew((SampleDTO) p_sample.getBean(),
											null));
						else
							onFailure(new LoginRequiredException());
					}

					public void onSuccess(Object result) {
					}
				}.begin();
			}
		});
		addSubsample.addStyleName(Styles.ADDLINK);
		subsamples_ft.setWidget(0, 1, addSubsample);

		Label Subsamples_label = new Label(LocaleHandler.lc_text.subsamples());;
		subsamples_ft.setWidget(0, 0, Subsamples_label);

		final SubsampleListEx list = new SubsampleListEx() {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<SubsampleDTO>> ac) {
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

	public void addSubsamplesToLeft() {

		new ServerOp() {
			@Override
			public void begin() {
				MpDb.subsample_svc.all(sampleId, this);
			}
			public void onSuccess(Object result) {
				MetPetDBApplication.clearLeftSide();
				if (((List<SubsampleDTO>) result).size() > 0) {
					subsamplesLeft = new MySubsamples(
							(List<SubsampleDTO>) result);
					MetPetDBApplication.appendToLeft(subsamplesLeft);
				} else {
					new ServerOp() {
						@Override
						public void begin() {
							MpDb.sample_svc.details(sampleId, this);
						}
						public void onSuccess(Object result2) {
							subsamplesLeft = new MySubsamples(
									(SampleDTO) result2);
							MetPetDBApplication.appendToLeft(subsamplesLeft);
						}
					}.begin();
				}
			}
		}.begin();

	};

	private void addGoogleMaps() {
		final MapWidget map = new MapWidget(samplePosition, 4);
		map.setSize("300px", "300px");
		final Marker sampleMarker = new Marker(samplePosition);
		sampleMarker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent sender) {
				map.getInfoWindow().open(
						sampleMarker,
						new InfoWindowContent("Lat: "
								+ samplePosition.getLatitude() + " Lng:"
								+ samplePosition.getLongitude()
								+ "<br>Lat Error: " + latError + " LngError: "
								+ lngError));
			}
		});

		map.addOverlay(sampleMarker);
		map.setCenter(samplePosition);

		map.addControl(new LargeMapControl());
		map.addControl(new MapTypeControl());
		map.addControl(new ScaleControl());

		this.add(map);
	}

	public SampleDetails showById(final long id) {
		sampleId = id;
		p_sample.load();
		addExtraElements();
		addComments();
		addSubsamplesToLeft();
		return this;
	}

	public SampleDetails createNew() {
		final SampleDTO s = new SampleDTO();
		s.setOwner(MpDb.currentUser());
		p_sample.edit(s);
		return this;
	}
}
