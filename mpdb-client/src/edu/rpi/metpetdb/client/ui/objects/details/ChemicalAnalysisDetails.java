package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.ChooseImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AnalysisMaterialAttribute;

public class ChemicalAnalysisDetails extends FlowPanel {
	private static GenericAttribute[] chemicalAnalysisAtts = {
			new TextAttribute(MpDb.oc.ChemicalAnalysis_sampleName)
					.setReadOnly(true),
			new TextAttribute(MpDb.oc.ChemicalAnalysis_subsampleName)
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
			new ListboxAttribute(MpDb.doc.ChemicalAnalysis_elements),
			new ListboxAttribute(MpDb.doc.ChemicalAnalysis_oxides),
	};

	private final ObjectEditorPanel p_chemicalAnalysis;

	private long chemicalAnalysisId;

	public ChemicalAnalysisDetails() {
		p_chemicalAnalysis = new ObjectEditorPanel(chemicalAnalysisAtts) {
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
		};
		add(new OnEnterPanel.ObjectEditor(p_chemicalAnalysis));
	}

	public void addChemistry() {
		// final Label adder = new Label("Add:");

		final HorizontalPanel hp = new HorizontalPanel();

		final ListBox species = new ListBox();
		species.addItem("Species...", "Species...");
		species.addItem("Element", "Element");
		species.addItem("Oxide", "Oxide");
		species.setVisibleItemCount(1);

		final ListBox type = new ListBox();
		type.addItem("Type...", "Type...");
		type.addItem("Silicate", "Silicate");
		type.addItem("Oxide", "Oxide");
		type.addItem("Carbonate", "Carbonate");
		type.addItem("Phosphate", "Phosphate");
		type.addItem("Other", "Other");
		type.setVisibleItemCount(1);

		if (species.getSelectedIndex() != -1 && !(species.isItemSelected(1))) {
			if (type.getSelectedIndex() != -1 && !(type.isItemSelected(1))) {

			}
		}

		// add(adder);
		hp.add(species);
		hp.add(type);

		add(hp);
	}
	public ChemicalAnalysisDetails showById(final long id) {
		chemicalAnalysisId = id;
		p_chemicalAnalysis.load();
		// addChemistry();
		return this;
	}

	public ChemicalAnalysisDetails createNew(final SubsampleDTO ss) {
		final ChemicalAnalysisDTO ma = new ChemicalAnalysisDTO();
		ss.addChemicalAnalysis(ma);
		p_chemicalAnalysis.edit(ma);
		return this;
	}
}
