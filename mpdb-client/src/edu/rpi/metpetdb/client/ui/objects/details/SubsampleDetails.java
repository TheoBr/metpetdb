package edu.rpi.metpetdb.client.ui.objects.details;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
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
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.HyperlinkAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AddImageAttribute;
import edu.rpi.metpetdb.client.ui.left.side.MySubsamples;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisListEx;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MLinkandText;

public class SubsampleDetails extends FlowPanel {
	private FlexTable ft;
	private MLinkandText Header;
	final Element sampleHeader;

	private static GenericAttribute[] subsampleAtts = {
			new HyperlinkAttribute(MpDb.oc.Subsample_sampleName)
					.setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_name),
			new ListboxAttribute(MpDb.doc.Subsample_type),
			new AddImageAttribute(MpDb.doc.Subsample_images),
			new TextAttribute(MpDb.oc.Subsample_imageCount).setReadOnly(true),
			new TextAttribute(MpDb.oc.Subsample_analysisCount)
					.setReadOnly(true),
	};

	private final ObjectEditorPanel<SubsampleDTO> p_subsample;
	private long subsampleId;
	private long sampleId;
	private ServerOp continuation;
	private String sampleAlias;
	private MLink map;

	public SubsampleDetails() {
		final SubsampleDetails me = this;
		p_subsample = new ObjectEditorPanel<SubsampleDTO>(subsampleAtts,
				LocaleHandler.lc_text.addSubsample(), LocaleHandler.lc_text
						.addSubsampleDescription(sampleAlias)) {
			protected void loadBean(final AsyncCallback<SubsampleDTO> ac) {
				final SubsampleDTO s = (SubsampleDTO) getBean();
				MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: subsampleId, ac);
			}

			protected void saveBean(final AsyncCallback<SubsampleDTO> ac) {
				MpDb.subsample_svc.save((SubsampleDTO) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback<Object> ac) {
				MpDb.subsample_svc.delete(((SubsampleDTO) getBean()).getId(),
						ac);
			}

			protected boolean canEdit() {
				final SampleDTO s = ((SubsampleDTO) getBean()).getSample();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}

			protected void onSaveCompletion(final MObjectDTO result) {
				if (continuation != null) {
					continuation.onSuccess((MObjectDTO) result);
				} else
					this.show((MObjectDTO) result);
				addSubsamplesToLeft();
			}

			protected void onLoadCompletion(final MObjectDTO result) {
				super.onLoadCompletion(result);
				final SubsampleDTO s = (SubsampleDTO) result;
				if (s.getGrid() == null) {
					map.setText("Create Map");
					map.addClickListener(new ClickListener() {
						public void onClick(final Widget sender) {
							MetPetDBApplication.show(new ImageBrowserDetails()
									.createNew(s.getId()));
						}
					});
				} else {
					map.setText("View Map");
					map
							.setTargetHistoryToken(TokenSpace.detailsOf(s
									.getGrid()));
				}

				DOM.setInnerText(sampleHeader, "Subsample " + s.getName());
				sampleId = s.getSample().getId();
			}
		};

		sampleHeader = DOM.createElement("h2");
		DOM.appendChild(this.getElement(), sampleHeader);

		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_subsample);
		ft = new FlexTable();
		Label details_label = new Label("Attributes");
		details_label.addStyleName("bold");
		map = new MLink();
		ft.setWidget(0, 0, details_label);
		ft.setWidget(0, 1, map);
		ft.setWidget(1, 0, oep);
		ft.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setWidth("40%");
		ft.getFlexCellFormatter().setHeight(0, 0, "35px");
		ft.getFlexCellFormatter().setWidth(0, 1, "100px");
		ft.getFlexCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getRowFormatter().setStyleName(0, "mpdb-dataTableLightBlue");

		add(ft);
	}

	public void showChemicalAnalysis() {
		FlexTable chemft = new FlexTable();
		final ChemicalAnalysisListEx list = new ChemicalAnalysisListEx(
				LocaleHandler.lc_text.noChemicalAnalysesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<ChemicalAnalysisDTO>> ac) {
				MpDb.chemicalAnalysis_svc.all(p, subsampleId, ac);
			}
		};
		chemft.setWidget(1, 0, list);
		chemft.getFlexCellFormatter().setColSpan(1, 0, 10);

		final MLink addChemicalAnalysis = new MLink(LocaleHandler.lc_text
				.addChemicalAnalysis(), new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							History
									.newItem(TokenSpace
											.createNewChemicalAnalysis((SubsampleDTO) p_subsample
													.getBean()));
						else
							onFailure(new LoginRequiredException());
					}

					public void onSuccess(Object result) {
					}
				}.begin();
			}
		});
		addChemicalAnalysis.addStyleName(Styles.ADDLINK);
		final Label chemAnalysis = new Label("Chemical Analysis");
		chemAnalysis.getElement().setClassName("h2");
		chemft.setWidget(0, 0, chemAnalysis);
		chemft.setWidget(0, 1, addChemicalAnalysis);
		chemft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		chemft.getRowFormatter().setStyleName(0, "mpdb-dataTableLightBlue");
		chemft.setWidth("100%");
		add(chemft);
	}

	public SubsampleDetails showById(final long id) {
		subsampleId = id;
		p_subsample.load();
		showChemicalAnalysis();
		return this;
	}

	public SubsampleDetails createNew(final SampleDTO s, final ServerOp r) {
		continuation = r;
		SubsampleDTO ss = new SubsampleDTO();
		s.addSubsample(ss);
		p_subsample.edit(ss);
		sampleAlias = s.getAlias();
		sampleId = s.getId();
		return this;
	}

	public SubsampleDetails edit(final long id) {
		subsampleId = id;
		new ServerOp() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					final SubsampleDTO s = (SubsampleDTO) p_subsample.getBean();
					MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s
							.getId() : subsampleId, this);
				} else {
					onFailure(new LoginRequiredException());
				}
			}

			public void onSuccess(final Object result) {
				p_subsample.edit((SubsampleDTO) result);
			}

			public void cancel() {
				p_subsample.load();
			}
		}.begin();
		return this;
	}

	public void addSubsamplesToLeft() {

		new ServerOp() {
			@Override
			public void begin() {
				MpDb.subsample_svc.all(sampleId, this);
			}
			public void onSuccess(Object result) {
				MetPetDBApplication.clearLeftSide();
				MetPetDBApplication.appendToLeft(new MySubsamples(
						(List<SubsampleDTO>) result));
			}
		}.begin();

	};
}
