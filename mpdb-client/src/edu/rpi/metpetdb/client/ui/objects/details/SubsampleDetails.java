package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidLoggedInOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageListViewer;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AddImageAttribute;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisListEx;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

public class SubsampleDetails extends MPagePanel {

	private static GenericAttribute[] subsampleAtts = {
			new TextAttribute(MpDb.doc.Subsample_owner).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_name),
			new RadioButtonAttribute(MpDb.doc.Subsample_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new ListboxAttribute(MpDb.doc.Subsample_subsampleType),
			new AddImageAttribute(MpDb.doc.Subsample_images),
			new TextAttribute(MpDb.doc.Subsample_imageCount).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_analysisCount)
					.setReadOnly(true),
	};

	private final ObjectEditorPanel<Subsample> p_subsample;
	private long subsampleId;
	private long sampleId;
	private String sampleNumber;
	private final MLink map = new MLink();
	private Widget images;
	private final MTwoColPanel panel = new MTwoColPanel();

	public SubsampleDetails() {
		p_subsample = new ObjectEditorPanel<Subsample>(subsampleAtts,
				LocaleHandler.lc_text.addSubsample(), LocaleHandler.lc_text
						.addSubsampleDescription(sampleNumber)) {
			protected void loadBean(final AsyncCallback<Subsample> ac) {
				final Subsample s = (Subsample) getBean();
				MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: subsampleId, ac);
			}

			protected void saveBean(final AsyncCallback<Subsample> ac) {
				MpDb.subsample_svc.save((Subsample) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback<Object> ac) {
				sampleId = getBean().getSampleId();
				MpDb.subsample_svc.delete(((Subsample) getBean()).getId(), ac);
			}

			protected boolean canEdit() {
				// TODO temporary while testing permissions
				// final Sample s = ((Subsample) getBean()).getSample();
				// if (s.isPublicData())
				// return false;
				// if (MpDb.isCurrentUser(s.getOwner()))
				// return true;
				// return false;
				return true;
			}

			protected void onSaveCompletion(final Subsample result) {
				if (History.getToken().equals(
						TokenSpace.detailsOf((Subsample) result))) {
					TokenSpace.dispatch(TokenSpace
							.detailsOf((Subsample) result));
				} else {
					History.newItem(TokenSpace.detailsOf((Subsample) result));
				}
			}

			protected void onLoadCompletion(final Subsample result) {
				super.onLoadCompletion(result);
				final Subsample s = (Subsample) result;
				if (s.getGrid() == null) {
					map.setText("Create Map");
					map.setTargetHistoryToken(TokenSpace.createNewImageMap(s));
				} else {
					map.setText("View Map");
					map
							.setTargetHistoryToken(TokenSpace.detailsOf(s
									.getGrid()));
				}
				setPageTitle(s.getName(), "Subsample");
				addPageActionItem(map);

			}
			protected void onDeleteCompletion(final Object result) {
				History.newItem(TokenSpace.detailsOfSample((sampleId)));
			}
		};

		final OnEnterPanel.ObjectEditor oep = new OnEnterPanel.ObjectEditor(
				p_subsample);
		panel.getLeftCol().add(oep);
		add(panel);
	}

	public void showChemicalAnalysis() {
		FlexTable chemft = new FlexTable();
		final ChemicalAnalysisListEx list = new ChemicalAnalysisListEx(
				LocaleHandler.lc_text.noChemicalAnalysesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<ChemicalAnalysis>> ac) {
				MpDb.chemicalAnalysis_svc.all(p, subsampleId, ac);
			}
		};
		chemft.setWidget(1, 0, list);
		chemft.getFlexCellFormatter().setColSpan(1, 0, 10);

		final MLink addChemicalAnalysis = new MLink(LocaleHandler.lc_text
				.addChemicalAnalysis(), new ClickListener() {
			public void onClick(final Widget sender) {
				new VoidLoggedInOp() {
					public void command() {
						History.newItem(TokenSpace
								.createNewChemicalAnalysis(p_subsample
										.getBean()));
					}
				}.begin();
			}
		});
		addChemicalAnalysis.setStyleName(CSS.ADDLINK);
		final Label chemAnalysis = new Label("Chemical Analysis");
		chemAnalysis.setStyleName("h2");
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
		images = new ImageListViewer(subsampleId, false);
		panel.getRightCol().add(images);
		showChemicalAnalysis();
		return this;
	}

	public SubsampleDetails createNew(final Sample s) {
		Subsample ss = new Subsample();
		ss.setOwner(MpDb.currentUser());
		s.addSubsample(ss);
		ss.setSampleName(s.getNumber());
		p_subsample.edit(ss);
		sampleNumber = s.getNumber();
		return this;
	}

	public SubsampleDetails edit(final long id) {
		subsampleId = id;
		new ServerOp<Subsample>() {
			public void begin() {
				if (MpDb.isLoggedIn()) {
					final Subsample s = (Subsample) p_subsample.getBean();
					MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s
							.getId() : subsampleId, this);

				} else {
					onFailure(new LoginRequiredException());
				}
			}

			public void onSuccess(final Subsample result) {
				p_subsample.edit((Subsample) result);
			}

			public void cancel() {
				p_subsample.load();
			}
		}.begin();
		return this;
	}
}
