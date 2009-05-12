package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;

public class MakePublicDialog extends MDialogBox{
	private ArrayList<Sample> samples;
	private ArrayList<Subsample> subsamples = new ArrayList();
	private ArrayList<Grid> imageMaps = new ArrayList();
	
	private Map<Long, List<ChemicalAnalysis>> chemicalAnalysesMap;
	private Map<Long, List<Subsample>> subsamplesMap;
	
	private ArrayList<ChemicalAnalysis> selectedChemicalAnalyses = new ArrayList();
	private ArrayList<Subsample> selectedSubsamples = new ArrayList();
	private ArrayList<Grid> selectedImageMaps = new ArrayList();
	
	private Vector ChemicalAnalysesTotal = new Vector();
	private Vector ImageMapsTotal = new Vector();
	
	private Tree subsampleTree = new Tree();
	private boolean custom = true; //keeps track of whether or not custom dialog is being used for navigation purposes
	private boolean fromSummary = false;
	
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
		
	}
	
	private Widget loading(){
		final FlowPanel container = new FlowPanel();
		container.add(new Label("Loading..."));
		
		return container;
	}
	
	// Step 1 in the process, unless only 1 sample is selected, then goes directly to step 2 createInterfaceMakeSamplesPublicCustom
	private Widget createInterfaceMakeSamplesPublicOptions(){
		final FlowPanel container = new FlowPanel();
		
		selectedSubsamples.clear();
		selectedChemicalAnalyses.clear();
		selectedImageMaps.clear();
		
		final RadioButton rb0 = new RadioButton("make-public", "All subsamples, chemical analyses, and image maps");
		final RadioButton rb1 = new RadioButton("make-public", "All subsamples and chemical analyses only");
		final RadioButton rb2 = new RadioButton("make-public", "All subsamples and image maps only");
		final RadioButton rb3 = new RadioButton("make-public", "All subsamples only");
		final RadioButton rb4 = new RadioButton("make-public", "Custom selection");
		
		container.add(new Label("Make " + samples.size() + " Samples Public"));
		container.add(new Label("Please select which related data you would like to also make public"));
		container.add(new Label("In addition to the samples, make the following public:"));
		
		container.add(rb0);
		container.add(rb1);
		container.add(rb2);
		container.add(rb3);
		container.add(rb4);
		
		final Button next = new Button("Next");
		next.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				// clear the dialog box for the next step
				MakePublicDialog.this.clear();
				
				//"Custom Selection"
				if(rb4.isChecked()){
					custom = true;
					for(Sample s: samples){
						ChemicalAnalysesTotal.add(s.getAnalysisCount());
					}
					countAllImageMaps(ImageMapsTotal);
					MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicCustom());
				}
				else {
					selectedSubsamples = (ArrayList<Subsample>) subsamples.clone();
					//Totals are always the same
					for(Sample s: samples){
						ChemicalAnalysesTotal.add(s.getAnalysisCount());
					}	
					countAllImageMaps(ImageMapsTotal);
					custom = false;
					//"All subsamples, chemical analyses, and image maps"
					if(rb0.isChecked()){
						Iterator itr = chemicalAnalysesMap.entrySet().iterator();
						while(itr.hasNext()){
							Map.Entry pair = (Map.Entry)itr.next();
							selectedChemicalAnalyses.addAll((ArrayList<ChemicalAnalysis>)pair.getValue());
						}
						selectedImageMaps = (ArrayList<Grid>) imageMaps.clone();
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					//"All subsamples and chemical analyses only"
					else if(rb1.isChecked()){
						Iterator itr = chemicalAnalysesMap.entrySet().iterator();
						while(itr.hasNext()){
							Map.Entry pair = (Map.Entry)itr.next();
							selectedChemicalAnalyses.addAll((ArrayList<ChemicalAnalysis>)pair.getValue());
						}
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					//"All subsamples and image maps only"
					else if(rb2.isChecked()){
						selectedImageMaps = (ArrayList<Grid>) imageMaps.clone();
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					//"All subsamples only"
					else if(rb3.isChecked()){
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
				}
			}
		});
		
		// actually add stuff to the container
		//container.add(closeX);
		container.add(next);
		container.add(cancel);
		
		return container;
	}
	
	// Step 2 in the process, only used if custom selection is selected or if only 1 sample is selected
	// In here you need to fill the selectedSubsamples, selectedChemicalAnalyses, and selectedImageMaps ArrayLists
	private boolean forward; //keeps track of direction for listIterator
	private Widget createInterfaceMakeSamplesPublicCustom(){
		final FlowPanel container = new FlowPanel();
		
		custom = true;
		//calculate totals
		for(Sample s: samples){
			ChemicalAnalysesTotal.add(s.getAnalysisCount());
		}
		countAllImageMaps(ImageMapsTotal);
		
		//no samples were selected
		if(samples.isEmpty()){
			container.add(new Label("No Samples are selected"));
			Button close = new Button("Close");
			close.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					MakePublicDialog.this.hide();			
				}
			});
			container.add(close);
			return container;
		}
		
		final Button next = new Button("Next");
		final Button back = new Button("Back");
		final ListIterator<Sample> itr = samples.listIterator();
		forward = true;
		//If returning from the summary dialog, start at the last sample.
		//Else, start from the beginning
		Sample current = itr.next();
		if(fromSummary){
			fromSummary = false;
			while(itr.hasNext()){
				current = itr.next();
			}
		}
	
		final Label header = new Label("Make " + current.getNumber() + " Public");
		final Label counter = new Label((itr.previousIndex() + 1) + " of " + samples.size() + " Samples");
		final Label header2 = new Label("Please select any related data you would like to also make public.");
		setSubsampleTree(current);
		
		back.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				if(itr.hasPrevious()){
					if(forward){
						forward = false;
						itr.previous();
					}
					if(itr.hasPrevious()){
						Sample current = itr.previous();
						header.setText("Make " + current.getNumber() + " Public");
						counter.setText((itr.previousIndex() + 2) + " of " + samples.size() + " Samples");
						setSubsampleTree(current);
					}
					else{
						MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicOptions());
					}
				}
				else{
					MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicOptions());
				}
			}
		});
		
		next.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				if(itr.hasNext()){
					if(!forward){
						forward = true;
						itr.next();
					}
					Sample current = itr.next();
					header.setText("Make " + current.getNumber() + " Public");
					counter.setText((itr.previousIndex() + 1) + " of " + samples.size() + " Samples");
					setSubsampleTree(current);
				}
				else{
					MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
				}
			}
		});
		
		container.add(header);
		container.add(counter);
		container.add(header2);
		container.add(subsampleTree);
		container.add(back);
		container.add(next);
		container.add(cancel);
		
		return container;
	}
	
	// Step 3 in the process, user can officially make the selected data public
	private Widget createInterfaceMakeDataPublic(){
		final FlowPanel container = new FlowPanel();
		
		container.add(new Label("Make " + samples.size() + " Samples Public"));
		container.add(new Label("Please review the summary below"));
		
		final Button back = new Button("Back");
		final Button makePublic = new Button("Make Public");
		
		int id = 0;
		//count selected objects
		for(Sample s: samples){
			int selectedSubsamplesCount = 0, selectedChemicalAnalysesCount = 0, selectedImageMapsCount = 0;
			long sampleID = s.getId();
			
			List<Subsample> ssList = subsamplesMap.get(sampleID);
			if(!(ssList == null) && !ssList.isEmpty()){
				Iterator<Subsample> ssItr = ssList.iterator();
				while(ssItr.hasNext()){
					Subsample current = (Subsample) ssItr.next();
					//increment selectedSubsampleCount if this subsample is selected
					if(selectedSubsamples.contains(current)) selectedSubsamplesCount++;
					//increment imageMapCount if a grid is found for this subsample and it's selected
					if(current.getGrid() != null && selectedImageMaps.contains(current.getGrid())) selectedImageMapsCount++;
					
					//get the list of analyses for this subsample and increment if it's selected
					List<ChemicalAnalysis> caList = chemicalAnalysesMap.get(current.getId());
					if(!(caList == null) && !caList.isEmpty()){
						Iterator<ChemicalAnalysis> caItr = caList.iterator();
						while(caItr.hasNext()){
							ChemicalAnalysis ca = (ChemicalAnalysis) caItr.next();
							if(selectedChemicalAnalyses.contains(ca)) selectedChemicalAnalysesCount++;
						}
					}
				}
			}
			
			container.add(new Label("Sample " + s.getNumber() + ": " +
					selectedSubsamplesCount + "/" + s.getSubsampleCount() + " subsamples, " + 
					"Chem: " + selectedChemicalAnalysesCount + "/" + ChemicalAnalysesTotal.get(id) + " " + 
					"Img map: " + selectedImageMapsCount + "/" + ImageMapsTotal.get(id)));
			id++;
		}
		
		//container.add(closeX);
		container.add(back);
		container.add(makePublic);
		container.add(cancel);
		
		//back sends the user back to step1 or step2 depending
		back.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				//if custom, go back to the last sample
				if(custom){
					fromSummary = true;
					MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicCustom());
				}
				else{
					MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicOptions());
				}
			}
		});
		
		// onClick needs to call makeDataPublic();
		makePublic.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				makeDataPublic();
				MakePublicDialog.this.hide(); 
			}
		});
		
		return container;
	}
	
	private void getAllData(final ArrayList<Sample> allSamples){
		// get subsamples
		new ServerOp<Map<Long, List<Subsample>>>() {
			@Override
			public void begin() {
				MakePublicDialog.this.setWidget(loading());
				Iterator<Sample> itr = allSamples.iterator();
				Collection<Long> sampleIds = new ArrayList();
				while (itr.hasNext()) {
					Sample current = itr.next();
					sampleIds.add(current.getId());
				}
				MpDb.subsample_svc.allFromManySamples(sampleIds, this);
			}
			public void onSuccess(Map<Long, List<Subsample>> result) {
				subsamplesMap = result;
				//Populate a subsample list so calls that pass in subsampleIds will work
				//final ListIterator<Sample> itr = samples.listIterator();
				Iterator itr = subsamplesMap.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry pair = (Map.Entry)itr.next();
					subsamples.addAll((ArrayList<Subsample>)pair.getValue());
				}
				//subsamples.addAll(result);
					// get chemical analyses
				new ServerOp<Map<Long, List<ChemicalAnalysis>>>() {
					@Override
					public void begin() {
						Iterator<Subsample> itr = subsamples.iterator();
						Collection<Long> subsampleIds = new ArrayList();
						while (itr.hasNext()) {
							Subsample current = itr.next();
							subsampleIds.add(current.getId());
						}
						MpDb.chemicalAnalysis_svc.allFromManySubsamples(subsampleIds, this);
					}
					public void onSuccess(Map<Long, List<ChemicalAnalysis>> result) {
						chemicalAnalysesMap = result;
						//chemicalAnalyses.addAll(result);
						// get image maps
						new ServerOp<List<Grid>>() {
							@Override
							public void begin() {
								Iterator<Subsample> itr = subsamples.iterator();
								Collection<Long> subsampleIds = new ArrayList();
								while (itr.hasNext()) {
									Subsample current = itr.next();
									subsampleIds.add(current.getId());
								}
								MpDb.imageBrowser_svc.allFromManySubsamples(subsampleIds, this);
							}
							public void onSuccess(List<Grid> result) {
								imageMaps.addAll(result);
								// all data retrieved, we're ready to display the dialog
								if(samples.size() == 1){
									MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicCustom());
								}
								else if(samples.size() > 1){
									MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicOptions());
								}
							}
						}.begin();
					}
				}.begin();
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
					
				}
				MpDb.sample_svc.saveAll(samples, this);
			}
			public void onSuccess(Object result) {

			}
		}.begin();
	}
	
	private void makeChemicalAnalysesPublic(){
		new VoidServerOp() {
			@Override
			public void begin() {
				Iterator<ChemicalAnalysis> itr = selectedChemicalAnalyses.iterator();
				while (itr.hasNext()) {
					ChemicalAnalysis current = itr.next();
					current.setPublicData(true);
				}
				MpDb.chemicalAnalysis_svc.saveAll(selectedChemicalAnalyses, this);
			}
			public void onSuccess() {

			}
		}.begin();
	}

	private void makeSubsamplesPublic(){
		new VoidServerOp() {
			@Override
			public void begin() {
				Iterator<Subsample> itr = selectedSubsamples.iterator();
				while (itr.hasNext()) {
					Subsample current = itr.next();
					current.setPublicData(true);
				}
				MpDb.subsample_svc.saveAll(selectedSubsamples, this);
			}
			public void onSuccess() {

			}
		}.begin();
	}
	
	private void makeImageMapsPublic(){
		new VoidServerOp() {
			@Override
			public void begin() {
				Iterator<Grid> itr = selectedImageMaps.iterator();
				while (itr.hasNext()) {
					Grid current = itr.next();
					current.setPublicData(true);
				}
				MpDb.imageBrowser_svc.saveAll(selectedImageMaps, this);
			}
			public void onSuccess() {

			}
		}.begin();
	}
	
	private void setSubsampleTree(final Sample sample){
		subsampleTree.clear();
		
		//create a TreeItem for each subsample
		final Iterator itr = subsamples.iterator();
		while(itr.hasNext()){
			final Subsample current = (Subsample) itr.next();
			final int thisSample = current.getSampleId().intValue() - 1;		
			if(current.getSampleId().equals(sample.getId())){	
				final CheckBox ssBox = new CheckBox(current.getName());
				ssBox.addClickListener(new ClickListener(){
					public void onClick(final Widget sender){
						if(ssBox.isChecked()){
							selectedSubsamples.add(current);
						}
						else{
							selectedSubsamples.remove(current);
							//unselect all ca's
							long subsampleID = current.getId();
							List<ChemicalAnalysis> analyses = chemicalAnalysesMap.get(subsampleID);
							for(final ChemicalAnalysis ca : analyses){
								selectedChemicalAnalyses.remove(ca);
							}
							
							//unselect the image map
							selectedImageMaps.remove(current.getGrid());
							
							setSubsampleTree(sample);
						}
					}
				});
				//set ssbox to checked if it's in the selected list
				if(selectedSubsamples.contains(current)) ssBox.setChecked(true);
				
				TreeItem subsample = new TreeItem(ssBox);
				
				if(current.getAnalysisCount() > 0){
					TreeItem chemAnalyses = new TreeItem("Chemical Analyses");
					chemAnalyses.addItem("Select: All None");
					
					long subsampleID = current.getId();
					List<ChemicalAnalysis> analyses = chemicalAnalysesMap.get(subsampleID);
					for(final ChemicalAnalysis ca : analyses){
						final CheckBox chemBox = new CheckBox("Spot ID: " + ca.getSpotId()); 
						chemBox.addClickListener(new ClickListener(){
							public void onClick(final Widget sender){
								if(chemBox.isChecked()){
									//if this subsample is not in the selected list, it must now be added
									if(!ssBox.isChecked()){
										ssBox.setChecked(true);
										selectedSubsamples.add(current);
									}
									selectedChemicalAnalyses.add(ca);
								}
								else{
									selectedChemicalAnalyses.remove(ca);
								}
							}
						});
						//set chemBox to checked if it's in selected list
						if(selectedChemicalAnalyses.contains(ca)) chemBox.setChecked(true);
						chemAnalyses.addItem(chemBox);
					}
					subsample.addItem(chemAnalyses); 
				}
				
				if(current.getGrid() != null){
					final CheckBox mapBox = new CheckBox("Image Map");
					mapBox.addClickListener(new ClickListener(){
						public void onClick(final Widget sender){
							if(mapBox.isChecked()){
								//if this subsample is not in the selected list, it must now be added
								if(!ssBox.isChecked()){
									ssBox.setChecked(true);
									selectedSubsamples.add(current);
								}
								selectedImageMaps.add(current.getGrid());
							}
							else{
								selectedImageMaps.remove(current.getGrid());
							}
						}
					});
					//set mapBox to check if it's in the selected list
					if(selectedImageMaps.contains(current.getGrid())) mapBox.setChecked(true);
					subsample.addItem(mapBox);
				}
				subsampleTree.addItem(subsample);
			}
		}
		if(subsampleTree.getItem(0) == null) subsampleTree.addItem("This sample has no subsamples");
	}
	
	private void countAllImageMaps(Vector ImageMapsCount){
		for(Sample s: samples){
			int count = 0;
			long sampleID = s.getId();
			Iterator<Subsample> itr = subsamples.iterator();
			while (itr.hasNext()) {
				Subsample current = itr.next();
				if(current.getSampleId().equals(sampleID) && current.getGrid() != null) count++;
			}
			ImageMapsCount.add(count);
		}
	}
	
}