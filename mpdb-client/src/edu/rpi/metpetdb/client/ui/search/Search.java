package edu.rpi.metpetdb.client.ui.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.EarthInstanceHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.PageChangeListener;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchInterface;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabLocation;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabMetamorphicGrade;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabMinerals;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabProvenance;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabRockTypes;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarth;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class Search extends MPagePanel implements PageChangeListener {

	private static  SearchTabAttribute[] searchTabs = {
		new SearchTabRockTypes(),
		new SearchTabMetamorphicGrade(), 
		new SearchTabLocation(), 
		new SearchTabMinerals(), 
		new SearchTabChemicalAnalysis(), 
		new SearchTabProvenance()
	};
	private static final String cookieString = "SearchView";
	private static final String urlParameter = "url";
	private static final String samplesParameter = "Samples";
	private final MLink exportExcel = new MLink();
	private final MLink exportGoogleEarth = new MLink();
	private final MLink viewGoogleEarth = new MLink();
	private MLink customCols;
	private final FlowPanel columnViewPanel = new FlowPanel();
	private FlowPanel samplesContainer = new FlowPanel();
	private final ObjectSearchPanel searchPanel;
	private MDialogBox earthPopup = new MDialogBox();
	
	private SearchSample ss;
	private final SampleListEx sampleList = new SampleListEx(
			LocaleHandler.lc_text.search_noSamplesFound()) {

		@Override
		public void update(PaginationParameters p,
				AsyncCallback<Results<Sample>> ac) {
			if (ss != null)
				MpDb.search_svc.search(p,ss, MpDb.currentUser(), ac);
			else
				ac.onSuccess(new Results<Sample>(0, new ArrayList<Sample>()));
		}

	};
	private List<Sample> results = new ArrayList<Sample>();

	public Search() {
		MetPetDBApplication.registerPageWatcher(this);
		setStyleName(CSS.SEARCH);
		setPageTitle("Search");
		SearchInterface sui = new SearchInterface(searchTabs);
		searchPanel = new ObjectSearchPanel<SearchSample>(sui) {
			// TODO: Make that null into the current user from session. I don't
			// know how to do this right now however
			protected void searchBean(final AsyncCallback ac) {
				ss = (SearchSample) getBean();
				sampleList.refresh();
			}
			protected void onSearchCompletion(final FormOp ac) {
				saveSearch();
				saveSearchPagination();
				sampleList.refresh();
				ac.enable(true);
			}
		};
		
		exportExcel.setText("Export as Spreadsheet (.tsv)");
		exportExcel.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				exportExcel();
			}
		});
		sui.passActionWidget(exportExcel);
		
		exportGoogleEarth.setText("Export for Google Earth (.kml)");
		exportGoogleEarth.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				exportGoogleEarth();
			}
		});
		sui.passActionWidget(exportGoogleEarth);
		
		viewGoogleEarth.setText("View in Google Earth");
		viewGoogleEarth.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				viewGoogleEarth();
			}
		});
		sui.passActionWidget(viewGoogleEarth);
		
		add(searchPanel);
		
		createViewFromCookie();
		buildSampleFilters();
		samplesContainer.add(columnViewPanel);
		samplesContainer.add(sampleList);
		add(samplesContainer);
	}

	// TODO ideally the interface should be loading while we do the server call to find out if there's a 
	// search sample in the session. Right now it waits for the response and then either loads the interface
	// with a new search sample or the one from the session
	public Search createNew() {		
		loadSearch();
		loadSearchSample();
		return this;
	}

	public void exportExcel() {
		final FormPanel fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_GET);
		fp.setEncoding(FormPanel.ENCODING_URLENCODED);
		String values = "";
		
		for (int i = 1; i < sampleList.getDisplayColumns().size(); i++){
			values+=sampleList.getDisplayColumns().get(i).getTitle() +"\t";
		}
		values+="\n";
		
		int currentpage = sampleList.getScrollTable().getCurrentPage();
		for (int page = 0; page < sampleList.getScrollTable().getNumPages(); page++) {
			sampleList.getScrollTable().gotoPage(page, false);
			for(int i = 0; i <sampleList.getScrollTable().getDataTable().getRowCount(); i++) {
				for (int j = 1; j < sampleList.getScrollTable().getDataTable().getColumnCount(); j++){
					if (sampleList.getScrollTable().getDataTable().getWidget(i, j) instanceof Image){
						values+=sampleList.getScrollTable().getDataTable().getWidget(i, j).toString() +"\t";
					} else {
						values+=sampleList.getScrollTable().getDataTable().getText(i, j) +"\t";
					}
				}
				values+="\n";
			}
		}
		
		sampleList.getScrollTable().gotoPage(currentpage, false);
		Hidden data = new Hidden("excel",values);
		fp.add(data);
		fp.setAction(GWT.getModuleBaseURL() + "excel.svc?");
		fp.setVisible(false);
		add(fp);
		fp.submit();
	}
	
	public void exportGoogleEarth() {
		final FormPanel fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_GET);
		fp.setEncoding(FormPanel.ENCODING_URLENCODED);
		final HorizontalPanel hp = new HorizontalPanel();
		for (int i = 0; i < results.size(); i++) {
			Hidden sample = new Hidden(samplesParameter, String
					.valueOf(results.get(i).getId()));
			hp.add(sample);
		}
		Hidden url = new Hidden(urlParameter,GWT.getModuleBaseURL() + "#" + 
				LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater());
		hp.add(url);
		fp.add(hp);
		fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
		fp.setVisible(false);
		add(fp);
		fp.submit();
	}
	
	public void viewGoogleEarth() {
		final FlowPanel earthContainer = new FlowPanel();
		final Button close = new Button("Close");
		close.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				earthPopup.hide();
			}
		});
		final MapWidget map = new MapWidget();
		new ServerOp<String>(){
			public void begin(){
				final String baseURL = GWT.getModuleBaseURL() + "#" + 
				LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater();
				MpDb.search_svc.createKML((ArrayList<Sample>) sampleList.data, baseURL, this);
			}
			public void onSuccess(final String kml){
				map.getEarthInstance(new EarthInstanceHandler(){
					public void onEarthInstance(final EarthInstanceEvent e){
						MGoogleEarth.parseKML(e,kml);
				      }
				});
			}
		}.begin();
		
		map.setSize("500px", "500px");
	    map.addControl(new SmallMapControl());
	    map.addMapType(MapType.getEarthMap());
	    map.setCurrentMapType(MapType.getEarthMap());
	    
    	earthContainer.add(map);
    	earthContainer.add(close);
		earthPopup.setWidget(earthContainer);
		earthPopup.show();
	}
	
	private void buildSampleFilters() {
		final MLink simple = new MLink("Simple", new ClickListener() {
			public void onClick(Widget sender) {
				sampleList.simpleView();
			}
		});


		final MLink detailed = new MLink("Detailed", new ClickListener() {
			public void onClick(Widget sender) {
				sampleList.newView(sampleList.getOriginalColumns());
			}
		});

		customCols = new MLink("Custom", new ClickListener() {
			public void onClick(Widget sender) {
				CustomTableView myView = new CustomTableView(sampleList, cookieString);
			}
		});

		final Label columnsLabel = new Label("Columns:");

		columnViewPanel.add(columnsLabel);
		columnViewPanel.add(simple);
		columnViewPanel.add(detailed);
		columnViewPanel.add(customCols);

		columnViewPanel.addStyleName(CSS.DATATABLE_HEADER_FILTERS);

	}
	
	private void createViewFromCookie() {
		final ArrayList<Column> originalColumns = new ArrayList<Column>(sampleList
				.getOriginalColumns());
		final ArrayList<String> cookiedColumns;
		final ArrayList<Column> displayColumns = new ArrayList<Column>();
		if (Cookies.getCookie(cookieString) != null) {
			cookiedColumns = new ArrayList<String>(Arrays.asList(Cookies
					.getCookie(cookieString).split(",")));
			Iterator<Column> itr = originalColumns.iterator();
			while (itr.hasNext()) {
				final Column col = itr.next();
				if (cookiedColumns.contains(col.getTitle()))
					displayColumns.add(col);
			}
			sampleList.newView(displayColumns);
		}
	}
	
	public void onPageChanged(){
		if (!History.getToken().equals(TokenSpace.search.makeToken(null))){
			MetPetDBApplication.removePageWatcher(this);
			new ServerOp<Void>(){
				public void begin(){
					try{
						searchPanel.startValidation(this);	
					} catch (Exception e){
						// Do nothing, we don't want to display the errors for this.
						// The user doesn't know that we're saving his current search object.
						// If there's an exception for one of the attributes then that value
						// won't be loaded.
					}
				}
				public void onSuccess(Void v){
					new ServerOp<Void>(){
						public void begin(){
							MpDb.search_svc.setSessionSearchSample((SearchSample) searchPanel.getBean(), this);	
						}
						public void onSuccess(Void vv){
							
						}
					}.begin();
				}
			}.begin();
		}
	}
	
	public void saveSearch(){
		new ServerOp<Void>(){
			public void begin(){
				MpDb.search_svc.setSessionLastSearchedSearchSample((SearchSample) searchPanel.getBean(), this);	
			}
			public void onSuccess(Void v){
				
			}
		}.begin();
	}
	
	public void saveSearchPagination(){
		new ServerOp<Void>(){
			public void begin(){
				MpDb.search_svc.setSessionLastSearchPagination(sampleList.getPaginationParameters(), this);	
			}
			public void onSuccess(Void v){
				
			}
		}.begin();
	}
	
	public void loadSearchSample(){
		new ServerOp<SearchSample>(){
			public void begin(){
				MpDb.search_svc.getSessionSearchSample(this);
			}
			public void onSuccess(final SearchSample searchSamp){
				if (searchSamp != null){
					searchPanel.edit(searchSamp);
				} else {
					searchPanel.edit(new SearchSample());
				}
			}
		}.begin();
	}
	
	public void loadSearch(){
		new ServerOp<SearchSample>(){
			public void begin(){
				MpDb.search_svc.getSessionLastSearchedSearchSample(this);
			}
			public void onSuccess(final SearchSample searchSamp){
				if (searchSamp != null){
					ss = searchSamp;
					loadPagination();			
				}
			}
		}.begin();
	}
	
	public void loadPagination(){
		new ServerOp<PaginationParameters>(){
			public void begin(){
				MpDb.search_svc.getSessionLastSearchPagination(this);
			}
			public void onSuccess(final PaginationParameters p){
				if (p != null){
					sampleList.refresh(p);
				} else {
					sampleList.refresh();
				}
			}
		}.begin();
	}
}
