package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.ChooseImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.HyperlinkAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AnalysisMaterialAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ChemistryAttribute;

public class ChemicalAnalysisDetails extends FlowPanel {
	private static GenericAttribute[] chemicalAnalysisAtts = {
			new HyperlinkAttribute(MpDb.oc.ChemicalAnalysis_sampleName)
					.setReadOnly(true),
			new HyperlinkAttribute(MpDb.oc.ChemicalAnalysis_subsampleName)
					.setReadOnly(true),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_spotId),
			new ChooseImageAttribute(MpDb.doc.ChemicalAnalysis_image,
					MpDb.doc.ChemicalAnalysis_pointX,
					MpDb.doc.ChemicalAnalysis_pointY),
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
					MpDb.doc.ChemicalAnalysis_oxides),
	};

	private final ObjectEditorPanel<ChemicalAnalysisDTO> p_chemicalAnalysis;

	private long chemicalAnalysisId;

	public ChemicalAnalysisDetails() {
		p_chemicalAnalysis = new ObjectEditorPanel<ChemicalAnalysisDTO>(
				chemicalAnalysisAtts) {
			protected void loadBean(final AsyncCallback ac) {
				final ChemicalAnalysisDTO ma = (ChemicalAnalysisDTO) getBean();
				MpDb.chemicalAnalysis_svc.details(
						ma != null && !ma.mIsNew() ? ma.getId()
								: chemicalAnalysisId, ac);
			}

			protected void saveBean(final AsyncCallback ac) {
				MpDb.chemicalAnalysis_svc.save((ChemicalAnalysisDTO) getBean(),
						ac);
			}

			protected void deleteBean(final AsyncCallback ac) {
				MpDb.chemicalAnalysis_svc.delete(
						((ChemicalAnalysisDTO) getBean()).getId(), ac);
			}

			protected boolean canEdit() {
				final SampleDTO s = ((ChemicalAnalysisDTO) getBean())
						.getSubsample().getSample();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}

			protected void onLoadCompletion(final MObjectDTO result) {
				super.onLoadCompletion(result);
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_chemicalAnalysis));
	}
	public ChemicalAnalysisDetails showById(final long id) {
		chemicalAnalysisId = id;
		p_chemicalAnalysis.load();
		return this;
	}

	public ChemicalAnalysisDetails createNew(final SubsampleDTO ss) {
		final ChemicalAnalysisDTO ma = new ChemicalAnalysisDTO();
		ss.addChemicalAnalysis(ma);
		p_chemicalAnalysis.edit(ma);
		return this;
	}
}
