package edu.rpi.metpetdb.client.ui.objects.details;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.ChooseImageAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.AnalysisMaterialAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.ChemistryAttribute;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class ChemicalAnalysisDetails extends MPagePanel {
	private static GenericAttribute[] chemicalAnalysisAtts = {
			new TextAttribute(MpDb.doc.ChemicalAnalysis_owner)
					.setReadOnly(true),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_spotId, true, true, false),
			new RadioButtonAttribute(MpDb.doc.ChemicalAnalysis_publicData,
					LocaleHandler.lc_text.publicDataWarning()),
			new ChooseImageAttribute<ChemicalAnalysis>(MpDb.doc.ChemicalAnalysis_image,
					MpDb.doc.ChemicalAnalysis_referenceX,
					MpDb.doc.ChemicalAnalysis_referenceY),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_analysisMethod),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_location),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_analyst),
			new DateAttribute(MpDb.doc.ChemicalAnalysis_analysisDate,
					MpDb.doc.ChemicalAnalysis_datePrecision),
			
			new TextAreaAttribute(MpDb.doc.ChemicalAnalysis_description),
			new AnalysisMaterialAttribute(MpDb.doc.ChemicalAnalysis_mineral,
					MpDb.doc.ChemicalAnalysis_largeRock),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_total,true,false,false),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_stageX,true,true,false),
			new TextAttribute(MpDb.doc.ChemicalAnalysis_stageY,true,true,false),
			new ChemistryAttribute(MpDb.doc.ChemicalAnalysis_elements,
					MpDb.doc.ChemicalAnalysis_oxides),
	};

	private final ObjectEditorPanel<ChemicalAnalysis> p_chemicalAnalysis;

	private long chemicalAnalysisId;
	private Subsample subsampleObj;

	public ChemicalAnalysisDetails() {
		p_chemicalAnalysis = new ObjectEditorPanel<ChemicalAnalysis>(
				chemicalAnalysisAtts) {
			
			@Override
			public void load() {
				new ServerOp<ChemicalAnalysis>() {
					public void begin() {
						loadBean(this);
					}
					public void onSuccess(final ChemicalAnalysis result) {
						
						if (!MpDb.isCurrentUser(result.getSubsample().getOwner()))
						{
						p_chemicalAnalysis.hideEditButtons();
						}
						
						onLoadCompletion(result);
					}
				}.begin();
			}
			
			protected void loadBean(final AsyncCallback<ChemicalAnalysis> ac) {
				final ChemicalAnalysis ma = (ChemicalAnalysis) getBean();
				MpDb.chemicalAnalysis_svc.details(
						ma != null && !ma.mIsNew() ? ma.getId()
								: chemicalAnalysisId, ac);
				
			}

			protected void saveBean(final AsyncCallback<ChemicalAnalysis> ac) {
				//If the user attempts to make the analysis public, make sure the owning subsample is public
				if(((ChemicalAnalysis) getBean()).isPublicData()){
					new ServerOp(){
						@Override
						public void begin(){
							MpDb.subsample_svc.details(((ChemicalAnalysis) getBean()).getSubsampleId(), this);
						}
						public void onSuccess(Object result){
							if(!((Subsample) result).isPublicData()){
								//Can't save this subsample as public
								cannotMakePublic();
							} else {
								MpDb.chemicalAnalysis_svc
								.save((ChemicalAnalysis) getBean(), ac);
							}
						}
					}.begin();
				} else{
					MpDb.chemicalAnalysis_svc
					.save((ChemicalAnalysis) getBean(), ac);
				}
			}

			protected void deleteBean(final AsyncCallback<MObject> ac) {
						new ConfirmationDialogBox("Are you sure you want to delete this analysis?"
								, true) {
							public void onSubmit(){
								subsampleObj = ((ChemicalAnalysis) getBean()).getSubsample();
								MpDb.chemicalAnalysis_svc.delete(((ChemicalAnalysis) getBean())
										.getId(), ac);
							}
							
							public void onCancel() {
								p_chemicalAnalysis.setEnabled(true);
							}
					}.show();
			}

			protected boolean canEdit() {
				final Subsample s = ((ChemicalAnalysis) getBean()).getSubsample();
				if (MpDb.isCurrentUser(s.getOwner()))
					return true;
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
				setPageTitle(result.getSubsampleName(),
						"Chemical Analysis Spot");
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
	
	private void cannotMakePublic(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("Cannot make analysis public. It is owned by a private Subsample"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				box.hide();
				p_chemicalAnalysis.getBean().setPublicData(false);
				p_chemicalAnalysis.edit(p_chemicalAnalysis.getBean());
			}
		});
		container.add(ok);
		box.setWidget(container);
		box.show();
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
