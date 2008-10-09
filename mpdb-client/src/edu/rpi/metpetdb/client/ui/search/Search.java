package edu.rpi.metpetdb.client.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.ObjectSearchPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchInterface;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabChemicalAnalysis;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabLocation;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabMinerals;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabOther;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchTabRockTypes;
import edu.rpi.metpetdb.client.ui.objects.list.SampleListEx;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class Search extends MPagePanel implements ClickListener {
//	private static GenericAttribute[] rocktype = {
//		new SRockTypesAttribute(MpDb.doc.SearchSample_possibleRockTypes, 4),
//	};
//	private static GenericAttribute[] Region = {
//		new SearchLocationAttribute(MpDb.oc.SearchSample_boundingBox),
//		new SearchCountriesAttribute(MpDb.oc.SearchSample_country),
//		new RegionAttribute(MpDb.oc.SearchSample_region)
//	};
//	private static GenericAttribute[] Minerals = {
//		new SearchMineralsAttribute(MpDb.doc.SearchSample_minerals, 4),
//	};
//	private static GenericAttribute[] Chemistry = {
//		new SearchChemistryAttribute(MpDb.doc.SearchSample_elements,
//				MpDb.doc.SearchSample_oxides),
//	};
//	private static GenericAttribute[] Other = {
//			new SearchOwnersAttribute(MpDb.oc.SearchSample_owner),
//			new SearchCollectorsAttribute(MpDb.oc.SearchSample_collector),
//			new TextAttribute(MpDb.oc.SearchSample_alias),	
//			new TextAttribute(MpDb.oc.SearchSample_sesarNumber),
//			new MetamorphicGradeAttribute(MpDb.oc.SearchSample_metamorphicGrades),
//			new ReferenceAttribute(MpDb.oc.SearchSample_references),
//			new DateRangeAttribute(MpDb.oc.SearchSample_collectionDateRange)
//	};
//	private static GenericAttribute[] searchAtts = {
//		new SearchTabAttribute(MpDb.doc.SearchSample_tabs,
//				new GenericAttribute[][] {
//						rocktype, Region, Minerals, Chemistry, Other
//				}, new String[] { LocaleHandler.lc_entity.Search_Tab_RockType(),
//				LocaleHandler.lc_entity.Search_Tab_Region(),
//				LocaleHandler.lc_entity.Search_Tab_Minerals(),
//				LocaleHandler.lc_entity.Search_Tab_Chemistry(),
//				LocaleHandler.lc_entity.Search_Tab_Other()
//		})
//
//	};
	private static  SearchTabAttribute[] searchTabs = {
		new SearchTabRockTypes(), new SearchTabLocation(), new SearchTabMinerals(), new SearchTabChemicalAnalysis(), new SearchTabOther()
	};

	private static final String samplesParameter = "Samples";
	private Button exportExcelButton;
	private Button exportGoogleEarthButton;
	private final ObjectSearchPanel p_searchSample;
	private final SampleListEx sampleList = new SampleListEx(
			LocaleHandler.lc_text.search_noSamplesFound()) {

		@Override
		public void update(PaginationParameters p,
				AsyncCallback<Results<Sample>> ac) {
			/*
			 * in reality this would somehow tie into the search rpc call to
			 * pass in sorting data and pagination data to the server
			 */
			final Results<Sample> r = new Results<Sample>();
			r.setCount(results.size());
			r.setList(results);
			ac.onSuccess(r);
		}

	};
	private List<Sample> results = new ArrayList<Sample>();

	public Search() {
		setStyleName(CSS.SEARCH);
		setPageTitle("Search");
		p_searchSample = new ObjectSearchPanel<SearchSample>(new SearchInterface(searchTabs),
				LocaleHandler.lc_text.search(), LocaleHandler.lc_text.search()) {
			// TODO: Make that null into the current user from session. I don't
			// know how to do this right now however
			protected void searchBean(final AsyncCallback<List<Sample>> ac) {
				MpDb.search_svc.search((SearchSample) getBean(), MpDb
						.currentUser(), ac);
			}
			protected void onSearchCompletion(final List<Sample> r) {
				if (r != null) {
					results = r;
					sampleList.refresh();
				}
			}

		};

		add(p_searchSample);
		addResultListHeader();
		add(sampleList);
	}

	private void addResultListHeader() {

		final HorizontalPanel hpExport = new HorizontalPanel();

		final Label exportResultsLabel = new Label(LocaleHandler.lc_text
				.search_exportResults());
		final Label exportExcelLabel = new Label(LocaleHandler.lc_text
				.search_exportExcel());
		final Label exportGoogleEarthLabel = new Label(LocaleHandler.lc_text
				.search_exportKML());

		exportExcelButton = new Button(LocaleHandler.lc_text
				.buttonExportExcel(), this);
		exportGoogleEarthButton = new Button(LocaleHandler.lc_text
				.buttonExportKML(), this);

		exportResultsLabel.setStyleName("bold");
		exportExcelButton.setStyleName("bold");
		exportGoogleEarthButton.setStyleName("bold");

		final VerticalPanel vpExcel = new VerticalPanel();
		final VerticalPanel vpGoogleEarth = new VerticalPanel();

		vpExcel.add(exportExcelButton);
		vpExcel.add(exportExcelLabel);

		vpGoogleEarth.add(exportGoogleEarthButton);
		vpGoogleEarth.add(exportGoogleEarthLabel);

		hpExport.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hpExport.add(exportResultsLabel);
		hpExport.add(vpExcel);
		hpExport.add(vpGoogleEarth);

		hpExport.setStyleName("mpdb-dataTableBlue");
		hpExport.setWidth("100%");

		add(hpExport);

	}

	public Search createNew() {
		SearchSample s = new SearchSample();
		p_searchSample.edit(s);
		return this;
	}

	public void onClick(Widget sender) {
		if (sender == exportExcelButton) {
		    final FormPanel fp = new FormPanel();
			fp.setMethod(FormPanel.METHOD_GET);
			fp.setEncoding(FormPanel.ENCODING_URLENCODED);
			String values = "";
			for (int i = 1; i < sampleList.getScrollTable().getHeaderTable().getColumnCount(); i++){
				values+=sampleList.getScrollTable().getHeaderTable().getText(0, i) +"\t";
			}
			values+="\n";
			int currentpage = sampleList.getScrollTable().getCurrentPage();
			for (int page = 0; page < sampleList.getScrollTable().getNumPages(); page++) {
				sampleList.getScrollTable().gotoPage(page, false);
				for(int i = 0; i <sampleList.getScrollTable().getDataTable().getRowCount(); i++) {
					for (int j = 1; j < sampleList.getScrollTable().getDataTable().getColumnCount(); j++){
						values+=sampleList.getScrollTable().getDataTable().getText(i, j) +"\t";
					}
					values+="\n";
				}
			}
			sampleList.getScrollTable().gotoPage(currentpage, true);
			Hidden data = new Hidden("excel",values);
			fp.add(data);
			fp.setAction(GWT.getModuleBaseURL() + "excel.svc");
			fp.setVisible(false);
			add(fp);
			fp.submit();
			
		} else if (sender == exportGoogleEarthButton) {
			final FormPanel fp = new FormPanel();
			fp.setMethod(FormPanel.METHOD_GET);
			fp.setEncoding(FormPanel.ENCODING_URLENCODED);
			final HorizontalPanel hp = new HorizontalPanel();
			for (int i = 0; i < results.size(); i++) {
				Hidden sample = new Hidden(samplesParameter, String
						.valueOf(results.get(i).getId()));
				hp.add(sample);
			}
			fp.add(hp);
			fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
			fp.setVisible(false);
			add(fp);
			fp.submit();
		}
	}
}
