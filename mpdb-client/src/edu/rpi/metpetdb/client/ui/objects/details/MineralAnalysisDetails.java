package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.rpi.metpetdb.client.model.MineralAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.ChooseImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AnalysisMaterialAttribute;

public class MineralAnalysisDetails extends FlowPanel {
	private static GenericAttribute[] mineralAnalysisAtts = {
			new TextAttribute(MpDb.oc.MineralAnalysis_sampleName)
					.setReadOnly(true),
			new TextAttribute(MpDb.oc.MineralAnalysis_subsampleName)
					.setReadOnly(true),
			new TextAttribute(MpDb.doc.MineralAnalysis_spotId),
			new ChooseImageAttribute(MpDb.doc.MineralAnalysis_image,
					MpDb.doc.MineralAnalysis_pointX,
					MpDb.doc.MineralAnalysis_pointY),
			new TextAttribute(MpDb.doc.MineralAnalysis_analysisMethod),
			new TextAttribute(MpDb.doc.MineralAnalysis_location),
			new TextAttribute(MpDb.doc.MineralAnalysis_analyst),
			new DateAttribute(MpDb.doc.MineralAnalysis_analysisDate),
			new TextAttribute(MpDb.doc.MineralAnalysis_referenceId),
			new TextAreaAttribute(MpDb.doc.MineralAnalysis_description),
			new AnalysisMaterialAttribute(MpDb.doc.MineralAnalysis_mineral,
					MpDb.doc.MineralAnalysis_largeRock),};

	private final ObjectEditorPanel p_mineralAnalysis;

	private long mineralAnalysisId;

	public MineralAnalysisDetails() {
		p_mineralAnalysis = new ObjectEditorPanel(mineralAnalysisAtts) {
			protected void loadBean(final AsyncCallback ac) {
				final MineralAnalysis ma = (MineralAnalysis) getBean();
				MpDb.mineralAnalysis_svc.details(ma != null && !ma.mIsNew()
						? ma.getId()
						: mineralAnalysisId, ac);
			}

			protected void saveBean(final AsyncCallback ac) {
				// ((MineralAnalysis) getBean()).getSubsample().addImage(
				// (Image) mineralAnalysisAtts[3];
				MpDb.mineralAnalysis_svc.saveMineralAnalysis(
						(MineralAnalysis) getBean(), ac);
			}
			protected void deleteBean(final AsyncCallback ac) {
				MpDb.mineralAnalysis_svc.delete(((MineralAnalysis)getBean()).getId(),ac);
			}
			protected boolean canEdit() {
				final Sample s = ((MineralAnalysis) getBean()).getSubsample()
						.getSample();
				if (s.isPublicData())
					return false;
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
				return false;
			}
		};
		add(new OnEnterPanel.ObjectEditor(p_mineralAnalysis));
	}

	public void addChemistry(){
		//final Label adder = new Label("Add:");
		
		final HorizontalPanel hp = new HorizontalPanel();
		
		final ListBox species = new ListBox();
		species.addItem("Species...", "Species...");
		species.addItem("Element", "Element");
		species.addItem("Oxide", "Oxide");
		species.setVisibleItemCount(1);
		
		final ListBox type = new ListBox();
		type.addItem("Type...","Type...");
		type.addItem("Silicate", "Silicate");
		type.addItem("Oxide", "Oxide");
		type.addItem("Carbonate", "Carbonate");
		type.addItem("Phosphate", "Phosphate");
		type.addItem("Other", "Other");
		type.setVisibleItemCount(1);
		
		if(species.getSelectedIndex() != -1 && !(species.isItemSelected(1)) ){
			if(type.getSelectedIndex() != -1 && !(type.isItemSelected(1)) ){
				
			}
		}
		
		//add(adder);
		hp.add(species);
		hp.add(type);
		
		add(hp);
	}
	
	public MineralAnalysisDetails showById(final long id) {
		mineralAnalysisId = id;
		p_mineralAnalysis.load();
		//addChemistry();
		return this;
	}

	public MineralAnalysisDetails createNew(final Subsample ss) {
		final MineralAnalysis ma = new MineralAnalysis();
		ss.addMineralAnalysis(ma);
		p_mineralAnalysis.edit(ma);
		return this;
	}
}