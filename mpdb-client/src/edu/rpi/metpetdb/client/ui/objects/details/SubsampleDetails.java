package edu.rpi.metpetdb.client.ui.objects.details;

import org.gwtwidgets.client.ui.pagination.DataProvider;
import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
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
import edu.rpi.metpetdb.client.ui.objects.list.MineralAnalysisList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class SubsampleDetails extends FlowPanel {
	private static GenericAttribute[] subsampleAtts = {
			new TextAttribute(MpDb.oc.Subsample_sampleName).setReadOnly(true),
			new TextAttribute(MpDb.doc.Subsample_name),
			new ListboxAttribute(MpDb.doc.Subsample_type),
			new AddImageAttribute(MpDb.doc.Subsample_images),
			new TextAttribute(MpDb.oc.Subsample_imageCount)
			.setReadOnly(true),
			new TextAttribute(MpDb.oc.Subsample_analysisCount)
			.setReadOnly(true),};

	private final ObjectEditorPanel p_subsample;
	private long subsampleId;
	private ServerOp continuation;
	private String sampleAlias;

	public SubsampleDetails() {
		final SubsampleDetails me = this;
		p_subsample = new ObjectEditorPanel(subsampleAtts, LocaleHandler.lc_text
				.addSubsample(), LocaleHandler.lc_text
				.addSubsampleDescription(sampleAlias)) {
			protected void loadBean(final AsyncCallback ac) {
				final Subsample s = (Subsample) getBean();
				MpDb.subsample_svc.details(s != null && !s.mIsNew()
						? s.getId()
						: subsampleId, ac);
			}
			protected void saveBean(final AsyncCallback ac) {
				MpDb.subsample_svc.saveSubsample((Subsample) getBean(), ac);
			}
			protected void deleteBean(final AsyncCallback ac) {
				MpDb.subsample_svc.delete(((Subsample)getBean()).getId(),ac);
			}
			protected boolean canEdit() {
				final Sample s = ((Subsample) getBean()).getSample();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}
			protected void onSaveCompletion(final MObject result) {
				if (continuation != null) {
					continuation.onSuccess((MObject) result);
				} else
					this.show((MObject) result);
			}
			protected void onLoadCompletion(final MObject result) {
				super.onLoadCompletion(result);
				final Subsample s = (Subsample) result;
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
		final MLink addMineralAnalysis = new MLink(LocaleHandler.lc_text
				.addMineralAnalysis(), new ClickListener() {
			public void onClick(final Widget sender) {
				new ServerOp() {
					public void begin() {
						if (MpDb.isLoggedIn())
							MetPetDBApplication
									.show(new MineralAnalysisDetails()
											.createNew((Subsample) p_subsample
													.getBean()));
						else
							onFailure(new LoginRequiredException());
					}
					public void onSuccess(Object result) {
					}
				}.begin();
			}
		});
		addMineralAnalysis.addStyleName(Styles.ADDLINK);
		add(addMineralAnalysis);
		final MineralAnalysisList list = new MineralAnalysisList(
				new DataProvider() {
					public void update(final PaginationParameters p,
							final AsyncCallback ac) {
						MpDb.mineralAnalysis_svc.all(p, subsampleId, ac);
					}
				});
		add(list);
		return this;
	}

	public SubsampleDetails createNew(final Sample s, final ServerOp r) {
		continuation = r;
		Subsample ss = new Subsample();
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
					final Subsample s = (Subsample) p_subsample.getBean();
					MpDb.subsample_svc.details(s != null && !s.mIsNew() ? s
							.getId() : subsampleId, this);
				} else {
					onFailure(new LoginRequiredException());
				}
			}
			public void onSuccess(final Object result) {
				p_subsample.edit((Subsample) result);
			}
			public void cancel() {
				p_subsample.load();
			}
		}.begin();
		return this;
	}
}
