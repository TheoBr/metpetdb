package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.ChooseImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.HyperlinkAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AnalysisMaterialAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ChemistryAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class ChemicalAnalysisDetails extends MPagePanel {
	private static GenericAttribute[] chemicalAnalysisAtts = {
			new TextAttribute(MpDb.doc.ChemicalAnalysis_spotId),
			new RadioButtonAttribute(MpDb.doc.ChemicalAnalysis_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new ChooseImageAttribute(MpDb.doc.ChemicalAnalysis_image,
					MpDb.doc.ChemicalAnalysis_referenceX,
					MpDb.doc.ChemicalAnalysis_referenceY),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_analysisMethod),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_location),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_analyst),
			new DateAttribute(MpDb.doc.ChemicalAnalysis_analysisDate,
					MpDb.doc.ChemicalAnalysis_datePrecision),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_reference),
			new TextAreaAttribute(MpDb.doc.ChemicalAnalysis_description),
			new AnalysisMaterialAttribute(MpDb.doc.ChemicalAnalysis_mineral,
					MpDb.doc.ChemicalAnalysis_largeRock),
			new ChemistryAttribute(MpDb.doc.ChemicalAnalysis_elements,
					MpDb.doc.ChemicalAnalysis_oxides)
	};

	private final ObjectEditorPanel<ChemicalAnalysis> p_chemicalAnalysis;

	private long chemicalAnalysisId;
	private Subsample subsampleObj;

	public ChemicalAnalysisDetails() {
		p_chemicalAnalysis = new ObjectEditorPanel<ChemicalAnalysis>(
				chemicalAnalysisAtts) {
			protected void loadBean(final AsyncCallback ac) {
				final ChemicalAnalysis ma = (ChemicalAnalysis) getBean();
				MpDb.chemicalAnalysis_svc.details(
						ma != null && !ma.mIsNew() ? ma.getId()
								: chemicalAnalysisId, ac);
			}

			protected void saveBean(final AsyncCallback ac) {
				MpDb.chemicalAnalysis_svc
						.save((ChemicalAnalysis) getBean(), ac);
			}

			protected void deleteBean(final AsyncCallback ac) {
				subsampleObj = ((ChemicalAnalysis) getBean()).getSubsample();
				MpDb.chemicalAnalysis_svc.delete(((ChemicalAnalysis) getBean())
						.getId(), ac);
			}

			protected boolean canEdit() {
				//TODO temporary while testing permissions
//				final Sample s = ((ChemicalAnalysis) getBean()).getSubsample()
//						.getSample();
//				if (s.isPublicData())
//					return false;
//				if (MpDb.isCurrentUser(s.getOwner()))
//					return true;
//				return false;
				return true;
			}

			protected void onSaveCompletion(final ChemicalAnalysis result) {
				if (History.getToken().equals(
						TokenSpace.detailsOf((ChemicalAnalysis) result))) {
					TokenSpace.dispatch(TokenSpace
							.detailsOf((ChemicalAnalysis) result));
				} else {
					History.newItem(TokenSpace
							.detailsOf((ChemicalAnalysis) result));
				}
			}

			protected void onLoadCompletion(final ChemicalAnalysis result) {
				super.onLoadCompletion(result);
				setPageTitle(result.getSubsampleName(), "Chemical Analysis Spot");
			}

			protected void onDeleteCompletion(final Object result) {
				History.newItem(TokenSpace.detailsOf((subsampleObj)));
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_chemicalAnalysis));
	}
	public ChemicalAnalysisDetails showById(final long id) {
		chemicalAnalysisId = id;
		p_chemicalAnalysis.load();
		return this;
	}

	public ChemicalAnalysisDetails createNew(final Subsample ss) {
		final ChemicalAnalysis ma = new ChemicalAnalysis();
		ma.setOwner(MpDb.currentUser());
		ss.addChemicalAnalysis(ma);
		ma.setSubsampleName(ss.getName());
		ma.setSampleName(ss.getSampleName());
		p_chemicalAnalysis.edit(ma);
		return this;
	}
}
