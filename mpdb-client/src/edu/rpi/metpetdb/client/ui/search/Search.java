package edu.rpi.metpetdb.client.ui.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabLocation;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabMetamorphicGrade;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabMinerals;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabProvenance;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabRockTypes;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisList;
import edu.rpi.metpetdb.client.ui.objects.list.SampleList;
import edu.rpi.metpetdb.client.ui.user.UsesCurrentUser;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class Search extends MPagePanel implements UsesCurrentUser {

	private SearchTabAttribute[] searchTabs = {
			new SearchTabRockTypes(), new SearchTabMetamorphicGrade(),
			new SearchTabLocation(), new SearchTabMinerals(),
			new SearchTabChemicalAnalysis(), new SearchTabProvenance()
	};

	private FlowPanel samplesContainer = new FlowPanel();
	private FlowPanel chemContainer = new FlowPanel();
	private ObjectSearchPanel searchPanel;
	private SearchInterface sui = new SearchInterface(searchTabs);
	private boolean outputSamples = true;
	private SearchSample ss = new SearchSample();
	private boolean initLoad = true;
	private boolean searched = false;

	private SampleList sampleList;
	private ChemicalAnalysisList chemList;
	
	private RadioButton returnSamples;
	private RadioButton returnChemicalAnalyses;

	public void init() {
		searchPanel = new ObjectSearchPanel(sui) {
			protected void performSearch() {
				searched = true;
				sampleList.getScrollTable().gotoPage(0, true);
				chemList.getScrollTable().gotoPage(0, true);

			}
			protected void onSearchCompletion(final FormOp<SearchSample> ac) {}
		};
		sui.insertActionWidget(createResultTypeToggle(), 0);

		sampleList = new SampleList() {

			@Override
			public void update(PaginationParameters p,
					AsyncCallback<Results<Sample>> ac) {
				if (ss != null && !initLoad && searched && returnSamples.getValue().equals(Boolean.TRUE))
					MpDb.search_svc.sampleSearch(p, ss, MpDb.currentUser(), ac);
				else {
					ac
							.onSuccess(new Results<Sample>(0,
									new ArrayList<Sample>()));
					initLoad = false;
				}
			}

			@Override
			public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
				if (ss != null)
					MpDb.search_svc.sampleSearchIds(ss, MpDb.currentUser(), ac);
				else
					ac.onSuccess(new HashMap<Object, Boolean>());
			}

		};

		chemList = new ChemicalAnalysisList() {

			@Override
			public void update(PaginationParameters p,
					AsyncCallback<Results<ChemicalAnalysis>> ac) {
				
			
					
				
				if (ss != null && !initLoad && searched && returnChemicalAnalyses.getValue().equals(Boolean.TRUE))
					MpDb.search_svc.chemicalAnalysisSearch(p, ss, MpDb
							.currentUser(), ac);
				else {
					
					ac.onSuccess(new Results<ChemicalAnalysis>(0,
							new ArrayList<ChemicalAnalysis>()));
					initLoad = false;
				}
			}

			@Override
			public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
				if (ss != null)
					MpDb.search_svc.chemicalAnalysisSearchIds(ss, MpDb
							.currentUser(), ac);
				else
					ac.onSuccess(new HashMap<Object, Boolean>());
			}



		};

		samplesContainer.add(sampleList);
		chemContainer.add(chemList);
		searchPanel.edit(ss);
		chemContainer.setVisible(!outputSamples);
		samplesContainer.setVisible(outputSamples);
	}

	public Search() {
		init();
		setStyleName(CSS.SEARCH);
		setPageTitle("Search");
		add(searchPanel);
		add(samplesContainer);
		add(chemContainer);
	}

	public void reload() {
		sampleList.getScrollTable().reloadPage();
		chemList.getScrollTable().reloadPage();
	}

	private Widget createResultTypeToggle() {
		final HTMLPanel container = new HTMLPanel(
				"<span>Search for:</span> <span id=\"radio-samples\"></span><span id=\"radio-analyses\"></span>");
		final String groupString = "resultType_attribute";
	//	final RadioButton 
		returnSamples = new RadioButton(groupString,
				"Samples");
		//final RadioButton 
		returnChemicalAnalyses = new RadioButton(groupString,
				"Chemical Analyses");

		
		
		/*
		returnSamples.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				outputSamples = true;
				updateResults();
			}
		});

		returnChemicalAnalyses.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				outputSamples = false;
				updateResults();
			}
		});

*/
		
		searchPanel.getSearchBtn().addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
			
				if (returnSamples.getValue().equals(Boolean.TRUE))
				{
					outputSamples = true;
					updateResults();
				//	sampleList.doSearch();
				}
				
				if (returnChemicalAnalyses.getValue().equals(Boolean.TRUE))
				{
					outputSamples = false;
					updateResults();
				//	chemList.doSearch();
				}
			
			}
		});
		
		returnSamples.setChecked(true);

		container.addAndReplaceElement(returnSamples, "radio-samples");
		container
				.addAndReplaceElement(returnChemicalAnalyses, "radio-analyses");
		container.setStyleName("return-type");
		return container;
	}

	private void updateResults() {
		if (outputSamples) {
			samplesContainer.setVisible(true);
			chemContainer.setVisible(false);
		} else {
			samplesContainer.setVisible(false);
			chemContainer.setVisible(true);
		}
	}

	public void onCurrentUserChanged(User whoIsIt)
			throws LoginRequiredException {

	}
}
