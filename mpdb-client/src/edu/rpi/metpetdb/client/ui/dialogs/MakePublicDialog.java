package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;

public class MakePublicDialog extends MDialogBox{
	private ArrayList<Sample> samples;
	private ArrayList<ChemicalAnalysis> chemicalAnalyses = new ArrayList();
	private ArrayList<Subsample> subsamples = new ArrayList();;
	private ArrayList<Grid> imageMaps = new ArrayList();;
	
	private ArrayList<ChemicalAnalysis> selectedChemicalAnalyses = new ArrayList();
	private ArrayList<Subsample> selectedSubsamples = new ArrayList();;
	private ArrayList<Grid> selectedImageMaps = new ArrayList();;
	
	
	private Button closeX;
	private Button cancel;
	
	public MakePublicDialog(final ArrayList<Sample> samples){
		super();
		getAllData(samples);
		this.samples = samples;
		
		// These two buttons are in every dialog so we create them once here
		closeX = new Button("X");
		closeX.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				new ServerOp<Boolean>(){
					public void begin() {
						new ConfirmationDialogBox("Cancel?", true, this);
					}
					public void onSuccess(final Boolean result) {
						if (result)
							MakePublicDialog.this.hide();
					}
				}.begin();		
			}
		});
		
		cancel = new Button("Cancel");
		cancel.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				new ServerOp<Boolean>(){
					public void begin() {
						new ConfirmationDialogBox("Cancel?", true, this);
					}
					public void onSuccess(final Boolean result) {
						if (result)
							MakePublicDialog.this.hide();
					}
				}.begin();			
			}
		});
		
		// If multiple samples are selected go to step 1, else go to step 2.
		if (samples.size() > 1)
			this.setWidget(createInterfaceMakeSamplesPublicOptions());
		else 
			this.setWidget(createInterfaceMakeSamplesPublicCustom());
	}
	
	// Step 1 in the process, unless only 1 sample is selected, then goes directly to step 2 createInterfaceMakeSamplesPublicCustom
	private Widget createInterfaceMakeSamplesPublicOptions(){
		final FlowPanel container = new FlowPanel();
		
		final Button next = new Button("Next");
		next.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				// clear the dialog box for the next step
				MakePublicDialog.this.clear();
				
				//Call corresponding function based on selection from radio button list and set correct selected data
				
				// if custom selection, then call
				
					// MakePublicDialog.this.add(createInterfaceMakeSamplesPublicCustom());
				
				// else
					// every case selects the subsamples
					// selectedSubsamples = subsamples;
					// if all subsamples, chemical analysis, image maps then					
						// selectedChemicalAnalyses = chemicalAnalyses;
						// selectedImageMaps = imageMaps;
				
					// if all subsamples, chemical analysis then 
						// selectedChemicalAnalyses = chemicalAnalyses;
					
					// if all subsamples, image maps then 
						// selectedImageMaps = imageMaps;
					
					// if all subsamples, then nothing, we aleady took care of that
					
					// MakePublicDialog.this.add();			
				
			}
		});
		
		// actually add stuff to the container
		container.add(closeX);
		container.add(next);
		container.add(cancel);
		
		return container;
	}
	
	// Step 2 in the process, only used if custom selection is selected or if only 1 sample is selected
	// In here you need to fill the selectedSubsamples, selectedChemicalAnalyses, and selectedImageMaps ArrayLists
	private Widget createInterfaceMakeSamplesPublicCustom(){
		final FlowPanel container = new FlowPanel();
		
		return container;
	}
	
	// Step 3 in the process, user can officially make the selected data public
	private Widget createInterfaceMakeDataPublic(){
		final FlowPanel container = new FlowPanel();
		
		// onClick needs to call makeDataPublic();
		
		return container;
	}
	
	private void getAllData(final ArrayList<Sample> allSamples){
		// get subsamples
		new ServerOp<List<Subsample>>() {
			int succeeded;
			@Override
			public void begin() {
				Iterator<Sample> itr = allSamples.iterator();
				while (itr.hasNext()) {
					Sample current = itr.next();
					MpDb.subsample_svc.all(current.getId(), this);
				}
			}
			public void onSuccess(List<Subsample> result) {
				subsamples.addAll(result);
				succeeded++;
				if (succeeded == allSamples.size()){
					// get chemical analyses
					new ServerOp<List<ChemicalAnalysis>>() {
						int succeeded;
						@Override
						public void begin() {
							Iterator<Subsample> itr = subsamples.iterator();
							while (itr.hasNext()) {
								Subsample current = itr.next();
								MpDb.chemicalAnalysis_svc.all(current.getId(), this);
							}
							if (subsamples.size() == 0){
								onSuccess(new ArrayList<ChemicalAnalysis>());
							}
						}
						public void onSuccess(List<ChemicalAnalysis> result) {
							chemicalAnalyses.addAll(result);
							succeeded++;
							if (succeeded >= subsamples.size()){
								// get image maps
								new ServerOp<Grid>() {
									int succeeded;
									@Override
									public void begin() {
										Iterator<Subsample> itr = subsamples.iterator();
										while (itr.hasNext()) {
											Subsample current = itr.next();
											MpDb.imageBrowser_svc.details(current.getId(), this);
										}
										if (subsamples.size() == 0){
											onSuccess(null);
										}
									}
									public void onSuccess(Grid result) {
										if (result != null) {
											imageMaps.add(result);
										}
										succeeded++;
										if (succeeded >= subsamples.size()){
											// all data retrieved, we're ready to display the dialog
											MakePublicDialog.this.show();
										}
									}
								}.begin();
							}
						}
					}.begin();
				}
			}
		}.begin();
	}
	
	private void makeDataPublic(){
		makeSamplesPublic();
		makeSubsamplesPublic();
		makeChemicalAnalysesPublic();
		makeImageMapsPublic();
	}
	
	private void makeSamplesPublic(){
		new ServerOp() {
			@Override
			public void begin() {
				Iterator<Sample> itr = samples.iterator();
				while (itr.hasNext()) {
					Sample current = itr.next();
					current.setPublicData(true);
					MpDb.sample_svc.save(current, this);
				}
			}
			public void onSuccess(Object result) {

			}
		}.begin();
	}
	
	private void makeChemicalAnalysesPublic(){
		new ServerOp() {
			@Override
			public void begin() {
				Iterator<ChemicalAnalysis> itr = selectedChemicalAnalyses.iterator();
				while (itr.hasNext()) {
					ChemicalAnalysis current = itr.next();
					current.setPublicData(true);
					MpDb.chemicalAnalysis_svc.save(current, this);
				}
			}
			public void onSuccess(Object result) {

			}
		}.begin();
	}

	private void makeSubsamplesPublic(){
		new ServerOp() {
			@Override
			public void begin() {
				Iterator<Subsample> itr = selectedSubsamples.iterator();
				while (itr.hasNext()) {
					Subsample current = itr.next();
					current.setPublicData(true);
					MpDb.subsample_svc.save(current, this);
				}
			}
			public void onSuccess(Object result) {

			}
		}.begin();
	}
	
	private void makeImageMapsPublic(){
		new ServerOp() {
			@Override
			public void begin() {
				Iterator<Grid> itr = selectedImageMaps.iterator();
				while (itr.hasNext()) {
					Grid current = itr.next();
					current.setPublicData(true);
					MpDb.imageBrowser_svc.saveGrid(current, this);
				}
			}
			public void onSuccess(Object result) {

			}
		}.begin();
	}
	
}