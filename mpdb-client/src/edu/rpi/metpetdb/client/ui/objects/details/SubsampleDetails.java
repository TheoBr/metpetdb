package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AddImageAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class SubsampleDetails extends FlowPanel {
	private static GenericAttribute[] subsampleAtts = {
			new TextAttribute(MpDb.oc.Subsample_sampleName).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_name),
			new ListboxAttribute(MpDb.doc.Subsample_type),
			new AddImageAttribute(MpDb.doc.Subsample_images),
			new TextAttribute(MpDb.oc.Subsample_imageCount).setReadOnly(true),
			new TextAttribute(MpDb.oc.Subsample_analysisCount)
					.setReadOnly(true), };

	private final ObjectEditorPanel p_subsample;
	private long subsampleId;
	private ServerOp continuation;
	private String sampleAlias;

	public SubsampleDetails() {
		final SubsampleDetails me = this;
		p_subsample = new ObjectEditorPanel(subsampleAtts,
				LocaleHandler.lc_text.addSubsample(), LocaleHandler.lc_text
						.addSubsampleDescription(sampleAlias)) {
			protected void loadBean(final AsyncCallback ac) {
				final SubsampleDTO s = (SubsampleDTO) getBean();
				MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s.getId()
						: subsampleId, ac);
			}

			protected void saveBean(final AsyncCallback ac) {
				MpDb.subsample_svc.save((SubsampleDTO) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback ac) {
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
			}

			protected void onLoadCompletion(final MObjectDTO result) {
				super.onLoadCompletion(result);
				final SubsampleDTO s = (SubsampleDTO) result;
				if (s.getGrid() == null) {
					me.add(new MLink("Create Map", new ClickListener() {
						public void onClick(final Widget sender) {
							MetPetDBApplication.show(new ImageBrowserDetails()
									.createNew(s.getId()));
						}
					}));
				} else {
					me.add(new MLink("View Map", TokenSpace.detailsOf(s
							.getGrid())));
				}
				// DOM.setInnerText(header,s.getSample().getAlias());
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_subsample));
	}

	public SubsampleDetails showById(final long id) {
		subsampleId = id;
		p_subsample.load();
		final MLink addChemicalAnalysis = new MLink(LocaleHandler.lc_text
				.addChemicalAnalysis(), new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							MetPetDBApplication
									.show(new ChemicalAnalysisDetails()
											.createNew((SubsampleDTO) p_subsample
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
		add(addChemicalAnalysis);
		return this;
	}

	public SubsampleDetails createNew(final SampleDTO s, final ServerOp r) {
		continuation = r;
		SubsampleDTO ss = new SubsampleDTO();
		s.addSubsample(ss);
		p_subsample.edit(ss);
		sampleAlias = s.getAlias();
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
}
