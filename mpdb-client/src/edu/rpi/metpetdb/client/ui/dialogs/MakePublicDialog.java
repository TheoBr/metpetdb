package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	private ArrayList<ChemicalAnalysis> chemicalAnalyses = new ArrayList();
	private ArrayList<Subsample> subsamples = new ArrayList();
	private ArrayList<Grid> imageMaps = new ArrayList();
	
	private ArrayList<ChemicalAnalysis> selectedChemicalAnalyses = new ArrayList();
	private ArrayList<Subsample> selectedSubsamples = new ArrayList();
	private ArrayList<Grid> selectedImageMaps = new ArrayList();
	
	private Vector selectedChemicalAnalysesCount = new Vector();
	private Vector selectedSubsamplesCount = new Vector();
	private Vector selectedImageMapsCount = new Vector();
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
		
		// If multiple samples are selected go to step 1, else go to step 2.
		if (samples.size() > 1)
			this.setWidget(createInterfaceMakeSamplesPublicOptions());
		else 
			this.setWidget(createInterfaceMakeSamplesPublicCustom());
	}
	
	// Step 1 in the process, unless only 1 sample is selected, then goes directly to step 2 createInterfaceMakeSamplesPublicCustom
	private Widget createInterfaceMakeSamplesPublicOptions(){
		final FlowPanel container = new FlowPanel();
		
		//clear counters in case back was hit
		selectedChemicalAnalysesCount.clear();
		selectedSubsamplesCount.clear();
		selectedImageMapsCount.clear();
		
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
					
				// MakePublicDialog.this.createInterfaceMakeDataPublic()
				if(rb4.isChecked()){
					custom = true;
					for(Sample s: samples){
						ChemicalAnalysesTotal.add(s.getAnalysisCount());
					}
					countAllImageMaps(ImageMapsTotal);
					MakePublicDialog.this.setWidget(createInterfaceMakeSamplesPublicCustom());
				}
				else {
					selectedSubsamples = subsamples;
					//Totals are always the same
					for(Sample s: samples){
						selectedSubsamplesCount.add(s.getSubsampleCount());
						ChemicalAnalysesTotal.add(s.getAnalysisCount());
					}	
					countAllImageMaps(ImageMapsTotal);
					
					//Count the selected Analyses & maps for the selected option
					if(rb0.isChecked()){
						custom = false;
						selectedChemicalAnalyses = chemicalAnalyses;
						selectedImageMaps = imageMaps;
						for(Sample s: samples){
							selectedChemicalAnalysesCount.add(s.getAnalysisCount());
						}
						countAllImageMaps(selectedImageMapsCount);
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					else if(rb1.isChecked()){
						custom = false;
						selectedChemicalAnalyses = chemicalAnalyses;
						for(Sample s: samples){
							selectedChemicalAnalysesCount.add(s.getAnalysisCount());
							selectedImageMapsCount.add(0);
						}
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					else if(rb2.isChecked()){
						custom = false;
						selectedImageMaps = imageMaps;
						for(Sample s: samples){
							selectedChemicalAnalysesCount.add(0);
						}
						countAllImageMaps(selectedImageMapsCount);
						MakePublicDialog.this.setWidget(createInterfaceMakeDataPublic());
					}
					else if(rb3.isChecked()){
						custom = false;
						for(Sample s: samples){
							selectedImageMapsCount.add(0);
							selectedChemicalAnalysesCount.add(0);
						}
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
				itr.next();
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
					Sample current = itr.previous();
					header.setText("Make " + current.getNumber() + " Public");
					counter.setText((itr.previousIndex() + 2) + " of " + samples.size() + " Samples");
					setSubsampleTree(current);
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
					//Temporary until checkboxes work
					for(Sample s: samples){
						selectedSubsamplesCount.add(s.getSubsampleCount());
						selectedImageMapsCount.add(0);
						selectedChemicalAnalysesCount.add(0);
					}
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
		for(Sample s: samples){
			container.add(new Label("Sample " + s.getNumber() + ": " +
					selectedSubsamplesCount.get(id) + "/" + s.getSubsampleCount() + " subsamples, " + 
					"Chem: " + selectedChemicalAnalysesCount.get(id) + "/" + ChemicalAnalysesTotal.get(id) + " " + 
					"Img map: " + selectedImageMapsCount.get(id) + "/" + ImageMapsTotal.get(id)));
			id++;
		}
		
		//Not using the images yet
		/*for(Sample s: samples){
			container.add(new Label("Sample " + s.getNumber() + ": " +
					selectedSubsamplesCount.get(id) + "/" + s.getSubsampleCount() + " subsamples, ")); 
			container.add(new Image("icon-chemical-analysis.png"));
			container.add(new Label(selectedChemicalAnalysesCount.get(id) + "/" + ChemicalAnalysesTotal.get(id) + " "));
			container.add(new Image("icon-image-map.png"));
			container.add(new Label(selectedImageMapsCount.get(id) + "/" + ImageMapsTotal.get(id)));
			id++;
		}*/
		
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
		new ServerOp<List<Subsample>>() {
			@Override
			public void begin() {
				Iterator<Sample> itr = allSamples.iterator();
				Collection<Long> sampleIds = new ArrayList();
				while (itr.hasNext()) {
					Sample current = itr.next();
					sampleIds.add(current.getId());
				}
				MpDb.subsample_svc.allFromManySamples(sampleIds, this);
			}
			public void onSuccess(List<Subsample> result) {
				subsamples.addAll(result);
					// get chemical analyses
				new ServerOp<List<ChemicalAnalysis>>() {
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
					public void onSuccess(List<ChemicalAnalysis> result) {
						chemicalAnalyses.addAll(result);
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
								MakePublicDialog.this.show();
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
	
	private void setSubsampleTree(Sample sample){
		subsampleTree.clear();
		
		//create a TreeItem for each subsample
		//Iterating through the list of all subsamples because
		//  the get methods for retrieving a sample's subsamples don't work
		Iterator<Subsample> itr = subsamples.iterator();
		while(itr.hasNext()){
			Subsample current = itr.next();
			if(current.getSampleId().equals(sample.getId())){	
				CheckBox ssBox = new CheckBox(current.getName());
				ssBox.addClickListener(new ClickListener(){
					public void onClick(final Widget sender){

					}
				});
				TreeItem subsample = new TreeItem(ssBox);
				
				if(current.getAnalysisCount() > 0){
					TreeItem chemAnalyses = new TreeItem("Chemical Analyses");
					chemAnalyses.addItem("Select: All None");
					
					//checkbox for each analysis
					Iterator<ChemicalAnalysis> chemItr = chemicalAnalyses.iterator();
					while(chemItr.hasNext()){
						ChemicalAnalysis chemCurrent = chemItr.next();
						/* getSample() is always null :( */
						if(chemCurrent.getSample() != null && chemCurrent.getSample() == sample){
							chemAnalyses.addItem(new CheckBox(chemCurrent.getMineral().toString()));
						}
					}
					subsample.addItem(chemAnalyses);
				}
				
				if(current.getGrid() != null) subsample.addItem(new CheckBox("Image Map"));
				subsampleTree.addItem(subsample);
			}
		}
		if(subsampleTree.getItem(0) == null) subsampleTree.addItem("This sample has no subsamples");
	}
	
	private void countAllImageMaps(Vector ImageMapsCount){
		for(Sample s: samples){
			int count = 0;
			long sampleID = s.getId();
			Iterator<Subsample> itr = selectedSubsamples.iterator();
			while (itr.hasNext()) {
				Subsample current = itr.next();
				if(current.getSampleId().equals(sampleID) && current.getGrid() != null) count++;
			}
			ImageMapsCount.add(count);
		}
	}
	
}